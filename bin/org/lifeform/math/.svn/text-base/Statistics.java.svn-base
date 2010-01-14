/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ernan Hughes - initial API and implementation
 *******************************************************************************/
package org.lifeform.math;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.lifeform.core.Pair;
import org.lifeform.util.ArrayUtils;

public class Statistics {

	public static double[] bollinger(final int n, final int deviations,
			final double[] vals, final int skipdays) {
		final double[] value = new double[3];

		final double centerband = SMA(n, vals, skipdays);

		final double t2 = deviation(n, vals, skipdays);

		final double upper = centerband + (deviations * t2);
		final double lower = centerband - (deviations * t2);

		value[2] = upper;
		value[1] = centerband;
		value[0] = lower;

		return value;
	}

	/**
	 * method to compute the compound interest.
	 * 
	 * @param cash
	 *            i.e. 5000
	 * @param unitsToComputeFor
	 *            i.e. 12 days
	 * @param unitsPerAnnum
	 *            i.e. 360 days
	 * @param interestRatePerAnnum
	 *            i.e. 0.04 for 4%.
	 * @return the interest result, according to ((
	 *         Math.pow((1.0+(interestRatePerAnnum/365.0)),daysToComputeFor) -
	 *         1) * cash; )
	 */
	public static double compoundInterest(final double cash,
			final double unitsToComputeFor, final double unitsPerAnnum,
			final double interestRatePerAnnum) {
		final double interest = (Math.pow(
				(1.0 + (interestRatePerAnnum / unitsPerAnnum)),
				unitsToComputeFor) - 1)
				* cash;
		return interest;
	}

	/**
	 * checks if the values 0 and 1 of two double series cross over each other.
	 * Example:<br>
	 * series1: 10,9,8,7 <br>
	 * series2: 9,10,11<br>
	 * This would yield yes. <br>
	 * 
	 * @param series1
	 * @param series2
	 * @return
	 */
	public static boolean cross(final double[] series1, final double[] series2) {
		if ((series1[0] < series2[0]) && (series1[1] > series2[1])) {
			return true;
		}
		if ((series1[0] > series2[0]) && (series1[1] < series2[1])) {
			return true;
		}
		return false;
	}

	public static double deviation(final int n, final double[] vals,
			final int skipdays) {
		final double centerband = SMA(n, vals, skipdays);

		double t1 = 0.0;

		for (int i = 0; i < n; i++) {
			t1 += ((vals[i + skipdays] - centerband) * (vals[i + skipdays] - centerband));
		}

		final double t2 = Math.sqrt(t1 / n);

		return t2;
	}

	/**
	 * returns the exponential moving average <br/>
	 * see http://www.quotelinks.com/technical/ema.html
	 * 
	 * @param n
	 * @param candles
	 * @param skipdays
	 * @return the exponential moving average
	 */
	public static double EMA(final int n, final double[] vals,
			final int skipdays) {
		double value = 0;

		final double exponent = 2 / (double) (n + 1);

		value = vals[vals.length - 1];

		for (int i = vals.length - 1; i > skipdays - 1; i--) {

			value = (vals[i] * exponent) + (value * (1 - exponent));

		}

		return value;
	}

	/**
	 * Calculates Pivot Points, contributed by Dan O'Rourke.
	 * 
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 * @param positionInTime
	 * @return
	 */
	public static double[] getPivotPoints(final double[] open,
			final double[] high, final double[] low, final double[] close,
			final int pos) {
		final double[] ret = new double[7];

		ret[3] = (high[pos] + low[pos] + close[pos]) / 3;
		// r1
		ret[2] = (ret[2] * 2) - low[pos];
		// r2
		ret[1] = (ret[2] + high[pos] - low[pos]);
		// r3
		ret[0] = (ret[1] + high[pos] - low[pos]);

		// s1
		ret[4] = (ret[3] * 2) - high[pos];
		// s2
		ret[5] = (ret[4] - high[pos] + low[pos]);
		// s3
		ret[6] = (ret[5] - high[pos] + low[pos]);

		return ret;
	}

	/**
	 * maps to the metastock hhvbars method.
	 * 
	 * @param n
	 * @param data
	 * @return
	 */
	public static int hhvbars(int n, double[] data) {
		int periodsSinceHigh = 0;
		double max = -10000000;
		if (n > data.length - 1) {
			n = data.length - 1;
		}
		for (int i = 0; i < n; i++) {
			if (data[i] > max) {
				periodsSinceHigh = i;
				max = data[i];
			}
		}
		return periodsSinceHigh;
	}

	/**
	 * checks if something is a hammer or not.
	 * 
	 * @param series
	 * @param filterPeriod
	 * @param multiplier
	 * @param position
	 * @return
	 */
	// public static boolean isHammer(
	// final org.activequant.core.domainmodel.data.CandleSeries series,
	// final int filterPeriod, final double multiplier, final int position) {
	// //
	//
	// //
	// final double xaverage = EMA(filterPeriod, series.getCloses(), position);
	//
	// if (series.get(position).getClosePrice() < xaverage) {
	//
	// final double open = series.get(position).getOpenPrice();
	// final double high = series.get(position).getHighPrice();
	// final double low = series.get(position).getLowPrice();
	// final double close = series.get(position).getClosePrice();
	//
	// final double bodyMin = minOf(close, open);
	// final double bodyMax = maxOf(close, open);
	// final double candleBody = bodyMax - bodyMin;
	// final double rangeMedian = (high + low) * 0.5;
	// final double upShadow = high - bodyMax;
	// final double downShadow = bodyMin - low;
	//
	// final boolean isHammer = (open != close) && (bodyMin > rangeMedian)
	// && (downShadow > (candleBody * multiplier))
	// && (upShadow < candleBody);
	//
	// return isHammer;
	//
	// } else {
	// return false;
	// }
	//
	// }

	/**
	 * checks if something is a hammer or not by another algorithm
	 * 
	 * @param series
	 * @param filterPeriod
	 * @param multiplier
	 * @param position
	 * @return
	 */
	// public static boolean isHammer2(
	// final org.activequant.core.domainmodel.data.CandleSeries series,
	// final int filterPeriod, final double multiplier, final int position) {
	// //
	// int ups = 0;
	// int downs = 0;
	// for (int i = position + 1; i < position + filterPeriod + 1; i++) {
	// if (series.get(i).isRising()) {
	// ups++;
	// } else {
	// downs++;
	// }
	// }
	// if (ups > downs) {
	// return false;
	// }
	//
	// if (series.get(position + 1).isRising()) {
	// return false;
	// }
	// if (series.get(position).getClosePrice() > series.get(position + 1)
	// .getClosePrice()) {
	// return false;
	// }
	//
	// final double open = series.get(position).getOpenPrice();
	// final double high = series.get(position).getHighPrice();
	// final double low = series.get(position).getLowPrice();
	// final double close = series.get(position).getClosePrice();
	//
	// final double bodyMin = minOf(close, open);
	// final double bodyMax = maxOf(close, open);
	// final double candleBody = bodyMax - bodyMin;
	// final double rangeMedian = (high + low) * 0.5;
	// final double upShadow = high - bodyMax;
	// final double downShadow = bodyMin - low;
	//
	// final boolean isHammer = (open != close) && (bodyMin > rangeMedian)
	// && (downShadow > (candleBody * multiplier))
	// && (upShadow < downShadow);
	//
	// return isHammer;
	//
	// }

	/**
	 * maps to the metastock llvbars method.
	 * 
	 * @param n
	 * @param data
	 * @return
	 */
	public static int llvbars(int n, double[] data) {
		int periodsSinceHigh = 0;
		double min = 1000000000;
		if (n > data.length - 1) {
			n = data.length - 1;
		}
		for (int i = 0; i < n; i++) {
			if (data[i] < min) {
				periodsSinceHigh = i;
				min = data[i];
			}
		}
		return periodsSinceHigh;
	}

	/**
	 * returns the logarithmic change value for double[0] and double[1].
	 * double[0] must be the more recent value.
	 * 
	 * @param in
	 * @return
	 * @throws NotEnoughDataException
	 */
	public static double logChange(final double... in) {
		assert in.length == 2 : "Too few inputs.";
		return Math.log(in[0] / in[1]);
	}

	public static double max(final double... values) {
		double max = Double.MIN_VALUE;
		for (double value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	public static double max(final int start, final int end,
			final double... values) {
		final double[] sublist = new double[end - start + 1];
		System.arraycopy(values, start, sublist, 0, sublist.length);
		return max(sublist);
	}

	public static double maxOf(final double v1, final double v2) {
		if (v1 >= v2) {
			return v1;
		} else {
			return v2;
		}
	}

	/**
	 * returns the max value out of three.
	 */
	public static double maxOf(final double v1, final double v2, final double v3) {
		if ((v1 >= v2) && (v1 >= v3)) {
			return v1;
		}
		if ((v2 >= v1) && (v2 >= v3)) {
			return v2;
		}
		if ((v3 >= v1) && (v3 >= v2)) {
			return v3;
		}
		return v1;
	}

	/**
	 * calculates the plain mean of an array of doubles
	 * 
	 * @param vals
	 * @return plain mean of an array of doubles
	 */
	public static double mean(final double... vals) {
		double v = 0;
		for (final double val : vals) {
			v += val;
		}
		v /= vals.length;
		return v;
	}

	/**
	 * calculates the MEMA
	 * 
	 * @param period
	 * @param candles
	 * @param skipdays
	 * @return the MEMA
	 */
	public static double MEMA(final int period, final double[] values,
			final int skipdays) {
		double mema = 0.0;
		double smoothing = 1;

		if (period != 0) {
			smoothing = 1 / (double) period;
		}

		int max = values.length;
		if (max > 600 + skipdays + 2 + period) {
			max = 500 + skipdays + 2 + period;
		} else {
			max = values.length - skipdays - 1 - period;
		}
		for (int i = max; i >= skipdays; i--) {
			final double value = values[i];
			if (i == max) {
				// ok, beginning of calculation
				mema = SMA(period, values, i);
			} else {
				mema = (smoothing * value) + ((1 - smoothing) * mema);
			}
		}
		return mema;
	}

	public static double min(final double... values) {
		double min = Double.MAX_VALUE;
		for (final double value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	public static double min(final int start, final int end,
			final double... values) {
		final double[] sublist = new double[end - start + 1];
		System.arraycopy(values, start, sublist, 0, sublist.length);
		return min(sublist);
	}

	/**
	 * returns the minimum value of two doubles.
	 * 
	 * @param v1
	 * @param v2
	 * @return minimum of a value of two doubles.
	 */
	public static double minOf(final double v1, final double v2) {
		if (v1 <= v2) {
			return v1;
		}
		if (v2 <= v1) {
			return v2;
		}
		return v1;
	}

	/**
	 * returns the minimum value of three doubles
	 * 
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return the minimum value of three doubles
	 */

	/**
	 * computes the average price from a non discrete signal using a simple
	 * integral. List must be in order t(0) is oldest and t(X) is newest.
	 * 
	 * @param values
	 * @return
	 */
	public static double nonDiscreteAverage(
			final List<Pair<Calendar, Double>> values) {

		Pair<Calendar, Double> current = values.get(0);
		double wholeArea = 0.0;
		for (final Pair<Calendar, Double> val : values) {
			final double baseArea = (val.getSecond())
					* (val.getFirst().getTimeInMillis() - current.getFirst()
							.getTimeInMillis());
			final double upperArea = ((current.getSecond() - val.getSecond()) * (val
					.getFirst().getTimeInMillis() - current.getFirst()
					.getTimeInMillis())) / 2.0;
			wholeArea = baseArea + upperArea;
			current = val;
		}

		return wholeArea;
	}

	/**
	 * returns a normalized copy of the input array
	 */
	public static double[] normalizeArray(final double... in) {
		final double min = min(in);
		final double max = max(in);
		final double[] ret = new double[in.length];

		for (int i = 0; i < in.length; i++) {
			ret[i] = (in[i] - min) / (max - min);
		}
		return ret;
	}

	/**
	 * returns the price range R as an array of doubles.
	 * 
	 * @return the price range
	 */
	public static double[] priceRange(final double[] opens,
			final double[] highs, final double[] lows, final double[] closes,
			final int skipdays) {
		final List<Double> results = new ArrayList<Double>();
		boolean first_run_range = true;
		int max = opens.length - 1;
		if (max > 200) {
			max = 200;
		}
		for (int i = max; i > skipdays - 1; i--) {
			double result = 0.0;
			if (first_run_range) {
				first_run_range = false;
				result = highs[i] - lows[i];
			} else {
				final double v1 = highs[i] - lows[i];
				final double v2 = highs[i] - closes[i + 1];
				final double v3 = closes[i + 1] - lows[i];

				if ((v1 >= v2) && (v1 >= v3)) {
					result = v1;
				} else if ((v2 >= v1) && (v2 >= v3)) {
					result = v2;
				} else if ((v3 >= v1) && (v3 >= v2)) {
					result = v3;
				}
			}
			results.add(0, result);
		}
		return ArrayUtils.unboxDoubles(results);
	}

	/**
	 * calculates the slope, relative to the price and scales it by 100.
	 * 
	 * @param n
	 * @param values
	 * @param skipdays
	 * @return
	 */
	public static double priceSlope(final int n, final double[] values,
			final int skipdays) {
		double value = 0.0;
		value = (values[skipdays] - values[n + skipdays]) / values[skipdays]
				* 100;
		return value;
	}

	/**
	 * returns the rate of change
	 * 
	 * @param n
	 * @param candles
	 * @param skipdays
	 * @return the rate of change
	 */
	public static double ROC(final int n, final double[] vals,
			final int skipdays) {
		double value = 0.0;
		final double v0 = vals[skipdays];
		final double v1 = vals[skipdays + n];

		value = (v0 - v1) / v0 * 100;

		return value;
	}

	/**
	 * returns the RSI
	 * 
	 * @param n
	 * @param values
	 * @param skipdays
	 * @return the RSI
	 */
	public static double RSI(final int n, final double[] vals,
			final int skipdays) {
		double U = 0.0;
		double D = 0.0;

		for (int i = 0; i < n; i++) {
			final double v0 = vals[skipdays + i];
			final double v1 = vals[skipdays + i + 1];

			final double change = v0 - v1;

			if (change > 0) {
				U += change;
			} else {
				D += Math.abs(change);
			}
		}

		// catch division by zero
		if ((D == 0) || ((1 + (U / D)) == 0)) {
			assert false : "Division by zero";
			return 0.0;
		}

		return 100 - (100 / (1 + (U / D)));
	}

	/**
	 * returns the parabolic SAR - double check with a reference implementation
	 * !
	 * 
	 * @param initialValue
	 *            TODO
	 * @param candles
	 * @param skipdays
	 * @param n
	 * 
	 * @return the parabolic SAR
	 */
	public static double SAR(final double af, final double max,
			final double[] lows, final double[] highs, final int skipdays) {

		final List<Double> l1 = new ArrayList<Double>();
		final List<Double> l2 = new ArrayList<Double>();
		for (final Double d : lows) {
			l1.add(d);
		}
		for (final Double d : highs) {
			l2.add(d);
		}

		Collections.reverse(l1);
		Collections.reverse(l2);

		// need to reverse from activequant norm for ta lib.
		final double[] lowsReversed = new double[lows.length - skipdays];
		final double[] highsReversed = new double[highs.length - skipdays];

		for (int i = 0; i < lowsReversed.length; i++) {
			lowsReversed[i] = l1.get(i);
			highsReversed[i] = l2.get(i);
		}

		Integer outBegIdx = new Integer(0);
		final double[] outArray = new double[highsReversed.length];
		final double value = outArray[outArray.length - 1 - outBegIdx];
		return value;
	}

	/**
	 * this function does scale the input parameters into the values -1...1. Can
	 * be useful for various aspects.
	 * 
	 * @param in
	 *            the input values
	 * @return the input values in the range -1 .. 1
	 */
	public static double[] scale(final double... in) {
		final double[] ret = new double[in.length];

		for (int i = 0; i < in.length; i++) {
			ret[i] = -1 + 2 * in[i];
		}
		return ret;
	}

	/**
	 * calculates the sharpe Ratio, you need to pipe in returns in PERCENT!!!!
	 * this sharpe ratio calculation calculates based on a periodically series
	 * of returns and the std deviation of these returns Example of use: create
	 * an array of weekly returns, i.e. {0.1, 0.2, 0.01, 0.04} know the weekly
	 * interest rate, for example 0.04/52
	 * 
	 * @param returns
	 *            - this double[] must contain the return in percents for a
	 *            given period (i.e. 0.1)
	 * @param interest
	 *            - the interest rate in percent for this period (i.e. 0.035)
	 * @return the sharpe ratio
	 */
	public static double sharpeRatio(final double interest,
			final double... returns) {
		if (returns.length > 1) {
			double ret = 0.0, avg = 0.0, stddev = 0.0;
			avg = mean(returns);
			stddev = deviation(returns.length, returns, 0);
			ret = (avg - interest) / stddev;
			return ret;
		} else if (returns.length == 1) {
			return returns[0] - interest;
		} else {
			return 0;
		}
	}

	/**
	 * returns the slope between two timepoints
	 * 
	 * @param n
	 * @param candles
	 * @param skipdays
	 * @return the slope
	 */
	public static double slope(final int n, final double[] values,
			final int skipdays) {
		double value = 0.0;
		value = (values[skipdays] - values[n + skipdays]) / n;
		return value;
	}

	public static double SMA(final int period, final double[] vals,
			final int skipdays) {

		double value = 0.0;
		// debugPrint("SMA("+period+") for "+candles.size()+ " skipd:
		// "+skipdays);

		for (int i = skipdays; i < (period + skipdays); i++) {
			value += vals[i];
		}

		value /= period;

		return value;
	}

	/**
	 * returns a SMA smoothed slope
	 * 
	 * @param n
	 * @param smoothingfactor
	 * @param candles
	 * @param skipdays
	 * @return smoother slope
	 */
	public static double smoothedSlope(final int n, final int smoothingunits,
			final double[] vals, final int skipdays) {
		double value = 0.0;
		final double[] values = new double[smoothingunits];
		for (int i = 0; i < (smoothingunits); i++) {
			values[i] = slope(n, vals, skipdays + i);
		}
		value = SMA(smoothingunits, values, 0);
		return value;
	}

	public static double standardDeviation(final double... values) {
		return standardDeviation(values);
	}

	public static double standardGradient(final double... values) {
		double sum = 0.0;
		for (int i = 0; i < values.length - 1; i++) {
			final double difference = values[i + 1] - values[i];
			sum += Math.abs(difference);
		}
		return sum / (values.length - 1);
	}

	public static double standardGradientDeviation(final double... values) {
		final double standardGradient = standardGradient(values);
		final double[] newValues = new double[values.length - 1];
		for (int i = 0; i < newValues.length; i++) {
			final double difference = Math.abs(values[i + 1] - values[i]);
			newValues[i] = Math.pow((difference - standardGradient), 2);
		}
		return Math.sqrt(mean(newValues));
	}

	/**
	 * returns the units / bars since the last change in the array.<br>
	 * Example:<br>
	 * series data: 10,10,10,10,3,4,5,4,4,4,4<br>
	 * would return 4<br>
	 * This function tries to work like the metastock barsSince(..) function.<br>
	 * 
	 * @param array
	 * @return
	 */
	public static int unitsSinceChange(final double... array) {
		int i = 0;
		for (i = 0; i < array.length - 1; i++) {
			if (array[i] != array[i + 1]) {
				return i;
			}
		}
		return i;
	}

	/**
	 * 
	 * This methods returns the the value of the array data at the n'th
	 * occurance of a true in the boolean array.<br>
	 * Example:<br>
	 * occurance : 2<br>
	 * boolean array: 0,1,0,0,0,1<br>
	 * data: 2,1,3,4,1,2<br>
	 * would return 2.
	 * 
	 * @param occurance
	 * @param booleanArray
	 * @param data
	 * @return the last (oldest) element in data if the criterias are never
	 *         matched, otherwise see above.
	 */
	public static double valueWhen(final int occurance,
			final boolean[] booleanArray, final double[] data) {
		int n = 0;
		for (int i = 0; i < booleanArray.length; i++) {
			if (booleanArray[i]) {
				n++;
				if (n == occurance) {
					return data[i];
				}
			}
		}
		return data[data.length - 1];
	}

	/**
	 * calculates the volatility Index, returns the trend following system
	 * working with SAR points. indicator requires at least 100 candles !
	 * 
	 * @param p1
	 *            the factor
	 * @param p2
	 *            the periods to work on.
	 * @param candles
	 *            this are the input candles.
	 * @param skipdays
	 *            this parameter specifies how many days to skip.
	 * @return the volatility Index
	 */
	public static double volatilityIndex(final int p1, final int p2,
			final double[] opens, final double[] highs, final double[] lows,
			final double[] closes, final int skipdays) {

		boolean first_run_vlx = true;
		boolean position_long = true;
		double sip = 0, sar = 0, next_sar = 0, smoothed_range = 0;

		int max = closes.length - skipdays - 2;
		if (max > 200) {
			max = 200;
		}

		for (int i = max; i > skipdays - 1; i--) {

			final double value = closes[i];
			smoothed_range = MEMA(p2,
					priceRange(opens, highs, lows, closes, i), 0);
			final double atr = smoothed_range * p1;

			if (first_run_vlx && (smoothed_range != 0)) {
				first_run_vlx = false;
				sip = max(i, i + p2, highs);
				next_sar = sip - atr;
				sar = next_sar;
			} else {
				sar = next_sar;
				if (position_long) {
					if (value < sar) {
						position_long = false;
						sip = value;
						next_sar = sip + (smoothed_range * p1);
					} else {
						position_long = true;
						sip = (value > sip) ? value : sip;
						next_sar = sip - (smoothed_range * p1);
					}
				} else {
					if (value > sar) {
						position_long = true;
						sip = value;
						next_sar = sip - (smoothed_range * p1);
					} else {
						position_long = false;
						sip = (value < sip) ? value : sip;
						next_sar = sip + (smoothed_range * p1);
					}
				}
			}
		}
		return sar;
	}

	public static double volatilityIndex(final int p1, final int p2,
			final double[][] ohlc, final int skipdays) {
		return volatilityIndex(p1, p2, ohlc[0], ohlc[1], ohlc[2], ohlc[3],
				skipdays);
	}

	/**
	 * returns the linearly weighted moving average.
	 * 
	 * @param period
	 * @param candles
	 * @param skipdays
	 * @return the wma
	 */
	public static double WMA(final int period, final double[] vals,
			final int skipdays) {

		double numerator = 0.0;

		int weight = period;
		for (int i = skipdays; i < (period + skipdays); i++) {
			numerator += vals[i] * weight;
			weight--;
		}

		final int denominator = period * (period + 1) / 2;
		final double value = numerator / denominator;

		return value;
	}

	/**
	 * returns the normalized yield between in1[0] and in2[lag].
	 * 
	 * @param in1
	 * @param in2
	 * @return double yield
	 * @throws NotEnoughDataException
	 */
	public static double[] yield(final double[] in2, final int lag,
			final double... in1) {
		final double[] ret = new double[in1.length];
		assert (in1.length < lag) && (in1.length != in2.length) : "Too few inputs.";
		for (int i = 0; i < in1.length - lag; i++) {
			ret[i] = Math.log(in1[i] / in2[i + lag]) * 100;
		}
		return ret;
	}

	/**
	 * returns the yield with a given lag.
	 * 
	 * @param in
	 * @return double
	 * @throws NotEnoughDataException
	 */
	public static double[] yield(final int lag, final double... in) {
		final double[] ret = new double[in.length];
		assert (in.length < lag) : "Too few inputs.";
		for (int i = 0; i < in.length - lag; i++) {
			ret[i] = Math.log(in[i] / in[i + lag]) * 100;
		}
		return ret;
	}

	private Statistics() {

	}

}
