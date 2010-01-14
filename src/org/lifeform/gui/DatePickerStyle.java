package org.lifeform.gui;

/**
 * the "DatePickerStyleParameter"
 */
public class DatePickerStyle {
	/**
	 * If this parameter is set the buttons to choose the Month/Years will be
	 * showen under the calendar and not over it.
	 */
	public static final int BUTTONS_ON_BOTTOM = 1; // 00000001
	/**
	 * If this parameter is set, buttons to choose the months will be showen.
	 */
	public static final int DISABLE_MONTH_BUTTONS = 2; // 00000010
	/**
	 * If this parameter is set, buttons to choose the years will be showen.
	 */
	public static final int YEAR_BUTTONS = 4; // 00000100
	/**
	 * If this parameter is set, buttons to choose 10-Yearjumps will be showen.
	 */
	public static final int TEN_YEARS_BUTTONS = 8; // 00001000
	/**
	 * With this parameter, no Today-Button will be showen.
	 */
	public static final int NO_TODAY_BUTTON = 16; // 00010000
	/**
	 * If this parameter is set, the DatePicker will be invisible if he loss his
	 * focus.
	 */
	public static final int HIDE_WHEN_NOT_IN_FOCUS = 32; // 00100000
	/**
	 * With this parameter, no doubleclick is needed to select a date.
	 */
	public static final int SINGLE_CLICK_SELECTION = 64; // 01000000
	/**
	 * If this parameter is set, the week starts monday.
	 */
	public static final int WEEKS_STARTS_ON_MONDAY = 128; // 10000000
}
