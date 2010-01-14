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

public class Position {
	private final LongShort direction;
	private final double quantity;

	public Position(final double quantity) {
		this(quantity, LongShort.Long);
	}

	public Position(final double quantity2, final LongShort long1) {
		this.quantity = quantity2;
		this.direction = long1;
	}

	public LongShort getDirection() {
		return direction;
	}

	public double getQuantity() {
		return quantity;
	}

}
