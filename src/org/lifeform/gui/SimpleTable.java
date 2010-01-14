package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class SimpleTable {

	public SimpleTable(final Composite shell) {
		final Table table = new Table(shell, SWT.SINGLE);
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText("Coloumn 1");
		col1.setWidth(80);
		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText("Coloumn 2");
		col2.setWidth(80);

		TableItem item1 = new TableItem(table, 0);
		item1.setText(new String[] { "a", "b" });
		TableItem item2 = new TableItem(table, 0);
		item2.setText(new String[] { "a", "b" });

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	public void setColumns(final String... columns) {

	}

	public static void main(final String[] args) {
		Shell shell = new Shell();
		shell.setText("Task List - TableViewer Example");

		// Set layout for shell
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);

		// Create a composite to hold the children
		Composite composite = new Composite(shell, SWT.NONE);
		SimpleTable tableViewerExample = new SimpleTable(composite);

		// Open the shell and run until a close event is detected
		shell.open();

		displayMozilla(shell);
		// tableViewerExample.run(shell);
	}

	public static void displayMozilla(final Shell shell) {
		Device.DEBUG = true;
		Display display = new Display();
		System.out.println(">>>Snippet creating SWT.MOZILLA-style Browser");
		try {
			new Browser(shell, SWT.MOZILLA);
			System.out.println(">>>succeeded");
		} catch (Error e) {
			System.out.println(">>>This failed with the following error:");
			e.printStackTrace();
			System.out.println("\n\nSnippet creating SWT.NONE-style Browser");
			try {
				new Browser(shell, SWT.NONE);
				System.out.println(">>>succeeded");
			} catch (Error e2) {
				System.out
						.println(">>>This failed too, with the following error:");
				e2.printStackTrace();
			}
		}
		display.dispose();
	}
}
