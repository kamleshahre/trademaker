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
 * <p>
 * A factory class that creates an ODESolver using a name.
 * </p>
 * 
 * @author W. Christian
 * @version 1.0
 */
public class ODESolverFactory {
	/**
	 * A factory method that creates an ODESolver using a name.
	 * 
	 * @param ode
	 *            ODE
	 * @param solverName
	 *            String the name of the algorithm
	 * @return ODESolver
	 */
	public static ODESolver createODESolver(ODE ode, String solverName) {
		solverName = solverName.trim().toLowerCase();
		if (solverName.equals("rk4")) {
			return new RK4(ode);
		} else if (solverName.equals("multistep")) {
			return new ODEMultistepSolver(ode);
		} else if (solverName.equals("adams4")) {
			return new Adams4(ode);
		} else if (solverName.equals("adams5")) {
			return new Adams5(ode);
		} else if (solverName.equals("adams6")) {
			return new Adams6(ode);
		} else if (solverName.equals("butcher5")) {
			return new Butcher5(ode);
		} else if (solverName.equals("cashkarp45")) {
			return new CashKarp45(ode);
		} else if (solverName.equals("dormandprince45")) {
			return new DormandPrince45(ode);
		} else if (solverName.equals("eulerrichardson")) {
			return new EulerRichardson(ode);
		} else if (solverName.equals("euler")) {
			return new Euler(ode);
		} else if (solverName.equals("fehlberg8")) {
			return new Fehlberg8(ode);
		} else if (solverName.equals("heun3")) {
			return new Heun3(ode);
		} else if (solverName.equals("ralston2")) {
			return new Ralston2(ode);
		} else if (solverName.equals("verlet")) {
			return new Verlet(ode);
		} else
			return null;
	}

	private ODESolverFactory() {
	}

}
