package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Regressed value Reference: http://en.wikipedia.org/wiki/Least_squares
 */
public class LeastSquaresValue extends Indicator {
	private final int period;

	public LeastSquaresValue(final QuoteHistory qh, final int period) {
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
		double intercept = (sumY - slope * sumX) / period;
		value = slope * lastBar + intercept;
		return value;
	}

}
