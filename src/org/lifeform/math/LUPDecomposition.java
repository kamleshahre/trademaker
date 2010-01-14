/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Lower Upper Permutation (LUP) decomposition See Object Oriented
 * Implementation of Numerical Methods by Didier H. Besset.
 * 
 * @author Didier H. Besset
 */
public class LUPDecomposition {

	/**
	 * Make sure the supplied matrix components are those of a symmetric matrix
	 * 
	 * @param components
	 *            double
	 */
	public static void symmetrizeComponents(final double[][] components) {
		for (int i = 0; i < components.length; i++) {
			for (int j = i + 1; j < components.length; j++) {
				components[i][j] += components[j][i];
				components[i][j] *= 0.5;
				components[j][i] = components[i][j];
			}
		}
	}

	/**
	 * Rows of the system
	 */
	private double[][] rows;

	/**
	 * Permutation
	 */
	private int[] permutation = null;

	/**
	 * Permutation's parity
	 */
	private int parity = 1;

	/**
	 * Constructor method
	 * 
	 * @param components
	 *            double[][]
	 * @exception DhbMatrixAlgebra.DhbIllegalDimension
	 *                the supplied matrix is not square
	 */
	public LUPDecomposition(final double[][] components)
			throws IllegalArgumentException {
		int n = components.length;
		if (components[0].length != n) {
			throw new IllegalArgumentException("Illegal system: a" + n + " by "
					+ components[0].length + " matrix is not a square matrix");
		}
		initialize(components);
	}

	/**
	 * @return double[]
	 * @param xTilde
	 *            double[]
	 */
	private double[] backwardSubstitution(final double[] xTilde) {
		int n = rows.length;
		double[] answer = new double[n];
		for (int i = n - 1; i >= 0; i--) {
			answer[i] = xTilde[i];
			for (int j = i + 1; j < n; j++) {
				answer[i] -= rows[i][j] * answer[j];
			}
			answer[i] /= rows[i][i];
		}
		return answer;
	}

	private void decompose() {
		int n = rows.length;
		permutation = new int[n];
		for (int i = 0; i < n; i++) {
			permutation[i] = i;
		}
		parity = 1;
		try {
			for (int i = 0; i < n; i++) {
				swapRows(i, largestPivot(i));
				pivot(i);
			}
		} catch (ArithmeticException e) {
			parity = 0;
		}
		;
	}

	/**
	 * @return boolean true if decomposition was done already
	 */
	private boolean decomposed() {
		if ((parity == 1) && (permutation == null)) {
			decompose();
		}
		return parity != 0;
	}

	/**
	 * Gets the determinant.
	 * 
	 * @return double[]
	 */
	public double determinant() {
		if (!decomposed()) {
			return Double.NaN;
		}
		double determinant = parity;
		for (int i = 0; i < rows.length; i++) {
			determinant *= rows[i][i];
		}
		return determinant;
	}

	/**
	 * @return double[]
	 * @param c
	 *            double[]
	 */
	private double[] forwardSubstitution(final double[] c) {
		int n = rows.length;
		double[] answer = new double[n];
		for (int i = 0; i < n; i++) {
			answer[i] = c[permutation[i]];
			for (int j = 0; j <= i - 1; j++) {
				answer[i] -= rows[i][j] * answer[j];
			}
		}
		return answer;
	}

	/**
	 * @param components
	 *            double[][] components obtained from constructor methods.
	 */
	private void initialize(final double[][] components) {
		int n = components.length;
		rows = new double[n][n];
		for (int i = 0; i < n; i++) { // loop over the rows
			System.arraycopy(components[i], 0, rows[i], 0, n);
		}
		permutation = null;
		parity = 1;
	}

	/**
	 * Calculates the inverse matrix components.
	 * 
	 * @return the matrix inverse or null if the inverse does not exist
	 */
	public double[][] inverseMatrixComponents() {
		if (!decomposed()) {
			return null;
		}
		int n = rows.length;
		double[][] inverseMatrix = new double[n][n];
		double[] column = new double[n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				column[j] = 0;
			}
			column[i] = 1;
			column = solve(column);
			for (int j = 0; j < n; j++) {
				if (Double.isNaN(column[j])) {
					return null;
				}
				inverseMatrix[j][i] = column[j];
			}
		}
		return inverseMatrix;
	}

	/**
	 * @return int
	 * @param k
	 *            int
	 */
	private int largestPivot(final int k) {
		double maximum = Math.abs(rows[k][k]);
		double abs;
		int index = k;
		for (int i = k + 1; i < rows.length; i++) {
			abs = Math.abs(rows[i][k]);
			if (abs > maximum) {
				maximum = abs;
				index = i;
			}
		}
		return index;
	}

	/**
	 * @param k
	 *            int
	 */
	private void pivot(final int k) {
		double inversePivot = 1 / rows[k][k];
		int k1 = k + 1;
		int n = rows.length;
		for (int i = k1; i < n; i++) {
			rows[i][k] *= inversePivot;
			for (int j = k1; j < n; j++) {
				rows[i][j] -= rows[i][k] * rows[k][j];
			}
		}
	}

	/**
	 * @return double[]
	 * @param c
	 *            double[]
	 */
	public double[] solve(final double[] c) {
		return decomposed() ? backwardSubstitution(forwardSubstitution(c))
				: null;
	}

	/**
	 * @param i
	 *            int
	 * @param k
	 *            int
	 */
	private void swapRows(final int i, final int k) {
		if (i != k) {
			double temp;
			for (int j = 0; j < rows.length; j++) {
				temp = rows[i][j];
				rows[i][j] = rows[k][j];
				rows[k][j] = temp;
			}
			int nTemp;
			nTemp = permutation[i];
			permutation[i] = permutation[k];
			permutation[k] = nTemp;
			parity = -parity;
		}
	}

	/**
	 * Returns a String that represents the value of this object.
	 * 
	 * @return a string representation of the receiver
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		char[] separator = { '[', ' ' };
		int n = rows.length;
		for (int i = 0; i < n; i++) {
			separator[0] = '{';
			for (int j = 0; j < n; j++) {
				sb.append(separator);
				sb.append(rows[i][j]);
				separator[0] = ' ';
			}
			sb.append('}');
			sb.append('\n');
		}
		if (permutation != null) {
			sb.append((parity == 1) ? '+' : '-');
			sb.append("( " + permutation[0]);
			for (int i = 1; i < n; i++) {
				sb.append(", " + permutation[i]);
			}
			sb.append(')');
			sb.append('\n');
		}
		return sb.toString();
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
