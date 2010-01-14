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
package org.lifeform.market.vol;

public class Calc {

	public static double annualizedHistoricalVolatility(final double[] rates) {
		return annualizedHistoricalVolatility(rates, 252);
	}

	public static double annualizedHistoricalVolatility(final double[] rates,
			final double tradingDaysInYear) {
		return historicalVolatility(rates) * Math.sqrt(tradingDaysInYear);
	}

	public static double average(final double[] rates) {
		return sum(rates) / rates.length;
	}

	/**
	 * To calculate historical volatility measure the day to day price changes
	 * in a parket
	 * 
	 * @param start
	 *            start price
	 * @param end
	 *            end price
	 * 
	 * @return LN(end/start)
	 */
	public static double dailyChange(final double start, final double end) {
		return Math.log(end / start);
	}

	/**
	 * Historical Volatility is the average variance form the mean
	 * 
	 * @param rates
	 *            daily rates we observe
	 * 
	 * @return standard deviation of input.
	 */
	public static double historicalVolatility(final double[] rates) {
		double average = average(rates);
		double count = rates.length;
		double result = 0.0d;
		for (double d : rates) {
			result += Math.pow(d - average, 2);
		}
		return Math.sqrt(result / (count - 1));
	}

	public static double sum(final double[] input) {
		double result = 0.0d;
		for (double i : input) {
			result += i;
		}
		return result;
	}

}
