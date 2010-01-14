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
package org.lifeform.math;

/**
 * AbstractODE provides a common superclass for ODESolvers.
 */
public abstract class AbstractODESolver extends Object implements ODESolver {
	protected double stepSize = 0.1; // parameter increment such as delta time
	protected int numEqn = 0; // number of equations
	protected ODE ode; // object that computes rate

	/**
	 * Constructs the ODESolver for a system of ordinary differential equations.
	 * 
	 * @param _ode
	 *            the system of differential equations.
	 */
	public AbstractODESolver(final ODE _ode) {
		ode = _ode;
		initialize(0.1);
	}

	/**
	 * Gets the step size.
	 * 
	 * The step size is constant in this algorithm
	 * 
	 * @return the step size
	 */
	public double getStepSize() {
		return stepSize;
	}

	/**
	 * Initialises the ODE solver.
	 * 
	 * The rate array is allocated. The number of differential equations is
	 * determined by invoking getState().length on the ODE.
	 * 
	 * @param _stepSize
	 */
	public void initialize(final double _stepSize) {
		stepSize = _stepSize;
		double state[] = ode.getState();
		if (state == null) { // state vector not defined
			numEqn = 0;
		} else {
			numEqn = state.length;
		}
	}

	/**
	 * Sets the step size.
	 * 
	 * The step size remains fixed in this algorithm
	 * 
	 * @param _stepSize
	 */
	public void setStepSize(final double _stepSize) {
		stepSize = _stepSize;
	}

	/**
	 * Steps (advances) the differential equations by the stepSize.
	 * 
	 * The ODESolver invokes the ODE's getRate method to obtain the initial
	 * state of the system. The ODESolver then advances the solution and copies
	 * the new state into the state array at the end of the solution step.
	 * 
	 * @return the step size
	 */
	abstract public double step();

}
