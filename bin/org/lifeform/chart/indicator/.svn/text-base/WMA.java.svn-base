package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Weighted Moving Average. Check here for details:
 * http://en.wikipedia.org/wiki/Moving_average
 */
public class WMA extends Indicator {
	private final int length;

	public WMA(final QuoteHistory qh, final int length) {
		super(qh);
		this.length = length;
	}

	@Override
	public double calculate() {
		int endBar = qh.size() - 1;
		int startBar = endBar - length + 1;
		double wma = 0, multiplier, multipliers = 0;

		for (int bar = startBar; bar <= endBar; bar++) {
			multiplier = length - (endBar - bar);
			wma += qh.getPriceBar(bar).getClose() * (multiplier);
			multipliers = multipliers + multiplier;
		}

		value = wma / multipliers;
		return value;
	}
}
