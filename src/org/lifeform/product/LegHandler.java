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
import java.util.List;

import org.lifeform.time.DateUtil;

public class LegHandler {
	public static Calendar getStartDate(final List<Leg> legs) {
		Calendar result = DateUtil.getNullDate();
		for (Leg leg : legs) {
			if (leg.getStart().before(result)) {
				result = leg.getStart();
			}
		}
		return result;
	}

	public static Calendar getEndDate(final List<Leg> legs) {
		Calendar result = DateUtil.getNullDate();
		for (Leg leg : legs) {
			if (leg.getMaturity().after(result)) {
				result = leg.getMaturity();
			}
		}
		return result;
	}

}
