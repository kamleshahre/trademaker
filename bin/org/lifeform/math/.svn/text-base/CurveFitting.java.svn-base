/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

import org.lifeform.pricer.solver.Function;

/**
 * Class CurveFitting defines various curve fitting algorithms inluding linear
 * regression.
 * 
 * This class cannot be subclassed or instantiated because all methods are
 * static.
 * 
 * @author Wolfgang Christian
 */
public class CurveFitting {
	/**
	 * Computes the linear regression for the given data.
	 * 
	 * @param xpoints
	 *            double[]
	 * @param ypoints
	 *            double[]
	 * @return Function the linear regression function
	 */
	public static Function linearRegression(final double[] xpoints, final double[] ypoints) {
		double xBar_yBar = 0;
		double xBar = 0;
		double yBar = 0;
		double x2Bar = 0;
		double x = 0;
		double y = 0;
		for (int i = 0; i < xpoints.length; i++) {
			x = xpoints[i];
			y = ypoints[i];
			xBar_yBar += x * y;
			xBar += x;
			yBar += y;
			x2Bar += x * x;
		}
		int n = xpoints.length;
		xBar_yBar = xBar_yBar / n;
		xBar = xBar / n;
		yBar = yBar / n;
		x2Bar = x2Bar / n;
		double deltaX2 = x2Bar - xBar * xBar;
		final double m = (xBar_yBar - xBar * yBar) / deltaX2;
		final double b = yBar - m * xBar;
		return new Function() {
			public double evaluate(final double x) {
				return m * x + b;
			}

			public String toString() {
				return "linear regression: y(x) = " + m + "x + " + b;
			}
		};
	}

	private CurveFitting() {
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
