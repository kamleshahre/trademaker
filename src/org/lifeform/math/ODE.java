package org.lifeform.math;

public interface ODE {

	/**
	 * Gets the rate of change using the argument's state variables.
	 * 
	 * This method may be invoked many times with different intermediate states
	 * as an ODESolver is carrying out the solution.
	 * 
	 * @param state
	 *            the state array
	 * @param rate
	 *            the rate array
	 */
	public void getRate(final double[] state, final double[] rate);

	/**
	 * Gets the state variables.
	 * 
	 * The getState method is invoked by an ODESolver to obtain the initial
	 * state of the system. The ODE solver advances the solution and then copies
	 * new values into the state array at the end of the solution step.
	 * 
	 * @return state the state
	 */
	public double[] getState();
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
