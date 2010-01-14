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

/**
 * A risk entity can be thought of like a borrower. It has a certain propability
 * of default. the default propability (PD) in a certain simulated scenario
 * depends on the current value of the portfolio's factor model. This is
 * modelled by a ability-to-pay-index, which is a gaussian factor for this
 * specific risk entity. If it's simulated value is below a critical value the
 * R.E. defaults.
 * 
 * @author ernan
 * 
 */
public class RiskEntity {
	public String name;
	public String ID;
	private double PDE1;
	public double criticalValue1;
	public boolean defaultedThisTime;
	public ObviousFactor factor;

	public RiskEntity(final String theName, final String theID, final double thePDE1) {
		this.factor = new ObviousFactor(theName, theID, -1.0d);
		this.SetPDE1(thePDE1);
		this.name = theName;
		this.ID = theID;
	}

	public double GetPDE1() {
		return this.PDE1;

	}

	public void SetPDE1(final double thePDE1) {
		this.PDE1 = thePDE1;
		this.criticalValue1 = Statistics.NormQuantil(thePDE1);
	}

	public boolean Simulate(final FactorModel theFactormodel) {
		this.factor.Simulate(theFactormodel);
		this.defaultedThisTime = (this.factor.SimuWert <= this.criticalValue1);
		return (this.defaultedThisTime);
	}
}
