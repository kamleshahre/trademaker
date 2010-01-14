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

public class ContributionStorage {
	public static double LBarrier;
	public static double UBarrier;

	public static void InitBarrier(final double theLBarrier, final double theUBarrier) {
		LBarrier = theLBarrier;
		UBarrier = theUBarrier;
	}

	private final double[] LossPerRange = { 0.0, 0.0, 0.0 };

	private final int[] HitsPerRange = { 0, 0, 0 };

	public ContributionStorage() {
		LBarrier = 1e99;
		UBarrier = 2e99;
	}

	public double GetEshContrib() {
		double mySum = this.LossPerRange[2];
		int myN = this.HitsPerRange[2];
		return myN > 0 ? mySum / myN : 0.0;
	}

	public int GetPfLossGrade(final double thePortfolioLoss) {
		if (thePortfolioLoss < LBarrier)
			return 0;
		else if (thePortfolioLoss < UBarrier)
			return 1;
		else
			return 2;

	}

	public double GetVaRContrib() {
		double mySum = this.LossPerRange[1] + this.LossPerRange[2];
		int myN = this.HitsPerRange[1] + this.HitsPerRange[2];
		return myN > 0 ? mySum / myN : 0.0;
	}

	public void Save(final int thePortfolioLossGrade, final double theLoss) {
		this.LossPerRange[thePortfolioLossGrade] += theLoss;
		this.HitsPerRange[thePortfolioLossGrade] += 1;
	}
}
