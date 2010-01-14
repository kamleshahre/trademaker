package org.lifeform.chart.indicator.depth;

import org.lifeform.chart.indicator.Indicator;

/**
 * Depth balance in the latest market snapshot
 */
public class DepthBalance extends Indicator {

	public double calculate() {
		value = marketBook.getSnapshot().getBalance();
		return value;
	}

	public void reset() {
		calculate();
	}
}
