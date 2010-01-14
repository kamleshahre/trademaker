package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Child window to show messages
 */
public class MessageWindow {
	private Shell m_child = null;
	private Label m_icon = null;
	private Label m_message = null;

	MessageWindow(final Shell parent, final Image icon, final String message) {
		m_child = new Shell(parent);
		m_child.setText("Trade Maker");
		// center positioning window
		int width = 400;
		int height = 100;
		m_child.setSize(width, height);
		int x = (parent.getDisplay().getClientArea().width) / 2;
		int y = (parent.getDisplay().getClientArea().height) / 2;
		m_child.setLocation(x - width / 2, y - height / 2);
		// add icon
		FormData data = null;
		m_child.setLayout(new FormLayout());
		m_icon = new Label(m_child, SWT.NONE);
		if (icon != null) {
			m_icon.setImage(icon);
		}
		data = new FormData();
		data.top = new FormAttachment(0, 15);
		data.left = new FormAttachment(0, 20);
		m_icon.setLayoutData(data);

		// add message
		m_message = new Label(m_child, SWT.NONE);
		m_message.setText(message);
		data = new FormData();
		data.top = new FormAttachment(0, 15);
		data.left = new FormAttachment(m_icon, 15);
		data.right = new FormAttachment(100, -10);
		m_message.setLayoutData(data);

	}

	public void open() {
		m_child.open();
	}

	public void close() {
		m_child.close();
		m_child.dispose();
	}
}
