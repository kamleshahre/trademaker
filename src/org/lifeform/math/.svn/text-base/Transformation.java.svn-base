/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Transformation maps coordinates from one coordinate system to another.
 */
public interface Transformation extends Cloneable {

	/**
	 * Provides a copy of this transformation. This is used by an OSP 3D Element
	 * that will explicitely get a clone of it whenever its setTransformation()
	 * method is invoked. Thus, changing the original transformation directly
	 * has no effect unless a new setTransformation is invoked.
	 */
	public Object clone();

	/**
	 * Transforms a given point
	 * 
	 * @param point
	 *            double[] the coordinates to be transformed (the array's
	 *            contents will be changed accordingly)
	 * @return double[] the transformed vector (i.e. point)
	 */
	public double[] direct(final double[] point);

	/**
	 * The inverse transformation (if it exists). If the transformation is not
	 * invertible, then a call to this method must throw a
	 * UnsupportedOperationException exception.
	 * 
	 * @param point
	 *            double[] the coordinates to be transformed (the array's
	 *            contents will be changed accordingly)
	 * @return double[] the transformed vector (i.e. point)
	 * @throws UnsupportedOperationException
	 *             If the transformation is not invertible
	 */
	public double[] inverse(final double[] point)
			throws UnsupportedOperationException;
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
