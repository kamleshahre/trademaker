package org.lifeform.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lifeform.util.AppUtil;

public class Login {
	public Login(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		shell.setText("Login form");
		Label userLbl = new Label(shell, SWT.NULL);
		userLbl.setText("User Name: ");

		final Text username = new Text(shell, SWT.SINGLE | SWT.BORDER);
		username.setText("");
		username.setToolTipText("Enter your user name here");
		username.setTextLimit(30);

		Label passwordLbl = new Label(shell, SWT.NULL);
		passwordLbl.setText("Password: ");

		final Text password = new Text(shell, SWT.SINGLE | SWT.BORDER);
		password.setEchoChar('*');
		password.setToolTipText("Enter your password here");
		password.setTextLimit(30);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Login");
		
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				String user = username.getText();
				String passwd = password.getText();

				if (user == "") {
					AppUtil.showError("Login", "Please enter a user name");
				}
				if (passwd == "") {
					AppUtil.showError("Login", "Please enter a password");
				} else {
					AppUtil.showMessage("Welcome: " + username.getText());
				}
			}
		});
		username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}

	public static void main(final String[] args) {
		new Login(new Shell(new Display()));
	}
}
