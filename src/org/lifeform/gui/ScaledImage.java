/*******************************************************************************
 * Copyright (c) 2004 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *     IBM Corporation - original SWT CLabel implementation on which this class is based
 *******************************************************************************/

package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * An image / gradient component. Under development.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 21, 2005
 * @version $Id: ScaledImage.java 346 2005-07-11 20:15:57 +0000 (Mon, 11 Jul
 *          2005) szeiger $
 */

public class ScaledImage extends Canvas {
	private static final Rectangle DEFAULT_BOUNDS = new Rectangle(0, 0, 32, 32);

	public static final int IMAGE_PLACEMENT_STRETCH = 0;
	public static final int IMAGE_PLACEMENT_TILE = 1;

	private Image image;

	private Color[] gradientColors;

	private int[] gradientPercents;

	private boolean gradientVertical;

	private int imagePlacement = IMAGE_PLACEMENT_STRETCH;

	public ScaledImage(final Composite parent, final int style) {
		super(parent, style | SWT.NO_BACKGROUND);

		addListener(SWT.Paint, new Listener() {
			public void handleEvent(final Event event) {
				Rectangle rect = getClientArea();
				GC gc = event.gc;
				if (image == null
						|| image.getImageData().getTransparencyType() != SWT.TRANSPARENCY_NONE) {

					if (gradientColors != null) {
						// draw a gradient behind the text
						final Color oldBackground = gc.getBackground();
						if (gradientColors.length == 1) {
							if (gradientColors[0] != null)
								gc.setBackground(gradientColors[0]);
							gc.fillRectangle(0, 0, rect.width, rect.height);
						} else {
							final Color oldForeground = gc.getForeground();
							Color lastColor = gradientColors[0];
							if (lastColor == null)
								lastColor = oldBackground;
							int pos = 0;
							for (int i = 0; i < gradientPercents.length; ++i) {
								gc.setForeground(lastColor);
								lastColor = gradientColors[i + 1];
								if (lastColor == null)
									lastColor = oldBackground;
								gc.setBackground(lastColor);
								if (gradientVertical) {
									final int gradientHeight = (gradientPercents[i]
											* rect.height / 100)
											- pos;
									gc.fillGradientRectangle(0, pos,
											rect.width, gradientHeight, true);
									pos += gradientHeight;
								} else {
									final int gradientWidth = (gradientPercents[i]
											* rect.width / 100)
											- pos;
									gc.fillGradientRectangle(pos, 0,
											gradientWidth, rect.height, false);
									pos += gradientWidth;
								}
							}
							if (gradientVertical && pos < rect.height) {
								gc.setBackground(getBackground());
								gc.fillRectangle(0, pos, rect.width,
										rect.height - pos);
							}
							if (!gradientVertical && pos < rect.width) {
								gc.setBackground(getBackground());
								gc.fillRectangle(pos, 0, rect.width - pos,
										rect.height);
							}
							gc.setForeground(oldForeground);
						}
						gc.setBackground(oldBackground);
					} else {
						if ((getStyle() & SWT.NO_BACKGROUND) != 0) {
							gc.setBackground(getBackground());
							gc.fillRectangle(rect);
						}
					}

				}
				if (image != null) {
					Rectangle ib = image.getBounds();
					if (imagePlacement == IMAGE_PLACEMENT_TILE) {
						int maxStartX = rect.x + rect.width;
						int maxStartY = rect.y + rect.height;
						for (int x = rect.x; x < maxStartX; x += ib.width)
							for (int y = rect.y; y < maxStartY; y += ib.height)
								event.gc.drawImage(image, x, y);
					} else // IMAGE_PLACEMENT_STRETCH
					{
						event.gc.drawImage(image, ib.x, ib.y, ib.width,
								ib.height, rect.x, rect.y, rect.width,
								rect.height);
					}
				}
			}
		});
	}

	public void setImage(final Image image) {
		this.image = image;
		redraw();
	}

	public void setImagePlacement(final int imagePlacement) {
		this.imagePlacement = imagePlacement;
		redraw();
	}

	@Override
	public Point computeSize(int wHint, int hHint, final boolean changed) {
		checkWidget();
		Rectangle ib = image != null ? image.getBounds() : DEFAULT_BOUNDS;
		if (wHint == SWT.DEFAULT)
			wHint = ib.width;
		if (hHint == SWT.DEFAULT)
			hHint = ib.height;
		return new Point(wHint, hHint);
	}

	@Override
	public void setBackground(final Color color) {
		super.setBackground(color);
		// Are these settings the same as before?
		if (color != null && gradientColors == null && gradientPercents == null) {
			Color background = getBackground();
			if (color.equals(background)) {
				return;
			}
		}
		gradientColors = null;
		gradientPercents = null;
		redraw();
	}

	public void setBackground(final Color[] colors, final int[] percents) {
		setBackground(colors, percents, false);
	}

	public void setBackground(Color[] colors, int[] percents,
			final boolean vertical) {
		checkWidget();
		if (colors != null) {
			if (percents == null || percents.length != colors.length - 1) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (getDisplay().getDepth() < 15) {
				// Don't use gradients on low color displays
				colors = new Color[] { colors[colors.length - 1] };
				percents = new int[] {};
			}
			for (int i = 0; i < percents.length; i++) {
				if (percents[i] < 0 || percents[i] > 100) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (i > 0 && percents[i] < percents[i - 1]) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
			}
		}

		// Are these settings the same as before?
		final Color background = getBackground();
		if ((gradientColors != null) && (colors != null)
				&& (gradientColors.length == colors.length)) {
			boolean same = false;
			for (int i = 0; i < gradientColors.length; i++) {
				same = (gradientColors[i] == colors[i])
						|| ((gradientColors[i] == null) && (colors[i] == background))
						|| ((gradientColors[i] == background) && (colors[i] == null));
				if (!same)
					break;
			}
			if (same) {
				for (int i = 0; i < gradientPercents.length; i++) {
					same = gradientPercents[i] == percents[i];
					if (!same)
						break;
				}
			}
			if (same && this.gradientVertical == vertical)
				return;
		}
		// Store the new settings
		if (colors == null) {
			gradientColors = null;
			gradientPercents = null;
			gradientVertical = false;
		} else {
			gradientColors = new Color[colors.length];
			for (int i = 0; i < colors.length; ++i)
				gradientColors[i] = (colors[i] != null) ? colors[i]
						: background;
			gradientPercents = new int[percents.length];
			for (int i = 0; i < percents.length; ++i)
				gradientPercents[i] = percents[i];
			gradientVertical = vertical;
		}
		// Refresh with the new settings
		redraw();
	}
}
