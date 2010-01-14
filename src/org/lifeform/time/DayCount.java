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
package org.lifeform.time;

import java.util.Calendar;

public class DayCount {
	enum Type {
		ACTUAL, T_30_360, GERMAN, GERMAN_SPECIAL, ENGLISH, FRENCH, US, ITALIAN, ISMA_YEAR, ISMA99_NORMAL, ISMA99_ULTIMO
	}

	final Type type;
	public DayCount() {
		this(Type.ACTUAL);
	}

	public DayCount(final DayCount.Type type) {
		this.type = type;
	}

	public static final DayCount ACTUAL = new DayCount(Type.ACTUAL);

	public double getDays(final Calendar start, final Calendar end) {
		return DateUtil.getDaysBetween(start, end);
	}

	public double getDays(final long start, final long end) {
		return DateUtil.getDaysBetween(start, end);
	}

	public double getYears(final Period period) {
		double days = period.getDays();
		double daysInAyear = getYearDays();
		switch (type) {
		case T_30_360: {
			
		}
		default: {
			return days / daysInAyear;
		}
		}
	}

	public double getYearDays() {
		return 365.0;
	}
}
