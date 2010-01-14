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

import org.lifeform.core.Log;

public class Term {
	public enum Type {
		D, W, M, Q, SA, Y
	}

	int multiplier;
	Type type;

	public Term(final int multiplier, final Term.Type type) {
		this.multiplier = multiplier;
		this.type = type;
	}

	public String toString() {
		return Integer.valueOf(multiplier) + "" + type;
	}

	public Calendar addTo(final Calendar start) {
		switch (type) {
		case D: {
			return DateUtil.addDays(start, multiplier);
		}
		case W: {
			return DateUtil.addDays(start, multiplier * 7);
		}
		case M: {
			return DateUtil.addMonths(start, multiplier);
		}
		case Q: {
			return DateUtil.addMonths(start, multiplier * 3);
		}
		case SA: {
			return DateUtil.addMonths(start, multiplier * 6);
		}
		case Y: {
			return DateUtil.addYears(start, multiplier);
		}
		default: {
			Log.log(Log.Type.ERROR, "Invalid Term");
			return start;
		}
		}
	}
}
