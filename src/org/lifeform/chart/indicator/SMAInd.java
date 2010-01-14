package org.lifeform.chart.indicator;

import java.util.List;

public class SMAInd extends Indicator {
	private final int length;

	public SMAInd(final Indicator parent, final int length) {
		super(parent);
		this.length = length;
	}

	@Override
	public double calculate() {
		List<IndicatorValue> indicatorHistory = parent.getHistory();
		int endBar = indicatorHistory.size() - 1;
		int startBar = endBar - length + 1;
		double sma = 0;

		for (int bar = startBar; bar <= endBar; bar++) {
			sma += indicatorHistory.get(bar).getValue();
		}

		value = sma / length;
		return value;

	}
}
