package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

public class TrailIndicator extends Indicator {

	public TrailIndicator(final QuoteHistory qh) {
		super(qh);
		value = -1;
	}

	@Override
	public double calculate() {
		if (value == -1) {
			value = qh.getLastPriceBar().getClose();
		}

		return value;
	}

	public void setTrailValue(final double value) {
		this.value = value;
	}

}
