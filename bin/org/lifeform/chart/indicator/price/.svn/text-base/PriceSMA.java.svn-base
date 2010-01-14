package org.lifeform.chart.indicator.price;

import java.util.LinkedList;

import org.lifeform.chart.indicator.Indicator;

public class PriceSMA extends Indicator {
	private final int period;
	private final LinkedList<Double> prices;
	private double sum;

	public PriceSMA(final int period) {
		this.period = period;
		prices = new LinkedList<Double>();
	}

	@Override
	public double calculate() {

		double price = marketBook.getSnapshot().getPrice();
		sum += price;

		// In with the new
		prices.add(price);

		// Out with the old
		while (prices.size() > period) {
			sum -= prices.removeFirst();
		}

		if (prices.size() > 0) {
			value = sum / prices.size();
		}
		return value;

	}

	@Override
	public void reset() {
		value = sum = 0.0;
		prices.clear();
	}
}
