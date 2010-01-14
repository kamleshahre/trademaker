package org.lifeform.chart.indicator;

import java.util.List;

import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;

/**
 * Exponential Moving Average.
 */
public class EMA extends Indicator {
	private final int length;
	private final double multiplier;

	public EMA(final QuoteHistory qh, final int length) {
		super(qh);
		this.length = length;
		multiplier = 2. / (length + 1.);
	}

	@Override
	public double calculate() {
		List<PriceBar> priceBars = qh.getAll();
		int lastBar = priceBars.size() - 1;
		int firstBar = lastBar - 2 * length + 1;
		double ema = priceBars.get(firstBar).getClose();

		for (int bar = firstBar; bar <= lastBar; bar++) {
			double barClose = priceBars.get(bar).getClose();
			ema += (barClose - ema) * multiplier;
		}

		value = ema;

		return value;
	}
}
