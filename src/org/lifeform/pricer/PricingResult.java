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
package org.lifeform.pricer;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import org.lifeform.product.Product;
import org.lifeform.time.DateUtil;

public class PricingResult {
	public enum Type {
		Price, CashSettlement, NPV, Delta, Gamma, Vega, Rho, Yield
	}

	Map<Type, Object> data = new Hashtable<Type, Object>();
	final Product product;
	final long resultDate;

	public PricingResult(final Product product, final Calendar valTime) {
		this.product = product;
		this.resultDate = valTime.getTimeInMillis();
	}

	public void put(final Type name, final Object val) {
		data.put(name, val);
	}

	public Map<Type, Object> getData() {
		return data;
	}

	public Product getProduct() {
		return product;
	}

	public Calendar getResultDate() {
		return DateUtil.getDate(resultDate);
	}

	public void dump(final PrintStream out) {
		out.print(data);
	}

	public String toString() {
		return new StringBuffer(product.getDescription()).append("-").append(
				DateUtil.toDateString(resultDate)).append(data).toString();
	}
}
