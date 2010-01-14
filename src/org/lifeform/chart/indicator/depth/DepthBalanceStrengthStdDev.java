package org.lifeform.chart.indicator.depth;

import java.util.LinkedList;

import org.lifeform.chart.indicator.Indicator;

/**
 * This indicator computes the standard deviation of the last <period> prices.
 * <p/>
 * To compute the Bollinger Bands, use this indicator, and take: getMean() +
 * getValue() * NumStdDev for the upper band and getMnea() - getValue() *
 * NumStdDev for the lower band.
 * 
 * @author mkoistinen
 */
public class DepthBalanceStrengthStdDev extends Indicator {

	private final int period;

	private final LinkedList<Double> history = new LinkedList<Double>();
	private double strength, sum, mean, sum_sqr;

	public DepthBalanceStrengthStdDev(final int period) {
		this.period = period;
		value = sum = sum_sqr = mean = 0.0;
		history.clear();
	}

	@Override
	public void reset() {
		value = sum = sum_sqr = mean = 0.0;
		history.clear();
	}

	@Override
	public double calculate() {
		strength = Math.abs(marketBook.getSnapshot().getBalance());

		history.addLast(strength);
		sum += strength;
		sum_sqr += strength * strength;

		if (history.size() > period) {
			strength = history.removeFirst();
			sum -= strength;
			sum_sqr -= strength * strength;
		}

		if (history.size() > 0) {
			mean = sum / history.size();
			value = Math.sqrt((sum_sqr - sum * mean) / history.size());
		}
		return value;
	}

	public double getMean() {
		return mean;
	}

}
