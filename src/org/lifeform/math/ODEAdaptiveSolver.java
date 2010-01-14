/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * ODEAdaptiveSolver extends the ODE solver to add adaptive step size
 * capabilities.
 * 
 * Adaptive ODE solvers adjust the step size until that the desired tolerance is
 * reached.
 * 
 * The client's state can effect the internal state of the ODE solver. Some
 * adaptive solvers advance an internal copy of client's state. This internal
 * state is then copied to the client after every step. Other solvers estimate
 * the optimal time step using the client's state. Clients should therfore
 * always invoke the solver's initialize method after setting their initial
 * conditions.
 * 
 * @author Wolfgang Christian
 */
public interface ODEAdaptiveSolver extends ODESolver {

	public static final int NO_ERROR = 0;
	public static final int DID_NOT_CONVERGE = 1;
	public static final int BISECTION_EVENT_NOT_FOUND = 2;

	/**
	 * Gets the error code. Error codes: ODEAdaptiveSolver.NO_ERROR
	 * ODEAdaptiveSolver.DID_NOT_CONVERGE
	 * ODEAdaptiveSolver.BISECTION_EVENT_NOT_FOUND=2;
	 * 
	 * @return int
	 */
	public int getErrorCode();

	/**
	 * Gets the tolerance of the adaptive ODE sovler.
	 * 
	 * @return
	 */
	public double getTolerance();

	/**
	 * Sets the tolerance of the adaptive ODE sovler.
	 * 
	 * @param tol
	 *            the tolerance
	 */
	public void setTolerance(final double tol);

}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
