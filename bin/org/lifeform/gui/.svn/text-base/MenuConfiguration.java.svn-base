package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.lifeform.util.Configuration;

public class MenuConfiguration extends Configuration {

	public Menu getMenu(final Shell shell) {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		for (Object val : getKeys()) {
			String name = String.valueOf(val);
			MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);

		}
		return null;
	}
}
