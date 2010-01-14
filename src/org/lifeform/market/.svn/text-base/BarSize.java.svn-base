package org.lifeform.market;

public enum BarSize {
	Sec5(5, "10000 S"), Sec15(15, "30000 S"), Sec30(30, "86400 S"), Min1(60,
			"6 D"), Min2(120, "6 D"), Min5(300, "6 D"), Min15(900, "20 D"), Min30(
			1800, "34 D"), Hour1(3600, "34 D");

	private final static int SECONDS_IN_MINUTE = 60;
	private final static int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60;
	private final int seconds;
	private final String histRequestDuration;

	private BarSize(int seconds, String histRequestDuration) {
		this.seconds = seconds;
		this.histRequestDuration = histRequestDuration;
	}

	public int toSeconds() {
		return seconds;
	}

	public String getHistRequestDuration() {
		return histRequestDuration;
	}

	@Override
	public String toString() {
		String timeUnit = "sec";
		int units = seconds;
		if (seconds >= SECONDS_IN_MINUTE) {
			timeUnit = "min";
			units = seconds / SECONDS_IN_MINUTE;
		}
		if (seconds >= SECONDS_IN_HOUR) {
			timeUnit = "hr";
			units = seconds / SECONDS_IN_HOUR;
		}
		return units + " " + timeUnit;
	}

	/**
	 * IB-type formatting for requesting historical data
	 */
	public String toIBText() {
		String ibText = toString();
		if (ibText.contains("sec")) {
			ibText += "s";
		}
		if (ibText.contains("min") && !ibText.contains("1 min")) {
			ibText += "s";
		}
		if (ibText.contains("hr")) {
			ibText = ibText.replace("hr", "hour");
		}
		return ibText;
	}

	public int getSize() {
		return seconds * 1000;
	}

	public static BarSize getBarSize(String name) {
		for (BarSize barSize : values()) {
			if (barSize.histRequestDuration.equals(name)) {
				return barSize;
			}
		}
		return null;
	}

}
