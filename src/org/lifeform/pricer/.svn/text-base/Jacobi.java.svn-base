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
package org.lifeform.pricer;

public class Jacobi {
	public static int cyclicMethod(final int N, final double dx, final double dt, final double[][] A,
			final double[][] q, final double abstol) {
		int i = 1, j = 1, k = 1;
		int maxit = 100000;
		double sum = 0.0d;
		double[][] Aold = new double[N][N];
		double D = dt / (dx * dx);
		for (i = 1; i < N - 1; i++) {
			for (j = 1; j < N - 1; j++) {
				Aold[i][j] = 1.0;
			}
		}
		/* Boundary Conditions -- all zeros */
		for (i = 0; i < N; i++) {
			A[0][i] = 0.0;
			A[N - 1][i] = 0.0;
			A[i][0] = 0.0;
			A[i][N - 1] = 0.0;
		}
		for (k = 0; k < maxit; k++) {
			for (i = 1; i < N - 1; i++) {
				for (j = 1; j < (N - 1); j++) {
					A[i][j] = dt
							* q[i][j]
							+ Aold[i][j]
							+ D
							* (Aold[i + 1][j] + Aold[i][j + 1] - 4.0
									* Aold[i][j] + Aold[i - 1][j] + Aold[i][j - 1]);
				}
			}
			sum = 0.0;
			for (i = 0; i < N; i++) {
				for (j = 0; j < N; j++) {
					sum += (Aold[i][j] - A[i][j]) * (Aold[i][j] - A[i][j]);
					Aold[i][j] = A[i][j];
				}
			}
			if (Math.sqrt(sum) < abstol) {
				return k;
			}
		}
		return maxit;
	}
}
