/*******************************************************************************
 * Copyright (c) 2005 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

/**
 * An internal shell which can be placed on a DesktopForm.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>RESIZE, CLOSE, MAX, ON_TOP, TOOL, NO_RADIO_GROUP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * </p>
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 21, 2005
 * @version $Id: InternalShell.java 344 2005-07-09 22:37:51 +0000 (Sat, 09 Jul
 *          2005) szeiger $
 */

// [TODO] Support styles NO_TRIM, BORDER, TITLE
// [TODO] Separate "minimized" from "not visible"

public class InternalShell extends Composite {
	private static final int BORDER_SIZE = 4;

	private final Composite contentPane;
	private final TitleBar titleBar;
	private SizeGrip sizeGrip;
	private final SizeBorder sizeBorder;
	private int minWidth = 112;
	private int minHeight;
	private final DesktopForm desktop;
	private boolean maximized;
	private Rectangle pluralizedBounds;
	private final int titleHeight;
	private final int style;
	private TitleBarButton closeButton, maxButton, minButton;

	Control focusControl;

	public InternalShell(DesktopForm parent, int style) {
		super(parent, checkStyle(style));
		this.desktop = parent;
		this.style = style;
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		FormLayout layout = new FormLayout();
		setLayout(layout);
		FormData fd;

		titleBar = new TitleBar(this, style
				& (SWT.CLOSE | SWT.RESIZE | SWT.MAX | SWT.TOOL | SWT.MIN));
		titleHeight = titleBar.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;

		Control leftButton = null;

		if ((style & (SWT.CLOSE | SWT.MAX | SWT.MIN)) != 0) {
			closeButton = new TitleBarButton(this, SWT.CLOSE);
			if ((style & SWT.CLOSE) == 0)
				closeButton.setEnabled(false);
			closeButton.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					close();
				}
			});
			fd = new FormData(titleHeight, titleHeight);
			if (leftButton != null)
				fd.right = new FormAttachment(leftButton);
			else
				fd.right = new FormAttachment(100, -BORDER_SIZE);
			fd.top = new FormAttachment(0, BORDER_SIZE);
			closeButton.setLayoutData(fd);
			leftButton = closeButton;

			if ((style & (SWT.MAX | SWT.MIN)) != 0) {
				maxButton = new TitleBarButton(this, SWT.MAX);
				if ((style & SWT.MAX) == 0)
					maxButton.setEnabled(false);
				maxButton.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						setMaximized(!maximized);
					}
				});
				fd = new FormData(titleHeight, titleHeight);
				if (leftButton != null)
					fd.right = new FormAttachment(leftButton);
				else
					fd.right = new FormAttachment(100, -BORDER_SIZE);
				fd.top = new FormAttachment(0, BORDER_SIZE);
				maxButton.setLayoutData(fd);
				leftButton = maxButton;

				minButton = new TitleBarButton(this, SWT.MIN);
				if ((style & SWT.MIN) == 0)
					minButton.setEnabled(false);
				minButton.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						setMinimized(true);
					}
				});
				fd = new FormData(titleHeight, titleHeight);
				if (leftButton != null)
					fd.right = new FormAttachment(leftButton);
				else
					fd.right = new FormAttachment(100, -BORDER_SIZE);
				fd.top = new FormAttachment(0, BORDER_SIZE);
				minButton.setLayoutData(fd);
				leftButton = minButton;
			}
		}

		fd = new FormData();
		fd.left = new FormAttachment(0, BORDER_SIZE);
		if (leftButton != null)
			fd.right = new FormAttachment(leftButton);
		else
			fd.right = new FormAttachment(100, -BORDER_SIZE);
		fd.top = new FormAttachment(0, BORDER_SIZE);
		titleBar.setLayoutData(fd);

		contentPane = new Composite(this, SWT.NONE);
		fd = new FormData();
		fd.left = new FormAttachment(0, BORDER_SIZE);
		fd.right = new FormAttachment(100, -BORDER_SIZE);
		fd.top = new FormAttachment(titleBar, 1);
		fd.bottom = new FormAttachment(100, -BORDER_SIZE);
		contentPane.setLayoutData(fd);

		sizeBorder = new SizeBorder(this, this, SWT.BORDER);
		sizeBorder.setBorderWidth(BORDER_SIZE);
		fd = new FormData();
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(0);
		fd.bottom = new FormAttachment(100);
		sizeBorder.setLayoutData(fd);

		minHeight = titleHeight + 2 * BORDER_SIZE;
		sizeBorder.setMinimumShellSize(minWidth, minHeight);
		sizeBorder.setCornerSize(titleHeight + BORDER_SIZE);
		if ((style & SWT.RESIZE) == 0)
			sizeBorder.setEnabled(false);

		setSize(320, 240);
		setVisible(false);

		desktop.manage(this);
	}

	private static int checkStyle(int style) {
		int mask = SWT.NO_RADIO_GROUP;
		style &= mask;
		return style;
	}

	@Override
	public int getStyle() {
		return style;
	}

	public Composite getContentPane() {
		return contentPane;
	}

	public void setText(String s) {
		titleBar.setText(s);
	}

	public String getText() {
		return titleBar.getText();
	}

	public void setCustomMenu(Menu menu) {
		titleBar.setMenu(menu);
	}

	public Menu getCustomMenu() {
		return titleBar.getMenu();
	}

	public void setImage(Image image) {
		titleBar.setImage(image);
	}

	public Image getImage() {
		return titleBar.getImage();
	}

	public void createSizeGrip(int style) {
		checkWidget();
		if (sizeGrip != null)
			throw new SWTException("SizeGrip was already created");
		if ((this.style & SWT.RESIZE) == 0)
			throw new SWTException(
					"Cannot create SizeGrip for InternalShell without style RESIZE");
		sizeGrip = new SizeGrip(this, this, style);
		sizeGrip.setBackground(contentPane.getBackground());
		sizeGrip.moveAbove(contentPane);
		FormData fd = new FormData();
		fd.right = new FormAttachment(100, -BORDER_SIZE);
		fd.bottom = new FormAttachment(100, -BORDER_SIZE);
		sizeGrip.setLayoutData(fd);
		sizeGrip.setMinimumShellSize(minWidth, minHeight);
		if (isVisible())
			layout(true);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Point p = super.computeSize(wHint, hHint, changed);
		if (p.x < minWidth)
			p.x = minWidth;
		if (p.y < minHeight)
			p.y = minHeight;
		return p;
	}

	@Override
	public void setSize(int width, int height) {
		if (width < minWidth)
			width = minWidth;
		if (height < minHeight)
			height = minHeight;
		super.setSize(width, height);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		if (width < minWidth)
			width = minWidth;
		if (height < minHeight)
			height = minHeight;
		super.setBounds(x, y, width, height);
	}

	public void setMinimumSize(int width, int height) {
		checkWidget();
		minWidth = width;
		minHeight = height;
		sizeGrip.setMinimumShellSize(minWidth, minHeight);
		sizeBorder.setMinimumShellSize(minWidth, minHeight);
		Point size = getSize();
		if (size.x < minWidth || size.y < minHeight)
			setSize(Math.max(minWidth, size.x), Math.max(minHeight, size.y));
	}

	public void close() {
		Event event = new Event();
		notifyListeners(SWT.Close, event);
		if (event.doit && !isDisposed())
			dispose();
	}

	public void open() {
		desktop.activate(this);
		setVisible(true);
		setFocus();
	}

	@Override
	public void setVisible(boolean visible) {
		if (!visible)
			desktop.shellVisibilityChanged(this, false);
		super.setVisible(visible);
		if (visible)
			desktop.shellVisibilityChanged(this, true);
	}

	public void setActive() {
		desktop.activate(this);
	}

	public void setMaximized(boolean maximized) {
		checkWidget();
		if (this.maximized == maximized)
			return;
		setMaximizedWithoutNotification(maximized);
		desktop.shellMaximizedOrRestored(this, maximized);
	}

	public void setMinimized(boolean minimized) {
		checkWidget();
		boolean wasMaximized = maximized;
		setVisible(!minimized);
		maximized = wasMaximized;
	}

	public boolean getMinimized() {
		return getVisible();
	}

	void setMaximizedWithoutNotification(boolean maximized) {
		if (this.maximized == maximized)
			return;
		this.maximized = maximized;
		if (maximized) {
			pluralizedBounds = getBounds();
			desktopResized(desktop.getClientArea());
		} else {
			setBounds(pluralizedBounds);
		}
		// Note: This method may be called in a Dispose event for this object
		if (sizeGrip != null && !sizeGrip.isDisposed())
			sizeGrip.setVisible(!maximized);
		if (!sizeBorder.isDisposed())
			sizeBorder.setEnabled(!maximized && (style & SWT.RESIZE) != 0);
		if (maxButton != null && !maxButton.isDisposed())
			maxButton.redraw();
	}

	public boolean getMaximized() {
		checkWidget();
		return maximized;
	}

	void redrawDecorationsAfterActivityChange() {
		// Note: This method may be called in a Dispose event for this object
		if (!titleBar.isDisposed())
			titleBar.redraw();
		if (closeButton != null && !closeButton.isDisposed())
			closeButton.redraw();
		if (maxButton != null && !maxButton.isDisposed())
			maxButton.redraw();
		if (minButton != null && !minButton.isDisposed())
			minButton.redraw();
	}

	void desktopResized(Rectangle deskCA) {
		if (maximized) {
			int hideTitle = desktop.getShowMaximizedTitle() ? 0
					: (titleHeight + 1);
			setBounds(deskCA.x - BORDER_SIZE, deskCA.y - BORDER_SIZE
					- hideTitle, deskCA.width + 2 * BORDER_SIZE, deskCA.height
					+ 2 * BORDER_SIZE + hideTitle);
		} else
			forceVisibleLocation(deskCA);
	}

	@Override
	public boolean setFocus() {
		if (focusControl != null && focusControl != this
				&& !focusControl.isDisposed())
			return focusControl.setFocus();
		return super.setFocus();
	}

	public boolean isActiveShell() {
		return desktop.getActiveShell() == this;
	}

	private void forceVisibleLocation(Rectangle deskCA) {
		Point p = getLocation();
		Point minGrabSize = titleBar.getMinGrabSize();
		int x = p.x, y = p.y;
		int minX = minGrabSize.x + BORDER_SIZE, minY = minGrabSize.y
				+ BORDER_SIZE;
		x = Math.min(Math.max(x, deskCA.x + minY), deskCA.x + deskCA.width
				- minX);
		y = Math.min(Math.max(y, deskCA.y + minY), deskCA.y + deskCA.height
				- minY);
		if (x != p.x || y != p.y)
			setLocation(x, y);
	}
}
