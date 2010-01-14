package org.lifeform.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

/**
 * The DatePickerCombo class represents a selectable user interface object that
 * combines a date field and a date picker and issues notification when an item
 * is selected from the date picker.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER, READ_ONLY, FLAT</dd>
 * <dt><b>Events:</b>
 * <dd>DateSelectedEvent</dd>
 * </dl>
 */
public final class DatePickerCombo extends Composite {
	private Text text;

	private Shell popup;

	private Button arrow;

	private boolean hasFocus;

	private Listener listener, filter;

	private Color foreground, background;

	private Font font;

	private DatePicker datePicker;

	private DateFormat dateFormat = DateFormat.getDateInstance();

	private Set pickerOpenKeys;

	private Date date;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param swtStyle
	 *            the style of widget to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 * 
	 * @see SWT#BORDER
	 * @see SWT#READ_ONLY
	 * @see SWT#FLAT
	 * @see Widget#getStyle()
	 */
	public DatePickerCombo(final Composite parent, final int swtStyle) {
		super(parent, compositeStyle(swtStyle));
		initialize(swtStyle, 0);
	}

	public DatePickerCombo(final Composite parent, final int swtStyle,
			final int dpStyle) {
		super(parent, compositeStyle(swtStyle));
		initialize(swtStyle, dpStyle);
	}

	private void initialize(final int swtStyle, final int dpStyle) {
		text = new Text(this, SWT.SINGLE | textStyle(swtStyle));
		arrow = new Button(this, SWT.ARROW | SWT.DOWN | buttonStyle(swtStyle));
		// don't tab to the button. the picker can be opened by keyboard
		setTabList(new Control[] { text });
		pickerOpenKeys = new HashSet();
		pickerOpenKeys.add(new Integer(SWT.CR));
		listener = new Listener() {
			public void handleEvent(final Event event) {
				if (popup == event.widget) {
					popupEvent(event);
					return;
				}
				if (datePicker == event.widget) {
					pickerEvent(event);
					return;
				}
				if (arrow == event.widget) {
					arrowEvent(event);
					return;
				}
				if (text == event.widget) {
					textEvent(event);
					return;
				}
				if (DatePickerCombo.this == event.widget) {
					comboEvent(event);
					return;
				}
				if (getShell() == event.widget) {
					handleFocus(SWT.FocusOut);
				}
			}
		};
		filter = new Listener() {
			public void handleEvent(final Event event) {
				Shell shell = ((Control) event.widget).getShell();
				if (shell == DatePickerCombo.this.getShell()) {
					handleFocus(SWT.FocusOut);
				}
			}
		};
		int[] comboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };
		for (int i = 0; i < comboEvents.length; i++)
			this.addListener(comboEvents[i], listener);
		int[] textEvents = { SWT.KeyDown, SWT.KeyUp, SWT.Modify, SWT.MouseDown,
				SWT.MouseUp, SWT.Traverse, SWT.FocusIn };
		for (int i = 0; i < textEvents.length; i++)
			text.addListener(textEvents[i], listener);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				try {
					String strText = text.getText().trim();
					if (strText.length() == 0) {
						date = null;
					} else {
						date = dateFormat.parse(strText);
					}
				} catch (ParseException e1) {
					date = null;
				}
			}
		});
		int[] arrowEvents = { SWT.Selection, SWT.FocusIn };
		for (int i = 0; i < arrowEvents.length; i++)
			arrow.addListener(arrowEvents[i], listener);
		if (dpStyle == 0)
			createPopup();
		else
			createPopup(dpStyle);
		datePicker.addDateSelectionListener(new DateSelectionListener() {
			public void dateSelected(final DateSelectedEvent e) {
				setDate(e.date);
			}
		});
		updateText();
		initAccessible();
	}

	static int compositeStyle(final int style) {
		// allowed styles for composite. Note that BORDER is forbidden because
		// it is used for text
		int mask = SWT.NO_BACKGROUND | SWT.NO_MERGE_PAINTS
				| SWT.NO_REDRAW_RESIZE | SWT.EMBEDDED | SWT.DOUBLE_BUFFERED
				| SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	private int textStyle(final int style) {
		// allowed styles for text:
		int mask = SWT.FLAT | SWT.BORDER | SWT.LEFT_TO_RIGHT
				| SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	private int buttonStyle(final int style) {
		// allowed styles for button
		int mask = SWT.FLAT;
		return style & mask;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is modified, by sending it one of the messages
	 * defined in the <code>ModifyListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #removeModifyListener
	 */
	public void addModifyListener(final ModifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's selection changes, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the combo's list selection
	 * changes. <code>widgetDefaultSelected</code> is typically called when
	 * ENTER is pressed the combo's text area.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	void arrowEvent(final Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			handleFocus(SWT.FocusIn);
			break;
		}
		case SWT.Selection: {
			dropDown(!isDropped());
			break;
		}
		}
	}

	void textEvent(final Event event) {
		switch (event.type) {
		case SWT.FocusIn:
			handleFocus(SWT.FocusIn);
			break;
		case SWT.KeyUp:
			if (pickerOpenKeys.contains(new Integer(event.keyCode))) {
				dropDown(true);
				break;
			}
			break;
		case SWT.Traverse:
			switch (event.detail) {
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				// the arrow keys are used to navigate the text contents so
				// do not use them for traversal.
				event.doit = false;
				break;
			}
			Event e = new Event();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners(SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;

		case SWT.Modify: {
			if (!popup.isVisible()) {
				if (text.getText().length() == 0)
					datePicker.setDate(null);
				else {
					try {
						datePicker.setDate(dateFormat.parse(text.getText()));
					} catch (ParseException pe) {
						datePicker.setDate(null);
					}
				}
			}
			e = new Event();
			e.time = event.time;
			notifyListeners(SWT.Modify, e);
			break;

		}
		}
	}

	/**
	 * Add a keycode to which the picker should open when keyboard focus is in
	 * textfield.
	 * 
	 * @param keyCode
	 *            SWT keycode of key
	 */
	public void addPickerOpenKey(final int keyCode) {
		pickerOpenKeys.add(new Integer(keyCode));
	}

	/**
	 * Sets the selection in the receiver's text field to an empty selection
	 * starting just before the first character. If the text field is editable,
	 * this has the effect of placing the i-beam at the start of the text.
	 * <p>
	 * Note: To clear the selected items in the receiver's list, use
	 * <code>deselectAll()</code>.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #deselectAll
	 */
	public void clearSelection() {
		checkWidget();
		text.clearSelection();
	}

	void comboEvent(final Event event) {
		switch (event.type) {
		case SWT.Dispose:
			if (popup != null && !popup.isDisposed()) {
				// list.removeListener(SWT.Dispose, listener);
				popup.dispose();
			}
			Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			popup = null;
			text = null;
			datePicker = null;
			arrow = null;
			break;
		case SWT.Move:
			dropDown(false);
			break;
		case SWT.Resize:
			internalLayout(false);
			break;
		}
	}

	public Point computeSize(final int wHint, final int hHint,
			final boolean changed) {
		checkWidget();
		int width = 0, height = 0;
		int textWidth = 0;
		GC gc = new GC(text);
		int spacer = gc.stringExtent(" ").x; //$NON-NLS-1$
		gc.dispose();
		Point textSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		int borderWidth = getBorderWidth();
		height = Math.max(hHint, Math.max(textSize.y, arrowSize.y) + 2
				* borderWidth);
		width = Math.max(wHint, textWidth + 2 * spacer + arrowSize.x + 2
				* borderWidth);
		return new Point(width, height);
	}

	void createPopup() {
		createPopup(DatePickerStyle.DISABLE_MONTH_BUTTONS);
	}

	void createPopup(final int dpStyle) {
		// create shell and date picker
		popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
		int style = getStyle();
		int pickerStyle = SWT.SINGLE | SWT.V_SCROLL;
		if ((style & SWT.FLAT) != 0)
			pickerStyle |= SWT.FLAT;
		if ((style & SWT.RIGHT_TO_LEFT) != 0)
			pickerStyle |= SWT.RIGHT_TO_LEFT;
		if ((style & SWT.LEFT_TO_RIGHT) != 0)
			pickerStyle |= SWT.LEFT_TO_RIGHT;
		if (dpStyle == 0)
			datePicker = new DatePicker(popup, pickerStyle);
		else
			datePicker = new DatePicker(popup, pickerStyle, dpStyle);
		if (font != null)
			datePicker.setFont(font);
		if (foreground != null)
			datePicker.setForeground(foreground);
		if (background != null)
			datePicker.setBackground(background);
		int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };
		for (int i = 0; i < popupEvents.length; i++)
			popup.addListener(popupEvents[i], listener);
	}

	void dropDown(final boolean drop) {
		if (drop == isDropped())
			return;
		if (!drop) {
			popup.setVisible(false);
			if (!isDisposed() && arrow.isFocusControl()) {
				text.setFocus();
			}
			return;
		}
		if (getShell() != popup.getParent()) {
			popup.dispose();
			popup = null;
			datePicker = null;
			createPopup();
		}
		Point pickerSize = datePicker.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				false);
		datePicker.setBounds(1, 1, pickerSize.x, pickerSize.y);
		Display display = getDisplay();
		Rectangle pickerRect = datePicker.getBounds();
		Rectangle parentRect = display.map(getParent(), null, getBounds());
		Point comboSize = getSize();
		Rectangle displayRect = getMonitor().getClientArea();
		int width = pickerRect.width + 2;
		int height = pickerRect.height + 2;
		int x = parentRect.x;
		int y = parentRect.y + comboSize.y;
		if (y + height > displayRect.y + displayRect.height)
			y = parentRect.y - height;
		popup.setBounds(x, y, width, height);
		popup.setVisible(true);
		datePicker.setDate(date);
		datePicker.setFocus();
	}

	/*
	 * Return the Label immediately preceding the receiver in the z-order, or
	 * null if none.
	 */
	Label getAssociatedLabel() {
		Control[] siblings = getParent().getChildren();
		for (int i = 0; i < siblings.length; i++) {
			if (siblings[i] == DatePickerCombo.this) {
				if (i > 0 && siblings[i - 1] instanceof Label) {
					return (Label) siblings[i - 1];
				}
			}
		}
		return null;
	}

	/**
	 * Gets the editable state.
	 * 
	 * @return whether or not the reciever is editable
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public boolean getEditable() {
		checkWidget();
		return text.getEditable();
	}

	char getMnemonic(final String string) {
		int index = 0;
		int length = string.length();
		do {
			while ((index < length) && (string.charAt(index) != '&'))
				index++;
			if (++index >= length)
				return '\0';
			if (string.charAt(index) != '&')
				return string.charAt(index);
			index++;
		} while (index < length);
		return '\0';
	}

	/**
	 * Returns a <code>Point</code> whose x coordinate is the start of the
	 * selection in the receiver's text field, and whose y coordinate is the end
	 * of the selection. The returned values are zero-relative. An "empty"
	 * selection as indicated by the the x and y coordinates having the same
	 * value.
	 * 
	 * @return a point representing the selection start and end
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getSelection() {
		checkWidget();
		return text.getSelection();
	}

	public int getStyle() {
		int style = super.getStyle();
		style &= ~SWT.READ_ONLY;
		if (!text.getEditable())
			style |= SWT.READ_ONLY;
		return style;
	}

	/**
	 * Returns a string containing a copy of the contents of the receiver's text
	 * field.
	 * 
	 * @return the receiver's text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getText() {
		checkWidget();
		return text.getText();
	}

	/**
	 * Returns the height of the receivers's text field.
	 * 
	 * @return the text height
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getTextHeight() {
		checkWidget();
		return text.getLineHeight();
	}

	/**
	 * Returns the maximum number of characters that the receiver's text field
	 * is capable of holding. If this has not been changed by
	 * <code>setTextLimit()</code>, it will be the constant
	 * <code>Combo.LIMIT</code>.
	 * 
	 * @return the text limit
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getTextLimit() {
		checkWidget();
		return text.getTextLimit();
	}

	void handleFocus(final int type) {
		if (isDisposed())
			return;
		switch (type) {
		case SWT.FocusIn: {
			if (hasFocus)
				return;
			if (getEditable())
				text.selectAll();
			hasFocus = true;
			Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			shell.addListener(SWT.Deactivate, listener);
			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			display.addFilter(SWT.FocusIn, filter);
			Event e = new Event();
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus)
				return;
			Control focusControl = getDisplay().getFocusControl();
			if (focusControl == arrow || focusControl == datePicker
					|| focusControl == text)
				return;
			hasFocus = false;
			Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			Event e = new Event();
			notifyListeners(SWT.FocusOut, e);
			break;
		}
		}
	}

	void initAccessible() {
		AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {
			public void getName(final AccessibleEvent e) {
				String name = null;
				Label label = getAssociatedLabel();
				if (label != null) {
					name = stripMnemonic(label.getText());
				}
				e.result = name;
			}

			public void getKeyboardShortcut(final AccessibleEvent e) {
				String shortcut = null;
				Label label = getAssociatedLabel();
				if (label != null) {
					String text = label.getText();
					if (text != null) {
						char mnemonic = getMnemonic(text);
						if (mnemonic != '\0') {
							shortcut = "Alt+" + mnemonic; //$NON-NLS-1$
						}
					}
				}
				e.result = shortcut;
			}

			public void getHelp(final AccessibleEvent e) {
				e.result = getToolTipText();
			}
		};
		getAccessible().addAccessibleListener(accessibleAdapter);
		text.getAccessible().addAccessibleListener(accessibleAdapter);
		datePicker.getAccessible().addAccessibleListener(accessibleAdapter);
		arrow.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getName(final AccessibleEvent e) {
				e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			public void getKeyboardShortcut(final AccessibleEvent e) {
				e.result = "Alt+Down Arrow"; //$NON-NLS-1$
			}

			public void getHelp(final AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});
		getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {
			public void getCaretOffset(final AccessibleTextEvent e) {
				e.offset = text.getCaretPosition();
			}
		});
		getAccessible().addAccessibleControlListener(
				new AccessibleControlAdapter() {
					public void getChildAtPoint(final AccessibleControlEvent e) {
						Point testPoint = toControl(e.x, e.y);
						if (getBounds().contains(testPoint)) {
							e.childID = ACC.CHILDID_SELF;
						}
					}

					public void getLocation(final AccessibleControlEvent e) {
						Rectangle location = getBounds();
						Point pt = toDisplay(location.x, location.y);
						e.x = pt.x;
						e.y = pt.y;
						e.width = location.width;
						e.height = location.height;
					}

					public void getChildCount(final AccessibleControlEvent e) {
						e.detail = 0;
					}

					public void getRole(final AccessibleControlEvent e) {
						e.detail = ACC.ROLE_COMBOBOX;
					}

					public void getState(final AccessibleControlEvent e) {
						e.detail = ACC.STATE_NORMAL;
					}

					public void getValue(final AccessibleControlEvent e) {
						e.result = getText();
					}
				});
		text.getAccessible().addAccessibleControlListener(
				new AccessibleControlAdapter() {
					public void getRole(final AccessibleControlEvent e) {
						e.detail = text.getEditable() ? ACC.ROLE_TEXT
								: ACC.ROLE_LABEL;
					}
				});
		arrow.getAccessible().addAccessibleControlListener(
				new AccessibleControlAdapter() {
					public void getDefaultAction(final AccessibleControlEvent e) {
						e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				});
	}

	boolean isDropped() {
		return popup.getVisible();
	}

	public boolean isFocusControl() {
		checkWidget();
		if (text.isFocusControl() || arrow.isFocusControl()
				|| datePicker.isFocusControl() || popup.isFocusControl()) {
			return true;
		}
		return super.isFocusControl();
	}

	void internalLayout(final boolean changed) {
		if (isDropped())
			dropDown(false);
		Rectangle rect = getClientArea();
		int width = rect.width;
		int height = rect.height;
		Point arrowSize = arrow.computeSize(SWT.DEFAULT, height, changed);
		text.setBounds(0, 0, width - arrowSize.x, height);
		arrow.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);
	}

	void pickerEvent(final Event event) {
		switch (event.type) {
		case SWT.Dispose:
			if (getShell() != popup.getParent()) {
				popup = null;
				datePicker = null;
				createPopup();
			}
			break;
		case SWT.FocusIn: {
			handleFocus(SWT.FocusIn);
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1)
				return;
			dropDown(false);
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_ESCAPE:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				event.doit = false;
				break;
			}
			Event e = new Event();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners(SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		case SWT.KeyUp: {
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyUp, e);
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.ESC) {
				// Escape key cancels popup list
				dropDown(false);
			}
			if ((event.stateMask & SWT.ALT) != 0
					&& (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
				dropDown(false);
			}
			if (event.character == SWT.CR) {
				// Enter causes default selection
				dropDown(false);
				Event e = new Event();
				e.time = event.time;
				e.stateMask = event.stateMask;
				notifyListeners(SWT.DefaultSelection, e);
			}
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed())
				break;
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyDown, e);
			break;
		}
		}
	}

	void popupEvent(final Event event) {
		switch (event.type) {
		case SWT.Paint:
			// draw black rectangle around list
			Rectangle listRect = datePicker.getBounds();
			Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
			event.gc.setForeground(black);
			event.gc.drawRectangle(0, 0, listRect.width + 1,
					listRect.height + 1);
			break;
		case SWT.Close:
			event.doit = false;
			dropDown(false);
			break;
		case SWT.Deactivate:
			// postpone hide - maybe it is the arrow that has got
			// focus, so the popup should not be hidden
			getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (!hasFocus) {
						dropDown(false);
					}
				}
			});
			break;
		}
	}

	public void redraw() {
		super.redraw();
		text.redraw();
		arrow.redraw();
		if (popup.isVisible())
			datePicker.redraw();
	}

	public void redraw(final int x, final int y, final int width,
			final int height, final boolean all) {
		super.redraw(x, y, width, height, true);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's text is modified.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(final ModifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Modify, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's selection changes.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
	}

	public void setBackground(final Color color) {
		super.setBackground(color);
		background = color;
		if (text != null)
			text.setBackground(color);
		if (datePicker != null)
			datePicker.setBackground(color);
		if (arrow != null)
			arrow.setBackground(color);
	}

	/**
	 * Sets the editable state.
	 * 
	 * @param editable
	 *            the new editable state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public void setEditable(final boolean editable) {
		checkWidget();
		text.setEditable(editable);
	}

	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		if (popup != null)
			popup.setVisible(false);
		if (text != null)
			text.setEnabled(enabled);
		if (arrow != null)
			arrow.setEnabled(enabled);
	}

	public boolean setFocus() {
		checkWidget();
		return text.setFocus();
	}

	public void setFont(final Font font) {
		super.setFont(font);
		this.font = font;
		text.setFont(font);
		datePicker.setFont(font);
		internalLayout(true);
	}

	public void setForeground(final Color color) {
		super.setForeground(color);
		foreground = color;
		if (text != null)
			text.setForeground(color);
		if (datePicker != null)
			datePicker.setForeground(color);
		if (arrow != null)
			arrow.setForeground(color);
	}

	/**
	 * Sets the layout which is associated with the receiver to be the argument
	 * which may be null.
	 * <p>
	 * Note : No Layout can be set on this Control because it already manages
	 * the size and position of its children.
	 * </p>
	 * 
	 * @param layout
	 *            the receiver's new layout or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLayout(final Layout layout) {
		checkWidget();
		return;
	}

	/**
	 * Sets the selection in the receiver's text field to the range specified by
	 * the argument whose x coordinate is the start of the selection and whose y
	 * coordinate is the end of the selection.
	 * 
	 * @param selection
	 *            a point representing the new selection start and end
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(final Point selection) {
		checkWidget();
		if (selection == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		text.setSelection(selection.x, selection.y);
	}

	public void setToolTipText(final String string) {
		checkWidget();
		super.setToolTipText(string);
		arrow.setToolTipText(string);
		text.setToolTipText(string);
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		if (!visible)
			popup.setVisible(false);
	}

	String stripMnemonic(final String string) {
		int index = 0;
		int length = string.length();
		do {
			while ((index < length) && (string.charAt(index) != '&'))
				index++;
			if (++index >= length)
				return string;
			if (string.charAt(index) != '&') {
				return string.substring(0, index - 1)
						+ string.substring(index, length);
			}
			index++;
		} while (index < length);
		return string;
	}

	private void updateText() {
		if (date == null) {
			text.setText("");
		} else {
			String dateString = dateFormat.format(date);
			text.setText(dateString);
		}
		dropDown(false);
	}

	public void setDate(final Date date) {
		this.date = date;
		updateText();
	}

	public Date getDate() {
		return date;
	}

	public void setLocale(final Locale locale) {
		datePicker.setLocale(locale);
		updateText();
	}

	public void setDateFormat(final DateFormat dateFormat) {
		this.dateFormat = dateFormat;
		updateText();
	}

	public void addDateSelectionListener(final DateSelectionListener listener) {
		datePicker.addDateSelectionListener(listener);
	}

	public void removeDateSelectionListener(final DateSelectionListener listener) {
		datePicker.removeDateSelectionListener(listener);
	}
}
