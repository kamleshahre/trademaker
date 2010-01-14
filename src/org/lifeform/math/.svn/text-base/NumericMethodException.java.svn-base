/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Indicates that an error occured in a numeric method.
 */
public final class NumericMethodException extends RuntimeException {
	private static final long serialVersionUID = -6325322489843487893L;

	/** Field error_value stores an optional numeric error. */
	public double error_value;

	/** Field error_code sotes an optional error code */
	public int error_code;

	/**
	 * Constructs a <code>RuntimeException</code> with no detail message.
	 */
	public NumericMethodException() {
		super();
	}

	/**
	 * Constructs a <code>RuntimeException</code> with the specified detail
	 * message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public NumericMethodException(final String msg) {
		super(msg);
	}

	/**
	 * Constructs a <code>RuntimeException</code> with the specified detail
	 * message, error code, and error estimate.
	 * 
	 * @param msg
	 * @param code
	 * @param val
	 */
	public NumericMethodException(final String msg, final int code, final double val) {
		super(msg);
		error_code = code;
		error_value = val;
	}

	/**
	 * Returns the error message string of this throwable object.
	 * 
	 * 
	 */
	public String getMessage() {
		return super.getMessage() + "\n error code=" + error_code
				+ "  error value=" + error_value;
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
