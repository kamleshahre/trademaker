package org.lifeform.chart.indicator;

import java.util.List;

public class LSValueInd extends Indicator {
	private final int length;

	public LSValueInd(final Indicator parent, final int length) {
		super(parent);
		this.length = length;
	}

	@Override
	public double calculate() {
		List<IndicatorValue> indicatorHistory = parent.getHistory();
		int lastBar = indicatorHistory.size() - 1;
		int firstBar = lastBar - length + 1;

		double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
		for (int bar = firstBar; bar <= lastBar; bar++) {
			double y = indicatorHistory.get(bar).getValue();
			sumX += bar;
			sumY += y;
			sumXY += (bar * y);
			sumXX += (bar * bar);
		}

		double slope = length * sumXY - sumX * sumY;
		slope /= (length * sumXX - sumX * sumX);
		double intercept = (sumY - slope * sumX) / length;
		value = slope * lastBar + intercept;
		return value;

	}
}
