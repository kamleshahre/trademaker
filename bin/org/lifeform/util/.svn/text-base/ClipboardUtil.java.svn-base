package org.lifeform.util;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;

public class ClipboardUtil {
	public static String getTextFromClipBoard(final Display display) {
		Clipboard clipboard = new Clipboard(display);
		String data = (String)clipboard.getContents(TextTransfer.getInstance());
		return data;
	}
	public static void  setTextToClipBoard(final Display display, final String text) {
		Clipboard clipboard = new Clipboard(display);
		clipboard.setContents(new Object[]{text}, new TextTransfer[] {TextTransfer.getInstance()});
	}

	
}
