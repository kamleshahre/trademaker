package org.lifeform.chart.indicator.velocity;

import java.util.LinkedList;

import org.lifeform.chart.indicator.Indicator;

/**
 * Calculates the velocity of the rolling volatility of prices
 */
public class PriceVolatilityVelocity extends Indicator {
	private double sumPrice, sumPriceSquared;
	private final LinkedList<Double> prices;
	private final int periodLength;
	private double smoothed, fast, slow, multiplier;

	public PriceVolatilityVelocity(final int periodLength) {
		this.periodLength = periodLength;
		multiplier = 2. / (periodLength + 1.);
		prices = new LinkedList<Double>();
	}

	@Override
	public double calculate() {
		double price = marketBook.getSnapshot().getPrice();
		prices.add(price);
		sumPrice += price;
		sumPriceSquared += price * price;

		if (prices.size() > periodLength) {
			double oldPrice = prices.removeFirst();

			sumPrice -= oldPrice;
			sumPriceSquared -= oldPrice * oldPrice;
			double stdev = Math.sqrt((sumPriceSquared - (sumPrice * sumPrice)
					/ periodLength)
					/ periodLength);
			// double stdev = sumPriceSquared - (sumPrice * sumPrice) /
			// periodLength; //ekk

			smoothed += multiplier * (stdev - smoothed);
			fast += multiplier * (smoothed - fast);
			slow += multiplier * (fast - slow);

			value = 100 * (fast - slow);
		}
		return value;

	}

	@Override
	public void reset() {
		prices.clear();
		smoothed = fast = slow = sumPrice = sumPriceSquared = value = 0;
	}
}
