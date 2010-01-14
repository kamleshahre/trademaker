package org.lifeform.startup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class SnippetTest {

	public static void main(final String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		shell.setLayout(layout);

		Menu menuBar, fileMenu, helpMenu;
		menuBar = new Menu(shell, SWT.BAR);
		MenuItem fileMenuHeader, helpMenuHeader;

		MenuItem fileExitItem, fileSaveItem, helpGetHelpItem;
		menuBar = new Menu(shell, SWT.BAR);
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");
		fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);
		fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveItem.setText("&Save");
		fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("E&xit");

		helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);

		helpMenuHeader.setText("&Help");

		helpMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);

		helpMenuHeader.setText("&Help");

		helpMenu = new Menu(shell, SWT.DROP_DOWN);

		helpMenuHeader.setMenu(helpMenu);

		helpGetHelpItem = new MenuItem(helpMenu, SWT.PUSH);

		helpGetHelpItem.setText("&Get Help");
		shell.setMenuBar(menuBar);

		ToolBar toolbar = new ToolBar(shell, SWT.NONE);
		ToolItem item1 = new ToolItem(toolbar, SWT.PUSH);
		item1.setText("Now");

		ToolItem item2 = new ToolItem(toolbar, SWT.PUSH);
		item2.setText("is");

		ToolItem item3 = new ToolItem(toolbar, SWT.PUSH);
		item3.setText("the");

		ToolItem item4 = new ToolItem(toolbar, SWT.PUSH);
		item4.setText("time");
		toolbar.setBounds(0, 0, 200, 70);
		final Text text2 = new Text(shell, SWT.BORDER);

		text2.setBounds(0, 100, 200, 25);
		Listener listener = new Listener() {

			public void handleEvent(final Event event) {

				ToolItem item = (ToolItem) event.widget;

				String string = item.getText();

				text2.setText("You selected:" + string);
			}

		};

		item1.addListener(SWT.Selection, listener);

		item2.addListener(SWT.Selection, listener);

		item3.addListener(SWT.Selection, listener);

		item4.addListener(SWT.Selection, listener);

		final Button button = new Button(shell, SWT.PUSH);
		button.setText("Click Me");
		final Text text = new Text(shell, SWT.SHADOW_IN);
		button.addSelectionListener(new SelectionListener() {
			public void widgetSelected(final SelectionEvent event) {
				text.setText("No worries!");
			}

			public void widgetDefaultSelected(final SelectionEvent event) {
				text.setText("No worries!");
			}
		});

		final Label label = new Label(shell, SWT.BORDER);

		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setSize(200, 400);
		for (int i = 0; i < 12; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("Item " + i);
		}

		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				int[] selections = table.getSelectionIndices();
				String outText = "";

				for (int loopIndex = 0; loopIndex < selections.length;

				loopIndex++)
					outText += selections[loopIndex] + " ";

				System.out.println("You selected: " + outText);

			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {
				int[] selections = table.getSelectionIndices();

				String outText = "";

				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)

					outText += selections[loopIndex] + " ";

				System.out.println("You selected: " + outText);

			}
		});
		TableItem item = new TableItem(table, SWT.NONE, 1);
		item.setText("*** New Item " + table.indexOf(item) + " ***");
		shell.pack();
		shell.open();
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				label.setText("No worries!");
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
