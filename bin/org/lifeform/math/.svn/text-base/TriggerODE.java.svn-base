/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * TriggerODE is a wrapper for an ODE that can be used by ODEEventSolvers. It
 * acts as a mediator between the ODE and the solver The ODE state array must
 * have fixed length The ODESolver must call getState only once
 * 
 * @author Francisco Esquembre (March 2004)
 */
class TriggerODE implements ODE {
	protected ODE ode;
	private int size;
	private double[] odestate;
	private double[] state;

	public TriggerODE(final ODE _ode) {
		ode = _ode;
		odestate = ode.getState();
		size = odestate.length;
		state = new double[size];
		System.arraycopy(odestate, 0, state, 0, size);
	}

	public void getRate(final double[] _state, final double[] _rate) {
		ode.getRate(_state, _rate); // Defer to the real ODE
	}

	// Masking the real ODE
	public double[] getState() {
		return state;
	}

	// Interface with the real ODE
	public void readRealState() {
		odestate = ode.getState();
		System.arraycopy(odestate, 0, state, 0, size);
	}

	public void setState(final double[] newstate) {
		System.arraycopy(newstate, 0, state, 0, size);
	}

	public void updateRealState() {
		System.arraycopy(state, 0, odestate, 0, size);
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
