package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Trend divergence indicator
 */
public class TrendDivergence extends Indicator {
	private final int shorterPeriod;
	private final int longerPeriod;

	public TrendDivergence(final QuoteHistory qh, final int shorterPeriod, final int longerPeriod) {
		super(qh);
		this.shorterPeriod = shorterPeriod;
		this.longerPeriod = longerPeriod;
	}

	@Override
	public double calculate() {
		double shorterRSI = new RSI(qh, shorterPeriod).calculate();
		double longerRSI = new RSI(qh, longerPeriod).calculate();
		value = shorterRSI - longerRSI;

		return value;
	}
}
