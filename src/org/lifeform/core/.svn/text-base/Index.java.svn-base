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
package org.lifeform.core;

import java.util.Currency;

import org.lifeform.staticdata.Country;

public class Index {
	private final Country country;
	private final Currency currency;
	private final String name;

	public Index(final String name, final Currency currency) {
		this(name, currency, Country.fromCurrency(currency));
	}

	public Index(final String name, final Currency currency, final Country country) {
		this.name = name;
		this.currency = currency;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public Country getCountry() {
		return country;
	}

	public Currency getCurrency() {
		return currency;
	}
}
