/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

import org.lifeform.pricer.solver.Function;

/**
 * LagrangeInterpolator uses a polynomial interpolation formula to evaluate
 * values between data points.
 * 
 * @author W. Christian
 * @version 1.0
 */
public class LagrangeInterpolator implements Function {

	/**
	 * Polynomial coefficients.
	 */
	protected double[] hornerCoef; // generalized Horner expansion coefficients
	double[] xd;
	double[] yd;

	/**
	 * Constructs a Lagrange interpolating polynomial from the given data using
	 * Horner's expansion for the representation of the polynomial.
	 * 
	 * @param xdata
	 *            double[]
	 * @param ydata
	 *            double[]
	 */
	public LagrangeInterpolator(final double[] xdata, final double[] ydata) {
		hornerCoef = new double[xdata.length];
		xd = xdata;
		yd = ydata;
		computeCoefficients(xdata, ydata);
	}

	private void computeCoefficients(final double[] xd, final double[] yd) {
		int n = xd.length;
		// coefficients = new double[size];
		for (int i = 0; i < n; i++) {
			hornerCoef[i] = yd[i];
		}
		n -= 1;
		for (int i = 0; i < n; i++) {
			for (int k = n; k > i; k--) {
				int k1 = k - 1;
				int kn = k - (i + 1);
				hornerCoef[k] = (hornerCoef[k] - hornerCoef[k1])
						/ (xd[k] - xd[kn]);
			}
		}
	}

	/**
	 * Computes the interpolated y value for a given x value.
	 * 
	 * @param x
	 *            value
	 * @return interpolated y value
	 */
	public double evaluate(final double x) {
		int n = hornerCoef.length;
		double answer = hornerCoef[--n];
		while (--n >= 0) {
			answer = answer * (x - xd[n]) + hornerCoef[n];
		}
		return answer;
	}

	/**
	 * Gets the polynomial coefficients c in c[0] + c[1] * x + c[2] * x^2 + ....
	 * This routine should be used with care because the Vandermonde matrix that
	 * is being solved is ill conditioned. See Press et al.
	 * 
	 * @return double[]
	 */
	public double[] getCoefficients() {
		int n = xd.length;
		double[] temp = new double[n];
		double[] coef = new double[n];
		temp[n - 1] = -xd[0];
		for (int i = 1; i < n; i++) {
			for (int j = n - i - 1; j < n - 1; j++) {
				temp[j] -= xd[i] * temp[j + 1];
			}
			temp[n - 1] -= xd[i];
		}
		for (int j = 0; j < n; j++) {
			double a = n;
			for (int k = n - 1; k >= 1; k--) {
				a = k * temp[k] + xd[j] * a;
			}
			double b = yd[j] / a;
			double c = 1.0;
			for (int k = n - 1; k >= 0; k--) {
				coef[k] += c * b;
				c = temp[k] + xd[j] * c;
			}
		}
		return coef;
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
