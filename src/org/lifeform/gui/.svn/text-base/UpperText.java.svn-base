package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class UpperText extends Composite {

	public UpperText(final Composite parent) {
		this(parent, SWT.NONE);
	}

	public UpperText(final Composite parent, final int style) {
		super(parent, style);
		setLayout(new FillLayout());
		Text text = new Text(this, SWT.MULTI | SWT.V_SCROLL);

		text.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				if (e.text.startsWith("1")) {
					e.doit = false;
				} else {
					e.text = e.text.toUpperCase();
				}
			}
		});
	}
}
