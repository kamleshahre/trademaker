package org.lifeform.market;

/**
 * Encapsulates the price bar information.
 */

public class Snapshot {
	private final double open, high, low, close;
	private final long volume;

	public Snapshot(final double open, final double high, final double low, final double close,
			final long volume) {
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
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

	public double getMidpoint() {
		return (low + high) / 2;
	}

	public long getVolume() {
		return volume;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Snapshot: ");
		sb.append(" open: ").append(open);
		sb.append(" high: ").append(high);
		sb.append(" low: ").append(low);
		sb.append(" close: ").append(close);
		sb.append(" volume: ").append(volume);

		return sb.toString();
	}
}
