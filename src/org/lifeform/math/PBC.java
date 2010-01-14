/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Implements methods to support periodic boundary condtions.
 * 
 * @author W. Christian
 */
public class PBC {
	/**
	 * Adjusts the postion of a particle assuming peridoic boundary conditions.
	 * The postion will be in the interval [0,size).
	 * 
	 * @param r
	 *            double
	 * @param size
	 *            double
	 * @return double
	 */
	public static double position(final double r, final double size) {
		return r < 0 ? r % size + size : r % size;
	}

	/**
	 * Adjusts the postion of a particle assuming peridoic boundary conditions.
	 * The postion will be in the interval [0,size).
	 * 
	 * @param r
	 *            int
	 * @param size
	 *            int
	 * @return int
	 */
	public static int position(final int r, final int size) {
		return r < 0 ? (r + 1) % size + size - 1 : r % size;
	}

	/**
	 * Computes the minimum separation using periodic boundary conditions.
	 * 
	 * @param dr
	 *            double the separation
	 * @param size
	 *            double the box size
	 * @return double
	 */
	public static double separation(final double dr, final double size) {
		return dr - size * Math.floor(dr / size + 0.5);
	}

	/**
	 * Computes the minimum separation using periodic boundary conditions.
	 * 
	 * @param dr
	 *            int the separation
	 * @param size
	 *            int the box size
	 * @return int
	 */
	public static int separation(final int dr, final int size) {
		if (dr < 0) {
			return dr + size * ((-2 * dr + size) / (2 * size));
		}
		return dr - size * ((2 * dr + size - 1) / (2 * size));
	}

	private PBC() {
	} // prohibit instantiation
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
