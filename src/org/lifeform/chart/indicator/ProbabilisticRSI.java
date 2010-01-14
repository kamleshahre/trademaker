package org.lifeform.chart.indicator;

import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;

/**
 * Experimental, under development
 */
public class ProbabilisticRSI extends Indicator {
	private final int periodLength;

	public ProbabilisticRSI(final QuoteHistory qh, final int periodLength) {
		super(qh);
		this.periodLength = periodLength;
	}

	/**
	 * Stirling gamma function for approximating factorials.
	 * 
	 * @param z
	 *            Only works for 1 <= z <= 142 but also defined for non
	 *            integers.
	 * 
	 * @returns approximation to (z-1)!
	 */
	private double gamma(final double z) {
		return Math.exp(-z)
				* Math.pow(z, z - 0.5)
				* Math.sqrt(2 * Math.PI)
				* (1 + 1 / (12 * z) + 1 / (288 * z * z) - 139
						/ (51840 * z * z * z) - 571 / (2488320 * z * z * z * z));
	}

	private double probability(final double r, final double total) {
		double nom = gamma(total + 1);
		double denom = gamma(r + 1) * gamma(total - r + 1) * Math.pow(2, total);
		return nom / denom;
	}

	@Override
	public double calculate() {
		int qhSize = qh.size();
		int lastBar = qhSize - 1;
		int firstBar = lastBar - periodLength + 1;

		double gains = 0, losses = 0;

		for (int barIndex = firstBar + 1; barIndex <= lastBar; barIndex++) {
			PriceBar bar = qh.getPriceBar(barIndex);
			PriceBar prevBar = qh.getPriceBar(barIndex - 1);
			double change = bar.getClose() - prevBar.getClose();

			gains += Math.max(0, change);
			losses += Math.max(0, -change);
		}

		double totalChange = gains + losses;

		double trend = (totalChange == 0) ? 50 : (100 * gains / totalChange);
		// rescale so that the range is from -100 to +100
		double rsi = (trend - 50) * 2;
		double significance = 1 - probability(gains, totalChange);
		value = rsi * significance;
		if (trend < 0)
			value = -value;

		return value;
	}
}
