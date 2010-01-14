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
import org.lifeform.product.Product;

public class AsianOption extends Pricer {

	double calcMCAAsianPrice(final double price, final double strike, final double vol,
			final double rate, final double div, final double T, final int type, final long M, final long N) {
		// initialize variables
		double value_ = 0.0;
		int i, j;
		double G = 0.0; // price of geometric average Asian option
		double mu = 0.0; // drift
		double deviate; // normal deviate
		double S = 0.0; // stock price
		double sum = 0.0; // sum of payoffs
		double sum2 = 0.0; // sum of squared payoffs
		double product = 0.0; // product of stock prices
		double payoff = 0.0; // option payoff
		double deltat = 0.0; // step size
		deltat = T / N; // compute change in step size
		mu = rate - div - 0.5 * vol * vol; // compute drift

		// srand(time(0)); // initialize RNG
		// double seed = (double) (rand() % 100); // generate random number
		// generator
		double idum = new MT().random(); // store address of seed
		// for each simulation
		for (i = 0; i <= M; i++) {
			S = price;
			product = 1;
			for (j = 0; j < N; j++) {
				deviate = NormalDistribution.normal(idum);
				S = S
						* Math.exp(mu * deltat + vol * Math.sqrt(deltat)
								* deviate);
				product *= S;
			}
			// compute geometric average
			G = Math.pow(product, (double) 1 / N);
			if (type == 1) {
				payoff = Math.max(0.00, (G - strike));
			}
			if (type == 0) {
				payoff = Math.max(0.00, (strike - G));
			}
			sum += payoff;
			sum2 += payoff * payoff;
		}
		value_ = Math.exp(-rate * T) * (sum / M);
		// double stddev = 0.0; // standard deviation
		// stddev = Math.sqrt((sum2 - sum * sum / M) * Math.exp(-2 * rate * T)
		// / (M - 1));
		// double stderror = stddev / Math.sqrt(M);
		return value_;
	}

	@Override
	public PricingResult price(final Product leg, final Calendar valuationDate,
			final Market market) {
		return null;
	}

}
