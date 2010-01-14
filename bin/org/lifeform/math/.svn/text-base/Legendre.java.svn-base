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

/**
 * Legendre defines Legendre Polynomials based on of Alan Jeffrey's Handbook of
 * Mathematical Formulas an Integrals. Please see page 286-288. Information also
 * obtained from: http://mathworld.wolfram.com/LegendrePolynomial.html
 * 
 * This code is based on the Open Source Physics class for Hermite polynomials.
 * 
 * @author Nick Dovidio
 * @version 1.0
 */
public class Legendre {
	static final ArrayList<Polynomial> legendreList; // Stores our functions
	/**
	 * We have a static initialization list that initializes our array and the
	 * first two polynomials. These first two values are used to calculate the
	 * recursion.
	 */
	static {
		legendreList = new ArrayList<Polynomial>();
		Polynomial p = new Polynomial(new double[] { 1.0 });
		legendreList.add(p);
		p = new Polynomial(new double[] { 0, 1.0 });
		legendreList.add(p);
	}

	/**
	 * Gets the nth Legendre polynomial. If it has already been calculated it
	 * just returns it from the list. If we have not calculated it uses the
	 * recursion relationship to calculate based off of the prior polynomials.
	 */
	public static synchronized Polynomial getPolynomial(final int n) {
		if (n < legendreList.size()) {
			return legendreList.get(n);
		}
		Polynomial part1 = new Polynomial(new double[] { 0, (2 * (n - 1) + 1) });
		Polynomial p1 = getPolynomial(n - 1).multiply(part1);
		Polynomial p2 = getPolynomial(n - 2).multiply(n - 1);
		Polynomial p = p1.subtract(p2).multiply(1.0 / n);
		System.out.println("n=" + n);
		legendreList.add(p);
		return p;
	}

	private Legendre() {
	}

}
