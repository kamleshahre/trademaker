package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SuperLabel extends Composite {
	private Label label;

	public SuperLabel(final Composite parent, final int style) {
		super(parent, SWT.NONE);// checkStyle(style));
		initialize(style);
	}

	private void initialize(final int style) {
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 2;
		layout.marginWidth = 4;
		layout.numColumns = 1;
		label = new Label(this, style);
		int gridDataStyle = GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL
				| GridData.FILL_BOTH;
		if ((style & SWT.TOP) != 0)
			gridDataStyle |= GridData.VERTICAL_ALIGN_BEGINNING;
		if ((style & SWT.BOTTOM) != 0)
			gridDataStyle |= GridData.VERTICAL_ALIGN_END;
		if (((style & SWT.CENTER) != 0) && ((style & SWT.HORIZONTAL) != 0))
			gridDataStyle |= GridData.HORIZONTAL_ALIGN_CENTER;
		if (((style & SWT.CENTER) != 0) && ((style & SWT.VERTICAL) != 0))
			gridDataStyle |= GridData.VERTICAL_ALIGN_CENTER;
		setLayout(layout);
		GridData gd = new GridData(gridDataStyle);
		label.setLayoutData(gd);
	}

	public void setText(final String string) {
		label.setText(string);
		layout();
	}

	public String getText() {
		return label.getText();
	}

	public static int checkStyle(final int style) {
		return style;
	}

	public void setBackground(final Color color) {
		label.setBackground(color);
		super.setBackground(color);
	}

	public void setForeground(final Color color) {
		label.setForeground(color);
		super.setForeground(color);
	}

	public Point computeSize(final int wHint, final int hHint) {
		int borderWidth = getBorderWidth();
		GC gc = new GC(label);
		int wSpacer = gc.stringExtent("33").x; //$NON-NLS-1$
		int hSpacer = gc.stringExtent("Q").y; //$NON-NLS-1$
		gc.dispose();
		// int wSpacer = label.getBounds().width ;
		// int hSpacer = label.getBounds().height;
		int width = Math.max(wHint, wSpacer + 2 * borderWidth + 8);
		int height = Math.max(hHint, hSpacer + 2 * borderWidth);
		return new Point(width, height);
	}

	public void addControlListener(final ControlListener listener) {
		label.addControlListener(listener);
		super.addControlListener(listener);
	}

	public void addFocusListener(final FocusListener listener) {
		label.addFocusListener(listener);
		super.addFocusListener(listener);
	}

	public void addHelpListener(final HelpListener listener) {
		label.addHelpListener(listener);
		super.addHelpListener(listener);
	}

	public void addKeyListener(final KeyListener listener) {
		label.addKeyListener(listener);
		super.addKeyListener(listener);
	}

	public void addMouseListener(final MouseListener listener) {
		label.addMouseListener(listener);
		super.addMouseListener(listener);
	}

	public void addMouseMoveListener(final MouseMoveListener listener) {
		label.addMouseMoveListener(listener);
		super.addMouseMoveListener(listener);
	}

	public void addMouseTrackListener(final MouseTrackListener listener) {
		label.addMouseTrackListener(listener);
		super.addMouseTrackListener(listener);
	}

	public void addPaintListener(final PaintListener listener) {
		label.addPaintListener(listener);
		super.addPaintListener(listener);
	}

	public void addTraverseListener(final TraverseListener listener) {
		label.addTraverseListener(listener);
		super.addTraverseListener(listener);
	}

	public void removeControlListener(final ControlListener listener) {
		label.removeControlListener(listener);
		super.removeControlListener(listener);
	}

	public void removeFocusListener(final FocusListener listener) {
		label.removeFocusListener(listener);
		super.removeFocusListener(listener);
	}

	public void removeHelpListener(final HelpListener listener) {
		label.removeHelpListener(listener);
		super.removeHelpListener(listener);
	}

	public void removeKeyListener(final KeyListener listener) {
		label.removeKeyListener(listener);
		super.removeKeyListener(listener);
	}

	public void removeMouseListener(final MouseListener listener) {
		label.removeMouseListener(listener);
		super.removeMouseListener(listener);
	}

	public void removeMouseMoveListener(final MouseMoveListener listener) {
		label.removeMouseMoveListener(listener);
		super.removeMouseMoveListener(listener);
	}

	public void removeMouseTrackListener(final MouseTrackListener listener) {
		label.removeMouseTrackListener(listener);
		super.removeMouseTrackListener(listener);
	}

	public void removePaintListener(final PaintListener listener) {
		label.removePaintListener(listener);
		super.removePaintListener(listener);
	}

	public void removeTraverseListener(final TraverseListener listener) {
		label.removeTraverseListener(listener);
		super.removeTraverseListener(listener);
	}

	public String toString() {
		return super.toString() + ":" + label.getText();
	}
}
