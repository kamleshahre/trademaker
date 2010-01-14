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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * A desktop which manages internal shells.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 21, 2005
 * @version $Id: DesktopForm.java 344 2005-07-09 22:37:51 +0000 (Sat, 09 Jul
 *          2005) szeiger $
 */

public class DesktopForm extends Composite {
	private static final InternalShell[] EMPTY_INTERNALSHELL_ARRAY = new InternalShell[0];
	private static final int FIRST_SHELL_LOCATION = 32;
	private static final int SHELL_LOCATION_OFFSET = 16;

	private InternalShell activeShell;
	private final List<DesktopListener> desktopListeners = new ArrayList<DesktopListener>();
	private final List<InternalShell> allShells = new ArrayList<InternalShell>();
	private final List<InternalShell> visibleShells = new ArrayList<InternalShell>();
	private int nextShellLocation = FIRST_SHELL_LOCATION;
	private boolean showMaximizedTitle;
	private boolean autoMaximize = true;
	private boolean enableCtrlTab = true;
	private boolean allowDeactivate;

	public DesktopForm(Composite parent, int style) {
		super(parent, style);
		final Display display = getDisplay();
		final Shell shell = getShell();

		Color bg = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
		setBackground(bg);
		int brightness = bg.getRed() + bg.getGreen() + bg.getBlue();
		setForeground(display.getSystemColor(brightness > 400 ? SWT.COLOR_BLACK
				: SWT.COLOR_WHITE));

		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				Rectangle ca = getClientArea();
				for (Control c : getChildren()) {
					if (c instanceof InternalShell)
						((InternalShell) c).desktopResized(ca);
				}
			}
		});

		final Listener mouseDownFilter = new Listener() {
			public void handleEvent(Event event) {
				if (!(event.widget instanceof Control))
					return;
				Control c = (Control) event.widget;
				if (c.getShell() != shell)
					return;
				boolean[] desktopHit = new boolean[1];
				InternalShell ishell = getInternalShell(c, desktopHit);
				if (desktopHit[0] && allowDeactivate)
					activate(null);
				if (ishell == null)
					return;
				activate(ishell);
			}
		};

		final Listener focusInFilter = new Listener() {
			public void handleEvent(Event event) {
				if (!(event.widget instanceof Control))
					return;
				Control c = (Control) event.widget;
				if (c.getShell() != shell)
					return;
				boolean[] desktopHit = new boolean[1];
				InternalShell ishell = getInternalShell(c, desktopHit);
				if (desktopHit[0] && allowDeactivate)
					activate(null);
				if (ishell == null)
					return;
				ishell.focusControl = c;
			}
		};

		final Listener traverseFilter = new Listener() {
			public void handleEvent(Event event) {
				if (!enableCtrlTab)
					return;
				if (!event.doit)
					return; // don't steal traverse event if a control wants to
				// handle it directly
				if ((event.stateMask & SWT.CTRL) == 0)
					return;
				if (event.detail != SWT.TRAVERSE_TAB_NEXT
						&& event.detail != SWT.TRAVERSE_TAB_PREVIOUS)
					return;
				if (!(event.widget instanceof Control))
					return;
				Control c = (Control) event.widget;
				if (c.getShell() != shell)
					return;
				boolean[] desktopHit = new boolean[1];
				InternalShell ishell = getInternalShell(c, desktopHit);
				if (ishell != null || desktopHit[0]) {
					if (event.detail == SWT.TRAVERSE_TAB_NEXT)
						activateNextShell();
					else
						activatePreviousShell();
					event.doit = false;
				}
			}
		};

		display.addFilter(SWT.MouseDown, mouseDownFilter);
		display.addFilter(SWT.FocusIn, focusInFilter);
		display.addFilter(SWT.Traverse, traverseFilter);

		addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				display.removeFilter(SWT.MouseDown, mouseDownFilter);
				display.removeFilter(SWT.FocusIn, focusInFilter);
				display.removeFilter(SWT.Traverse, traverseFilter);
			}
		});
	}

	void manage(final InternalShell ishell) {
		Rectangle bounds = getBounds();
		if (nextShellLocation > bounds.height - 100
				|| nextShellLocation > bounds.width - 100)
			nextShellLocation = FIRST_SHELL_LOCATION;
		ishell.setLocation(bounds.x + nextShellLocation, bounds.y
				+ nextShellLocation);
		nextShellLocation += SHELL_LOCATION_OFFSET;

		ishell.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				allShells.remove(ishell);
				visibleShells.remove(ishell);
				if (ishell == activeShell) {
					activateTopmostVisibleShellExcept(ishell);
					if (autoMaximize && !hasVisibleMaximizedShell())
						setAllVisibleMaximized(false);
				}
				notifyDesktopListenersDispose(ishell);
			}
		});
		allShells.add(ishell);
		if (ishell.isVisible())
			visibleShells.add(ishell);
		notifyDesktopListenersCreate(ishell);
	}

	private InternalShell activateTopmostVisibleShellExcept(InternalShell except) {
		Control[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			Control c = children[i];
			if (c == except)
				continue;
			if (c instanceof InternalShell && c.isVisible()) {
				InternalShell ishell = (InternalShell) c;
				activate(ishell);
				return ishell;
			}
		}
		activeShell = null;
		notifyDesktopListenersActivate(null);
		return null;
	}

	void activate(InternalShell ishell) {
		if (ishell == activeShell)
			return;
		checkWidget();
		if (ishell != null) {
			if (!ishell.isVisible())
				ishell.setVisible(true);
			if ((ishell.getStyle() & SWT.ON_TOP) != 0)
				ishell.moveAbove(null);
			else {
				InternalShell firstRegular = getTopmostRegularShell();
				if (firstRegular != null && firstRegular != ishell)
					ishell.moveAbove(firstRegular);
				else {
					Control[] children = getChildren();
					if (children.length > 0)
						ishell.moveAbove(children[0]);
				}
			}
		}
		InternalShell oldActiveShell = activeShell;
		activeShell = ishell;
		if (oldActiveShell != null)
			oldActiveShell.redrawDecorationsAfterActivityChange();
		if (ishell != null) {
			if (activeShell.isVisible())
				activeShell.redrawDecorationsAfterActivityChange();
			setTabList(new Control[] { activeShell });
			activeShell.setFocus();
		} else {
			setTabList(new Control[] {});
			forceFocus();
		}
		notifyDesktopListenersActivate(ishell);
	}

	private InternalShell getTopmostRegularShell() {
		for (Control c : getChildren()) {
			if (!(c instanceof InternalShell))
				continue;
			if ((c.getStyle() & SWT.ON_TOP) == 0)
				return (InternalShell) c;
		}
		return null;
	}

	private InternalShell getBottommostOnTopShell() {
		Control[] ch = getChildren();
		for (int i = ch.length - 1; i >= 0; i--) {
			Control c = ch[i];
			if (!(c instanceof InternalShell))
				continue;
			if ((c.getStyle() & SWT.ON_TOP) != 0)
				return (InternalShell) c;
		}
		return null;
	}

	void shellVisibilityChanged(InternalShell ishell, boolean visible) {
		if (visible) {
			if (!visibleShells.contains(ishell)) {
				visibleShells.add(ishell);
				if (autoMaximize && !ishell.getMaximized()
						&& (ishell.getStyle() & SWT.MAX) != 0
						&& hasVisibleMaximizedShell())
					ishell.setMaximizedWithoutNotification(true);
			}
			if (ishell.getMaximized())
				ishell.desktopResized(getClientArea());
		} else {
			visibleShells.remove(ishell);
			if (ishell == activeShell) {
				activateTopmostVisibleShellExcept(ishell);
				if (autoMaximize && !hasVisibleMaximizedShell())
					setAllVisibleMaximized(false);
			}
		}
	}

	private InternalShell getInternalShell(Control c, boolean[] desktopHit) {
		while (c != null && c != DesktopForm.this) {
			if (c instanceof InternalShell
					&& ((InternalShell) c).getParent() == this)
				return (InternalShell) c;
			c = c.getParent();
		}
		if (desktopHit != null && c == DesktopForm.this)
			desktopHit[0] = true;
		return null;
	}

	public InternalShell getActiveShell() {
		return activeShell;
	}

	public InternalShell[] getVisibleShells() {
		checkWidget();
		return visibleShells.toArray(EMPTY_INTERNALSHELL_ARRAY);
	}

	public InternalShell[] getShells() {
		checkWidget();
		return allShells.toArray(EMPTY_INTERNALSHELL_ARRAY);
	}

	public void setShowMaximizedTitle(boolean b) {
		checkWidget();
		showMaximizedTitle = b;
		Rectangle ca = getClientArea();
		for (Control c : getChildren()) {
			if (c instanceof InternalShell)
				((InternalShell) c).desktopResized(ca);
		}
	}

	public boolean getShowMaximizedTitle() {
		checkWidget();
		return showMaximizedTitle;
	}

	public void setAutoMaximize(boolean b) {
		checkWidget();
		autoMaximize = b;
		boolean hasMax = false;
		for (InternalShell is : visibleShells) {
			if (is.getMaximized()) {
				hasMax = true;
				break;
			}
		}
		if (hasMax) {
			// Maximize all shells
			for (InternalShell is : visibleShells) {
				if ((is.getStyle() & SWT.MAX) != 0)
					is.setMaximized(true);
			}
		}
	}

	public boolean getAutoMaximize() {
		checkWidget();
		return autoMaximize;
	}

	public void setEnableCtrlTab(boolean b) {
		checkWidget();
		this.enableCtrlTab = b;
	}

	public boolean getEnableCtrlTab() {
		return enableCtrlTab;
	}

	public void setAllowDeactivate(boolean b) {
		checkWidget();
		this.allowDeactivate = b;
		if (!allowDeactivate && activeShell == null)
			activateTopmostVisibleShellExcept(null);
	}

	public boolean getAllowDeactivate() {
		return allowDeactivate;
	}

	void shellMaximizedOrRestored(InternalShell ishell, boolean maximized) {
		setAllVisibleMaximized(maximized);
	}

	private void setAllVisibleMaximized(boolean maximized) {
		if (autoMaximize) // maximize or restore all shells
		{
			for (Control c : getChildren()) {
				if (c instanceof InternalShell) {
					InternalShell ishell = (InternalShell) c;
					if ((ishell.getStyle() & SWT.MAX) != 0
							&& ishell.isVisible())
						((InternalShell) c)
								.setMaximizedWithoutNotification(maximized);
				}
			}
		}
	}

	private void activateNextShell() {
		if (activeShell == null) {
			activateTopmostVisibleShellExcept(null);
			return;
		}
		if (visibleShells.size() < 2)
			return;
		InternalShell topReg = getTopmostRegularShell();
		InternalShell botTop = getBottommostOnTopShell();
		if ((activeShell.getStyle() & SWT.ON_TOP) != 0) {
			activeShell.moveBelow(botTop);
			if (topReg != null)
				activate(topReg);
			else
				activateTopmostVisibleShellExcept(null);
		} else {
			activeShell.moveBelow(null);
			activateTopmostVisibleShellExcept(null);
		}
	}

	private void activatePreviousShell() {
		if (activeShell == null) {
			activateTopmostVisibleShellExcept(null);
			return;
		}
		if (visibleShells.size() < 2)
			return;
		InternalShell topReg = getTopmostRegularShell();
		InternalShell botTop = getBottommostOnTopShell();
		if (activeShell == topReg && botTop != null)
			activate(botTop);
		else {
			Control[] ch = getChildren();
			for (int i = ch.length - 1; i >= 0; i--) {
				if (ch[i] instanceof InternalShell && ch[i].isVisible()) {
					activate((InternalShell) ch[i]);
					break;
				}
			}
		}
	}

	public void addDesktopListener(DesktopListener l) {
		desktopListeners.add(l);
	}

	public void removeDesktopListener(DesktopListener l) {
		desktopListeners.remove(l);
	}

	private void notifyDesktopListenersCreate(InternalShell ishell) {
		Event event = new Event();
		event.widget = ishell;
		for (DesktopListener l : desktopListeners)
			l.shellCreated(event);
	}

	private void notifyDesktopListenersDispose(InternalShell ishell) {
		Event event = new Event();
		event.widget = ishell;
		for (DesktopListener l : desktopListeners)
			l.shellDisposed(event);
	}

	private void notifyDesktopListenersActivate(InternalShell ishell) {
		Event event = new Event();
		event.widget = ishell;
		for (DesktopListener l : desktopListeners)
			l.shellActivated(event);
	}

	private boolean hasVisibleMaximizedShell() {
		for (InternalShell is : visibleShells)
			if (is.getMaximized())
				return true;
		return false;
	}
}
