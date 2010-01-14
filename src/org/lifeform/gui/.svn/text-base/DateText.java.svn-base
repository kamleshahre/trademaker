package org.lifeform.gui;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DateText extends Canvas {

	private Text text;

	public DateText(final Composite parent, final int style) {
		super(parent, checkStyle(style));
		FillLayout layout = new FillLayout();
		setLayout(layout);
		text = new Text(this, style);
	}

	private static int checkStyle(final int style) {
		return style;
	}

	public String getText() {
		return text.getText();
	}

}
