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

import java.util.Arrays;

public class BrownianBridge {
	double[] leftIndex;
	double[] rightIndex;
	double[] bridgeIndex;
	double[] leftWeight;
	double[] rightWeight;
	double[] stddev;
	double[] normalVariates;
	MT mt = new MT();

	public BrownianBridge(final int numberOfSteps) {
		leftIndex = new double[numberOfSteps];
		rightIndex = new double[numberOfSteps];
		bridgeIndex = new double[numberOfSteps];
		leftWeight = new double[numberOfSteps];
		rightWeight = new double[numberOfSteps];
		stddev = new double[numberOfSteps];

		double[] map = new double[numberOfSteps];
		Arrays.fill(map, Double.NaN);
		int k, l;
		// map is used to indicated which points are already constructed. If
		// map[i] is
		// zero, path point i is yet unconstructed. map[i] - 1 is the index of
		// the
		// variate that constructs the path point i.
		map[numberOfSteps - 1] = 1; // the first point in the construction is
		// the global step
		bridgeIndex[0] = numberOfSteps - 1; // bridge index
		stddev[0] = Math.sqrt((numberOfSteps)); // the standard deviation of the
		// global
		// step
		leftWeight[0] = rightWeight[0] = 0; // global step to the last point in
		// time
		for (int j = 0, i = 0; i < numberOfSteps; ++i) {
			while (map[j] == Double.NaN) {
				++j; // find the next unpopulated entry in the
			}
			// map
			k = j;
			while (map[k] != Double.NaN)
				// find the next unpopulated entry in the
				++k; // map from there
			l = j + ((k - 1 - j) >> 1); // l is now the index of the point to
			// be constructed next
			map[l] = i;
			bridgeIndex[i] = l; // the ith gaussian variate to be used to set
			// point l
			leftIndex[i] = j; // point j-1 is the left strut of the bridge for
			// point l
			rightIndex[i] = k; // point k is the right strut of the bridge
			leftWeight[i] = (k - l) / (k + 1 - j);
			rightWeight[i] = (l + 1 - j) / (k + 1 - j);
			stddev[i] = Math.sqrt((((l + 1 - j) * (k - 1)) / (k + 1 - j)));
			j = k + 1;
			if (j >= numberOfSteps)
				j = 0; // wrap around
		}
	}

	/**********************************************************************************
	 * generateDeviates: generates a sequences of normal random deviates [in]
	 * numberOfSteps: number of steps per path (= number of deviates needed per
	 * path) [out] none
	 **********************************************************************************/
	public void generateDeviates(final int numberOfSteps) {
		for (int i = 0; i < numberOfSteps; i++) {
			double deviate = NormalDistribution.normal(mt.random());
			normalVariates[i] = deviate;
		}
	}
}
