package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * MACD
 */
public class MACD extends Indicator {
	private final int fastLength, slowLength;

	public MACD(final QuoteHistory qh, final int fastLength, final int slowLength) {
		super(qh);
		this.fastLength = fastLength;
		this.slowLength = slowLength;
	}

	@Override
	public double calculate() {
		double fastEMA = new EMA(qh, fastLength).calculate();
		double slowEMA = new EMA(qh, slowLength).calculate();
		value = fastEMA - slowEMA;

		return value;
	}
}
