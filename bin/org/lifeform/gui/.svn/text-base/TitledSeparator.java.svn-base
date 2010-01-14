package org.lifeform.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSeparator;

public class TitledSeparator extends JPanel {
	private static final long serialVersionUID = 7128505233225085516L;

	public TitledSeparator(final Component component) {
		component.setFont(component.getFont().deriveFont(Font.BOLD));
		setLayout(new GridBagLayout());
		add(component);
		GridBagConstraints constrants = new GridBagConstraints();
		constrants.anchor = GridBagConstraints.WEST;
		constrants.fill = GridBagConstraints.HORIZONTAL;
		constrants.weightx = 1;
		constrants.insets = new Insets(0, 5, 0, 0);
		add(new JSeparator(), constrants);
	}
}
