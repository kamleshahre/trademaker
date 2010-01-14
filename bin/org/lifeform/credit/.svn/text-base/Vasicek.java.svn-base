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

public class Vasicek {
	public static double Solve4_Rho_Vas(final double theP, final double theVariance) {
		int myI = 0;

		// jetzt loesen...intervallschacht.
		double[] myRho = { 0, 0.25, 0.7 };
		double[] myVar = { 0, 0.5, 1.0 };
		myVar[0] = Variance_Vas(theP, myRho[0]);
		myVar[2] = Variance_Vas(theP, myRho[2]);

		do {
			myI++;
			myRho[1] = 0.5 * (myRho[0] + myRho[2]);
			myVar[1] = Variance_Vas(theP, myRho[1]);
			if (myVar[1] > theVariance) {
				myRho[2] = myRho[1];
				myVar[2] = myVar[1];
			} else {
				myRho[0] = myRho[1];
				myVar[0] = myVar[1];
			}

		} while (Math.abs(myRho[0] - myRho[2]) > 0.00000001);
		return myRho[1];
	}

	public static double Variance_Vas(final double theP, final double theRho) {
		double myN1P = Statistics.NormQuantil(theP);
		return NormalApprox.Normal2_CDF(myN1P, myN1P, theRho)
				- Math.pow(theP, 2.0);
	}

}
