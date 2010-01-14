package org.lifeform.chart.indicator.velocity;

import org.lifeform.chart.indicator.Indicator;

/**
 * Velocity of price
 */
public class PriceVelocity extends Indicator {
	private final double fastMultiplier, slowMultiplier;
	private double fast, slow;

	public PriceVelocity(final int fastPeriod, final int slowPeriod) {
		fastMultiplier = 2. / (fastPeriod + 1.);
		slowMultiplier = 2. / (slowPeriod + 1.);
	}

	@Override
	public double calculate() {
		double price = marketBook.getSnapshot().getPrice();
		fast += (price - fast) * fastMultiplier;
		slow += (price - slow) * slowMultiplier;
		value = fast - slow;
		return value;

	}

	@Override
	public void reset() {
		fast = slow = marketBook.getSnapshot().getPrice();
		value = 0;
	}
}
