/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

import java.text.DecimalFormat;

import org.lifeform.pricer.solver.Function;

/**
 * A utility class for numerical analysis. This class cannot be subclassed or
 * instantiated because all methods are static.
 * 
 * @author Wolfgang Christian
 * @version 1.0
 */
public final class Util {
	public static final double SQRT2PI = Math.sqrt(2 * Math.PI);
	public static final double LOG10 = Math.log(10);

	/** The default precision for numerical analysis. */
	public static final double defaultNumericalPrecision = Math
			.sqrt(Double.MIN_VALUE);

	/** Parser for simple arithmetic expressions. */
	private static SuryonoParser parser = new SuryonoParser(0); // parser
	// without
	// variables
	// standard output formats
	static DecimalFormat format2 = new DecimalFormat("#0.00");
	static DecimalFormat format3 = new DecimalFormat("#0.000");
	static DecimalFormat format4 = new DecimalFormat("#0.0000");
	static DecimalFormat format_E2 = new DecimalFormat("0.00E0");
	static DecimalFormat format_E3 = new DecimalFormat("0.000E0");
	static DecimalFormat format_E4 = new DecimalFormat("0.0000E0");

	/**
	 * Checks if an array is sorted. Returns: Positive integer if array is
	 * sorted in increasing value. Negative integer if array is sorted in
	 * decreasing value. Zero if array is not sorted.
	 * 
	 * @param array
	 *            double[]
	 * @return int 1,0,-1 based on sorting
	 */
	public static int checkSorting(final double[] array) {
		int sign = (array[0] <= array[array.length - 1]) ? 1 : -1;
		for (int i = 1, n = array.length; i < n; i++) {
			switch (sign) {
			case -1:
				if (array[i - 1] < array[i]) {
					return 0; // not sorted
				}
				break;
			case 1:
				if (array[i - 1] > array[i]) {
					return 0; // not sorted
				}
			}
		}
		return sign;
	}

	/**
	 * Computes the average value of a subset of an array.
	 * 
	 * @param array
	 *            the data to be averaged
	 * @param start
	 *            the index of the first point to be averaged
	 * @param num
	 *            the total number of points to be averaged
	 * @return
	 */
	public static double computeAverage(final double[] array, final int start, final int num) {
		double sum = 0;
		for (int i = start, stop = start + num; i < stop; i++) {
			sum += array[i];
		}
		return sum / num;
	}

	/**
	 * Creates a function having a constant value.
	 * 
	 * @param c
	 * @return
	 */
	public static Function constantFunction(final double c) {
		return new Function() {
			public double evaluate(final double x) {
				return c;
			}
		};
	}

	/**
	 * Evalautes a mathematical expression without variables.
	 * 
	 * @param str
	 *            String
	 * @return double
	 */
	public static synchronized double evalMath(final String str) {
		try {
			parser.parse(str);
			return parser.evaluate();
		} catch (Exception ex) {
		}
		return Double.NaN;
	}

	/**
	 * Convert a double to a string, printing two decimal places.
	 * 
	 * @param d
	 *            Input double
	 */
	static public String f2(final double d) {
		return format2.format(d);
	}

	/**
	 * Convert a double to a string, printing three decimal places.
	 * 
	 * @param d
	 *            Input double
	 */
	static public String f3(final double d) {
		return format3.format(d);
	}

	/**
	 * Convert a double to a string, printing four decimal places.
	 * 
	 * @param d
	 *            Input double
	 */
	static public String f4(final double d) {
		return format4.format(d);
	}

	/**
	 * Fills the given double[n] array with f(x) on the interval [start, stop].
	 * 
	 * @param f
	 *            Function
	 * @param start
	 *            double starting value of x
	 * @param stop
	 *            double stopping value of x
	 * @param data
	 *            double[]
	 * @return double[]
	 */
	static public double[] functionFill(final Function f, final double start, final double stop,
			final double[] data) {
		double dx = 1;
		int n = data.length;
		if (n > 1) {
			dx = (stop - start) / (n - 1);
		}
		double x = start;
		for (int i = 0; i < n; i++) {
			data[i] = f.evaluate(x);
			x += dx;
		}
		return data;
	}

	/**
	 * Fills the given double[2][n] array with x and f(x) values on interval
	 * [start, stop].
	 * 
	 * @param f
	 *            Function
	 * @param start
	 *            double
	 * @param stop
	 *            double
	 * @param data
	 *            double[][]
	 * @return double[][]
	 */
	static public double[][] functionFill(final Function f, final double start,
			final double stop, final double[][] data) {
		double dx = 1;
		int n = data[0].length;
		if (n > 1) {
			dx = (stop - start) / (n - 1);
		}
		double x = start;
		for (int i = 0; i < n; i++) {
			data[0][i] = x;
			data[1][i] = f.evaluate(x);
			x += dx;
		}
		return data;
	}

	/**
	 * Creates a Guassian (normal) distribution function.
	 * 
	 * The distribution is normalized. The full width at half maximum is
	 * 2*sigma*Math.sqrt(2 Math.log(2)) ~ 2.3548*sigma
	 * 
	 * @param x0
	 *            double center of the distribution
	 * @param sigma
	 *            double width of the distributuon
	 * @return Function
	 */
	public static Function gaussian(final double x0, final double sigma) {
		final double s2 = 2 * sigma * sigma;
		return new Function() {
			public double evaluate(final double x) {
				return Math.exp(-(x - x0) * (x - x0) / s2) / sigma / SQRT2PI;
			}
		};
	}

	/**
	 * Gets the approximate range of a function within the given domain.
	 * 
	 * The range is deterermiend by evaluating the function at n points and
	 * finding the minimum and maximum values.
	 * 
	 * @param f
	 *            Function
	 * @param a
	 *            double
	 * @param b
	 *            double
	 * @param n
	 *            int
	 * @return double[]
	 */
	public static double[] getRange(final Function f, final double a, final double b, final int n) {
		double min = f.evaluate(a);
		double max = f.evaluate(a);
		double x = a, dx = (b - a) / (n - 1);
		for (int i = 1; i < n; i++) {
			double y = f.evaluate(x);
			min = Math.min(min, y);
			max = Math.max(max, y);
			x += dx;
		}
		return new double[] { min, max };
	}

	/**
	 * Creates a linear function with the given slope and intercept.
	 * 
	 * @param m
	 *            double slope
	 * @param b
	 *            double intercept
	 * @return Function
	 */
	public static Function linearFunction(final double m, final double b) {
		return new Function() {
			public double evaluate(final double x) {
				return m * x + b;
			}
		};
	}

	/**
	 * Computes the relativePrecision except near zero where the absolute
	 * precision is returned.
	 * 
	 * @param epsilon
	 *            the absolute error
	 * @param result
	 *            the result
	 * 
	 * @return the relative error
	 */
	public static double relativePrecision(final double epsilon,
			final double result) {
		return (result > defaultNumericalPrecision) ? epsilon / result
				: epsilon;
	}

	private Util() {
	} // prohibit instantiation because all methods are static
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
