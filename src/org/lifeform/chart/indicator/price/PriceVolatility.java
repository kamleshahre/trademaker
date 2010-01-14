package org.lifeform.chart.indicator.price;

import java.util.LinkedList;

import org.lifeform.chart.indicator.Indicator;

/**
 * Calculates the rolling volatility of prices
 */
public class PriceVolatility extends Indicator {
	private double sumPrice, sumPriceSquared;
	private final LinkedList<Double> prices;
	private final int periodLength;

	public PriceVolatility(final int periodLength) {
		this.periodLength = periodLength;
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
			value = 10000 * stdev / (sumPrice / periodLength);
		}
		return value;

	}

	@Override
	public void reset() {
		prices.clear();
		sumPrice = sumPriceSquared = value = 0;
	}
}
