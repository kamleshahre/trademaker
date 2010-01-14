package org.lifeform.time;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * TradingInterval defines the time period during which a strategy can trade. A
 * trading interval is defined by the interval start and the interval end.
 * Trading can start after the "open" time. Open positions will be closed at the
 * "end" time, unless "allowCarry" is set to true. The "start" and "end" times
 * must be specified in the military time format within the specified time zone.
 * <p/>
 * Example: suppose the strategy defines the following trading interval:
 * setTradingInterval("9:35", "15:45", "America/New_York", false); Then the
 * following trading timeline is formed: -- start trading at 9:35 -- close open
 * positions at 15:45 (do not allow position carry)
 * <p/>
 * You can exclude a time period by setting a start time older than the end time
 * Example: suppose the strategy defines the following trading interval:
 * setTradingInterval("15:45", "15:15", "America/New_York", false); Then the
 * following trading timeline is formed: -- start trading now -- close open
 * positions at 15:15 (do not allow position carry) -- restart trading at 15:45
 */
public class TradingInterval {
	private final Calendar start, end;
	private final boolean allowCarry;
	private final TimeZone tz;

	public TradingInterval(final String startTime, final String endTime, final String timeZone,
			final boolean allowCarry) throws Exception {
		tz = DateUtil.getTimeZone(timeZone);
		start = DateUtil.getTime(startTime, tz);
		end = DateUtil.getTime(endTime, tz);
		this.allowCarry = allowCarry;
	}

	public TradingInterval() throws Exception {
		this("0:00", "0:00", "America/New_York", false);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[start: ").append(
				DateUtil.HOUR_MINUTE_DATEFORMAT.format(start.getTime()));
		sb.append(" end: ").append(
				DateUtil.HOUR_MINUTE_DATEFORMAT.format(end.getTime()));
		return sb.toString();
	}

	public Calendar getStart() {
		return start;
	}

	public Calendar getEnd() {
		return end;
	}

	public int getStartMinutes() {
		return DateUtil.getMinutes(start);
	}

	public int getEndMinutes() {
		return DateUtil.getMinutes(end);
	}

	public boolean getAllowCarry() {
		return allowCarry;
	}

	public TimeZone getTimeZone() {
		return tz;
	}

	public boolean getExclusionMode() {
		return !end.after(start);
	}
}
