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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Holiday {
	private final String name;

	public Holiday(final String name) {
		this.name = name;
	}

	public List<Calendar> getHolidays(final Period period) {
		List<Calendar> holidays = new ArrayList<Calendar>();
		for (Calendar date = period.getStart(); date.before(period.getEnd()); date = DateUtil
				.addDays(date, 1)) {
			if (isHoliday(date)) {
				holidays.add(date);
			}
		}
		return holidays;
	}

	public String getName() {
		return name;
	}

	public boolean isHoliday(final Calendar date) {
		return false;
	}
}
