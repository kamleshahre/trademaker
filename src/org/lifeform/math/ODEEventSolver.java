/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * ODEEventSolver is an interface for a Solver for ODE which accepts and deals
 * with StateEvents
 * 
 * @author Francisco Esquembre (March 2004)
 */
public interface ODEEventSolver extends ODESolver {

	/**
	 * Adds a StateEvent to the list of events
	 * 
	 * @param event
	 *            The event to be added
	 */
	public void addEvent(final StateEvent event);

	/**
	 * Removes a StateEvent from the list of events
	 * 
	 * @param event
	 *            The event to be removed
	 */
	public void removeEvent(final StateEvent event);
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
