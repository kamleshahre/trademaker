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
package org.lifeform.staticdata;

import java.util.Currency;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;

public class Country {
	public static Country fromCurrency(final Currency currency) {
		return null;
	}

	public static Vector<Country> getDomain() {
		Locale[] locales = Locale.getAvailableLocales();
		TreeMap<String, String> countriesMap = new TreeMap<String, String>();
		for (Locale locale : locales) {
			String countryName = locale.getDisplayCountry();
			if (countryName.length() > 0) {
				String countryCode = locale.getCountry();
				countriesMap.put(countryName, countryCode);
			}
		}
		Vector<Country> countries = new Vector<Country>();
		for (String countryName : countriesMap.keySet()) {
			String countryCode = countriesMap.get(countryName);
			Country c = new Country(countryName, countryCode);
			countries.add(c);
		}
		return countries;
	}

	private final String name;

	private final String code;

	public Country(final String code, final String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
