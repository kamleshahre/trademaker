package org.lifeform.chart;

/**
 * Encapsulates the price/indicator bar information.
 */
public class Bar {
	private final long time;
	private final double open;
	private double high, low, close;

	private Bar(final long time, final double open, final double high, final double low, final double close) {
		this.time = time;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}

	public Bar(final long time, final double value) {
		this(time, value, value, value, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" time: ").append(time);
		sb.append(" open: ").append(open);
		sb.append(" high: ").append(high);
		sb.append(" low: ").append(low);
		sb.append(" close: ").append(close);
		return sb.toString();
	}

	public double getOpen() {
		return open;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getClose() {
		return close;
	}

	public void setHigh(final double high) {
		this.high = high;
	}

	public void setLow(final double low) {
		this.low = low;
	}

	public void setClose(final double close) {
		this.close = close;
	}

	public long getTime() {
		return time;
	}

}
