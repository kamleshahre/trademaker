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

public class FactorModel {
	public HiddenFactor[] hiddenFactors;
	public ObviousFactor[] obviousFactors;
	private final int lastHiddenFactorNr;
	public double SimuWert;

	public FactorModel(final boolean theStandardIni) {
		this.hiddenFactors = new HiddenFactor[3];
		this.obviousFactors = new ObviousFactor[3];

		this.obviousFactors[0] = new ObviousFactor("X_GLOBAL", "X_GLOBAL", 0);
		this.hiddenFactors[0] = new HiddenFactor("S_0", 0);
		this.obviousFactors[1] = new ObviousFactor("X_A", "X_A", 1);
		this.hiddenFactors[1] = new HiddenFactor("S_1", 1);
		this.obviousFactors[2] = new ObviousFactor("X_B", "X_B", 2);
		this.hiddenFactors[2] = new HiddenFactor("S_2", 2);
		this.lastHiddenFactorNr = 2;
	}

	public void InitFromDirectory(final String thePath) {
		// initialisieren aus dem directory
	}

	public void ReBalance() {

	}

	public void Simulate() {
		for (int i = 0; i <= this.lastHiddenFactorNr; i++) {
			this.hiddenFactors[i].Simulate();
		}
	}
}
