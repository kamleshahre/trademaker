/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Ralston2 implements Ralston's algorithm for solving ODEs by evaluating the
 * rate at the initial state, estimating the rate for the final state, and using
 * a weighted average of these two rates to advance the state.
 * 
 * @version 1.0
 */
public class Ralston2 extends AbstractODESolver {
	private double[] rate1, rate2, estimated_state;

	public Ralston2(final ODE ode) {
		super(ode);
	}

	/**
	 * Initializes the ODE solver and allocates the rate and state arrays. The
	 * number of differential equations is determined by invoking
	 * getState().length on the superclass.
	 * 
	 * @param stepSize
	 */
	public void initialize(final double stepSize) {
		super.initialize(stepSize);
		rate1 = new double[numEqn];
		rate2 = new double[numEqn];
		estimated_state = new double[numEqn];
	}

	/**
	 * Steps (advances) the differential equations by the stepSize.
	 * 
	 * The ODESolver invokes the ODE's getState method to obtain the initial
	 * state of the system. The ODESolver then uses this state estimate the rate
	 * a intermediate points. Finally, the ODESolver advances the solution and
	 * copies the new state into the state array at the end of the solution
	 * step.
	 * 
	 * @return the step size
	 */
	public double step() {
		double state[] = ode.getState();
		if (state.length != numEqn) {
			initialize(stepSize);
		}
		ode.getRate(state, rate1);
		for (int i = 0; i < numEqn; i++) {
			estimated_state[i] = state[i] + 3. / 4. * stepSize * rate1[i];
		}
		ode.getRate(estimated_state, rate2);
		for (int i = 0; i < numEqn; i++) {
			state[i] = state[i] + stepSize * (rate1[i] + 2. * rate2[i]) / 3.0;
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
