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
package org.lifeform.cashflow;

import java.util.Calendar;
import java.util.Hashtable;

public class TimeBasket extends Hashtable<Calendar, double[]> {
	private static final long serialVersionUID = -2951021766025270912L;

	public TimeBasket() {
		super();
	}

	public TimeBasket(final Calendar[] dates, final double[][] values) {
		for (int i = 0; i < dates.length; ++i) {
			put(dates[i], values[i]);
		}
	}
}
