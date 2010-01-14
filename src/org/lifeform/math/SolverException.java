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

public class SolverException extends RuntimeException {
	private static final long serialVersionUID = -4116271368474221875L;

	public SolverException() {
		super("ODE solver failed.");
	}

	public SolverException(final String msg) {
		super(msg);
	}
}
