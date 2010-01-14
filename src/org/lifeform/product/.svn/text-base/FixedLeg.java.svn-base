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
package org.lifeform.product;

import org.lifeform.time.DayCount;
import org.lifeform.time.Period;

public class FixedLeg extends Leg {
	private final DayCount dc;

	public FixedLeg(final double principal, final Period accrual, final DayCount dc) {
		super(principal, accrual);
		this.dc = dc;
	}

	public DayCount getDc() {
		return dc;
	}
}
