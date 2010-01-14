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
package org.lifeform.cashflow;

import java.util.Calendar;
import java.util.Currency;

public class FractionalDividend extends Dividend {
	private final double fraction;

	public FractionalDividend(final Calendar date, final double amount, final Currency currency,
			final double fraction) {
		super(date, amount, currency);
		this.fraction = fraction;
	}

	public double getFraction() {
		return fraction;
	}
}
