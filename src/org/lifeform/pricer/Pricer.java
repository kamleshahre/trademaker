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

import java.util.Calendar;

import org.lifeform.excel.Workbook;
import org.lifeform.market.Market;
import org.lifeform.product.Product;

public abstract class Pricer {
	public abstract PricingResult price(final Product product,
			final Calendar valuationDate, final Market market);

	public Workbook exportToExcel() {
		return null;
	}
}
