package org.lifeform.chart.indicator;

import java.util.List;

import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;

/**
 * Average True Range: http://en.wikipedia.org/wiki/Average_true_range
 * 
 * NOTE: following the convention of EMA.java, we will set the initial value to
 * the close.
 * 
 * true_range = max( high, close_prev ) - min( low, close_prev ) ATR = EMA(
 * true_range )
 * 
 * the length parameter refers to the EMA multiplier
 * 
 * Due to close_prev in the equation, we can only start from the second tick.
 * 
 * ---------------------------------------------------------------------
 * 
 * @author Ken Feng
 * @version 1.0
 * @date 08/02/2009
 */

public class ATR extends Indicator {
	private final int length;
	private final double multiplier;

	public ATR(final QuoteHistory qh, final int length) {
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
			double trueRange = priceBars.get(bar).getClose();
			ema += (trueRange - ema) * multiplier;
		}
		value = ema;
		return value;
	}
}
