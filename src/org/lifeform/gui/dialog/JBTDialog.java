package org.lifeform.gui.dialog;

import java.awt.Frame;
import java.awt.Point;

import javax.swing.JDialog;

public class JBTDialog extends JDialog {
	public static final double MAC_MENUBAR_HEIGHT = 22;

	public JBTDialog(final Frame owner) {
		super(owner);
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		// This is to prevent the dialog from being drawn under the Mac menu
		// bar.
		boolean onMac = System.getProperty("os.name").toLowerCase().startsWith(
				"mac os x");
		if (onMac) {
			Point point = getLocation();
			if (point.getY() < MAC_MENUBAR_HEIGHT) {
				point.translate(0, (int) (MAC_MENUBAR_HEIGHT - point.getY()));
			}
			setLocation(point);
		}
	}
}
