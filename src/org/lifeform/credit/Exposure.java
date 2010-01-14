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

public class Exposure {
	public String ID;
	public String Description;
	public int expType;
	public double Loss_Medium;
	public double Loss_High;
	public double Loss_Low;
	public double LGD;
	public String LGD_type;
	public double ExposureAmount;
	public double YearsToMaturity;
	public boolean belongsToPortfolio = true;

	// public double BedingteExposureWsk; -still 2 come
	public double Loss_Simu;
	public RiskEntity RiskEntity;
	public boolean shouldCalcContribution = true;
	public ContributionStorage Schuld;

	public Exposure(final RiskEntity theRE, final boolean theShouldCalcContribution) {
		this.RiskEntity = theRE;
		this.belongsToPortfolio = true;
		this.shouldCalcContribution = theShouldCalcContribution;
		if (theShouldCalcContribution) {
			this.Schuld = new ContributionStorage();
		}
	}

	public double exposure() {
		this.LGD = this.Loss_Medium; // preliminary!
		return (this.Loss_Medium * this.RiskEntity.GetPDE1());
	}

	public void saveContribution(final int thePortfolioLossRange) {
		if (shouldCalcContribution)
			this.Schuld.Save(thePortfolioLossRange, this.Loss_Simu);
	}

	public double Simulate() {
		// schaue ob RE ausgefallen
		if (RiskEntity.defaultedThisTime) {
			if (this.LGD_type == "FIX") {
				Loss_Simu = Loss_Medium;
				// Console.WriteLine (this.LGD_type);
			} else // LGD_type == "UNI", der default
			{
				Loss_Simu = Loss_Medium * (0.5 + RandomStream.Uniform01());
				// gleichverteilt zwi. 50% und 150%
				// Console.WriteLine (this.LGD_type);
			}
		} else {
			Loss_Simu = 0.0;
		}
		return (Loss_Simu);
	}

	public double tailContribution() {

		return (this.Loss_Medium * this.RiskEntity.GetPDE1());
	}
}
