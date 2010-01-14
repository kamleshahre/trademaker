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

import org.lifeform.time.Period;

public class Coupon extends CashFlow {
	private final Period accrual;
	private final Period reference;

	public Coupon(final Calendar date, final double amount, final Currency currency,
			final Period accrual) {
		this(date, amount, currency, accrual, accrual);
	}

	public Coupon(final Calendar date, final double amount, final Currency currency,
			final Period accrual, final Period reference) {
		super(date, amount, currency);
		this.accrual = accrual;
		this.reference = reference;
	}

	public Period getAccrual() {
		return accrual;
	}

	public Period getReference() {
		return reference;
	}

}
