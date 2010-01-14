package org.lifeform.chart.indicator.depth;

import java.util.LinkedList;

import org.lifeform.chart.indicator.Indicator;

public class DepthBalanceSMA extends Indicator {

	private final int period;

	private final LinkedList<Double> history = new LinkedList<Double>();
	private double balance, sum;

	public DepthBalanceSMA(final int period) {
		this.period = period;
		value = sum = 0.0;
		history.clear();
	}

	@Override
	public void reset() {
		value = sum = 0.0;
		history.clear();
	}

	@Override
	public double calculate() {

		balance = marketBook.getSnapshot().getBalance();
		history.addLast(balance);
		sum += balance;

		while (history.size() > period) {
			sum -= history.removeFirst();
		}

		if (history.size() > 0) {
			value = sum / history.size();
		}
		return value;
	}

}
