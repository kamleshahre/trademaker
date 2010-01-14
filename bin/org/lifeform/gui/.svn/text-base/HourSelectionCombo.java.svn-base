package org.lifeform.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class HourSelectionCombo extends Composite {

	/** backing CCombo */
	private CCombo hCombo;

	private ComboSelectionListener comboListener = new ComboSelectionListener();
	private LinkedList listeners = new LinkedList();
	private int minuteInterval = 30;
	private DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	private int minimumHour = 0;
	private int maximumHour = 23;

	public HourSelectionCombo(final Composite parent, final int style) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());
		hCombo = new CCombo(this, style | SWT.NO_BACKGROUND);
		updateCombo();
	}

	private void updateCombo() {
		hCombo.removeAll();
		Calendar calendar = Calendar.getInstance();
		int index = Math.max(0, calendar.get(Calendar.HOUR_OF_DAY)
				- minimumHour);
		int itemsPerHour = 60 / minuteInterval;
		index = index * itemsPerHour
				+ (calendar.get(Calendar.MINUTE) / minuteInterval);
		calendar.set(Calendar.HOUR_OF_DAY, minimumHour);

		// fill the combo with 24 hours
		for (int i = minimumHour; i <= maximumHour; i++) {
			calendar.set(Calendar.MINUTE, 0);
			int currMin = 0;
			int prevMin = -1;
			while (prevMin < currMin) {
				hCombo.add(dateFormat.format(calendar.getTime()));
				calendar.roll(Calendar.MINUTE, minuteInterval);
				prevMin = currMin;
				currMin = calendar.get(Calendar.MINUTE);
			}
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			hCombo.setVisibleItemCount(7);
			hCombo.addSelectionListener(comboListener);
		}
		if (index >= hCombo.getItemCount())
			index = 0;
		hCombo.select(index);
	}

	public Point computeSize(final int wHint, final int hHint,
			final boolean changed) {
		return hCombo.computeSize(wHint, hHint, changed);
	}

	private class ComboSelectionListener implements SelectionListener {
		public void widgetSelected(final SelectionEvent e) {
			int i = hCombo.getSelectionIndex();
			Date date;
			try {
				date = dateFormat.parse(hCombo.getItem(i));
			} catch (ParseException e1) {
				date = Calendar.getInstance().getTime();
			}
			DateSelectedEvent dse = new DateSelectedEvent(date);
			for (Iterator itr = listeners.iterator(); itr.hasNext();) {
				DateSelectionListener l = (DateSelectionListener) itr.next();
				l.dateSelected(dse);
			}
		}

		public void widgetDefaultSelected(final SelectionEvent e) {
		}
	}

	public void addDateSelectionListener(final DateSelectionListener listener) {
		listeners.add(listener);
	}

	public void removeDateSelectionListener(final DateSelectionListener listener) {
		listeners.remove(listener);
	}

	public Date getTime() {
		Date d;
		try {
			d = dateFormat.parse(hCombo.getItem(hCombo.getSelectionIndex()));
		} catch (ParseException e) {
			d = Calendar.getInstance().getTime();
		}
		return d;
	}

	public void setTime(final Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int index = Math.max(0, calendar.get(Calendar.HOUR_OF_DAY)
				- minimumHour);
		int itemsPerHour = 60 / minuteInterval;
		index = index * itemsPerHour
				+ (calendar.get(Calendar.MINUTE) / minuteInterval);
		calendar.set(Calendar.HOUR_OF_DAY, minimumHour);

		if (index >= hCombo.getItemCount())
			index = 0;
		hCombo.select(index);
	}

	/**
	 * Get the selected hour as String
	 * 
	 * @return
	 */
	public String getText() {
		return hCombo.getText();
	}

	/**
	 * Set minutes interval between two hours
	 * 
	 * @param interval
	 */
	public void setMinuteInterval(final int interval) {
		if ((interval > 60) || (interval < 1))
			throw new AssertionError(
					"minute interval should be between 1 - 30 minutes");
		this.minuteInterval = interval;
		updateCombo();
	}

	/**
	 * Set the date format for display.<br/>
	 * The default is a simple date format with the following pattern: "hh:mm a"
	 * 
	 * @param dateFormat
	 */
	public void setDateFormat(final DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Limits the hour range to display between minimum value and maximum value
	 * 
	 * @param min
	 *            - minimun value
	 * @param max
	 *            - maximum value
	 */
	public void setHourRange(final int min, final int max) {
		if (min > max)
			throw new AssertionError(
					"Minimum value must big smaller that maximum");
		this.minimumHour = min;
		this.maximumHour = max;
		updateCombo();
	}

	public boolean setFocus() {
		return hCombo.setFocus();
	}

	public void setBackground(final Color color) {
		hCombo.setBackground(color);
	}

	public void setFont(final Font font) {
		hCombo.setFont(font);
	}

	public void setForeground(final Color color) {
		hCombo.setForeground(color);
	}

}
