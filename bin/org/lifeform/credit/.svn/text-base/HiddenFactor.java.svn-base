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
package org.lifeform.credit;

public class HiddenFactor extends Factor {
	public double SimuWert;

	public HiddenFactor(final String name, final double num) {
		super(name, -1, num);
	}

	public double Simulate() {
		return RandomStream.Normal01();
	}
}
