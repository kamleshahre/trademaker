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

import org.lifeform.core.NormalDistribution;

public class BlackScholesOption {
	/**********************************************************************************
	 * calcBSCall Price : calculates Black Scholes call price [in] : double vol
	 * : volatility double rate : interest rate double div : dividend yield
	 * double strike : strike price double price : stock price double T : time
	 * to maturity [out]: double : call price
	 **********************************************************************************/
	double calcBSCallPrice(final double vol, final double rate, final double div, final double strike,
			final double price, final double T) {
		double prob1;
		double prob2;
		double d1, d2;
		double callprice;
		// d1 = (log(price/strike) + (rate  div +
		// (vol)*(vol)/2)*T)/(vol*sqrt(T));
		d1 = Math.log(price / strike) + ((rate - div + vol / 2) * T)
				/ (vol * Math.sqrt(T));
		d2 = d1 - vol * Math.sqrt(T);
		prob1 = NormalDistribution.normal(d1);
		prob2 = NormalDistribution.normal(d2);
		// callprice = price*exp(-div*T)*prob1  strike*exp(-rate*T)*prob2;
		callprice = price * Math.exp(-div * T) * (prob1) - strike
				* Math.exp(-rate * T) * prob2; // * exp( - rate * T) * prob2;
		return callprice;
	}

	/**********************************************************************************
	 * calcBSPutPrice : calculates Black Scholes put price [in] : double vol :
	 * volatility 1.4 Black-Scholes and Diffusion Process Implementation 29
	 * double rate : interest rate double div : dividend yield double strike :
	 * strike price double price : stock price double T : time to maturity
	 * [out]: double : put price
	 **********************************************************************************/
	double calcBSPutPrice(final double vol, final double rate, final double div, final double strike,
			final double price, final double T) {
		double prob1;
		double prob2;
		double putprice;
		double d1, d2;
		// d1 = (log(price/strike) + (rate  div +
		// (vol)*(vol)/2)*T)/(vol*sqrt(T));
		d1 = Math.log(price / strike) + ((rate - div + vol / 2) * T)
				/ (vol * Math.sqrt(T));
		d2 = d1 - vol * Math.sqrt(T);
		prob1 = NormalDistribution.normal(d1);
		prob2 = NormalDistribution.normal(d2);
		// callprice = price*exp(-div*T)*prob1  strike*exp(-rate*T)*prob2;
		putprice = price * Math.exp(-div * T) * (prob1) - strike
				* Math.exp(-rate * T) * prob2; // * exp( - rate * T) * prob2;
		return putprice;
	}

}
