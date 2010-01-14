package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 */
public class NoiseAdjustedRSI extends Indicator {
	private final int periodLength;

	public NoiseAdjustedRSI(final QuoteHistory qh, final int periodLength) {
		super(qh);
		this.periodLength = periodLength;
	}

	@Override
	public double calculate() {
		int qhSize = qh.size();
		int lastBar = qhSize - 1;
		int firstBar = lastBar - periodLength + 1;

		double gains = 0, losses = 0;

		for (int bar = firstBar + 1; bar <= lastBar; bar++) {
			double change = qh.getPriceBar(bar).getClose()
					- qh.getPriceBar(bar - 1).getClose();
			gains += Math.max(0, change);
			losses += Math.max(0, -change);
		}

		double change = gains + losses;

		double rsi = (change == 0) ? 50 : (100 * gains / change);
		value = (rsi - 50) * Math.sqrt(periodLength - 1);

		return value;
	}
}
