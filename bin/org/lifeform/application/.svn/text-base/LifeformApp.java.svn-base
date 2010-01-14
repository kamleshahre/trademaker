package org.lifeform.application;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.lifeform.util.AppUtil;
import org.lifeform.util.Version;

public class LifeformApp {
	protected final URL baseURL;
	protected final ResourceManager resourceManager;
	protected final FontManager fontManager;
	protected final Display display;
	protected boolean disposed;
	protected Shell shell;
	protected ToolBar toolBar = null;

	public String getAppName() {
		return getClass().getSimpleName();
	}

	public LifeformApp(final Class<?> baseClass) throws LifeformAppException {
		this(baseURLFor(baseClass));
	}

	public LifeformApp(final URL baseURL) throws LifeformAppException {
		this.baseURL = baseURL;
		this.display = new Display();
		this.resourceManager = new ResourceManager();
		this.fontManager = new FontManager(display, true);
	}

	public String getResourceString(String name) {
		return name;
	}

	public String getTitle() {
		return getAppName();
	}

	public void loadProperties() {
		try {
			String appName = AppUtil.getAppName();
			File file = new File(System.getProperty("user.dir")
					+ File.separator + appName + ".config");
			if (file.exists()) {
				Properties propFromFile = new Properties();
				propFromFile.load(new FileInputStream(file));
				// overwrite pkbProps
				Iterator<?> itr = propFromFile.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String) itr.next();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void createDocument() {
		String hsqldbPath = System.getProperty("user.dir") + File.separator
				+ "data";
		if (System.getProperty("hsqldb.system.home") == null) {
			System.setProperty("hsqldb.system.home", hsqldbPath);
		}
		AppUtil.Log("hsqldb.system.home: ", hsqldbPath);
		boolean dbInitialized = true;
		try {
			// if (!DBHelper.startDB()) {
			// // recreate all the table
			// MessageBox msgBox = new MessageBox(shell, SWT.ICON_QUESTION
			// | SWT.YES | SWT.NO);
			// msgBox
			// .setMessage("The database is not initialized properly. Do you want to reinitialize the database?");
			// msgBox.setText("Initialize database");
			// if (msgBox.open() == SWT.YES) {
			// // MessageWindow messageWindow = new MessageWindow(shell,
			// // display.getSystemImage(SWT.ICON_WORKING),
			// //
			// "Please wait while PKB is initializing database. \nThis may take some time.");
			// // messageWindow.open();
			// DBHelper.initDB();
			// DBHelper.startDB();
			// // messageWindow.close();
			// showSuccessMessage("The database is initialized!");
			// } else {
			// dbInitialized = false;
			// }
			// }
		} catch (Exception ex) {
			AppUtil.showErrorMessage(ex);
			System.exit(0);
		}
	}

	public void createMenu() {
	}

	public void createView() {
	}

	public void exit() {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		messageBox.setMessage("Do you really want to exit?");
		messageBox.setText("Exiting " + getAppName());
		int response = messageBox.open();
		if (response == SWT.YES) {
			System.exit(0);
		}
	}

	public Version getVersion() {
		Version v = new Version();
		return v;
	}

	private static URL baseURLFor(final Class<?> baseClass) {
		String n = baseClass.getName();
		int sep = n.lastIndexOf('.');
		if (sep != -1)
			n = n.substring(sep + 1);
		return baseClass.getResource(n + ".class");
	}

	public synchronized void dispose() {
		if (disposed)
			return;
		display.dispose();
		disposed = true;
	}

	public Display getDisplay() {
		return display;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public FontManager getFontManager() {
		return fontManager;
	}

	public URL getBaseURL() {
		return baseURL;
	}

	public void createStatusBar() {
	}

	public void createTrayIcon() {
		try {
			final Tray tray = display.getSystemTray();
			if (tray == null) {
				System.out.println("The system tray is not available");
			} else {
				final TrayItem item = new TrayItem(tray, SWT.NONE);
				item.setVisible(false);
				// item.setToolTip(tip);
				item.setToolTipText(AppUtil.getProductName());
				item.addListener(SWT.Show, new Listener() {
					public void handleEvent(final Event event) {
						// show tray
					}
				});
				item.addListener(SWT.Hide, new Listener() {
					public void handleEvent(final Event event) {
						// hide tray
					}
				});
				item.addListener(SWT.Selection, new Listener() {
					public void handleEvent(final Event event) {
						// click tray icon
					}
				});
				item.addListener(SWT.DefaultSelection, new Listener() {
					public void handleEvent(final Event event) {
						// double click to restore main window
						Shell s = event.display.getShells()[0];
						s.setVisible(true);
						item.setVisible(false);
						s.setMinimized(false);
					}
				});
				final Menu popupMenu = new Menu(shell, SWT.POP_UP);
				MenuItem popItem = new MenuItem(popupMenu, SWT.PUSH);
				popItem.setText("Restore");
				popItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(final Event event) {
						// restore main window through popup menu
						Shell s = event.display.getShells()[0];
						s.setVisible(true);
						item.setVisible(false);
						s.setMinimized(false);
					}
				});
				popItem = new MenuItem(popupMenu, SWT.PUSH);
				popItem.setText("Close");
				popItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(final Event event) {
						exit();
					}
				});
				item.addListener(SWT.MenuDetect, new Listener() {
					public void handleEvent(final Event event) {
						popupMenu.setVisible(true);
					}
				});

				shell.addShellListener(new ShellListener() {
					public void shellDeactivated(
							final org.eclipse.swt.events.ShellEvent e) {
					}

					public void shellActivated(
							final org.eclipse.swt.events.ShellEvent e) {
					}

					public void shellClosed(
							final org.eclipse.swt.events.ShellEvent e) {
					}

					public void shellDeiconified(
							final org.eclipse.swt.events.ShellEvent e) {
					}

					public void shellIconified(
							final org.eclipse.swt.events.ShellEvent e) {
					}
				});
			}
		} catch (Exception ex) {
			AppUtil.reportError("System tray initialization error", ex);
		}
	}

	public void createToolbar() {

	}

	public void showWindow(String[] args) throws Exception {
		shell = new Shell(display);
		shell.setText(AppUtil.getProductName());
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;

		shell.setLayout(gridLayout);
		loadProperties();
		createDocument();
		createMenu();
		createToolbar();
		createView();
		createStatusBar();
		shell.pack();
		shell.setSize(500, 300);
		shell.open();
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(final Event event) {
				exit();
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
