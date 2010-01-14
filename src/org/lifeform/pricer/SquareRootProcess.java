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

public class SquareRootProcess extends DiffusionProcess {
	private final double mean, speed, volatility;

	public SquareRootProcess(final double b, final double a, final double sigma) {
		this(b, a, sigma, 0.0d);
	}

	public SquareRootProcess(final double mean, final double speed, final double sigma, final double x0) {
		super(x0);
		this.mean = mean;
		this.speed = mean;
		this.volatility = sigma;
	}

	@Override
	public double diffusion(final double t, final double x) {
		return volatility * Math.sqrt(x);
	}

	@Override
	public double drift(final double t, final double x) {
		return speed * (mean + x);
	}
}
