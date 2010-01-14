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

import java.util.List;
import java.util.Vector;

public class Flows {
	List<CashFlow> flows = new Vector<CashFlow>();

	public List<CashFlow> getFlows() {
		return flows;
	}

	public void setFlows(final List<CashFlow> flows) {
		this.flows = flows;
	}

}
