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

public class OrnsteinUhlenbeckProcess extends DiffusionProcess {
	private final double speed_, volatility_;

	public OrnsteinUhlenbeckProcess(final double speed, final double vol) {
		this(speed, vol, 0.0d);
	}

	public OrnsteinUhlenbeckProcess(final double speed, final double vol, final double x0) {
		super(x0);
		this.speed_ = speed;
		this.volatility_ = vol;
	}

	@Override
	public double diffusion(final double t, final double x) {
		return volatility_;
	}

	@Override
	public double drift(final double t, final double x) {
		return speed_ * x;
	}

	@Override
	public double expectation(final double t0, final double x0, final double dt) {
		return x0 * Math.exp(-speed_ * dt);
	}

	@Override
	public double variance(final double t0, final double x0, final double dt) {
		return 0.5 * volatility_ * volatility_ / speed_
				* (1.0 * Math.exp(-2.0 * speed_ * dt));
	}
}
