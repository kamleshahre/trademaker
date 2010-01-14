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

public class Period {
	private final long start;
	private final long end;

	public Period(final Calendar start, final Calendar end) {
		this.start = start.getTimeInMillis();
		this.end = end.getTimeInMillis();
	}

	public Period(final Calendar start, final Term term) {
		this.start = start.getTimeInMillis();
		this.end = DateUtil.addTerm(start, term).getTimeInMillis();
	}

	public double getDays() {
		return DateUtil.getDaysBetween(start, end);
	}

	public double getDays(final DayCount dc) {
		return dc.getDays(start, end);
	}

	public Calendar getEnd() {
		Calendar endC = Calendar.getInstance();
		endC.setTimeInMillis(end);
		return endC;
	}

	public Calendar getStart() {
		Calendar startC = Calendar.getInstance();
		startC.setTimeInMillis(start);
		return startC;
	}

	public double getYears(final DayCount dc) {
		return dc.getYears(this);
	}

	public String toString() {
		return new StringBuffer("{").append(DateUtil.toString(getStart()))
				.append("-").append(DateUtil.toString(getEnd())).append("}")
				.toString();
	}
}
