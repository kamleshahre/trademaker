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

public abstract class DiffusionProcess {
	double x0_;

	public DiffusionProcess(final double x0) {
		x0_ = 0.0d;
	}

	// returns the diffusion part of the equation, i.e. sigma(t,x_t)
	public abstract double diffusion(final double t, final double x);

	// returns the drift part of the equation, i.e. mu(t, x_t)
	public abstract double drift(final double t, final double x);

	// returns the expectation of the process after a time interval
	// returns E(x_{t_0 + delta t} | x_{t_0} = x_0) since it is Markov.
	// By default, it returns the Euler approximation defined by
	// x_0 + mu(t_0, x_0) delta t.
	public double expectation(final double t0, final double x0, final double dt) {
		return x0 + drift(t0, x0) * dt;
	}

	// returns the variance of the process after a time interval
	// returns Var(x_{t_0 + Delta t} | x_{t_0} = x_0).
	// By default, it returns the Euler approximation defined by
	// sigma(t_0, x_0)^2 \Delta t .
	public double variance(final double t0, final double x0, final double dt) {
		double sigma = diffusion(t0, x0);
		return sigma * sigma * dt;
	}

	// virtual DiffusionProcess() {}
	public double x0() {
		return x0_;
	}
}
