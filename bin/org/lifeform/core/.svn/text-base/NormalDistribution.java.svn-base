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
package org.lifeform.core;

public class NormalDistribution {
	public static double normal(final double seed) {
		double b1 = 0.319381530;
		double b2 = -0.356563782;
		double b3 = 1.781477937;
		double b4 = -1.821255978;
		double b5 = 1.330274429;
		double p = 0.2316419;
		double c = 0.39894228;

		if (seed >= 0.0) {
			double t = 1.0 / (1.0 + p * seed);
			return (1.0 - c * Math.exp(-seed * seed / 2.0) * t
					* (t * (t * (t * (t * b5 + b4) + b3) + b2) + b1));
		} else {
			double t = 1.0 / (1.0 - p * seed);
			return (c * Math.exp(-seed * seed / 2.0) * t * (t
					* (t * (t * (t * b5 + b4) + b3) + b2) + b1));
		}
	}
}
