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
package org.lifeform.market;

import java.util.Calendar;
import java.util.Currency;
import java.util.concurrent.ConcurrentHashMap;

public class Market extends ConcurrentHashMap<String, MarketItem> {
	private static final long serialVersionUID = -5313379538732049873L;

	private final String name;
	private final Calendar date;

	public Market(final String name, final Calendar date) {
		this.name = name;
		this.date = date;
	}

	public Calendar getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public Curve getDiscountCurve(final Currency currency) {
		return (Curve) get("Discount." + currency.getCurrencyCode());
	}

	public Curve getForcastCurve(final String currency) {
		return (Curve) get("Forecast ." + currency);
	}

	public double getDf(final Currency currency, final Calendar date) {
		Curve c = getDiscountCurve(currency);
		return c.getDF(date);
	}

	public double getForwardRate(final Currency currency, final Calendar fixing) {
		Curve c = getDiscountCurve(currency);
		return c.getForwardRate(date);
	}

}
