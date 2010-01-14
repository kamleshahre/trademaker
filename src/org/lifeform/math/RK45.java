/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * RK45 implements an Runge-Kutta 4/5 ODE solver with variable step size.
 * 
 * This RK45 class may be changed in future versions of the OSP library. Version
 * 1.0 of the libarary uses Dormand-Prince coefficients.
 * 
 * @see DormandPrince45
 * @see CashKarp45
 * @version 1.0
 */
public class RK45 extends DormandPrince45 {
	public RK45(final ODE ode) {
		super(ode);
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
