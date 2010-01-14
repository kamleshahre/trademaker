package org.lifeform.gui.dialog;

import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JDialog;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.lifeform.util.AppUtil;
import org.lifeform.util.Version;

import com.ib.client.EClientSocket;

public class AboutDialog extends JDialog {
	private static final long serialVersionUID = -6631487052266397102L;

	private final Shell parentShell;

	public AboutDialog(Shell parentShell) {
		this.parentShell = parentShell;
	}

	public void showDialog() {
		Shell shell = new Shell(parentShell, SWT.DIALOG_TRIM);
		shell.setText("About " + AppUtil.getAppName());
		shell.setLayout(new FillLayout());

		CTabFolder tabFolder = new CTabFolder(shell, SWT.NONE);
		CTabItem abouttem = new CTabItem(tabFolder, SWT.NULL);
		abouttem.setText("About");

		Composite aboutPanel = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		aboutPanel.setLayout(gridLayout);
		abouttem.setControl(aboutPanel);
		tabFolder.setSelection(0);

		createGridRow(aboutPanel, "Product Name:", AppUtil.getAppName());
		Version v = AppUtil.getVersion(null);
		createGridRow(aboutPanel, "Version:", v.getVersionString());
		createGridRow(aboutPanel, "Author:", "Ernan Hughes <ernan@gmx.co.uk>");
		createGridRow(aboutPanel, "Maintainer:",
				"Ernan Hughes <ernan@gmx.co.uk>");
		createGridRow(aboutPanel, "License:", "TBD");

		CTabItem item = new CTabItem(tabFolder, SWT.NULL);
		item.setText("IB API Info");
		Composite apiPanel = new Composite(tabFolder, SWT.NONE);
		apiPanel.setLayout(gridLayout);
		item.setControl(apiPanel);

		createGridRow(apiPanel, "Server Version:", "Disconnected from server");
		// Trader trader = Dispatcher.getTrader();
		// int version = trader.getAssistant().getServerVersion();
		// serverVersion = String.valueOf(version);
		createGridRow(apiPanel, "Client Version:", String
				.valueOf(EClientSocket.CLIENT_VERSION));

		CTabItem sysitem = new CTabItem(tabFolder, SWT.NULL);
		sysitem.setText("API Info");
		Composite systemInfoPanel = new Composite(tabFolder, SWT.V_SCROLL
				| SWT.H_SCROLL);
		systemInfoPanel.setLayout(new FillLayout());
		sysitem.setControl(systemInfoPanel);

		final Grid grid = new Grid(systemInfoPanel, SWT.V_SCROLL | SWT.H_SCROLL);
		grid.setHeaderVisible(true);
		grid.setFooterVisible(true);

		GridColumn column = new GridColumn(grid, SWT.NONE);
		column.setText("Name");
		column.setWidth(100);

		GridColumn column2 = new GridColumn(grid, SWT.NONE);
		column2.setText("Value");
		column2.setWidth(300);

		Properties properties = System.getProperties();
		Enumeration<?> propNames = properties.propertyNames();

		while (propNames.hasMoreElements()) {
			String key = (String) propNames.nextElement();
			String value = properties.getProperty(key);
			GridItem item1 = new GridItem(grid, SWT.NONE);
			item1.setText(0, key);
			item1.setText(1, value);
		}

		grid.setRedraw(true);
		grid.pack();

		shell.setSize(400, 200);
		shell.pack();
		shell.open();
	}

	private void createGridRow(Composite composite, String label, String value) {
		Label productLabel = new Label(composite, SWT.BORDER);
		// productLabel.setLayoutData(new GridData(
		// GridData.VERTICAL_ALIGN_BEGINNING));
		productLabel.setText(label);

		Label productValueLabel = new Label(composite, SWT.CENTER);
		// productLabel
		// .setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		productValueLabel.setText(value);
	}

	public static void main(final String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		AboutDialog dlg = new AboutDialog(shell);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
