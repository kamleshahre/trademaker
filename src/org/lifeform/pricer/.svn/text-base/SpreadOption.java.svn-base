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

import java.util.Calendar;

import org.lifeform.core.MT;
import org.lifeform.core.NormalDistribution;
import org.lifeform.market.Market;
import org.lifeform.product.ExerciseType;
import org.lifeform.product.Product;

public class SpreadOption extends Pricer {

	// ESCA-JAVA0138:
	public double calcMCEuroSpreadOption(final double price1,
			final double price2, final double strike, final double rate,
			final double vol1, final double vol2, final double div1,
			final double div2, final double rho, final double T,
			final ExerciseType type, final int M, int N) {
		int i, j;
		double dt = T / N; // size of time step
		double mu1 = (rate - div1 - 0.5 * vol1 * vol1); // drift for stock price
		// 1
		double mu2 = (rate - div2 - 0.5 * vol2 * vol2); // drift for stock price
		// 2
		double srho = Math.sqrt(1 - rho * rho); // square root of 1  rho*rho
		double sum1 = 0.0; // sum of all the call values on
		// stock 1 at time T
		double sum2 = 0.0; // sum of all the call values on
		// stock 2 at time T
		double S1 = 0.0; // stock price 1
		double S2 = 0.0; // stock price 2
		double deviate1 = 0.0; // deviate for stock price 1
		double deviate2 = 0.0; // deviate for stock price 2
		double z1 = 0.0; // correlated deviate for stock
		// price 1
		double z2 = 0.0; // correlated deviate for stock
		// price 2
		double CT = 0.0; // option price at maturity
		double value = 0.0; // spread option price
		double idum = new MT().random();
		N = 1; // no path dependency
		for (i = 0; i < M; i++) {
			// initialize prices for each simulation
			S1 = price1;
			S2 = price2;
			for (j = 0; j < N; j++) {
				// generate deviates
				deviate1 = NormalDistribution.normal(idum);
				deviate2 = NormalDistribution.normal(idum);
				// calculate correlated deviates
				z1 = deviate1;
				z2 = rho * deviate1 + srho * deviate2;
				S1 *= Math.exp(mu1 * dt + vol1 * z1 * Math.sqrt(dt));
				S2 *= Math.exp(mu2 * dt + vol2 * z2 * Math.sqrt(dt));
			}
			if (type == ExerciseType.Call) {
				CT = Math.max(S1 - S2 - strike, 0);
			} else {
				CT = Math.max(strike - S1 + S2, 0);
			}
			sum1 += CT;
			sum2 += CT * CT;
		}
		value = Math.exp(-rate * T) * (sum1 / M);
		// double SD = Math.sqrt((sum2 - sum1 * sum1 / M) * Math.exp(-2 * rate *
		// T)
		// / (M - 1));
		// double SE = SD / Math.sqrt(M);
		return value;
	}

	@Override
	public PricingResult price(final Product product,
			final Calendar valuationDate, final Market market) {
		return null;
	}
}
