package org.lifeform.chart.indicator;

import org.lifeform.market.QuoteHistory;

/**
 * Average Directional Index. Based on
 * http://technical.traders.com/tradersonline/display.asp?art=278
 * 
 * @author Carlos Aza Villarrubia
 * @version 1.1
 * @date 25/05/2008
 */
public class ADX extends Indicator {
	private final int periodLength;
	private double[] tr = null;
	private double[] dmPlus = null;
	private double[] dmMinus = null;
	private double[] trN = null;
	private double[] dmPlusN = null;
	private double[] dmMinusN = null;
	private double[] dx = null;
	private double[] adx = null;
	private int counter = 0;

	public ADX(final QuoteHistory qh, final int periodLength) {
		super(qh);
		this.periodLength = periodLength;
		this.tr = new double[periodLength];
		this.dmPlus = new double[periodLength];
		this.dmMinus = new double[periodLength];
		this.trN = new double[periodLength];
		this.dmPlusN = new double[periodLength];
		this.dmMinusN = new double[periodLength];
		this.dx = new double[periodLength];
		this.adx = new double[periodLength];
	}

	@Override
	public double calculate() {

		// int periodStart = qh.size() - periodLength;
		int periodEnd = qh.size() - 1;
		double high = qh.getLastPriceBar().getHigh();
		double low = qh.getLastPriceBar().getLow();
		// double close = qh.getLastPriceBar().getClose();
		double high_1 = qh.getPriceBar(periodEnd - 1).getHigh();
		double low_1 = qh.getPriceBar(periodEnd - 1).getLow();
		double close_1 = qh.getPriceBar(periodEnd - 1).getClose();

		for (int i = 0; i < periodLength - 1; i++) {
			tr[i] = tr[i + 1];
			dmPlus[i] = dmPlus[i + 1];
			dmMinus[i] = dmMinus[i + 1];
			trN[i] = trN[i + 1];
			dmPlusN[i] = dmPlusN[i + 1];
			dmMinusN[i] = dmMinusN[i + 1];
			dx[i] = dx[i + 1];
			adx[i] = adx[i + 1];
		}

		// the first calculation for ADX is the true range value (TR)
		tr[periodLength - 1] = Math.max(high - low, Math.max(Math.abs(high
				- close_1), Math.abs(low - close_1)));

		// determines the positive directional movement or returns zero if there
		// is no positive directional movement.
		dmPlus[periodLength - 1] = high - high_1 > low_1 - low ? Math.max(high
				- high_1, 0) : 0;

		// calculates the negative directional movement or returns zero if there
		// is no negative directional movement.
		dmMinus[periodLength - 1] = low_1 - low > high - high_1 ? Math.max(
				low_1 - low, 0) : 0;

		// The daily calculations are volatile and so the data needs to be
		// smoothed. First, sum the last N periods for TR, +DM and - DM
		double trSum = 0;
		double dmPlusSum = 0;
		double dmMinusSum = 0;
		for (int i = 0; i < periodLength; i++) {
			trSum += tr[i];
			dmPlusSum += dmPlus[i];
			dmMinusSum += dmMinus[i];
		}

		// The smoothing formula subtracts 1/Nth of yesterday's trN from
		// yesterday's trN and then adds today's TR value
		// The truncating function is used to calculate the indicator as close
		// as possible to the developer of the ADX's original form of
		// calculation (which was done by hand).
		trN[periodLength - 1] = ((int) (1000 * (trN[periodLength - 2]
				- (trN[periodLength - 2] / periodLength) + trSum))) / 1000;
		dmPlusN[periodLength - 1] = ((int) (1000 * (dmPlusN[periodLength - 2]
				- (dmPlusN[periodLength - 2] / periodLength) + dmPlusSum))) / 1000;
		dmMinusN[periodLength - 1] = ((int) (1000 * (dmMinusN[periodLength - 2]
				- (dmMinusN[periodLength - 2] / periodLength) + dmMinusSum))) / 1000;

		// Now we have a 14-day smoothed sum of TR, +DM and -DM.
		// The next step is to calculate the ratios of +DM and -DM to TR.
		// The ratios are called the +directional indicator (+DI) and
		// -directional indicator (-DI).
		// The integer function (int) is used because the original developer
		// dropped the values after the decimal in the original work on the ADX
		// indicator.
		double diPlus = (int) (100 * dmPlusN[periodLength - 1] / trN[periodLength - 1]);
		double diMinus = (int) (100 * dmMinusN[periodLength - 1] / trN[periodLength - 1]);
		;

		// The next step is to calculate the absolute value of the difference
		// between the +DI and the -DI and the sum of the +DI and -DI.
		double diDiff = Math.abs(diPlus - diMinus);
		double diSum = diPlus + diMinus;

		// The next step is to calculate the DX, which is the ratio of the
		// absolute value of the difference between the +DI and the -DI divided
		// by the sum of the +DI and the -DI.
		dx[periodLength - 1] = (int) (100 * (diDiff / diSum));

		// The final step is smoothing the DX to arrive at the value of the ADX.
		// First, average the last N days of DX values
		double dxMedia = 0;
		for (int i = 0; i < periodLength; i++) {
			dxMedia += dx[i];
		}
		dxMedia /= periodLength;

		// The smoothing process uses yesterday's ADX value multiplied by N-1,
		// and then add today's DX value. Finally, divide this sum by N.
		if (counter == 2 * (periodLength - 1)) {
			adx[periodLength - 2] = dxMedia;
		}
		adx[periodLength - 1] = (adx[periodLength - 2] * (periodLength - 1) + dx[periodLength - 1])
				/ periodLength;

		counter++;

		value = adx[periodLength - 1];
		return value;
	}
}
