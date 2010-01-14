/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

public class Factorials {
	static double[] cof = { 76.18009172947146, -86.50532032941677,
			24.01409824083091, -1.231739572450155, 0.1208650973866179e-2,
			-0.5395239384953e-5 };
	static long[] fac;

	static {
		long val = 1;
		int maxN = 1;
		while (val * maxN >= val) {
			val = val * maxN;
			maxN++;
		}
		fac = new long[maxN]; // maxN should be ~21
		fac[0] = 1;
		for (int i = 1; i < maxN; i++) {
			fac[i] = fac[i - 1] * i;
		}
	}

	/**
	 * Calculates the factorial.
	 * 
	 * @param n
	 *            int
	 * @return double
	 */
	public static double factorial(final int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"Negative value passed to factorial.");
		}
		if (n < fac.length) {
			return fac[n];
		}
		return Math.exp(gammaln(n + 1.0));
	}

	/**
	 * Calculates the logarithm of the Gamma function using the Lanczos
	 * approximation.
	 * 
	 * @param x
	 *            double
	 * @return double
	 */
	public static double gammaln(final double x) {
		double y = x, tmp = x + 5.5;
		tmp -= (x + 0.5) * Math.log(tmp);
		double sum = 1.000000000190015;
		for (int j = 0; j <= 5; j++) {
			sum += cof[j] / ++y;
		}
		return -tmp + Math.log(2.5066282746310005 * sum / x);
	}

	/**
	 * Returns the logarithm of the binomial coefficient (n, k) In other
	 * notation: log (n choose k) (n choose k) represents the number of ways of
	 * picking k unordered outcomes from n possibilities
	 * 
	 * @param n
	 * @param k
	 * @return log (n choose k)
	 */
	public static double logChoose(final int n, final int k) {
		return logFactorial(n) - logFactorial(k) - logFactorial(n - k);
	}

	/*
	 * --------------- test code --------------- public static void main(String
	 * args[]) { System.out.println(Factorials.factorial(3));
	 * System.out.println(Factorials.factorial(20));
	 * System.out.println(Factorials.factorial(21));
	 * System.out.println(Factorials.factorial(22)); }
	 */

	/**
	 * Returns log (n!) = log (n * (n-1) * ... 2 * 1)
	 * 
	 * @param n
	 * @return log(n!)
	 */
	public static double logFactorial(final int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"Negative value passed to logFactorial.");
		}
		return Factorials.gammaln(n + 1.0);
	}

	/**
	 * Returns the poisson distribution (nu^n e^(-nu) / n!)
	 * 
	 * @param nu
	 * @param n
	 * @return poisson_nu(n)
	 */
	public static double poisson(final double nu, final int n) {
		return Math.exp(n * Math.log(nu) - nu - logFactorial(n));
	}

	private Factorials() { // prohibit instantiation because all methods are
		// static
	}
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
