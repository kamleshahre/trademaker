/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ernan Hughes - initial API and implementation
 *******************************************************************************/
package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolbarShellExample {
	public static void main(final String[] a) {
		new ToolbarShellExample();
	}

	Display d;

	Shell shell;

	ToolbarShellExample() {
		d = new Display();
		shell = new Shell(d);
		shell.setSize(300, 100);
		shell.setText("Trade Maker");

		final ToolBar bar = new ToolBar(shell, SWT.HORIZONTAL);
		bar.setSize(300, 70);
		bar.setLocation(0, 0);
		String path = "F:\\Projects\\TradeMaker\\src\\res\\";

		// create images for toolbar buttons
		final Image saveIcon = new Image(d, path + "saveall.png");
		final Image openIcon = new Image(d, path + "open.png");
		final Image cutIcon = new Image(d, path + "quote.png");
		final Image copyIcon = new Image(d, path + "refresh.png");
		final Image pasteIcon = new Image(d, path + "sm_up.png");

		// create and add the button for performing an open operation
		final ToolItem openToolItem = new ToolItem(bar, SWT.PUSH);
		openToolItem.setImage(openIcon);
		openToolItem.setText("Open");
		openToolItem.setToolTipText("Open File");

		// create and add the button for performing a save operation
		final ToolItem saveToolItem = new ToolItem(bar, SWT.PUSH);
		saveToolItem.setImage(saveIcon);
		saveToolItem.setText("Save");
		saveToolItem.setToolTipText("Save File");

		// create and add the button for performing a cut operation
		final ToolItem cutToolItem = new ToolItem(bar, SWT.PUSH);
		cutToolItem.setImage(cutIcon);
		cutToolItem.setText("Cut");
		cutToolItem.setToolTipText("Cut");

		// create and add the button for performing a copy operation
		final ToolItem copyToolItem = new ToolItem(bar, SWT.PUSH);
		copyToolItem.setImage(copyIcon);
		copyToolItem.setText("Copy");
		copyToolItem.setToolTipText("Copy");

		// create and add the button for performing a paste operation
		final ToolItem pasteToolItem = new ToolItem(bar, SWT.PUSH);
		pasteToolItem.setImage(pasteIcon);
		pasteToolItem.setText("Paste");
		pasteToolItem.setToolTipText("Paste");

		// create the menu
		Menu m = new Menu(shell, SWT.BAR);

		// create a file menu and add an exit item
		final MenuItem file = new MenuItem(m, SWT.CASCADE);
		file.setText("&File");
		final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(filemenu);
		final MenuItem openMenuItem = new MenuItem(filemenu, SWT.PUSH);
		openMenuItem.setText("&Open\tCTRL+O");
		openMenuItem.setAccelerator(SWT.CTRL + 'O');
		openMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				System.out.println("Open");
			}
		});

		final MenuItem saveMenuItem = new MenuItem(filemenu, SWT.PUSH);
		saveMenuItem.setText("&Save\tCTRL+S");
		saveMenuItem.setAccelerator(SWT.CTRL + 'S');
		final MenuItem exitMenuItem = new MenuItem(filemenu, SWT.PUSH);
		exitMenuItem.setText("E&xit");

		// create an edit menu and add cut copy and paste items
		final MenuItem edit = new MenuItem(m, SWT.CASCADE);
		edit.setText("&Edit");
		final Menu editmenu = new Menu(shell, SWT.DROP_DOWN);
		edit.setMenu(editmenu);
		final MenuItem cutMenuItem = new MenuItem(editmenu, SWT.PUSH);
		cutMenuItem.setText("&Cut");
		final MenuItem copyMenuItem = new MenuItem(editmenu, SWT.PUSH);
		copyMenuItem.setText("Co&py");
		final MenuItem pasteMenuItem = new MenuItem(editmenu, SWT.PUSH);
		pasteMenuItem.setText("&Paste");

		// create a Window menu and add Child item
		final MenuItem window = new MenuItem(m, SWT.CASCADE);
		window.setText("&Window");
		final Menu windowmenu = new Menu(shell, SWT.DROP_DOWN);
		window.setMenu(windowmenu);
		final MenuItem maxMenuItem = new MenuItem(windowmenu, SWT.PUSH);
		maxMenuItem.setText("Ma&ximize");
		final MenuItem minMenuItem = new MenuItem(windowmenu, SWT.PUSH);
		minMenuItem.setText("Mi&nimize");

		// create a Help menu and add an about item
		final MenuItem help = new MenuItem(m, SWT.CASCADE);
		help.setText("&Help");
		final Menu helpmenu = new Menu(shell, SWT.DROP_DOWN);
		help.setMenu(helpmenu);
		final MenuItem aboutMenuItem = new MenuItem(helpmenu, SWT.PUSH);
		aboutMenuItem.setText("&About");
		aboutMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				Browser browser = null;
				Shell shell = new Shell(d);
				shell.setLayout(new FillLayout());
				try {
					browser = new Browser(shell, SWT.NONE);
				} catch (SWTError ex) {
					/*
					 * The Browser widget throws an SWTError if it fails to
					 * instantiate properly. Application code should catch this
					 * SWTError and disable any feature requiring the Browser
					 * widget. Platform requirements for the SWT Browser widget
					 * are available from the SWT FAQ website.
					 */
				}
				if (browser != null) {
					/* The Browser widget can be used */
					browser.setUrl("http://www.eclipse.org");
				}
				shell.open();
				while (!shell.isDisposed()) {
					if (!d.readAndDispatch())
						d.sleep();
				}
			}
		});

		saveMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				System.out.println("Save");
			}
		});

		exitMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				System.exit(0);
			}
		});

		cutMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				System.out.println("Cut");
			}
		});

		copyMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				System.out.println("Copy");
			}
		});

		pasteMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				System.out.println("Paste");
			}
		});

		maxMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				Shell parent = (Shell) maxMenuItem.getParent().getParent();
				parent.setMaximized(true);
			}
		});

		minMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
				Shell parent = (Shell) minMenuItem.getParent().getParent();
				parent.setMaximized(false);
			}
		});

		shell.setMenuBar(m);

		shell.open();
		while (!shell.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		d.dispose();
	}
}
