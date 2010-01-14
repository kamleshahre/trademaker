package org.lifeform.chart.indicator.depth;

import java.util.LinkedList;

import org.lifeform.chart.indicator.Indicator;

public class DepthBalanceStrengthSMA extends Indicator {
	private final int period;
	private final LinkedList<Double> balances;
	private double sum;

	public DepthBalanceStrengthSMA(final int period) {
		this.period = period;
		balances = new LinkedList<Double>();
		value = sum = 0.0;
		balances.clear();
	}

	@Override
	public double calculate() {
		double balance = Math.abs(marketBook.getSnapshot().getBalance());
		sum += balance;

		// In with the new
		balances.add(balance);

		// Out with the old
		while (balances.size() > period) {
			sum -= balances.removeFirst();
		}

		if (balances.size() > 0) {
			value = sum / balances.size();
		}
		return value;
	}

	@Override
	public void reset() {
		value = sum = 0.0;
		balances.clear();
	}
}
