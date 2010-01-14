package org.lifeform.chart.indicator;

import java.util.List;

import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;

public class EMAnAlpha extends Indicator {
	private final int length;
	private final double multiplier/* , alpha */;

	public EMAnAlpha(final QuoteHistory qh, final int length, final double alpha) {
		super(qh);
		this.length = length;
		// this.alpha = alpha;
		multiplier = alpha / (length + 1.);
	}

	@Override
	public double calculate() {
		List<PriceBar> priceBars = qh.getAll();
		int lastBar = priceBars.size() - 1;
		int firstBar = lastBar - length + 1;
		double ema = priceBars.get(firstBar).getClose();
		for (int bar = firstBar; bar <= lastBar; bar++) {
			double barClose = priceBars.get(bar).getClose();
			ema += (barClose - ema) * multiplier;
		}
		value = ema;
		return value;
	}
}
