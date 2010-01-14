package org.lifeform.chart.indicator.depth;

import org.lifeform.chart.indicator.Indicator;

public class DepthBalanceStrengthEMA extends Indicator {
	private final double multiplier;

	public DepthBalanceStrengthEMA(final int period) {
		this.multiplier = 2.0 / (period + 1);
		value = 0.0;
	}

	@Override
	public double calculate() {
		double balance = Math.abs(marketBook.getSnapshot().getBalance());
		value += multiplier * (balance - value);
		return value;
	}

	@Override
	public void reset() {
		value = 0.0;
	}
}
