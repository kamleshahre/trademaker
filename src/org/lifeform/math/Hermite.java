/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

import java.util.ArrayList;

/**
 * Calculates Hermite polynomials.
 * 
 * @author W. Christian
 * @version 1.0
 */
public class Hermite {
	static final ArrayList<Polynomial> hermiteList;
	static final Polynomial twoX = new Polynomial(new double[] { 0, 2.0 }); // 2x
	// used
	// in
	// recursion

	static {
		hermiteList = new ArrayList<Polynomial>();
		Polynomial p = new Polynomial(new double[] { 1.0 });
		hermiteList.add(p);
		p = new Polynomial(new double[] { 0, 2.0 });
		hermiteList.add(p);
	}

	/**
	 * Evaluates the n-th Hermite polynomial at x.
	 * 
	 * @return the value of the function
	 */
	public static double evaluate(final int n, final double x) {
		return getPolynomial(n).evaluate(x);
	}

	/**
	 * Gets the n-th Hermite polynomial. If it has already been calculated it
	 * just returns it from the list. If we have not calculated it uses the
	 * recursion relationship to construct the polynomial based on the prior
	 * polynomials.
	 */
	public static synchronized Polynomial getPolynomial(final int n) {
		if (n < hermiteList.size()) {
			return hermiteList.get(n);
		}
		Polynomial p1 = getPolynomial(n - 1).multiply(twoX);
		Polynomial p2 = getPolynomial(n - 2).multiply(2 * (n - 1));
		Polynomial p = p1.subtract(p2);
		hermiteList.add(p); // ploynomial was not on the list so add it.
		return p;
	}

	private Hermite() {
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
