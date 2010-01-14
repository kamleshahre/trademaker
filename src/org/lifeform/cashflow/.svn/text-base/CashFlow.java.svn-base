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

import org.lifeform.core.Event;
import org.lifeform.market.Market;

public class CashFlow implements Comparable<CashFlow>, Event {
	protected final double amount;
	protected final Calendar date;
	protected final Currency currency;

	public CashFlow(final Calendar date, final double amount, final Currency currency) {
		this.amount = amount;
		this.date = date;
		this.currency = currency;
	}

	@Override
	public int compareTo(final CashFlow other) {
		return getDate().compareTo(other.getDate());
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public Calendar getDate() {
		return date;
	}

	public Currency getCurrency() {
		return currency;
	}

	protected double calculate(final Market market) {
		double df = market.getDf(currency, date);
		return df * amount;
	}

}
