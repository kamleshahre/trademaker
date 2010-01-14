/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Polynomial least square fit without any error estimation.
 * 
 * See Object Oriented Implementation of Numerical Methods by Didier H. Besset
 * for fitting with error estimation.
 * 
 * @author Wolfgang Christian.
 */
public class PolynomialLeastSquareFit extends Polynomial {
	double[][] systemMatrix;
	double[] systemConstants;

	/**
	 * Constructs a PolynomialLeastSquareFit with the given coefficients. Added
	 * by D Brown 12/20/2005.
	 * 
	 * @param coeffs
	 *            the coefficients
	 */
	public PolynomialLeastSquareFit(final double[] coeffs) {
		super(coeffs);
		int n = coeffs.length;
		systemMatrix = new double[n][n];
		systemConstants = new double[n];
	}

	/**
	 * Constructs a PolynomialLeastSquareFit with the given order.
	 * 
	 * @param xd
	 *            double[]
	 * @param yd
	 *            double[]
	 * @param degree
	 *            int the degree of the polynomial
	 */
	public PolynomialLeastSquareFit(final double[] xd, double[] yd,
			final int degree) {
		super(new double[degree + 1]);
		int ncoef = degree + 1;
		systemMatrix = new double[ncoef][ncoef];
		systemConstants = new double[ncoef];
		// added by Doug Brown 12/1/05
		fitData(xd, yd);
	}

	/**
	 * Computes the polynomial coefficients.
	 */
	protected void computeCoefficients() {
		for (int i = 0; i < systemConstants.length; i++) {
			for (int j = i + 1; j < systemConstants.length; j++) {
				systemMatrix[i][j] = systemMatrix[j][i];
			}
		}
		LUPDecomposition lupSystem = new LUPDecomposition(systemMatrix);
		double[][] components = lupSystem.inverseMatrixComponents();
		LUPDecomposition.symmetrizeComponents(components);
		coefficients = lupSystem.solve(systemConstants);
	}

	/**
	 * Sets the data and updates the fit coefficients. Added by D Brown 12/1/05.
	 * 
	 * @param xd
	 *            double[]
	 * @param yd
	 *            double[]
	 */
	public void fitData(final double[] xd, final double[] yd) {
		if (xd.length != yd.length) {
			throw new IllegalArgumentException(
					"Arrays must be of equal length.");
		}
		// return if data array too short
		if (xd.length < degree() + 1)
			return;
		// clear old matrix data
		for (int i = 0; i < systemConstants.length; i++) {
			systemConstants[i] = 0;
			for (int j = 0; j < systemConstants.length; j++) {
				systemMatrix[i][j] = 0;
			}
		}
		// fill matrix with new data
		for (int i = 0, n = xd.length; i < n; i++) {
			double xp1 = 1;
			for (int j = 0; j < systemConstants.length; j++) {
				systemConstants[j] += xp1 * yd[i];
				double xp2 = xp1;
				for (int k = 0; k <= j; k++) {
					systemMatrix[j][k] += xp2;
					xp2 *= xd[i];
				}
				xp1 *= xd[i];
			}
		}
		// compute coefficients
		computeCoefficients();
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
