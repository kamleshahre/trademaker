package org.lifeform.chart.indicator.velocity;

import org.lifeform.chart.indicator.Indicator;

/**
 * Measures the velocity of the strength of a trend.
 */
public class TrendStrengthVelocity extends Indicator {
	private final double multiplier;
	private double upEma, downEma;
	private double smoothedTrend, doubleSmoothedTrend;
	private double previousPrice;

	public TrendStrengthVelocity(final int periodLength) {
		multiplier = 2.0 / (periodLength + 1.0);
	}

	@Override
	public double calculate() {
		double price = marketBook.getSnapshot().getPrice();

		if (previousPrice != 0) {
			double change = price - previousPrice;

			double up = (change > 0) ? change : 0;
			double down = (change < 0) ? -change : 0;

			upEma += multiplier * (up - upEma);
			downEma += multiplier * (down - downEma);
			double sum = upEma + downEma;
			double trend = (sum == 0) ? 0 : Math.abs(upEma - downEma) / sum;

			smoothedTrend += multiplier * (trend - smoothedTrend);
			doubleSmoothedTrend += multiplier
					* (smoothedTrend - doubleSmoothedTrend);

			value = 100 * (smoothedTrend - doubleSmoothedTrend);
		}

		previousPrice = price;
		return value;

	}

	@Override
	public void reset() {
		upEma = downEma = smoothedTrend = doubleSmoothedTrend = previousPrice = 0;
	}
}
