/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ernan Hughes - initial API and implementation
 *******************************************************************************/
package org.lifeform.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.lifeform.configuration.Defaults;
import org.lifeform.product.Product;

import com.toedter.calendar.JTextFieldDateEditor;

public final class DateUtil {

	public static final SimpleDateFormat DATE_DATEFORMAT = (SimpleDateFormat) DateFormat
			.getDateTimeInstance();
	public static final SimpleDateFormat DATE_TIME_DATEFORMAT = (SimpleDateFormat) DateFormat
			.getDateTimeInstance();
	public static final SimpleDateFormat FULL_DATEFORMAT = (SimpleDateFormat) DateFormat
			.getDateTimeInstance();
	public static final SimpleDateFormat HOUR_MINUTE_DATEFORMAT = (SimpleDateFormat) DateFormat
			.getDateTimeInstance();

	static {
		DATE_DATEFORMAT.applyPattern("yyyy-MM-dd");
		DATE_TIME_DATEFORMAT.applyPattern("yyyy-MM-dd HH:mm:ss");
		FULL_DATEFORMAT.applyPattern("yyyy-MM-dd G HH:mm:ss.SSS z");
		HOUR_MINUTE_DATEFORMAT.applyPattern("HH:mm");
	}

	private static final int miliPerDay = 60 * 60 * 24 * 1000;

	public static Calendar addDays(final Calendar calendar, final int days) {
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar;
	}

	public static Calendar addHour(final Calendar calendar, final int days) {
		calendar.add(Calendar.HOUR, days);
		return calendar;
	}

	public static Calendar addMinute(final Calendar calendar, final int days) {
		calendar.add(Calendar.MINUTE, days);
		return calendar;
	}

	public static Calendar addMonths(final Calendar calendar, final int days) {
		calendar.add(Calendar.MONTH, days);
		return calendar;
	}

	public static Calendar addSecond(final Calendar calendar, final int days) {
		calendar.add(Calendar.SECOND, days);
		return calendar;
	}

	public static Calendar addYears(final Calendar calendar, final int days) {
		calendar.add(Calendar.YEAR, days);
		return calendar;
	}

	public static boolean after(final Calendar first, final Calendar second) {
		return !before(first, second);
	}

	public static boolean before(final Calendar first, final Calendar second) {
		return first.compareTo(second) < 0;
	}

	public static Calendar create(final int year, final int month, final int day) {
		return create(year, month, day, 0, 0, 0);
	}

	public static Calendar create(final int year, final int month,
			final int dayOfMonth, final int hourOfDay, final int minute,
			final int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, dayOfMonth, hourOfDay, minute, second);
		return calendar;
	}

	public static Calendar create(final int year, final int month,
			final int dayOfMonth, final int hourOfDay, final int minute,
			final int second, final int milliSecond) {
		Calendar calendar = create(year, month - 1, dayOfMonth, hourOfDay,
				minute, second);
		calendar.set(Calendar.MILLISECOND, milliSecond);
		return calendar;
	}

	public static Calendar[] createEquidistantDates(Calendar reference,
			final int periods, final TimeUnit sampleUnit, final int sampleRate,
			Calendar calendar) {

		if (reference == null) {
			reference = Calendar.getInstance();
		}
		assert (periods < 0) : "Number of periods must be " + "nonnegative.\n"
				+ "periods: " + periods;
		assert (sampleRate == 0) : "Sample rate must not be 0.";
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		Calendar[] dates = new Calendar[periods + 1];
		calendar.setLenient(true);
		int field = 0;
		dates[0] = reference;
		for (int i = 1; i <= periods; i++) {
			calendar.add(field, sampleRate);
			dates[i] = calendar;
		}
		return dates;
	}

	public static double getDaysBetween(final Calendar start, final Calendar end) {
		return getDaysBetween(start.getTimeInMillis(), end.getTimeInMillis());
	}

	public static double getDaysBetween(final long start, final long end) {
		int days = (int) ((end - start) / miliPerDay);
		return days;
	}

	public static Calendar max(final Calendar... calendars) {
		Vector<Calendar> list = new Vector<Calendar>(Arrays.asList(calendars));
		Collections.sort(list);
		return list.lastElement();
	}

	public static Calendar min(final Calendar... calendars) {
		Vector<Calendar> list = new Vector<Calendar>(Arrays.asList(calendars));
		Collections.sort(list);
		return list.firstElement();
	}

	public static String toString(final long time) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(time);
		return toString(result);
	}

	public static String toString(final Calendar calendar) {
		return toString(calendar, "d MMM yyyy hh:mm aaa");
	}

	public static String toString(final Calendar calendar, final String format) {
		final SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}

	static Calendar nullDate = Calendar.getInstance();

	public static Calendar getNullDate() {
		return nullDate;
	}

	private DateUtil() {
	}

	public static Calendar addTerm(final Calendar start, final Term term) {
		return term.addTo(start);
	}

	public static Calendar getDate(final long milli) {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(milli);
		return date;
	}

	public static String toDateString(final long resultDate) {
		return toString(getDate(resultDate));
	}

	public static boolean isMaturityDate(final Calendar date,
			final Product product) {
		return isSameDay(date, product.getMaturity());
	}

	private static boolean isSameDay(final Calendar date1, final Calendar date2) {
		return getDaysBetween(date1, date2) < miliPerDay;
	}

	public static long fromString(final Object object, final String fmtStr) {
		SimpleDateFormat fmt = (SimpleDateFormat) SimpleDateFormat
				.getDateTimeInstance();
		fmt.applyPattern(fmtStr);
		try {
			Date dt = fmt.parse(String.valueOf(object));
			return dt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Calendar getDate(final JTextFieldDateEditor dateField) {
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(dateField.getDate().getTime());
		normalizeDate(result);
		return result;
	}

	public static void normalizeDate(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public static long Now() {
		return Calendar.getInstance().getTimeInMillis();
	}

	public static TimeZone getTimeZone(final String timeZone) throws Exception {
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		if (!tz.getID().equals(timeZone)) {
			String msg = "The specified time zone " + "\"" + timeZone + "\""
					+ " does not exist." + Defaults.getLineSeperator();
			msg += "Examples of valid time zones: "
					+ " America/New_York, Europe/London, Asia/Singapore.";
			throw new Exception(msg);
		}
		return tz;
	}

	public static Calendar getTime(final String time, final TimeZone tz)
			throws Exception {
		int hours, minutes;
		StringTokenizer st = new StringTokenizer(time, ":");
		int tokens = st.countTokens();
		if (tokens != 2) {
			String msg = "Time " + time
					+ " does not conform to the HH:MM format.";
			throw new Exception(msg);
		}

		String hourToken = st.nextToken();
		try {
			hours = Integer.parseInt(hourToken);
		} catch (NumberFormatException nfe) {
			String msg = hourToken + " in " + time
					+ " can not be parsed as hours.";
			throw new Exception(msg);
		}

		String minuteToken = st.nextToken();
		try {
			minutes = Integer.parseInt(minuteToken);
		} catch (NumberFormatException nfe) {
			String msg = minuteToken + " in " + time
					+ " can not be parsed as minutes.";
			throw new Exception(msg);
		}

		if (hours < 0 || hours > 23) {
			String msg = "Specified hours: " + hours
					+ ". Number of hours must be in the [0..23] range.";
			throw new Exception(msg);
		}

		if (minutes < 0 || minutes > 59) {
			String msg = "Specified minutes: " + minutes
					+ ". Number of minutes must be in the [0..59] range.";
			throw new Exception(msg);
		}

		Calendar period = Calendar.getInstance(tz);
		period.set(Calendar.HOUR_OF_DAY, hours);
		period.set(Calendar.MINUTE, minutes);
		// set seconds explicitly, otherwise they will be carried from the
		// current time
		period.set(Calendar.SECOND, 0);

		return period;
	}

	public static int getMinutes(final Calendar time) {
		return time.get(Calendar.HOUR_OF_DAY) * 60 + time.get(Calendar.MINUTE);
	}
}
