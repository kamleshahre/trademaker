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
package org.lifeform.credit;

public class NormalApprox {
	private static double f(final double x, final double y, final double aprime, final double bprime,
			final double rho) {
		double r = aprime * (2 * x - aprime) + bprime * (2 * y - bprime) + 2
				* rho * (x - aprime) * (y - bprime);
		return Math.exp(r);
	}

	public static double Fi_Inverse(final double theP) {

		// Define coefficients in rational approximations
		final double a1 = -39.6968302866538;
		final double a2 = 220.946098424521;
		final double a3 = -275.928510446969;
		final double a4 = 138.357751867269;
		final double a5 = -30.6647980661472;
		final double a6 = 2.50662827745924;

		final double b1 = -54.4760987982241;
		final double b2 = 161.585836858041;
		final double b3 = -155.698979859887;
		final double b4 = 66.8013118877197;
		final double b5 = -13.2806815528857;

		final double c1 = -7.78489400243029E-03;
		final double c2 = -0.322396458041136;
		final double c3 = -2.40075827716184;
		final double c4 = -2.54973253934373;
		final double c5 = 4.37466414146497;
		final double c6 = 2.93816398269878;

		final double d1 = 7.78469570904146E-03;
		final double d2 = 0.32246712907004;
		final double d3 = 2.445134137143;
		final double d4 = 3.75440866190742;

		// Define break-points
		final double p_low = 0.02425;
		final double p_high = 1.0 - p_low;

		// Define work variables
		double q;
		double r;

		// If argument out of bounds, raise error
		if (theP <= 0 || theP >= 1) {
			return -555.0;
		}

		if (theP < p_low) {
			// Rational approximation for lower region
			q = Math.sqrt(-2 * Math.log(theP));
			return (((((c1 * q + c2) * q + c3) * q + c4) * q + c5) * q + c6)
					/ ((((d1 * q + d2) * q + d3) * q + d4) * q + 1.0);
		} else if (theP <= p_high) {
			// Rational approximation for middle region
			q = theP - 0.5;
			r = q * q;
			return (((((a1 * r + a2) * r + a3) * r + a4) * r + a5) * r + a6)
					* q
					/ (((((b1 * r + b2) * r + b3) * r + b4) * r + b5) * r + 1);
		} else if (theP < 1.0) {
			// Rational approximation for upper region
			q = Math.sqrt(-2.0 * Math.log(1.0 - theP));
			return -(((((c1 * q + c2) * q + c3) * q + c4) * q + c5) * q + c6)
					/ ((((d1 * q + d2) * q + d3) * q + d4) * q + 1);
		}
		return -666.6; // should never be here;
	}

	public static double norm2Dens(final double r, final double mu, final double sigma) {
		double nv = 1.0 / (Math.sqrt(2.0 * Math.PI) * sigma);
		double z = (r - mu) / sigma;
		nv *= Math.exp(-0.5 * z * z);
		return nv;
	}

	// Numerical approximation to the bivariate normal distribution,
	// as described e.g. in Hulls book

	public static double Normal1_CDF(final double z) {
		double b1 = 0.31938153;
		double b2 = -0.356563782;
		double b3 = 1.781477937;
		double b4 = -1.821255978;
		double b5 = 1.330274429;
		double p = 0.2316419;
		double c2 = 0.3989423;

		if (z > 6.0) {
			return 1.0;
		}
		; // this guards against overflow
		if (z < -6.0) {
			return 0.0;
		}
		;
		double a = Math.abs(z); // fabs??
		double t = 1.0 / (1.0 + a * p);
		double b = c2 * Math.exp((-z) * (z / 2.0));
		double n = ((((b5 * t + b4) * t + b3) * t + b2) * t + b1) * t;
		n = 1.0 - b * n;
		if (z < 0.0)
			n = 1.0 - n;
		return n;
	}

	public static double Normal2_CDF(final double a, final double b, final double rho) {
		if ((a <= 0.0) && (b <= 0.0) && (rho <= 0.0)) {
			double aprime = a / Math.sqrt(2.0 * (1.0 - rho * rho));
			double bprime = b / Math.sqrt(2.0 * (1.0 - rho * rho));
			double[] A = { 0.3253030, 0.4211071, 0.1334425, 0.006374323 };
			double[] B = { 0.1337764, 0.6243247, 1.3425378, 2.2626645 };
			double sum = 0;
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					sum += A[i] * A[j] * f(B[i], B[j], aprime, bprime, rho);
				}
				;
			}
			;
			sum = sum * (Math.sqrt(1.0 - rho * rho) / Math.PI);
			return sum;
		} else if (a * b * rho <= 0.0) {
			if ((a <= 0.0) && (b >= 0.0) && (rho >= 0.0)) {
				return Normal1_CDF(a) - Normal2_CDF(a, -b, -rho);
			} else if ((a >= 0.0) && (b <= 0.0) && (rho >= 0.0)) {
				return Normal1_CDF(b) - Normal2_CDF(-a, b, -rho);
			} else if ((a >= 0.0) && (b >= 0.0) && (rho <= 0.0)) {
				return Normal1_CDF(a) + Normal1_CDF(b) - 1.0
						+ Normal2_CDF(-a, -b, rho);
			}
			;
		} else if (a * b * rho >= 0.0) {
			double denum = Math.sqrt(a * a - 2 * rho * a * b + b * b);
			double rho1 = ((rho * a - b) * sgn(a)) / denum;
			double rho2 = ((rho * b - a) * sgn(b)) / denum;
			double delta = (1.0 - sgn(a) * sgn(b)) / 4.0;
			return Normal2_CDF(a, 0.0, rho1) + Normal2_CDF(b, 0.0, rho2)
					- delta;
		}
		;
		return -99.9; // should never get here
	}

	public static double normDens(final double z) {
		return (1.0 / Math.sqrt(2.0 * Math.PI)) * Math.exp(-0.5 * z * z);
	}

	private static double sgn(final double x) { // sign function
		if (x >= 0.0)
			return 1.0;
		return -1.0;
	}

	public static double VasicekF(final double theRelLoss, final double theP, final double theRho) {
		return Normal1_CDF((Math.sqrt(1.0 - theRho) * Fi_Inverse(theRelLoss) - Fi_Inverse(theP))
				/ (Math.sqrt(theRho)));
	}
}
