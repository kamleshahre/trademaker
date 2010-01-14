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

public interface MarketItem {
	public enum Type {
		Quote, Curve, Surface, FX, Correlation
	}

	Currency getCurrency();

	Calendar getDate();

	String getId();

	Type getType();
}
