package org.lifeform.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.lifeform.util.AppUtil;

public class MainWindow {

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(889, 506);
		shell.setText("Trade Maker");
		Menu menu = createMenu(shell);
		shell.setMenuBar(menu);

	}

	protected Menu createMenu(final Shell shell) {
		SimpleMenuConfiguration config = AppUtil.getMenuConfiguration();
		return config.getMenu(shell);
	}

}
