package org.lifeform.chart.indicator.depth;

import org.lifeform.chart.indicator.Indicator;

/**
 * Exponential moving average of the market depth balance.
 */
public class DepthBalanceEMA extends Indicator {
	private final double multiplier;

	public DepthBalanceEMA(final int length) {
		multiplier = 2. / (length + 1.);
	}

	@Override
	public double calculate() {
		double balance = marketBook.getSnapshot().getBalance();
		value += (balance - value) * multiplier;
		return value;
	}

	@Override
	public void reset() {
		value = 0;
	}

}
