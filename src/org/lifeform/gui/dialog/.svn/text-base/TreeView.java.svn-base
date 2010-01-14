package org.lifeform.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeView {
	Display display;
	Shell shell;

	TreeView() {
		display = new Display();
		shell = new Shell(display);
		shell.setSize(230, 220);
		shell.setText("Tree");
		shell.setLayout(new FillLayout());

		Tree tree = new Tree(shell, SWT.SINGLE | SWT.BORDER);
		TreeItem item1 = new TreeItem(tree, SWT.NONE, 0);
		item1.setText("Task1");
		TreeItem item1a = new TreeItem(item1, SWT.NONE, 0);
		item1a.setText("Task 1.1");
		TreeItem item1b = new TreeItem(item1, SWT.NONE, 1);
		item1b.setText("Task 1.2");

		TreeItem item2 = new TreeItem(tree, SWT.NONE, 1);
		item2.setText("Task2");
		TreeItem item2a = new TreeItem(item2, SWT.NONE, 0);
		item2a.setText("Task 2.1");
		TreeItem item2a1 = new TreeItem(item2a, SWT.NONE, 0);
		item2a1.setText("Task 2.1.1");
		TreeItem item2a2 = new TreeItem(item2a, SWT.NONE, 1);
		item2a2.setText("Task 2.1.2");
		TreeItem item2b = new TreeItem(item2, SWT.NONE, 1);
		item2b.setText("Task 2.2");
		TreeItem item2c = new TreeItem(item2, SWT.NONE, 2);
		item2c.setText("Task 2.3");

		TreeItem item3 = new TreeItem(tree, SWT.NONE, 2);
		item3.setText("Task3");
		TreeItem item3a = new TreeItem(item3, SWT.NONE, 0);
		item3a.setText("Task 3.1");
		TreeItem item3b = new TreeItem(item3, SWT.NONE, 1);
		item3b.setText("Task 3.2");

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static void main(final String[] argv) {
		new TreeView();
	}
}
