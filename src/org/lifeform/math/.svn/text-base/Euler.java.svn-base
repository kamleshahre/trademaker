/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Euler implements an Euler method ODE solver.
 * 
 * The Euler method is unstable for many systems. It is included as an example
 * of how to use the ODE and ODESolver interface.
 * 
 * @author Wolfgang Christian
 * @version 1.0
 */
public class Euler extends AbstractODESolver {
	protected double[] rate; // array that stores the rate

	/**
	 * Constructs the Euler ODESolver for a system of ordinary differential
	 * equations.
	 * 
	 * @param ode
	 *            the system of differential equations.
	 */
	public Euler(final ODE ode) {
		super(ode);
	}

	/**
	 * Initializes the ODE solver and allocates the rate array. The number of
	 * differential equations is determined by invoking getState().length on the
	 * superclass.
	 * 
	 * @param stepSize
	 */
	public void initialize(final double stepSize) {
		super.initialize(stepSize);
		rate = new double[numEqn];
	}

	/**
	 * Steps (advances) the differential equations by the stepSize.
	 * 
	 * The ODESolver invokes the ODE's getState method to obtain the initial
	 * state of the system. The ODESolver then advances the solution and copies
	 * the new state into the state array at the end of the solution step.
	 * 
	 * @return the step size
	 */
	public double step() {
		double[] state = ode.getState();
		ode.getRate(state, rate);
		for (int i = 0; i < numEqn; i++) {
			state[i] = state[i] + stepSize * rate[i];
		}
		return stepSize;
	}
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
