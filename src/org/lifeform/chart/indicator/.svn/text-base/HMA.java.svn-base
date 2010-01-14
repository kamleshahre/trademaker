package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Hull Moving Average. as defined at http://www.alanhull.com.au/hma/hma.html
 */
public class HMA extends Indicator {
	private final int length;

	/*
	 * Public constructor of the indicator. Length should be at least 1 for
	 * meaningful results.
	 */
	public HMA(final QuoteHistory qh, final int length) {
		super(qh);
		this.length = length;

	}

	/*
	 * private utility to compute the weighted moving average of an array of
	 * doubles, of given length, ending at the given position of the array
	 */
	private double WMA(final double[] values, final int end, int len) {
		if (len <= 1) // special cases, return the element at end
			return values[end];

		int start = end - len + 1;
		if (start < 0) { // update start pos if there are not enough elements
			start = 0;
			len = end + 1;
		}

		double wma = 0;
		double count = 1;

		for (int bar = start; bar <= end; bar++) {
			wma += values[bar] * (count++);
		}

		return wma / (len * (len + 1) / 2);
	}

	@Override
	public double calculate() {
		int size = qh.size();
		if (size == 0)
			return 0;

		// in the beginning we don't have enough data, so use only what's
		// already there in the quote history
		int len = length < size ? length : size;

		int sqrlen = (int) Math.sqrt(len);
		if (sqrlen < 1)
			sqrlen = 1;

		// we need len + sqrlen number of data points for the rest of the
		// algorithm to work
		int arraysize = len + sqrlen;
		if (arraysize > size) // or less if we don't have that much yet
			arraysize = size;

		// copy the needed number of price data to an array of doubles
		// to speed up computation
		double[] values = new double[arraysize];
		for (int i = 0; i < values.length; i++) {
			values[i] = qh.getPriceBar(size - arraysize + i).getClose();
		}

		double[] wmavalues = new double[sqrlen];

		// calculate 2*WMA(period/2) - WMA(period) for sequences ending
		// at now, now-1, now-2, ..., now-sqrlen
		for (int i = 0; i < sqrlen; i++) {
			double dfull = WMA(values, arraysize - 1 - i, len); // full WMA
			double dhalf = WMA(values, arraysize - 1 - i, len / 2); // half len
			// store the WMA diff values in a temp array
			wmavalues[sqrlen - 1 - i] = 2 * dhalf - dfull;
		}

		// do another WMA on the values calculated above for smoothing
		value = WMA(wmavalues, sqrlen - 1, sqrlen);
		return value;
	}
}