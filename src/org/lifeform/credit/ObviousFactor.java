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

public class ObviousFactor extends Factor {
	private final String idStr;
	public double specificLoad = .5d;
	private final static double ONE = 1.0 - 0.1e-7;
	public int[] HiddenFactorNrs;
	public double[] HiddenFactorLoads;
	public double SimuWert;

	public ObviousFactor(final String name, final String id, final double num) {
		super(name, -1, num);
		this.idStr = id;
	}

	public String getIdStr() {
		return idStr;
	}

	public double Simulate(final FactorModel theModell) {
		double mySum = this.specificLoad * RandomStream.Normal01();
		if (this.specificLoad < ONE) { // look for the influencing factors
			for (int i = 0; i < HiddenFactorNrs.length; i++) {
				mySum += theModell.hiddenFactors[HiddenFactorNrs[i]].SimuWert
						* HiddenFactorLoads[i];
			}
		}
		this.SimuWert = mySum;
		return mySum;
	}

}
