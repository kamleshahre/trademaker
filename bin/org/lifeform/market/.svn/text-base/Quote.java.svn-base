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

public class Quote {
	public enum Type {
		Yield, Price, Future, Spread, Average
	}

	protected Type type;
	protected double value = 0;
	protected long date = 0;

	public Quote(final Type type, final double price) {
		this.type = type;
		this.value = price;
	}

	public long getDate() {
		return date;
	}

	public void setDate(final long date) {
		this.date = date;
	}

	public void setValue(final double value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(final Type type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(final float value) {
		this.value = value;
	}
}
