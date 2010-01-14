package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.lifeform.util.ClassFinder;

public class StrategyCombo extends Composite {

	public StrategyCombo(final Composite parent) {
		this(parent, SWT.NONE);
	}

	public StrategyCombo(final Composite parent, final int style) {
		super(parent, style);
		setLayout(new FillLayout());
		Combo combo = new Combo(this, SWT.DROP_DOWN);
		String[] strategies;
		try {
			strategies = new ClassFinder().getStrategyNames();
			for (String n : strategies) {
				combo.add(n);
			}
		} catch (Exception e) {
		}
	}

}
