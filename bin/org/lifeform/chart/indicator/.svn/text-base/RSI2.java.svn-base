package org.lifeform.chart.indicator;

import java.util.Stack;

import org.lifeform.market.QuoteHistory;

/**
 * Relative Strength Index. Implemented up to this specification:
 * 
 * The RSI calculation appears in its original and derived form. Average Up and
 * Average Down are calculated using a simple average method for the initial
 * observation. NOTE: The initial observation is the first date shown on the
 * scrolling graph, which may or may not be seen. Subsequent values are computed
 * using these initial values in conjunction with a damping factor to smooth out
 * extreme points. The RSI equation and two averaging methods are presented
 * below. RSI = 100 - [ 100/(1 + [Avg Up/Avg Dn])] where Avg Up: Sum of all
 * changes for advancing periods divided by the total number of RSI periods.
 * 
 * Avg Dn: Sum of all changes for declining periods divided by the total number
 * of RSI periods.
 * 
 * Subsequent RSI calculations are based on up and down sums calculated as
 * follows:
 * 
 * RSI = 100 - [100/(1 + [Next Avg Up/Next Avg Dn])]
 * 
 * Next Avg Up = [([Previous Avg Up * (RSI periods - 1)]) + today's up
 * close]/(RSI periods) Next Avg Dn = [([Previous Avg Dn * (RSI periods - 1)]) +
 * today's dn close]/(RSI periods) NOTE: If there is no up or down close,
 * today's up/dn close is zero.
 */

public class RSI2 extends Indicator {
	private final int periodLength;
	private final Stack<Averages> avgList;

	public RSI2(final QuoteHistory qh, final int periodLength) {
		super(qh);
		this.periodLength = periodLength;
		avgList = new Stack<Averages>();
	}

	public QuoteHistory getQuoteHistory() {
		return qh;
	}

	private class Averages {

		private final double avgUp;
		private final double avgDown;

		public Averages(final double up, final double down) {
			this.avgDown = down;
			this.avgUp = up;
		}

		public double getAvgUp() {
			return avgUp;
		}

		public double getAvgDown() {
			return avgDown;
		}
	}

	@Override
	public double calculate() {
		int qhSize = qh.size();
		int lastBar = qhSize - 1;
		int firstBar = lastBar - periodLength + 1;

		double gains = 0, losses = 0, avgUp = 0, avgDown = 0;

		double delta = qh.getPriceBar(lastBar).getClose()
				- qh.getPriceBar(lastBar - 1).getClose();
		gains = Math.max(0, delta);
		losses = Math.max(0, -delta);

		if (avgList.isEmpty()) {
			for (int bar = firstBar + 1; bar <= lastBar; bar++) {
				double change = qh.getPriceBar(bar).getClose()
						- qh.getPriceBar(bar - 1).getClose();
				gains += Math.max(0, change);
				losses += Math.max(0, -change);
			}
			avgUp = gains / periodLength;
			avgDown = losses / periodLength;
			avgList.push(new Averages(avgUp, avgDown));

		} else {

			Averages avg = avgList.pop();
			avgUp = avg.getAvgUp();
			avgDown = avg.getAvgDown();
			avgUp = ((avgUp * (periodLength - 1)) + gains) / (periodLength);
			avgDown = ((avgDown * (periodLength - 1)) + losses)
					/ (periodLength);
			avgList.add(new Averages(avgUp, avgDown));
		}
		value = 100 - (100 / (1 + (avgUp / avgDown)));

		return value;
	}

}