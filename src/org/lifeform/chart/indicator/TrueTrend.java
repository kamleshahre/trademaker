package org.lifeform.chart.indicator;

import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;

/**
 * TrueTrend
 */
public class TrueTrend extends Indicator {
	private final int periodLength;

	public TrueTrend(final QuoteHistory qh, final int periodLength) {
		super(qh);
		this.periodLength = periodLength;
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

			// double barMidPoint = (bar.getHigh() + bar.getLow()) / 2;
			// double prevBarMidPoint = (prevBar.getHigh() + prevBar.getLow()) /
			// 2;

			// double midPointChange = barMidPoint - prevBarMidPoint;
			double closeChange = bar.getClose() - prevBar.getClose();
			// double volume = bar.getVolume();

			// double trueChange = (closeChange + midPointChange) / 2;
			double change = closeChange;// * volume;// * volume;

			gains += Math.max(0, change);
			losses += Math.max(0, -change);
		}

		double totalChange = gains + losses;

		double trend = (totalChange == 0) ? 0 : (100 * gains / totalChange);
		// rescale so that the range is from -100 to +100
		value = (trend - 50) * 2;

		return value;
	}
}
