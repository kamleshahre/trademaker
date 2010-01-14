/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * An Euler-Richardson (midpoint) method ODE solver.
 * 
 * The Euler-Richardson method uses the state at the beginning of the interval
 * to estimate the state at the midpoint.
 * 
 * x(midpoint) = x(n) + v(n)*dt/2 v(midpoint) = v(n) + a(n)*dt/2 t(midpoint) =
 * t(n) + dt/2
 * 
 * The midpoint state is then used to calculate the final state.
 * 
 * @author Wolfgang Christian
 * @version 1.0
 */
public class EulerRichardson extends AbstractODESolver {
	private double[] rate; // array that stores the rate
	private double[] midstate; // midpoint

	/**
	 * Constructs the EulerRichardson ODESolver for a system of ordinary
	 * differential equations.
	 * 
	 * @param ode
	 *            the system of differential equations.
	 */
	public EulerRichardson(final ODE ode) {
		super(ode);
	}

	/**
	 * Initializes the ODE solver.
	 * 
	 * The rate and midstate arrays are allocated. The number of differential
	 * equations is determined by invoking getState().length on the ODE.
	 * 
	 * @param stepSize
	 */
	public void initialize(final double stepSize) {
		super.initialize(stepSize);
		rate = new double[numEqn];
		midstate = new double[numEqn];
	}

	/**
	 * Steps (advances) the differential equations by the stepSize.
	 * 
	 * The ODESolver invokes the ODE's getState method to obtain the initial
	 * state of the system. The ODESolver advances the solution and copies the
	 * new state into the state array at the end of the solution step.
	 * 
	 * @return the step size
	 */
	public double step() {
		double[] state = ode.getState();
		ode.getRate(state, rate); // get the rate at the start
		double dt2 = stepSize / 2;
		for (int i = 0; i < numEqn; i++) {
			// estimate the state at the midpoint
			midstate[i] = state[i] + rate[i] * dt2;
		}
		ode.getRate(midstate, rate); // get the rate at the midpoint
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
