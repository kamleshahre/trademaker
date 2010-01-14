package org.lifeform.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.lifeform.gui.SimpleMenuConfiguration;

public class AppUtil {
	private static String appPath = System.getProperty("user.dir");
	private static String userHome = System.getProperty("user.Home");

	private static Shell shell;

	static {
		shell = new Shell();
	}

	public static Shell getShell() {
		return shell;
	}

	public static String getAppPath() {
		return appPath;
	}

	public static String getUserHome() {
		return userHome;
	}

	public static boolean isRunning(final String appName) {
		try {
			File file = new File(getUserHome(), appName + ".tmp");
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			if (channel.tryLock() == null) {
				return true;
			}
		} catch (Exception e) {
			showMessage(null, e.getMessage());
		}
		return false;
	}

	public static String getAppName() {
		return "Trade Maker";
	}

	public static void showMessage(final Component parent, final String msg) {
		JOptionPane.showMessageDialog(parent, msg, getAppName(),
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showMessage(final String msg) {
		showMessage(null, msg);
	}

	public static void showError(final Component parent, final String msg) {
		MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		messageBox.setMessage(msg);
		if (messageBox.open() == SWT.OK) {
			System.out.println("Ok is pressed.");
		}
	}

	public static void showError(final String title, final String message) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR
				| SWT.CLOSE);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}

	public static void showLinkError(final UnsatisfiedLinkError ulex) {
		String msg = ulex.getMessage();
		if (msg != null) {
			StringBuffer buf = new StringBuffer();
			int pos32bit = msg.indexOf("32-bit");
			int pos64bit = msg.indexOf("64-bit");
			if (pos32bit >= 0 && pos64bit >= 0) {
				if (System.getProperty("os.name").toLowerCase().startsWith(
						"windows")) {
					// windows
					if (pos32bit < pos64bit) {
						// on 64-bit JVM
						buf.append(msg);
						buf.append("\nPlease execute run_win64.bat instead.");
					} else {
						// on 32-bit jvm
						buf.append(msg);
						buf.append("\nPlease execute run_win32.bat instead.");
					}
				}
				if (System.getProperty("os.name").toLowerCase().startsWith(
						"linux")) {
					// windows
					if (pos32bit < pos64bit) {
						// on 64-bit JVM
						buf.append(msg);
						buf
								.append("\nPlease execute run_linux_x86_64.sh instead.");
					} else {
						// on 32-bit jvm
						buf.append(msg);
						buf.append("\nPlease execute run_linux.sh instead.");
					}
				}
				if (buf.length() > 0) {
					JOptionPane.showMessageDialog(new JFrame(), buf.toString(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(), msg, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		ulex.printStackTrace();
	}

	public static void showError(final Throwable t) {
		showError("Exception: " + t.getCause(), t.getMessage());
	}

	private static final int DEFAULT_COLUMN_PADDING = 5;

	public static int autoResizeTable(final JTable aTable,
			final boolean includeColumnHeaderWidth) {
		return (autoResizeTable(aTable, includeColumnHeaderWidth,
				DEFAULT_COLUMN_PADDING));
	}

	public static int autoResizeTable(final JTable aTable,
			final boolean includeColumnHeaderWidth, final int columnPadding) {
		int columnCount = aTable.getColumnCount();
		// int currentTableWidth = aTable.getWidth();
		int tableWidth = 0;

		Dimension cellSpacing = aTable.getIntercellSpacing();

		if (columnCount > 0) // must have columns !
		{
			// STEP ONE : Work out the column widths

			int columnWidth[] = new int[columnCount];

			for (int i = 0; i < columnCount; i++) {
				columnWidth[i] = getMaxColumnWidth(aTable, i, true,
						columnPadding);

				tableWidth += columnWidth[i];
			}

			// account for cell spacing too
			tableWidth += ((columnCount - 1) * cellSpacing.width);

			// STEP TWO : Dynamically resize each column

			// try changing the size of the column names area
			JTableHeader tableHeader = aTable.getTableHeader();

			Dimension headerDim = tableHeader.getPreferredSize();

			// headerDim.height = tableHeader.getHeight();
			headerDim.width = tableWidth;
			tableHeader.setPreferredSize(headerDim);

			Dimension interCellSpacing = aTable.getIntercellSpacing();
			Dimension dim = new Dimension();
			int rowHeight = aTable.getRowHeight();

			if (rowHeight == 0)
				rowHeight = 16; // default rowheight

			// System.out.println ("Row Height : " + rowHeight );

			dim.height = headerDim.height
					+ ((rowHeight + interCellSpacing.height) * aTable
							.getRowCount());
			dim.width = tableWidth;

			// System.out.println
			// ("AutofitTableColumns.autoResizeTable() - Setting Table size to ( "
			// + dim.width + ", " + dim.height + " )" );
			// aTable.setPreferredSize ( dim );

			TableColumnModel tableColumnModel = aTable.getColumnModel();
			TableColumn tableColumn;

			for (int i = 0; i < columnCount; i++) {
				tableColumn = tableColumnModel.getColumn(i);

				tableColumn.setPreferredWidth(columnWidth[i]);
			}

			aTable.invalidate();
			aTable.doLayout();
			aTable.repaint();
		}

		return (tableWidth);
	}

	/*
	 * @param JTable aTable, the JTable to autoresize the columns on
	 * 
	 * @param int columnNo, the column number, starting at zero, to calculate
	 * the maximum width on
	 * 
	 * @param boolean includeColumnHeaderWidth, use the Column Header width as a
	 * minimum width
	 * 
	 * @param int columnPadding, how many extra pixels do you want on the end of
	 * each column
	 * 
	 * @returns The table width, just in case the caller wants it...
	 */

	private static int getMaxColumnWidth(final JTable aTable,
			final int columnNo, final boolean includeColumnHeaderWidth,
			final int columnPadding) {
		TableColumn column = aTable.getColumnModel().getColumn(columnNo);
		Component comp = null;
		int maxWidth = 0;

		if (includeColumnHeaderWidth) {
			TableCellRenderer headerRenderer = column.getHeaderRenderer();
			if (headerRenderer != null) {
				comp = headerRenderer.getTableCellRendererComponent(aTable,
						column.getHeaderValue(), false, false, 0, columnNo);

				if (comp instanceof JTextComponent) {
					JTextComponent jtextComp = (JTextComponent) comp;

					String text = jtextComp.getText();
					Font font = jtextComp.getFont();
					FontMetrics fontMetrics = jtextComp.getFontMetrics(font);

					maxWidth = SwingUtilities.computeStringWidth(fontMetrics,
							text);
				} else {
					maxWidth = comp.getPreferredSize().width;
				}
			} else {
				try {
					String headerText = (String) column.getHeaderValue();
					JLabel defaultLabel = new JLabel(headerText);

					Font font = defaultLabel.getFont();
					FontMetrics fontMetrics = defaultLabel.getFontMetrics(font);

					maxWidth = SwingUtilities.computeStringWidth(fontMetrics,
							headerText);
				} catch (ClassCastException ce) {
					// Can't work out the header column width..
					maxWidth = 0;
				}
			}
		}

		TableCellRenderer tableCellRenderer;
		// Component comp;
		int cellWidth = 0;

		for (int i = 0; i < aTable.getRowCount(); i++) {
			tableCellRenderer = aTable.getCellRenderer(i, columnNo);

			comp = tableCellRenderer.getTableCellRendererComponent(aTable,
					aTable.getValueAt(i, columnNo), false, false, i, columnNo);

			if (comp instanceof JTextComponent) {
				JTextComponent jtextComp = (JTextComponent) comp;

				String text = jtextComp.getText();
				Font font = jtextComp.getFont();
				FontMetrics fontMetrics = jtextComp.getFontMetrics(font);

				int textWidth = SwingUtilities.computeStringWidth(fontMetrics,
						text);

				maxWidth = Math.max(maxWidth, textWidth);
			} else {
				cellWidth = comp.getPreferredSize().width;

				// maxWidth = Math.max ( headerWidth, cellWidth );
				maxWidth = Math.max(maxWidth, cellWidth);
			}
		}

		return (maxWidth + columnPadding);
	}

	private static void openURLDefault(final String url) throws Exception {
		String[] browsers = { "firefox", "opera", "konqueror", "epiphany",
				"mozilla", "netscape" };
		String selectedBrowser = null;

		for (String browser : browsers) {
			String[] command = new String[] { "which", browser };
			if (Runtime.getRuntime().exec(command).waitFor() == 0) {
				selectedBrowser = browser;
				break;
			}
		}
		if (selectedBrowser != null) {
			Runtime.getRuntime().exec(new String[] { selectedBrowser, url });
		}
	}

	private static void openURLMac(final String url) throws Exception {
		Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
		Method openURL = fileMgr.getDeclaredMethod("openURL",
				new Class[] { String.class });
		openURL.invoke(null, url);
	}

	private static void openURLWindows(final String url) throws IOException {
		Runtime.getRuntime().exec(
				"rundll32 url.dll, FileProtocolHandler " + url);
	}

	public static void openURL(final String url) throws Exception {
		final String osName = System.getProperty("os.name");

		if (osName.startsWith("Mac OS")) {
			openURLMac(url);
		} else if (osName.startsWith("Windows")) {
			openURLWindows(url);
		} else { // assume Unix or Linux
			openURLDefault(url);
		}
	}

	/**
	 * Aligns the first <code>rows</code> * <code>cols</code> components of
	 * <code>parent</code> in a grid. Each component is as big as the maximum
	 * preferred width and height of the components. The parent is made just big
	 * enough to fit them all.
	 * 
	 * @param rows
	 *            number of rows
	 * @param cols
	 *            number of columns
	 * @param initialX
	 *            x location to start the grid at
	 * @param initialY
	 *            y location to start the grid at
	 * @param xPad
	 *            x padding between cells
	 * @param yPad
	 *            y padding between cells
	 */
	public static void makeGrid(final Container parent, final int rows,
			final int cols, final int initialX, final int initialY,
			final int xPad, final int yPad) {
		SpringLayout layout;
		try {
			layout = (SpringLayout) parent.getLayout();
		} catch (ClassCastException exc) {
			System.err
					.println("The first argument to makeGrid must use SpringLayout.");
			return;
		}

		Spring xPadSpring = Spring.constant(xPad);
		Spring yPadSpring = Spring.constant(yPad);
		Spring initialXSpring = Spring.constant(initialX);
		Spring initialYSpring = Spring.constant(initialY);
		int max = rows * cols;

		// Calculate Springs that are the max of the width/height so that all
		// cells have the same size.
		Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0))
				.getWidth();
		Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0))
				.getWidth();
		for (int i = 1; i < max; i++) {
			SpringLayout.Constraints cons = layout.getConstraints(parent
					.getComponent(i));

			maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
			maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
		}

		// Apply the new width/height Spring. This forces all the
		// components to have the same size.
		for (int i = 0; i < max; i++) {
			SpringLayout.Constraints cons = layout.getConstraints(parent
					.getComponent(i));

			cons.setWidth(maxWidthSpring);
			cons.setHeight(maxHeightSpring);
		}

		// Then adjust the x/y constraints of all the cells so that they
		// are aligned in a grid.
		SpringLayout.Constraints lastCons = null;
		SpringLayout.Constraints lastRowCons = null;
		for (int i = 0; i < max; i++) {
			SpringLayout.Constraints cons = layout.getConstraints(parent
					.getComponent(i));
			if (i % cols == 0) {// start of new row
				lastRowCons = lastCons;
				cons.setX(initialXSpring);
			} else {// x position depends on previous component
				cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
						xPadSpring));
			}

			if (i / cols == 0) {// first row
				cons.setY(initialYSpring);
			} else {// y position depends on previous row
				cons.setY(Spring.sum(lastRowCons
						.getConstraint(SpringLayout.SOUTH), yPadSpring));
			}
			lastCons = cons;
		}

		// Set the parent's size.
		SpringLayout.Constraints pCons = layout.getConstraints(parent);
		pCons.setConstraint(SpringLayout.SOUTH, Spring.sum(Spring
				.constant(yPad), lastCons.getConstraint(SpringLayout.SOUTH)));
		pCons.setConstraint(SpringLayout.EAST, Spring.sum(
				Spring.constant(xPad), lastCons
						.getConstraint(SpringLayout.EAST)));
	}

	/* Used by makeCompactGrid. */
	private static SpringLayout.Constraints getConstraintsForCell(
			final int row, final int col, final Container parent, final int cols) {
		SpringLayout layout = (SpringLayout) parent.getLayout();
		Component c = parent.getComponent(row * cols + col);
		return layout.getConstraints(c);
	}

	public static void makeOneLineGrid(final Container parent, final int cols) {
		makeCompactGrid(parent, 1, cols, 6, 0, 7, 6);
	}

	/**
	 * Aligns the first <code>rows</code> * <code>cols</code> components of
	 * <code>parent</code> in a grid. Each component in a column is as wide as
	 * the maximum preferred width of the components in that column; height is
	 * similarly determined for each row. The parent is made just big enough to
	 * fit them all.
	 * 
	 * @param rows
	 *            number of rows
	 * @param cols
	 *            number of columns
	 * @param initialX
	 *            x location to start the grid at
	 * @param initialY
	 *            y location to start the grid at
	 * @param xPad
	 *            x padding between cells
	 * @param yPad
	 *            y padding between cells
	 */
	public static void makeCompactGrid(final Container parent, final int rows,
			final int cols, final int initialX, final int initialY,
			final int xPad, final int yPad) {
		SpringLayout layout;
		try {
			layout = (SpringLayout) parent.getLayout();
		} catch (ClassCastException exc) {
			System.err
					.println("The first argument to makeCompactGrid must use SpringLayout.");
			return;
		}

		// Align all cells in each column and make them the same width.
		Spring x = Spring.constant(initialX);
		for (int c = 0; c < cols; c++) {
			Spring width = Spring.constant(0);
			for (int r = 0; r < rows; r++) {
				width = Spring.max(width, getConstraintsForCell(r, c, parent,
						cols).getWidth());
			}
			for (int r = 0; r < rows; r++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r,
						c, parent, cols);
				constraints.setX(x);
				constraints.setWidth(width);
			}
			x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
		}

		// Align all cells in each row and make them the same height.
		Spring y = Spring.constant(initialY);
		for (int r = 0; r < rows; r++) {
			Spring height = Spring.constant(0);
			for (int c = 0; c < cols; c++) {
				height = Spring.max(height, getConstraintsForCell(r, c, parent,
						cols).getHeight());
			}
			for (int c = 0; c < cols; c++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r,
						c, parent, cols);
				constraints.setY(y);
				constraints.setHeight(height);
			}
			y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
		}

		// Set the parent's size.
		SpringLayout.Constraints pCons = layout.getConstraints(parent);
		pCons.setConstraint(SpringLayout.SOUTH, y);
		pCons.setConstraint(SpringLayout.EAST, x);
	}

	public static void makeOneLineGrid(final Container parent) {
		makeCompactGrid(parent, 1, parent.getComponentCount(), 10, 0, 10, 10);
	}

	public static void makeTopOneLineGrid(final Container parent) {
		makeCompactGrid(parent, 1, parent.getComponentCount(), 10, 10, 10, 10);
	}

	// private static final Image CHECKED = Activator.getImageDescriptor(
	// "icons/checked.gif").createImage();
	// private static final Image UNCHECKED = Activator.getImageDescriptor(
	// "icons/unchecked.gif").createImage();

	public static void copytoClipboard(final String fileName) {
		Display display = Display.getCurrent();
		Clipboard clipboard = new Clipboard(display);
		String[] data = { fileName };
		clipboard.setContents(new Object[] { data },
				new Transfer[] { FileTransfer.getInstance() });
		clipboard.dispose();
	}

	public static SimpleMenuConfiguration getMenuConfiguration() {
		SimpleMenuConfiguration config = new SimpleMenuConfiguration();
		return config;
	}

	public static void saveFile(final String fileName, final String data) {
		if (fileName != null) {
			FileWriter file = null;
			try {
				file = new FileWriter(fileName);
				file.write(data);
				file.close();
			} catch (IOException e) {
				showError(e);
			} finally {
				try {
					if (file != null)
						file.close();
				} catch (IOException e) {
					showError(e);
				}
			}
		}
	}

	// public static Image loadImage(String fileName) {
	// Image image = null;
	// try {
	// InputStream sourceStream = getResourceAsStream(fileName);
	// ImageData source = new ImageData(sourceStream);
	// ImageData mask = source.getTransparencyMask();
	// image = new Image(display, source, mask);
	// sourceStream.close();
	// } catch (IOException e) {
	// showError(e);
	// }
	// return image;
	// }

	public static String readFile(final InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		StringBuffer buffer = new StringBuffer();
		String line;
		String lineDelimiter = "\n";
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append(lineDelimiter);
		}
		return buffer.toString();
	}

	public static Version getVersion(final String appName) {
		return new Version();
	}

	private static MessageBox errorMessageBox = null;
	private static MessageBox successMessageBox = null;

	static {
		errorMessageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		successMessageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
	}

	public static void showSuccessMessage(final String message) {
		successMessageBox.setText("Information");
		successMessageBox.setMessage(message);
		successMessageBox.open();
	}

	public static void showErrorMessage(final Exception ex) {
		ex.printStackTrace();
		showErrorMessage(ex.getMessage());
	}

	public static void showErrorMessage(final String message) {
		errorMessageBox.setText("Error");
		errorMessageBox.setMessage(message);
		errorMessageBox.open();
	}

	public static void reportError(final String context, final Exception ex) {
		if (shell == null) {
			Display display = new Display();
			shell = new Shell(display);
		}
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES
				| SWT.NO);
		messageBox
				.setMessage("An error occured, would you like to send this error by email ?");
		messageBox.setText("Exiting Application");
		if (messageBox.open() == SWT.YES) {
			try {
				String LINE_BREAK = System.getProperty("line.separator");
				StringBuffer buf = new StringBuffer();
				buf.append(context).append(LINE_BREAK).append(LINE_BREAK);
				buf.append(ex.getMessage()).append(LINE_BREAK).append(
						LINE_BREAK);
				ByteArrayOutputStream out = new ByteArrayOutputStream(10000);
				PrintWriter writer = new PrintWriter(out);
				ex.printStackTrace(writer);
				writer.close();
				out.close();
				buf.append(out.toString());
				String body = buf.toString().replace(LINE_BREAK, "%0A");
				Program
						.launch("mailto:ernan@gmx.co.uk?subject=[Error report]&body="
								+ body);
			} catch (Exception ex1) {
			}
			;
		}
	}

	public static String getProductName() {
		return "Trade Maker";
	}

	public static String normalizePath(final String path) {
		String resultPath = path.replace('/', File.separatorChar).replace('\\',
				File.separatorChar);
		return resultPath;
	}

	public static void Log(String... toLog) {
		StringBuffer buf = new StringBuffer();
		for (String s : toLog) {
			buf.append(s);
		}
		System.out.println(buf);
	}

	public static boolean yesNoMessageBox(Shell shell, String msgBoxTitle,
			String message) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		messageBox.setMessage(message);
		messageBox.setText(msgBoxTitle);
		return (messageBox.open() == SWT.YES);
	}

}
