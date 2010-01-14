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

import org.lifeform.time.DayCount;
import org.lifeform.time.Period;

public class FixedRateCoupon extends Coupon {
	private final DayCount dayCount;

	public FixedRateCoupon(final Calendar date, final double amount, final Currency currency,
			final Period accrual, final DayCount dc) {
		super(date, amount, currency, accrual);
		dayCount = dc;
	}

	public FixedRateCoupon(final Calendar date, final double amount, final Currency currency,
			final Period accrual, final Period refPeriod, final DayCount dc) {
		super(date, amount, currency, accrual, refPeriod);
		dayCount = dc;
	}

	public DayCount getDayCount() {
		return dayCount;
	}

}
