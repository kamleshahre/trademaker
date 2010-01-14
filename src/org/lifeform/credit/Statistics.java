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

public class Statistics {
	static Integer[] keyNQ;
	static Double[] normQ;
	private static final int LASTINDEX = 48;
	private static final double KEYNQ_UNIT = 0.00001;
	private static final double KEYNQ_UNITINV = 1.0 / KEYNQ_UNIT;

	public static double Fi(final double theNVValue) {
		if (theNVValue > 0.0)
			return (1.0 - Fi(-theNVValue));
		else if (theNVValue == 0.0)
			return 0.5;
		int myUpperIndex = -1;
		for (int i = 0; i <= LASTINDEX; i++) {
			if (normQ[i] > theNVValue) {
				myUpperIndex = i;
				break;
			}
		}
		// found: theNVValue lies between normQ[myUpperIndex-1] and
		// normQ[myUpperIndex]
		if (myUpperIndex == 0)
			return ((double) keyNQ[0]) * KEYNQ_UNIT;
		int myLowerIndex = myUpperIndex - 1;
		double myLambda = (theNVValue - normQ[myLowerIndex])
				/ (normQ[myUpperIndex] - normQ[myLowerIndex]);
		return KEYNQ_UNIT
				* (keyNQ[myLowerIndex] + myLambda
						* (keyNQ[myUpperIndex] - keyNQ[myLowerIndex]));

	}

	public static double NormQuantil(final double theProb) {
		if (theProb > 0.5) {
			return (NormQuantil(1.0 - theProb));
		} else {
			double myLookUp = KEYNQ_UNITINV * theProb;
			int myUpperKey = -1;
			int myLowerKey = -1;

			if (myLookUp < 1) {
				return (normQ[0]);
			}
			for (int i = 1; i <= LASTINDEX; i++) {

				if (keyNQ[i] >= myLookUp) {
					myUpperKey = i;
					myLowerKey = i - 1;
					break;
				}
			}

			double myLambda = (myLookUp - keyNQ[myLowerKey])
					/ (keyNQ[myUpperKey] - keyNQ[myLowerKey]);
			return (normQ[myLowerKey] + myLambda
					* (normQ[myUpperKey] - normQ[myLowerKey]));
		}
	}

	public Statistics() {
		keyNQ = new Integer[LASTINDEX + 1];
		normQ = new Double[LASTINDEX + 1];
		keyNQ[0] = 1;
		keyNQ[1] = 10;
		keyNQ[2] = 20;
		keyNQ[3] = 30;
		keyNQ[4] = 40;
		keyNQ[5] = 50;
		keyNQ[6] = 60;
		keyNQ[7] = 80;
		keyNQ[8] = 100;
		keyNQ[9] = 120;
		keyNQ[10] = 140;
		keyNQ[11] = 160;
		keyNQ[12] = 180;
		keyNQ[13] = 200;
		keyNQ[14] = 250;
		keyNQ[15] = 300;
		keyNQ[16] = 350;
		keyNQ[17] = 400;
		keyNQ[18] = 450;
		keyNQ[19] = 500;
		keyNQ[20] = 600;
		keyNQ[21] = 700;
		keyNQ[22] = 800;
		keyNQ[23] = 1000;
		keyNQ[24] = 1250;
		keyNQ[25] = 1500;
		keyNQ[26] = 1750;
		keyNQ[27] = 2000;
		keyNQ[28] = 2250;
		keyNQ[29] = 2500;
		keyNQ[30] = 2750;
		keyNQ[31] = 3000;
		keyNQ[32] = 3500;
		keyNQ[33] = 4000;
		keyNQ[34] = 5000;
		keyNQ[35] = 6000;
		keyNQ[36] = 7000;
		keyNQ[37] = 8000;
		keyNQ[38] = 10000;
		keyNQ[39] = 12500;
		keyNQ[40] = 15000;
		keyNQ[41] = 17500;
		keyNQ[42] = 20000;
		keyNQ[43] = 25000;
		keyNQ[44] = 30000;
		keyNQ[45] = 35000;
		keyNQ[46] = 40000;
		keyNQ[47] = 45000;
		keyNQ[48] = 50000;

		normQ[0] = -4.26484457154684;
		normQ[1] = -3.7191242961969;
		normQ[2] = -3.54024446199105;
		normQ[3] = -3.43180706558158;
		normQ[4] = -3.3530105080173;
		normQ[5] = -3.2907604924867;
		normQ[6] = -3.23912866692866;
		normQ[7] = -3.15617864984913;
		normQ[8] = -3.09052222578017;
		normQ[9] = -3.03597689775898;
		normQ[10] = -2.98919902485491;
		normQ[11] = -2.94816977475946;
		normQ[12] = -2.91157405914388;
		normQ[13] = -2.87850610774224;
		normQ[14] = -2.80739472291171;
		normQ[15] = -2.74815535295093;
		normQ[16] = -2.69722876369246;
		normQ[17] = -2.65246302787891;
		normQ[18] = -2.61245468829915;
		normQ[19] = -2.57623608130957;
		normQ[20] = -2.51256106406808;
		normQ[21] = -2.45768760966081;
		normQ[22] = -2.40934544666397;
		normQ[23] = -2.32678533255897;
		normQ[24] = -2.24184486611052;
		normQ[25] = -2.17053377834912;
		normQ[26] = -2.10880075991387;
		normQ[27] = -2.0541885887219;
		normQ[28] = -2.00509023562922;
		normQ[29] = -1.96039491692534;
		normQ[30] = -1.91930158033625;
		normQ[31] = -1.8812127935032;
		normQ[32] = -1.81231616292687;
		normQ[33] = -1.75107653110187;
		normQ[34] = -1.64521144014382;
		normQ[35] = -1.55509667223422;
		normQ[36] = -1.47607822090032;
		normQ[37] = -1.40532226383681;
		normQ[38] = -1.28172875650271;
		normQ[39] = -1.15043562626776;
		normQ[40] = -1.03643148518956;
		normQ[41] = -0.934503395364555;
		normQ[42] = -0.841456717354784;
		normQ[43] = -0.674189140043317;
		normQ[44] = -0.52400187038268;
		normQ[45] = -0.384877084996513;
		normQ[46] = -0.252933267826583;
		normQ[47] = -0.125380993102919;
		normQ[48] = -0.0;

	}

}
