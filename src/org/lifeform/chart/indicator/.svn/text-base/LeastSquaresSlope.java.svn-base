package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Slope of the regression line Reference:
 * http://en.wikipedia.org/wiki/Least_squares
 */
public class LeastSquaresSlope extends Indicator {
	private final int period;

	public LeastSquaresSlope(final QuoteHistory qh, final int period) {
		super(qh);
		this.period = period;
	}

	@Override
	public double calculate() {
		int lastBar = qh.size() - 1;
		int firstBar = lastBar - period + 1;

		double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
		for (int bar = firstBar; bar <= lastBar; bar++) {
			double y = qh.getPriceBar(bar).getClose();
			sumX += bar;
			sumY += y;
			sumXY += (bar * y);
			sumXX += (bar * bar);
		}

		double slope = period * sumXY - sumX * sumY;
		slope /= (period * sumXX - sumX * sumX);
		value = slope;
		return value;
	}

}
