package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

class PictureLabelLayout extends Layout {
	Point iExtent, tExtent; // the cached sizes

	protected Point computeSize(final Composite composite, final int wHint,
			final int hHint, final boolean changed) {
		Control[] children = composite.getChildren();
		if (changed || iExtent == null || tExtent == null) {
			iExtent = children[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
			tExtent = children[1].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		}
		int width = iExtent.x + 5 + tExtent.x;
		int height = Math.max(iExtent.y, tExtent.y);
		return new Point(width + 2, height + 2);
	}

	protected void layout(final Composite composite, final boolean changed) {

		Control[] children = composite.getChildren();

		if (changed || iExtent == null || tExtent == null) {

			iExtent = children[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

			tExtent = children[1].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

		}

		children[0].setBounds(1, 1, iExtent.x, iExtent.y);

		children[1].setBounds(iExtent.x + 5, 1, tExtent.x, tExtent.y);

	}

}

public class PictureLabel extends Composite {

	Label image, text;

	Color white;

	PictureLabel(final Composite parent, final int style) {

		super(parent, style);

		white = new Color(null, 255, 255, 255);

		image = new Label(this, 0);

		text = new Label(this, 0);

		setBackground(white);

		text.setBackground(white);

		image.setBackground(white);

		addDisposeListener(new DisposeListener() {

			public void widgetDisposed(final DisposeEvent e) {

				PictureLabel.this.widgetDisposed(e);

			}

		});

		setLayout(new PictureLabelLayout());

	}

	void widgetDisposed(final DisposeEvent e) {

		white.dispose();

	}

	public Image getImage() {

		return image.getImage();

	}

	public void setImage(final Image image) {

		this.image.setImage(image);

		layout(true);

	}

	public String getText() {

		return text.getText();

	}

	public void setText(final String text) {

		this.text.setText(text);

		layout(true);

	}

	public class ImageClickedEvent extends java.util.EventObject {

		public int x, y;

		public ImageClickedEvent(final Object source, final int x, final int y) {

			super(source);

			this.x = x;

			this.y = y;

		}

	}

	public interface ImageClickedListener extends java.util.EventListener {

		public void imageClicked(final ImageClickedEvent event);

	}

	public static void main(final String[] args) {

		Image image = new Image(null, 20, 20);

		Color red = new Color(null, 255, 0, 0);

		GC gc = new GC(image);

		gc.setBackground(red);

		gc.fillRectangle(image.getBounds());

		gc.dispose();

		red.dispose();

		Shell shell = new Shell();

		PictureLabel label = new PictureLabel(shell, 0);

		label.setImage(image);

		label.setText("Hi there!");

		Point size = label.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

		label.setSize(size);

		// label.addImageClickedListener(new ImageClickedListener() {
		//
		// public void imageClicked(ImageClickedEvent event) {
		//
		// ((PictureLabel) event.getSource()).setText("Red!");
		//
		// }
		//
		// });

		shell.open();

		Display display = shell.getDisplay();

		while (!shell.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();

		}

		image.dispose();

	}

}