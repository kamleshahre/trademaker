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
package org.lifeform.market.vol;

import java.util.Calendar;
import java.util.Currency;
import java.util.Vector;

import org.lifeform.market.MarketItem;
import org.lifeform.product.Product;
import org.lifeform.time.Term;

public class Surface implements MarketItem {

	protected Vector<Term> tenors = new Vector<Term>();
	protected Vector<Product> underlyings = new Vector<Product>();

	@Override
	public Currency getCurrency() {
		return null;
	}

	@Override
	public Calendar getDate() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.Surface;
	}

	public double getVolatility(final Calendar date, final double strike) {
		return 0.5;
	}

}
