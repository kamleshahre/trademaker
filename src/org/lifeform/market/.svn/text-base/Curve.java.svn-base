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
import java.util.List;
import java.util.Vector;

public class Curve implements MarketItem {
	String id;
	private final String name;
	private final Currency currency;
	private final Calendar date;

	private final List<CurvePoint> points;

	public Curve(final String name, final Currency currency, final Calendar date) {
		this.name = name;
		this.currency = currency;
		this.date = date;
		points = new Vector<CurvePoint>();
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getDF(final Calendar date) {
		return .9;
	}

	public double getForwardRate(final Calendar date) {
		return 3.0d;
	}

	public String getId() {
		return id;
	}

	public Calendar getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public List<CurvePoint> getPoints() {
		return points;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public Type getType() {
		return Type.Curve;
	}

}
