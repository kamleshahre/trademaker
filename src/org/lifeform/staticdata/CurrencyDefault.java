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

public final class CurrencyDefault extends StaticData {
	private final Currency currency;
	public final int spotDays;

	private CurrencyDefault(final Currency currency, final int spotDays) {
		this.currency = currency;
		this.spotDays = spotDays;
	}

	public Currency getCurrency() {
		return currency;
	}

	public int getSpotDays() {
		return spotDays;
	}
}
