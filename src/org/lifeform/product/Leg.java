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

import java.util.Calendar;
import java.util.Currency;

import org.lifeform.time.Period;

public class Leg {
	protected int id;
	protected double principal;
	protected Period accrual;
	protected Currency currency;

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(final Currency currency) {
		this.currency = currency;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setPrincipal(final double principal) {
		this.principal = principal;
	}

	public void setAccrual(final Period accrual) {
		this.accrual = accrual;
	}

	public Leg() {

	}

	public Leg(final double principal, final Period accrual) {
		this.principal = principal;
		this.accrual = accrual;
	}

	public Period getAccrual() {
		return accrual;
	}

	public int getId() {
		return id;
	}

	public Calendar getMaturity() {
		return accrual.getEnd();
	}

	public double getPrincipal() {
		return principal;
	}

	public Calendar getStart() {
		return accrual.getStart();
	}

}
