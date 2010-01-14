package org.lifeform.chart.indicator;

/**
 *
 */
public class ChartableIndicator {
	private final Indicator indicator;
	private final String name;
	private final int chart;

	public ChartableIndicator(final String name, final Indicator indicator, final int chart) {
		this.name = name;
		this.indicator = indicator;
		this.chart = chart;
	}

	public String getName() {
		return name;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public int getChart() {
		return chart;
	}

	public boolean isEmpty() {
		return indicator.getHistory().size() == 0;
	}

}
