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
package org.lifeform.credit;

import java.util.ArrayList;
import java.util.Hashtable;

public class FactorAllocation {
	public enum FactorAllocationType {
		Factor, Entity
	}

	private static ArrayList<FactorAllocation> allThoseInstances;

	public static void translate() {
		for (int i = 0; i <= allThoseInstances.size(); i++) {
			FactorAllocation myCurrentFactor = allThoseInstances.get(i);
			for (String myKey : myCurrentFactor.obviousLoads.keySet()) {
				if (myKey.toUpperCase() == "SPEC") {
					myCurrentFactor.addHidden("SPEC", 0,
							myCurrentFactor.obviousLoads.get(myKey));
				} else {
				}
			}

		}
	}

	public Hashtable<String, Double> obviousLoads;

	public Hashtable<String, Double> hiddenLoadsByName;

	public Hashtable<Integer, Double> hiddenLoadsByNr;

	FactorAllocationType type;

	public FactorAllocation(final String theName, final String theEQN,
			final FactorAllocation.FactorAllocationType theType) {
		this.type = theType;
		String[] myEntry;
		this.obviousLoads = new Hashtable<String, Double>();
		this.hiddenLoadsByName = new Hashtable<String, Double>();
		this.hiddenLoadsByNr = new Hashtable<Integer, Double>();
		String[] myRightSide = theEQN.split("=");
		String[] myPairs = myRightSide[1].split("+");
		for (String myPair : myPairs) {
			myEntry = myPair.split("*");
			myEntry[0] = myEntry[0].trim();
			myEntry[1] = myEntry[1].trim();
			this.addObvious(myEntry[1], Double.valueOf(myEntry[0]));
		}
		FactorAllocation.allThoseInstances.add(this);
	}

	public void addHidden(final String theFactorName, final int theNr, final double theBeta) {
		if (hiddenLoadsByName.containsKey(theFactorName)) {
			hiddenLoadsByName.put(theFactorName, theBeta
					+ (double) hiddenLoadsByName.get(theFactorName));
		} else {
			hiddenLoadsByName.put(theFactorName, theBeta);
		}
		if (hiddenLoadsByNr.containsKey(theNr)) {
			hiddenLoadsByNr.put(theNr, theBeta + hiddenLoadsByNr.get(theNr));
		} else {
			hiddenLoadsByNr.put(theNr, theBeta);
		}

	}

	public void addObvious(final String theFactorName, final double theBeta) {
		if (obviousLoads.containsKey(theFactorName)) {
			this.obviousLoads.put(theFactorName, theBeta
					+ (double) this.obviousLoads.get(theFactorName));
		} else {
			this.obviousLoads.put(theFactorName, theBeta);
		}
	}
}
