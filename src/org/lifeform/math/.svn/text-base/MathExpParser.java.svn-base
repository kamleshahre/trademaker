/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

import org.lifeform.pricer.solver.Function;

/**
 * MathExpParser defines an abstract super class for mathematical expression
 * parsers.
 */
public abstract class MathExpParser implements Function, MultiVarFunction {

	/** No error. */
	public static final int NO_ERROR = 0;

	/** Syntax error. */
	public static final int SYNTAX_ERROR = 1;

	public static MathExpParser createParser() {
		return new SuryonoParser(0);
	}

	/**
	 * Gets the function string.
	 */
	public abstract String getFunction();

	/**
	 * Parses the function string using existing variable names.
	 * 
	 * @param funcStr
	 *            the function to be parsed
	 */
	public abstract void setFunction(final String funcStr) throws Exception;

	/**
	 * Parses the function string using existing variable names.
	 * 
	 * @param funcStr
	 *            the function to be parsed
	 * @param vars
	 *            the function's variables
	 */
	public abstract void setFunction(final String funcStr, final String[] vars)
			throws Exception;
}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
