package org.lifeform.chart.indicator.price;

import org.lifeform.chart.indicator.Indicator;

public class PriceEMA extends Indicator {

	private final double alpha;

	private double price;

	public PriceEMA(final int period) {
		this.alpha = 2.0 / (period + 1.0);
		value = 0.0;
	}

	@Override
	public void reset() {
		value = 0.0;
	}

	@Override
	public double calculate() {
		price = marketBook.getSnapshot().getPrice();
		value += alpha * (price - value);
		return value;

	}

}
