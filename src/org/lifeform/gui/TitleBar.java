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

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * A title bar for an InternalShell.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 21, 2005
 * @version $Id: TitleBar.java 342 2005-07-09 20:37:13 +0000 (Sat, 09 Jul 2005)
 *          szeiger $
 */

public final class TitleBar extends Canvas {
	private static final long UPDATE_DELAY = 25;
	private static final int MINIMUM_GRAB_AREA = 2;
	private static final String ELLIPSIS = "...";
	private static final int LEFT_PADDING = 2;
	private static final int RIGHT_PADDING = 2;
	private static final int IMAGE_SIZE = 16;
	private static final int TOOL_SIZE = 14;
	private static final int TOP_PADDING = 1;
	private static final int BOTTOM_PADDING = 1;

	private int mouseDownOffsetX, mouseDownOffsetY, snapBackX, snapBackY;
	private boolean cancelled;
	private volatile long lastUpdate;
	private Timer timer = new Timer(true);
	private TimerTask timerTask;
	private final InternalShell ishell;
	private final DesktopForm desktop;
	private String text;
	private Image image;
	private final boolean styleClose, styleMax, styleTool, styleMin;
	private final Image closeImage, restoreImage, maximizeImage, minimizeImage;
	private final MenuItem restoreItem, closeItem, maximizeItem;
	private final Menu defaultPopup;
	private final Point minGrabSize = new Point(MINIMUM_GRAB_AREA,
			MINIMUM_GRAB_AREA);

	public TitleBar(InternalShell parent, int style) {
		super(parent, checkStyle(style));
		this.ishell = parent;
		this.desktop = (DesktopForm) ishell.getParent();
		this.styleClose = (style & SWT.CLOSE) != 0;
		this.styleMax = (style & SWT.MAX) != 0;
		this.styleMin = (style & SWT.MIN) != 0;
		this.styleTool = (style & SWT.TOOL) != 0;

		final Display display = getDisplay();
		final Shell shell = getShell();

		final Color gradStartColor = display
				.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		final Color gradEndColor = display
				.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
		final Color textColor = display
				.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		final Color inactiveGradStartColor = display
				.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
		final Color inactiveGradEndColor = display
				.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
		final Color inactiveTextColor = display
				.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);

		GC gc = new GC(this);
		int imgHeight = gc.getFontMetrics().getHeight() - 1;
		if (imgHeight % 2 == 0)
			imgHeight--;
		gc.dispose();

		closeImage = createMenuImage(IMAGE_TYPE_CLOSE, imgHeight);
		restoreImage = createMenuImage(IMAGE_TYPE_RESTORE, imgHeight);
		maximizeImage = createMenuImage(IMAGE_TYPE_MAXIMIZE, imgHeight);
		minimizeImage = createMenuImage(IMAGE_TYPE_MINIMIZE, imgHeight);

		setFont(createTitleFont(getFont(), styleTool));

		defaultPopup = new Menu(this);

		restoreItem = new MenuItem(defaultPopup, SWT.PUSH);
		restoreItem.setText(LocalizedStrings
				.get(LocalizedStrings.POPUP_RESTORE));
		restoreItem.setImage(restoreImage);
		restoreItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ishell.setMaximized(false);
			}
		});

		MenuItem minimizeItem = new MenuItem(defaultPopup, SWT.PUSH);
		minimizeItem.setText(LocalizedStrings
				.get(LocalizedStrings.POPUP_MINIMIZE));
		minimizeItem.setEnabled(styleMin);
		minimizeItem.setImage(minimizeImage);
		minimizeItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ishell.setMinimized(true);
			}
		});

		maximizeItem = new MenuItem(defaultPopup, SWT.PUSH);
		maximizeItem.setText(LocalizedStrings
				.get(LocalizedStrings.POPUP_MAXIMIZE));
		maximizeItem.setImage(maximizeImage);
		maximizeItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ishell.setMaximized(true);
			}
		});

		new MenuItem(defaultPopup, SWT.SEPARATOR);

		closeItem = new MenuItem(defaultPopup, SWT.PUSH);
		closeItem.setText(LocalizedStrings.get(LocalizedStrings.POPUP_CLOSE));
		closeItem.setEnabled(styleClose);
		closeItem.setImage(closeImage);
		closeItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ishell.close();
			}
		});

		addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				Rectangle r = getClientArea();
				if (r.width == 0 || r.height == 0)
					return;

				boolean active = (shell == display.getActiveShell() && ishell
						.isActiveShell());

				GC gc = event.gc;
				gc.setForeground(active ? gradStartColor
						: inactiveGradStartColor);
				gc.setBackground(active ? gradEndColor : inactiveGradEndColor);
				gc.fillGradientRectangle(r.x, r.y, r.width, r.height, false);

				int textLeftPadding = LEFT_PADDING;
				if (image != null) {
					Rectangle imageBounds = image.getBounds();
					if (imageBounds.width > IMAGE_SIZE
							|| imageBounds.height > IMAGE_SIZE)
						gc.drawImage(image, 0, 0, imageBounds.width,
								imageBounds.height, LEFT_PADDING, TOP_PADDING,
								IMAGE_SIZE, IMAGE_SIZE);
					else
						gc.drawImage(image, LEFT_PADDING
								+ (IMAGE_SIZE - imageBounds.width) / 2,
								(r.height - imageBounds.height) / 2);
					textLeftPadding += IMAGE_SIZE + LEFT_PADDING;
				}

				if (text != null && text.length() > 0) {
					gc.setForeground(active ? textColor : inactiveTextColor);
					String s = text;
					int availableWidth = r.width - textLeftPadding
							- RIGHT_PADDING;
					if (gc.textExtent(s, SWT.DRAW_TRANSPARENT).x > availableWidth) {
						int ellipsisWidth = gc.textExtent(ELLIPSIS,
								SWT.DRAW_TRANSPARENT).x;
						while (s.length() > 0) {
							s = s.substring(0, s.length() - 1);
							if (gc.textExtent(s, SWT.DRAW_TRANSPARENT).x
									+ ellipsisWidth <= availableWidth) {
								s += ELLIPSIS;
								break;
							}
						}
						setToolTipText(text);
					} else
						setToolTipText(null);
					if (s.length() > 0)
						gc.drawString(s, textLeftPadding, (r.height - gc
								.getFontMetrics().getHeight()) / 2, true);
				} else
					setToolTipText(null);
			}
		});

		addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 1) {
					if (image != null && event.x < LEFT_PADDING + IMAGE_SIZE) {
						cancelled = true;
						// left-clicking on the image always shows the default
						// popup menu
						instrumentDefaultPopup(true);
						defaultPopup.setLocation(toDisplay(0, getSize().y));
						defaultPopup.setVisible(true);
					} else {
						mouseDownOffsetX = event.x;
						mouseDownOffsetY = event.y;
						Point p = ishell.getLocation();
						snapBackX = p.x;
						snapBackY = p.y;
						cancelled = false;
					}
				} else if (event.button == 3) {
					if ((event.stateMask & SWT.BUTTON1) != 0
							&& snapBackX != Integer.MIN_VALUE
							&& snapBackY != Integer.MIN_VALUE) {
						ishell.setLocation(snapBackX, snapBackY);
						snapBackX = Integer.MIN_VALUE;
						snapBackY = Integer.MIN_VALUE;
						cancelled = true;
					} else {
					}
				}
			}
		});

		addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				event.doit = false;
				Menu m = getMenu();
				if (m == null || m.isDisposed()) {
					m = defaultPopup;
					instrumentDefaultPopup(false);
				}
				m.setLocation(event.x, event.y);
				m.setVisible(true);
			}
		});

		addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 1) {
					if (image != null && event.x < LEFT_PADDING + IMAGE_SIZE) {
						if (styleClose)
							ishell.close();
					} else {
						if (styleMax)
							ishell.setMaximized(!ishell.getMaximized());
					}
					cancelled = true;
				}
			}
		});

		addListener(SWT.MouseMove, new Listener() {
			public void handleEvent(final Event event) {
				if (!cancelled && (event.stateMask & SWT.BUTTON1) != 0
						&& !ishell.getMaximized()) {
					if (timerTask != null) {
						timerTask.cancel();
						timerTask = null;
					}
					long now = System.currentTimeMillis();
					if (lastUpdate + UPDATE_DELAY < now) {
						performMove(event);
						lastUpdate = now;
					} else {
						timerTask = new TimerTask() {
							public void run() {
								final TimerTask executingTask = this;
								event.display.asyncExec(new Runnable() {
									public void run() {
										if (executingTask != timerTask)
											return;
										performMove(event);
									}
								});
							}
						};
						timer.schedule(timerTask, UPDATE_DELAY);
					}
				}
			}
		});

		addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				if (ishell.getMaximized())
					return;
				if (image == null || event.x >= LEFT_PADDING + IMAGE_SIZE) {
					if (timerTask != null) {
						timerTask.cancel();
						timerTask = null;
					}
					if (!cancelled && (event.stateMask & SWT.BUTTON1) != 0) {
						performMove(event);
					}
				}
			}
		});

		final Listener activateListener = new Listener() {
			public void handleEvent(Event event) {
				redraw();
			}
		};
		final Listener deactivateListener = new Listener() {
			public void handleEvent(Event event) {
				redraw();
			}
		};
		shell.addListener(SWT.Activate, activateListener);
		shell.addListener(SWT.Deactivate, deactivateListener);

		addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				timer.cancel();
				shell.removeListener(SWT.Activate, activateListener);
				shell.removeListener(SWT.Deactivate, deactivateListener);
				closeImage.dispose();
				maximizeImage.dispose();
				restoreImage.dispose();
				minimizeImage.dispose();
				defaultPopup.dispose();
			}
		});
	}

	private void performMove(Event event) {
		Point p = ishell.getLocation();
		int newX = p.x + event.x - mouseDownOffsetX;
		int newY = p.y + event.y - mouseDownOffsetY;

		// Make sure that the minimum grab area stays visible
		Rectangle deskCA = desktop.getClientArea();
		Rectangle bounds = getBounds();
		newX = Math.min(Math.max(newX, deskCA.x - bounds.x - bounds.width
				+ MINIMUM_GRAB_AREA), deskCA.x - bounds.x + deskCA.width
				- minGrabSize.x);
		newY = Math.min(Math.max(newY, deskCA.y - bounds.y - bounds.height
				+ MINIMUM_GRAB_AREA), deskCA.y - bounds.y + deskCA.height
				- MINIMUM_GRAB_AREA);

		if (newX != p.x || newY != p.y)
			ishell.setLocation(newX, newY);
	}

	public Point getMinGrabSize() {
		return minGrabSize;
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		if (wHint == SWT.DEFAULT)
			wHint = 50;
		if (hHint == SWT.DEFAULT) {
			GC gc = new GC(this);
			hHint = gc.getFontMetrics().getHeight();
			hHint = Math.max(hHint, styleTool ? TOOL_SIZE : IMAGE_SIZE);
			hHint += TOP_PADDING + BOTTOM_PADDING;
			gc.dispose();
		}
		return new Point(wHint, hHint);
	}

	private static int checkStyle(int style) {
		// int mask = SWT.SHADOW_IN | SWT.FLAT;
		// style &= mask;
		style = SWT.NO_FOCUS;
		return style;
	}

	public boolean setFocus() {
		checkWidget();
		return false;
	}

	public boolean isReparentable() {
		checkWidget();
		return false;
	}

	public void setText(String text) {
		checkWidget();
		this.text = text;
		redraw();
	}

	public String getText() {
		return text;
	}

	public void setImage(Image image) {
		checkWidget();
		if (styleTool)
			return;
		this.image = image;
		minGrabSize.x = MINIMUM_GRAB_AREA;
		if (image != null)
			minGrabSize.x += LEFT_PADDING + IMAGE_SIZE;
		redraw();
	}

	public Image getImage() {
		return image;
	}

	private Font createTitleFont(Font f, boolean tool) {
		FontData[] fds = f.getFontData();
		for (FontData fd : fds) {
			fd.setStyle(fd.getStyle() | SWT.BOLD);
			if (tool)
				fd.setHeight((int) (fd.getHeight() * 0.9));
		}
		return new Font(getDisplay(), fds);
	}

	private void instrumentDefaultPopup(boolean onImage) {
		restoreItem.setEnabled(styleMax && ishell.getMaximized());
		maximizeItem.setEnabled(styleMax && !ishell.getMaximized());
		MenuItem def = null;
		if (onImage) {
			if (styleClose)
				def = closeItem;
		} else if (styleMax) {
			def = ishell.getMaximized() ? restoreItem : maximizeItem;
		}
		defaultPopup.setDefaultItem(def);
	}

	private static final int IMAGE_TYPE_CLOSE = 1;
	private static final int IMAGE_TYPE_MAXIMIZE = 2;
	private static final int IMAGE_TYPE_RESTORE = 3;
	private static final int IMAGE_TYPE_MINIMIZE = 4;

	private Image createMenuImage(int type, int height) {
		final Point size = new Point(height, height);
		final int imgWidth = height + height / 2;
		final Color fg = getForeground();
		final Color white = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		final RGB blackRGB = new RGB(0, 0, 0);

		ImageData id = new ImageData(imgWidth, size.y, 1, new PaletteData(
				new RGB[] { blackRGB, fg.getRGB() }));
		ImageData maskid = new ImageData(imgWidth, size.y, 1, new PaletteData(
				new RGB[] { blackRGB, white.getRGB() }));

		Image img = new Image(getDisplay(), id);
		GC gc = new GC(img);
		gc.setForeground(fg);
		drawMenuImage(gc, size, type);
		gc.dispose();

		Image maskimg = new Image(getDisplay(), maskid);
		gc = new GC(maskimg);
		gc.setForeground(white);
		drawMenuImage(gc, size, type);
		gc.dispose();

		Image transp = new Image(getDisplay(), img.getImageData(), maskimg
				.getImageData());
		img.dispose();
		maskimg.dispose();
		return transp;
	}

	private void drawMenuImage(GC gc, Point size, int type) {
		switch (type) {
		case IMAGE_TYPE_CLOSE:
			gc.drawLine(1, 1, size.x - 2, size.y - 2);
			gc.drawLine(2, 1, size.x - 2, size.y - 3);
			gc.drawLine(1, 2, size.x - 3, size.y - 2);
			gc.drawLine(1, size.y - 2, size.x - 2, 1);
			gc.drawLine(1, size.y - 3, size.x - 3, 1);
			gc.drawLine(2, size.y - 2, size.x - 2, 2);
			break;

		case IMAGE_TYPE_RESTORE:
			gc.drawRectangle(0, 4, size.x - 4, size.y - 6);
			gc.drawLine(1, 5, size.x - 5, 5);
			gc.drawLine(2, 1, size.x - 2, 1);
			gc.drawLine(2, 2, size.x - 2, 2);
			gc.drawPoint(2, 3);
			gc.drawLine(size.x - 2, 3, size.x - 2, size.y - 5);
			gc.drawPoint(size.x - 3, size.y - 5);
			break;

		case IMAGE_TYPE_MAXIMIZE:
			gc.drawRectangle(0, 0, size.x - 2, size.y - 2);
			gc.drawLine(1, 1, size.x - 3, 1);
			break;

		case IMAGE_TYPE_MINIMIZE:
			gc.drawLine(1, size.y - 2, size.x - 4, size.y - 2);
			gc.drawLine(1, size.y - 3, size.x - 4, size.y - 3);
			break;
		}
	}
}
