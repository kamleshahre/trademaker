/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

import org.lifeform.pricer.solver.Function;

/**
 * ParsedFunction defines a function of a single varianble using a String.
 * 
 * This function is immutable. That is, once an instance is created with a
 * particular function string, the function cannot be changed. Because immutable
 * objects cannot change, they are thread safe and can be freely shared in a
 * Java program.
 * 
 * @author Wolfgang Christian
 */
public final class ParsedFunction implements Function {
	private final String fStr;
	private final Function function;

	/**
	 * Constructs a function x with from the given string.
	 * 
	 * @param fStr
	 *            the function
	 */
	public ParsedFunction(final String fStr) throws Exception {
		this(fStr, "x");
	}

	/**
	 * Constructs a ParsedFunction from the given string and independent
	 * variable.
	 * 
	 * @param _fStr
	 *            the function
	 * @param var
	 *            the independent variable
	 */
	public ParsedFunction(final String _fStr, final String var) throws Exception {
		fStr = _fStr;
		SuryonoParser parser = null;
		parser = new SuryonoParser(fStr, var);
		function = parser;
	}

	/**
	 * Evaluates the function, f.
	 * 
	 * @param x
	 *            the value of the independent variable
	 * 
	 * @return the value of the function
	 */
	public double evaluate(final double x) {
		return function.evaluate(x);
	}

	/**
	 * Represents the function as a string.
	 * 
	 * @return the string
	 */
	public String toString() {
		return "f(x) = " + fStr;
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
