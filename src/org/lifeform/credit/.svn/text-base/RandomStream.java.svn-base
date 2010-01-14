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

import java.util.Random;

public class RandomStream {
	static boolean bSchonDa = false;
	static double dLastVal;
	static int iStream = 1;
	static double[] a;
	static double[] b = { 2221, 2221, 2221, 2221, 2221, 2221, 2221, 2221, 2221,
			2221, 2221 };
	static double[] m = { 40009, 40013, 40031, 50021, 50923, 51001, 51131,
			51133, 51137, 51151, 51203 };

	public static void Init(int theSeed) { // deterministic initialisation if
		theSeed = theSeed % 30000 + 1;
		double myR = (theSeed / 30002.0);

		if (theSeed <= 0) {
			myR = new Random(theSeed).nextDouble();
		}

		a = new double[11];
		for (int i = 0; i < a.length; i++) {
			a[i] = (long) (1.0 + 20000.0 * myR);
		}
	}

	public static double Normal_01() {
		// other Method, still a bad QQ-Plot
		if (bSchonDa) {
			bSchonDa = false;
			return dLastVal;
		} else {
			bSchonDa = true;
			double dUnifX = Uniform01();
			double dUnifY = Uniform01();
			if (dUnifY == 0.0) {
				dUnifY = 0.0000000001;
			}
			if (dUnifX == 0.0) {
				dUnifX = 0.0000000001;
			}
			double u1 = Math.PI * 2.0 * dUnifX;
			double u2 = Math.sqrt(-2.0 * Math.log(1.0 - dUnifY));

			dLastVal = u1 * Math.sin(u2);
			return (u1 * Math.cos(u2));
		}
	}

	public static double Normal01() {
		double myU1;
		double myU2;
		double myMagicConst = 1.71552776992141;
		double myZ;
		double myZZ;
		while (true) {
			myU1 = Uniform01();
			myU2 = Uniform01();
			myZ = myMagicConst * (myU1 - 0.5) / myU2;
			myZZ = myZ * myZ / 4.0;
			if (myZZ <= -Math.log(myU2))
				break;
		}
		return myZ;
	}

	public static double Normal01_Below(final double theBoundary) {
		double myRandomNumber = 0.0;
		do {
			myRandomNumber = Normal01();
			if (myRandomNumber > -theBoundary)
				myRandomNumber = -myRandomNumber;

		} while (myRandomNumber > theBoundary);
		return myRandomNumber;
	}

	public static double Normal01_Between(final double theLowerBoundary,
			final double theUpperBoundary) {
		boolean myMaySwap = (theLowerBoundary * theUpperBoundary > 0.0);
		double myRandomNumber = 0.0;
		do {
			myRandomNumber = Normal01();
			if (myMaySwap) {
				if ((myRandomNumber > theLowerBoundary)
						&& (myRandomNumber < theUpperBoundary))
					break;
				else
					myRandomNumber = -myRandomNumber;
			}
		} while ((myRandomNumber > theLowerBoundary)
				&& (myRandomNumber < theUpperBoundary));
		return myRandomNumber;
	}

	public static double Uniform01() {
		iStream = (iStream + 1) % 11;
		a[iStream] = (a[iStream] * b[iStream] + 1.0) % m[iStream];
		return (a[iStream] + 0.5) / m[iStream];
	}

	public RandomStream() {
		Random rand = new Random(-1L);
		double r = rand.nextDouble();
		a = new double[11];
		for (int i = 0; i < a.length; i++) {
			r = rand.nextDouble();
			a[i] = (long) (20000.0 * r);
		}
	}
}
