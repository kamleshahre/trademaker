/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * ODE defines a minimal differential equation solver.
 * 
 * @author Wolfgang Christian
 */
public interface ODESolver {

	/**
	 * Gets the step size.
	 * 
	 * @return the step size
	 */
	public double getStepSize();

	/**
	 * Initializes the ODE solver.
	 * 
	 * ODE solvers use this method to allocate temporary arrays that may be
	 * required to carry out the solution. The number of differential equations
	 * is determined by invoking getState().length on the ODE.
	 * 
	 * @param stepSize
	 */
	public void initialize(final double stepSize);

	/**
	 * Sets the initial step size.
	 * 
	 * The step size may change if the ODE solver implements an adaptive step
	 * size algorithm such as RK4/5.
	 * 
	 * @param stepSize
	 */
	public void setStepSize(final double stepSize);

	/**
	 * Steps (advances) the differential equations by the stepSize.
	 * 
	 * The ODESolver invokes the ODE's getRate method to obtain the initial
	 * state of the system. The ODESolver then advances the solution and copies
	 * the new state into the state array at the end of the solution step.
	 * 
	 * @return the step size
	 */
	public double step();
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
