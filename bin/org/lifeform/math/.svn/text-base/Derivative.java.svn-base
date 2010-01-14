/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

import org.lifeform.pricer.solver.Function;

/**
 * Derivative defines various derivative algorithms. This class cannot be
 * subclassed or instantiated because all methods are static.
 * 
 * @author Wolfgang Christian
 */
public class Derivative {
	/**
	 * Calculates the first derivative of a function using the finite difference
	 * approximation toward decreasing x.
	 * 
	 * @param f
	 *            the function
	 * @param x
	 *            the x value
	 * @param h
	 * @return first derivative
	 */
	static public double backward(final Function f, final double x,
			final double h) {
		return (f.evaluate(x - 2 * h) - 4 * f.evaluate(x - h) + 3 * f
				.evaluate(x))
				/ h / 2.0;
	}

	/**
	 * Calculates the first derivative of a function using the centered finite
	 * difference approximation.
	 * 
	 * @param f
	 *            the function
	 * @param x
	 *            the x value
	 * @param h
	 * @return first derivatve
	 */
	static public double centered(final Function f, final double x,
			final double h) {
		return (f.evaluate(x + h) - f.evaluate(x - h)) / h / 2.0;
	}

	/**
	 * Calculates the first derivative of a function at the given point.
	 * 
	 * The current implementation uses the centered finite difference method but
	 * this may change.
	 * 
	 * @param f
	 *            the function
	 * @param x
	 *            the x value
	 * @param h
	 * @return first derivative
	 */
	static public double first(final Function f, final double x, final double h) {
		return (f.evaluate(x + h) - f.evaluate(x - h)) / h / 2.0;
	}

	/**
	 * Gets the partial derivate of a multivariable function using the centered
	 * finite difference approximation.
	 * 
	 * @param f
	 *            MultiVarFunction
	 * @param x
	 *            double[] variables
	 * @param n
	 *            int index
	 * @param h
	 *            double change in the varible with index i
	 * @return double
	 */
	static public double firstPartial(final MultiVarFunction f,
			final double[] x, final int n, final double h) {
		double[] tempPlus = new double[x.length];
		System.arraycopy(x, 0, tempPlus, 0, x.length);
		tempPlus[n] += h;
		double[] tempMinus = new double[x.length];
		System.arraycopy(x, 0, tempMinus, 0, x.length);
		tempMinus[n] -= h;
		return (f.evaluate(tempPlus) - f.evaluate(tempMinus)) / 2.0 / h;
	}

	/**
	 * Calculates the first derivative of a function using the finite difference
	 * approximation toward increasing x.
	 * 
	 * @param f
	 *            the function
	 * @param x
	 *            the x value
	 * @param h
	 * @return first derivative
	 */
	static public double forward(final Function f, final double x,
			final double h) {
		return (-f.evaluate(x + 2 * h) + 4 * f.evaluate(x + h) - 3 * f
				.evaluate(x))
				/ h / 2.0;
	}

	/**
	 * Gets a derivative function using the centered difference approximation.
	 * 
	 * @param f
	 *            Function f(x)
	 * @param h
	 *            double change in x
	 * @return Function
	 */
	static public Function getFirst(final Function f, final double h) {
		return new Function() {
			public double evaluate(final double x) { // in-line the code for
														// speed
				return (f.evaluate(x + h) - f.evaluate(x - h)) / h / 2.0;
			}
		};
	}

	/**
	 * Gets a second derivative function using a second order finite difference
	 * approximation.
	 * 
	 * @param f
	 *            Function f(x)
	 * @param h
	 *            double change in x
	 * @return Function second derivate
	 */
	static public Function getSecond(final Function f, final double h) {
		return new Function() {
			public double evaluate(final double x) { // in-line the code for
														// speed
				return (f.evaluate(x + h) - 2 * f.evaluate(x) + f.evaluate(x
						- h))
						/ h / h;
			}
		};
	}

	/**
	 * Calculates the derivative using the Romberg scheme for Richardson
	 * extrapolation.
	 * 
	 * This method runs until all Romberg rows are filled or until the step size
	 * drops below defaultNumericalPrecision or if the desired tolerance is
	 * reached.
	 * 
	 * @param f
	 *            the function
	 * @param x0
	 *            where derivative is to be calculated
	 * @param h
	 *            initial step size
	 * @param tol
	 *            desired accuracy
	 * @return first derivative
	 */
	static public double romberg(final Function f, final double x0, double h,
			final double tol) {
		int n = 6; // max. number of columns in the Romberg scheme
		double[] d = new double[n];
		d[0] = (f.evaluate(x0 + h) - f.evaluate(x0 - h)) / h / 2.0;
		int error_code = 1;
		for (int j = 1; j <= n - 1; j++) {
			d[j] = 0.0;
			double d1 = d[0];
			double h2 = h;
			h *= 0.5;
			if (h < Util.defaultNumericalPrecision) {
				error_code = 2; /* step size less than defaultNumericalPrecision */
				break;
			}
			d[0] = (f.evaluate(x0 + h) - f.evaluate(x0 - h)) / h2;
			for (int m = 4, i = 1; i <= j; i++, m *= 4) {
				double d2 = d[i];
				d[i] = (m * d[i - 1] - d1) / (m - 1);
				d1 = d2;
			}
			if (Math.abs(d[j] - d[j - 1]) < tol) { /* desired accuracy reached */
				return d[j];
			}
		}
		throw new NumericMethodException("Derivative did not converge.",
				error_code, d[0]);
	}

	/**
	 * Computes the second derivate using the centered finite difference
	 * approximation.
	 * 
	 * @param f
	 *            Function
	 * @param x
	 *            double
	 * @param h
	 *            double
	 * @return double
	 */
	static public double second(final Function f, final double x, final double h) {
		return (f.evaluate(x + h) - 2 * f.evaluate(x) + f.evaluate(x - h)) / h
				/ h;
	}

	private Derivative() {
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
