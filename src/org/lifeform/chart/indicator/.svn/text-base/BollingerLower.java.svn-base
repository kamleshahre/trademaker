package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Lower Bollinger Band
 */
public class BollingerLower extends Indicator {
	private final int length;
	private final double deviations;

	public BollingerLower(final QuoteHistory qh, final int length, final double deviations) {
		super(qh);
		this.length = length;
		this.deviations = deviations;
	}

	@Override
	public double calculate() {
		int lastBar = qh.size() - 1;
		int firstBar = lastBar - length + 1;

		double squareSum = 0;
		double sum = 0;

		for (int bar = firstBar; bar <= lastBar; bar++) {
			double barClose = qh.getPriceBar(bar).getClose();
			sum += barClose;
			squareSum += barClose * barClose;
		}

		double stDev = length * squareSum - sum * sum;
		stDev /= (length * (length - 1));
		stDev = Math.sqrt(stDev);

		value = (sum / length) - deviations * stDev;
		return value;
	}
}
