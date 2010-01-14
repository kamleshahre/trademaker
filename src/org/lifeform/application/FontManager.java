package org.lifeform.application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public final class FontManager {
	private static final Log LOGGER = LogFactory.getLog(FontManager.class);

	private Map<String, ManagedFont> fonts;
	private Map<Font, ManagedFont> reverse;
	private final Display display;
	private final boolean autoDispose;

	private static final class ManagedFont {
		String resname;
		Font font;
		FontData fontData;
		int references;
	}

	/**
	 * Create a new FontManager.
	 */

	public FontManager(final Display display, final boolean autoDispose) {
		this.fonts = new HashMap<String, ManagedFont>();
		this.reverse = new HashMap<Font, ManagedFont>();
		this.display = display;
		this.autoDispose = autoDispose;

		display.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(final Event event) {
				disposeAll();
			}
		});
	}

	public synchronized Font getFont(final FontData[] fds) throws Exception {
		String fdstring;
		if (fds.length == 1)
			fdstring = fds[0].toString();
		else {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < fds.length; i++)
				buf.append("*__FD__*").append(fds[i]);
			fdstring = buf.toString();
		}

		ManagedFont mi = fonts.get(fdstring);
		if (mi == null) {
			mi = new ManagedFont();
			try {
				mi.font = new Font(display, fds);
			} catch (SWTException ex) {
				throw new Exception("Error loading font with font data \""
						+ fdstring + "\"", ex);
			}
			mi.resname = fdstring;
			fonts.put(fdstring, mi);
			reverse.put(mi.font, mi);
		}
		mi.references++;
		return mi.font;
	}

	public synchronized void returnFont(final Font font) throws Exception {
		if (font.isDisposed())
			return;
		ManagedFont mi = reverse.get(font);
		if (mi == null)
			throw new Exception(
					"The font supplied to returnFont() is not managed by this FontManager");
		if (mi.references == 0)
			throw new Exception("Can't return font: No references left");
		mi.references--;
		if (autoDispose && mi.references == 0) {
			LOGGER.debug("FontManager: Disposing of font {} " + mi.resname);
			mi.font.dispose();
			fonts.remove(mi.resname);
			reverse.remove(mi.font);
		}
	}

	public synchronized void disposeUnallocatedFonts() {
		for (Iterator<ManagedFont> it = fonts.values().iterator(); it.hasNext();) {
			ManagedFont mi = it.next();
			if (mi.references == 0) {
				LOGGER.debug("FontManager: Disposing of font {}" + mi.resname);
				mi.font.dispose();
				it.remove();
				reverse.remove(mi.font);
			}
		}
	}

	public synchronized void disposeAll() {
		for (ManagedFont mi : fonts.values()) {
			LOGGER.debug("FontManager: Disposing of font {}" + mi.resname);
			mi.font.dispose();
		}
		fonts = new HashMap<String, ManagedFont>();
		reverse = new HashMap<Font, ManagedFont>();
	}

	public static double decodeFontSize(final FontData base, final String sizeString)
			throws Exception {
		String s = sizeString;
		boolean em = false, inch = false, cm = false, mm = false;
		s = s.trim();
		if (s.endsWith("em")) {
			em = true;
			s = s.substring(0, s.length() - 2).trim();
		} else if (s.endsWith("in")) {
			inch = true;
			s = s.substring(0, s.length() - 2).trim();
		} else if (s.endsWith("cm")) {
			cm = true;
			s = s.substring(0, s.length() - 2).trim();
		} else if (s.endsWith("mm")) {
			mm = true;
			s = s.substring(0, s.length() - 2).trim();
		} else if (s.endsWith("pt")) {
			s = s.substring(0, s.length() - 2).trim();
		}
		if (base == null && em)
			throw new Exception(
					"Cannot decode font size in \"em\" measure without a base font");
		try {
			double d = Double.parseDouble(s);
			if (inch)
				d *= 72.0;
			if (cm)
				d *= 72.0 / 2.54;
			if (mm)
				d *= 72.0 / 25.4;
			if (em)
				d *= base.getHeight();
			return d;
		} catch (NumberFormatException ex) {
			throw new Exception("Error parsing font size \"" + sizeString
					+ "\"");
		}
	}

	public static FontData[] decodeFontSpec(final FontData[] base, final String spec)
			throws Exception {
		if (spec == null || spec.length() == 0) {
			if (base == null)
				throw new Exception(
						"Cannot create font data without either a base font or a font specification string");
			else
				return base;
		}
		StringTokenizer tok = new StringTokenizer(spec, ",");
		String name = null, height = null;
		int style = 0;
		boolean normal = false;
		while (tok.hasMoreElements()) {
			String s = tok.nextToken().trim();
			if (s.length() == 0)
				continue;
			if (s.equals("normal"))
				normal = true;
			else if (s.equals("bold"))
				style |= SWT.BOLD;
			else if (s.equals("italic"))
				style |= SWT.ITALIC;
			else if (Character.isDigit(s.charAt(0)))
				height = s;
			else
				name = s;
		}

		if (base == null) {
			int ptHeight = (height == null) ? -1 : (int) decodeFontSize(null,
					height);
			if (ptHeight == -1)
				throw new Exception(
						"Cannot create non-derived font without a font height");
			return new FontData[] { new FontData(name, ptHeight, style) };
		} else {
			FontData[] fda = new FontData[base.length];
			for (int i = 0; i < fda.length; i++) {
				int ptHeight = (height == null) ? -1 : (int) decodeFontSize(
						base[i], height);
				FontData fd = new FontData(base[i].toString());
				if (name != null)
					fd.setName(name);
				if (ptHeight != -1)
					fd.setHeight(ptHeight);
				if (style != 0 || normal)
					fd.setStyle(style);
				fda[i] = fd;
			}
			return fda;
		}
	}
}
