package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Simple Moving Average.
 */
public class SMA extends Indicator {
	private final int length;

	public SMA(final QuoteHistory qh, final int length) {
		super(qh);
		this.length = length;
	}

	@Override
	public double calculate() {
		int endBar = qh.size() - 1;
		int startBar = endBar - length + 1;
		double sma = 0;
		for (int bar = startBar; bar <= endBar; bar++) {
			sma += qh.getPriceBar(bar).getClose();
		}
		value = sma / length;
		return value;
	}
}
