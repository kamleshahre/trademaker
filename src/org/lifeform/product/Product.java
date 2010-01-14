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
package org.lifeform.product;

import java.util.List;
import java.util.Vector;

import org.lifeform.cashflow.CashFlow;
import org.lifeform.cashflow.Flows;
import org.lifeform.core.Index;
import org.lifeform.market.Quote;
import org.lifeform.time.DayCount;

public class Product extends Leg {

	Index index = null;
	DayCount dayCount = DayCount.ACTUAL;
	Quote price = null;
	LongShort longShort = LongShort.Long;

	public LongShort getLongShort() {
		return longShort;
	}

	public void setLongShort(final LongShort longShort) {
		this.longShort = longShort;
	}

	public Product(final int id) {
		this.id = id;
	}

	public ProductIdentity getProductIdentity() {
		return new ProductIdentity(id);
	}

	public String getDescription() {
		StringBuffer buf = new StringBuffer().append(getType());
		// .append(" ")
		// .append(getStartDate())
		// .append(" ")
		// .append(getEndDate());
		return buf.toString();
	}

	public String getType() {
		return getClass().getSimpleName();
	}

	public DayCount getDayCount() {
		return dayCount;
	}

	public void setDayCount(final DayCount dayCount) {
		this.dayCount = dayCount;
	}

	public void setIndex(final Index index) {
		this.index = index;
	}

	public void setPrice(final Quote price) {
		this.price = price;
	}

	public String getSubType() {
		return null;
	}

	public Index getIndex() {
		return index;
	}

	public Quote getPrice() {
		return price;
	}

	public double time() {
		return accrual.getYears(dayCount);
	}

	public Flows getFlows() {
		List<CashFlow> flows = new Vector<CashFlow>();
		Flows f = new Flows();
		f.setFlows(flows);
		return f;
	}
}
