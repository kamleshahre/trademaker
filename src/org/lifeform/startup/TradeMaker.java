package org.lifeform.startup;

import java.io.File;
import java.util.Calendar;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.lifeform.application.LifeformApp;
import org.lifeform.application.LifeformAppException;
import org.lifeform.gui.dialog.AboutDialog;
import org.lifeform.gui.dialog.ProgressBarDialog;
import org.lifeform.util.AppUtil;

public class TradeMaker extends LifeformApp {

	public TradeMaker() throws LifeformAppException {
		super(TradeMaker.class);
	}

	@Override
	public void createView() {
		final Grid grid = new Grid(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		grid.setHeaderVisible(true);
		grid.setFooterVisible(true);

		GridColumn column = new GridColumn(grid, SWT.NONE);
		column.setText("Activated");
		column.setWidth(20);

		GridColumn column2 = new GridColumn(grid, SWT.NONE);
		column2.setText("Strategy");
		column2.setWidth(150);

		GridColumn column3 = new GridColumn(grid, SWT.NONE);
		column3.setText("Ticker");
		column3.setWidth(30);

		GridColumn column4 = new GridColumn(grid, SWT.NONE);
		column4.setText("Bar Size");
		column4.setWidth(40);

		GridColumn column5 = new GridColumn(grid, SWT.NONE);
		column5.setText("Last Bar Time");
		column5.setWidth(120);

		GridColumn column6 = new GridColumn(grid, SWT.NONE);
		column6.setText("Last Bar Close");
		column6.setWidth(60);

		GridColumn column7 = new GridColumn(grid, SWT.NONE);
		column7.setText("Position");
		column7.setWidth(30);

		GridColumn column8 = new GridColumn(grid, SWT.NONE);
		column8.setText("Trades");
		column8.setWidth(30);

		/*
		 * columnModel.getColumn(0).setPreferredWidth(20); // Activation column
		 * columnModel.getColumn(1).setPreferredWidth(150); // strategy name
		 * column columnModel.getColumn(2).setPreferredWidth(30); // ticker
		 * column columnModel.getColumn(3).setPreferredWidth(40); // bar size
		 * column columnModel.getColumn(4).setPreferredWidth(120); // last bar
		 * time column columnModel.getColumn(5).setPreferredWidth(60); // last
		 * bar close column columnModel.getColumn(6).setPreferredWidth(30); //
		 * Position columnModel.getColumn(7).setPreferredWidth(30); // Trades
		 * columnModel.getColumn(8).setPreferredWidth(30); // P&L
		 * columnModel.getColumn(9).setPreferredWidth(30); // Max DD
		 * columnModel.getColumn(10).setPreferredWidth(30); // PF
		 * columnModel.getColumn(11).setPreferredWidth(30); // Kelly
		 * columnModel.getColumn(12).setPreferredWidth(120); // Trade
		 * distribution
		 */
		// GridColumn column8 = new GridColumn(grid, SWT.NONE);
		// column2.setText("Trades");
		// column2.setWidth(30);
		//
		// GridColumn column8 = new GridColumn(grid, SWT.NONE);
		// column2.setText("Trades");
		// column2.setWidth(30);
		//
		// GridColumn column8 = new GridColumn(grid, SWT.NONE);
		// column2.setText("Trades");
		// column2.setWidth(30);

		for (int i = 0; i < 100; i++) {
			GridItem item1 = new GridItem(grid, SWT.CHECK);
			item1.setText("First Item");
			item1.setText(1, "xxxxxxx");
			GridItem item2 = new GridItem(grid, SWT.NONE);
			item2.setText("This cell spans both columns");
			item1.setText(1, "xxxxxxx");
			item2.setColumnSpan(0, 1);
			GridItem item3 = new GridItem(grid, SWT.NONE);
			item3.setText("Third Item");
			item1.setText(1, "xxxxxxx");
		}

		grid.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if (e.y < grid.getHeaderHeight()) {
					System.out.println("header click");
				}
			}

		});

	}

	@Override
	public void createToolbar() {
		toolBar = new ToolBar(shell, SWT.FLAT | SWT.BORDER);

		Image image = new Image(display, 16, 16);
		Color color = display.getSystemColor(SWT.COLOR_RED);
		GC gc = new GC(image);
		gc.setBackground(color);
		gc.fillRectangle(image.getBounds());
		gc.dispose();

		final ToolItem itemBack = new ToolItem(toolBar, SWT.PUSH);
		itemBack.setText(getResourceString("Run"));
		itemBack.setImage(image);

		final ToolItem itemForward = new ToolItem(toolBar, SWT.PUSH);
		itemForward.setText(getResourceString("Chart"));

		final ToolItem itemStop = new ToolItem(toolBar, SWT.PUSH);
		itemStop.setText(getResourceString("Update"));
		itemStop.setImage(image);

		final ToolItem itemRefresh = new ToolItem(toolBar, SWT.PUSH);
		itemRefresh.setText(getResourceString("Refresh"));

		final ToolItem itemGo = new ToolItem(toolBar, SWT.PUSH);
		itemGo.setText(getResourceString("Exit"));
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem) event.widget;
			}
		};
		itemBack.addListener(SWT.Selection, listener);
		itemForward.addListener(SWT.Selection, listener);
		itemStop.addListener(SWT.Selection, listener);
		itemRefresh.addListener(SWT.Selection, listener);
		itemGo.addListener(SWT.Selection, listener);
		toolBar.pack();
	}

	@Override
	public void createMenu() {
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem itemFile = new MenuItem(menuBar, SWT.CASCADE);
		// Menu file
		itemFile.setText("&File");
		Menu menu = new Menu(itemFile);

		// Menu backup
		MenuItem itemBackup = new MenuItem(menu, SWT.PUSH);
		itemBackup.setText("&Backup");
		itemBackup.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DAY_OF_MONTH);
				dialog.setFileName("backup_pkb_" + year
						+ (month < 10 ? "0" + month : "" + month)
						+ (day < 10 ? "0" + day : "" + day) + ".zip");
				dialog.setFilterNames(new String[] { "Zip Files",
						"All Files (*.*)" });
				dialog.setFilterExtensions(new String[] { "*.zip", "*.*" });
				String fileName = dialog.open();
				if (fileName != null && fileName.trim().length() > 0) {
					File file = new File(fileName);
					if (file.exists()) {
						MessageBox messageBox = new MessageBox(shell,
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						messageBox
								.setMessage("File already exsists, do you want to replace?");
						messageBox.setText("Backup");
						int response = messageBox.open();
						if (response != SWT.YES) {
							return;
						}
					}
					try {
						ProgressBarDialog pb = new ProgressBarDialog(shell);
						// pb.registerProcessThread(new BackupThread(100, pb,
						// fileName));
						pb.open();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					AppUtil
							.showSuccessMessage("Database is succesfully backuped!");
				}
			}
		});

		// Menu restore
		MenuItem itemRestore = new MenuItem(menu, SWT.PUSH);
		itemRestore.setText("&Restore");
		itemRestore.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterNames(new String[] { "Zip Files",
						"All Files (*.*)" });
				dialog.setFilterExtensions(new String[] { "*.zip", "*.*" });
				String fileName = dialog.open();
				if (fileName != null && fileName.trim().length() > 0) {
					MessageBox messageBox = new MessageBox(shell,
							SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					messageBox
							.setMessage("Current database will be replaced by a backup, do you want to restore?");
					messageBox.setText("Restore");
					int response = messageBox.open();
					if (response != SWT.YES) {
						return;
					}
					try {
						ProgressBarDialog pb = new ProgressBarDialog(shell);
						// pb.registerProcessThread(new RestoreThread(100, pb,
						// fileName));
						pb.open();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					AppUtil
							.showSuccessMessage("Database is succesfully restored!");
					// MainScreen.Widget.getKnowledgeNavPanel()
					// .getKnowledgeTreeComponent().init();
					// MainScreen.Widget.getKnowledgeNavPanel()
					// .refreshAutoCompleteKeywords();
				}
			}
		});

		itemFile.setMenu(menu);

		// separator
		new MenuItem(menu, SWT.SEPARATOR);
		// export
		MenuItem itemExport = new MenuItem(menu, SWT.PUSH);
		itemExport.setText("Export");
		itemExport.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				dialog
						.setMessage("Please choose a directory to export the HTML version of knowledge base:");
				dialog.setFilterPath(System.getProperty("user.dir"));
				String dirName = dialog.open();
				if (dirName != null && dirName.trim().length() > 0) {
					try {
						ProgressBarDialog pb = new ProgressBarDialog(shell);
						// pb.registerProcessThread(new ExportThread(100, pb,
						// dirName));
						pb.open();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					AppUtil.showSuccessMessage("Export done!");
				}
			}
		});

		// Menu Rebuild Index
		MenuItem itemRebuildIndex = new MenuItem(menu, SWT.PUSH);
		itemRebuildIndex.setText("Rebuild &index");
		itemRebuildIndex.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				try {
					ProgressBarDialog pb = new ProgressBarDialog(shell);
					// pb.registerProcessThread(new RebuildIndexThread(100,
					// pb));
					pb.open();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				AppUtil.showSuccessMessage("Index is updated!");
				// MainScreen.Widget.getKnowledgeNavPanel()
				// .refreshAutoCompleteKeywords();
			}
		});

		// separator
		new MenuItem(menu, SWT.SEPARATOR);

		// preference
		MenuItem itemPreference = new MenuItem(menu, SWT.PUSH);
		itemPreference.setText("&Preference");
		itemPreference.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				// PreferencesDialog preferenceDlg = new
				// PreferencesDialog(shell);
				// preferenceDlg.open();
			}
		});

		// separator
		new MenuItem(menu, SWT.SEPARATOR);
		MenuItem itemExit = new MenuItem(menu, SWT.PUSH);
		itemExit.setText("&Exit");
		itemExit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				exit();
			}
		});

		itemFile.setMenu(menu);

		MenuItem itemHelp = new MenuItem(menuBar, SWT.CASCADE);
		itemHelp.setText("&Help");
		menu = new Menu(itemHelp);

		MenuItem itemHelpContents = new MenuItem(menu, SWT.PUSH);
		itemHelpContents.setText("&Home Page");
		itemHelpContents.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				try {
					String url = "http://code.google.com/p/trademaker/";
					AppUtil.openURL(url);
				} catch (Exception t) {
					AppUtil.showError(t);
				}
			}
		});

		MenuItem itemAbout = new MenuItem(menu, SWT.PUSH);
		itemAbout.setText("&About");
		itemAbout.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				AboutDialog aboutDlg = new AboutDialog(shell);
				aboutDlg.showDialog();
			}
		});
		itemHelp.setMenu(menu);

		shell.setMenuBar(menuBar);
	}

	public static void main(final String[] args) {
		try {
			TradeMaker tm = new TradeMaker();
			tm.showWindow(args);
		} catch (UnsatisfiedLinkError ulex) {
			AppUtil.showLinkError(ulex);
		} catch (Exception ex) {
			AppUtil.showError(ex);
		}
		System.exit(0);
	}

}
