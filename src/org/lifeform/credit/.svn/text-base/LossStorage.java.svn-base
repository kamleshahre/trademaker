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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Simulated losses of the portfolio are stored, can be sorted and be used in a
 * distribution function
 * 
 */
public class LossStorage {
	private ArrayList<Double> sortedList;
	private ArrayList<Double> sortedListInfo;
	private ArrayList<Double> unsortedList;
	private ArrayList<Double> unsortedListInfo;
	private final double LGDMid_Sum;
	public double Summe;
	public double QSumme;

	public double D;
	public double D_50; // D from kolmogorov-smirnov vs. vasicek distribution
	public double D_90;
	public double D_99;

	public LossStorage(final double theEL, final double theLGDSum) {
		sortedList = new ArrayList<Double>();
		sortedListInfo = new ArrayList<Double>();
		unsortedList = new ArrayList<Double>();
		unsortedListInfo = new ArrayList<Double>();
		LGDMid_Sum = theLGDSum;
		Summe = 0.0;
		QSumme = 0.0;
	}

	public void CalculateDelta(final LossStorage theCont,
			final LossStorage theDisc, final double theExposure,
			final String thePath2Write2, final double[] theMeasures2print) { // prints
		// statistics
		// for
		// CMC
		// and
		// DMC
		// ratios to the file thePath2write2

		BufferedWriter myFile;
		try {
			myFile = new BufferedWriter(new FileWriter(thePath2Write2));
			double myPortfolioRisk;
			double myDMC;
			double myCMC;

			myFile
					.write("RiskMeasure; Level; Pf_Risk; exp; DMC; CMC; DMC_Q%; CMC_Q% ;  DMC_PT% ; CMC_PT% \n");
			for (int i = 0; i <= theMeasures2print.length; i++) {
				// 1. VaR
				myPortfolioRisk = this.Quantil(theMeasures2print[i]);
				myDMC = myPortfolioRisk - theDisc.Quantil(theMeasures2print[i]);
				myCMC = 100.0 * (myPortfolioRisk - theCont
						.Quantil(theMeasures2print[i]));

				myFile.write("Q   ;" + theMeasures2print[i] + myPortfolioRisk
						+ ";" + theExposure + ";" + myDMC + ";" + myCMC + ";"
						+ 100.0 * myDMC / theExposure + ";" + 100.0 * myCMC
						/ theExposure + ";" + 100.0 * myDMC / myPortfolioRisk
						+ ";" + 100.0 * myCMC / myPortfolioRisk);

				// 2. ESh
				myPortfolioRisk = this.ExpectedShortfall(theMeasures2print[i]);
				myDMC = myPortfolioRisk
						- theDisc.ExpectedShortfall(theMeasures2print[i]);
				myCMC = 100.0 * (myPortfolioRisk - theCont
						.ExpectedShortfall(theMeasures2print[i]));
				myFile.write("ESh   ;" + theMeasures2print[i] + myPortfolioRisk
						+ ";" + theExposure + ";" + myDMC + ";" + myCMC + ";"
						+ 100.0 * myDMC / theExposure + ";" + 100.0 * myCMC
						/ theExposure + ";" + 100.0 * myDMC / myPortfolioRisk
						+ ";" + 100.0 * myCMC / myPortfolioRisk);

			}

			myFile.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double ExpectedShortfall(final double theNiveau) {
		final double myQ = this.Quantil(theNiveau);

		double mySum = 0.0;
		double myX = 0.0;
		int myHowMany = 0;

		for (int i = this.sortedList.size() - 1; i >= 0; i--) {
			myX = sortedList.get(i);
			if (myX < myQ) {
				break;
			}
			mySum += myX;
			myHowMany++;
		}
		return (mySum / myHowMany);
	}

	public double ExpectedValueSimu() {
		final int wieViele = this.sortedList.size() + this.unsortedList.size();
		return (this.Summe / wieViele);
	}

	public Object GetAInfo(final int i) {
		return sortedListInfo.get(i);
	}

	public double GetValue(final int i) {
		return sortedList.get(i);
	}

	public long MaxIndex() {
		return Long.valueOf(sortedList.size() - 1);
	}

	public double Probability(final double theLoss) { // returns the Probability
		// of
		// losses less or equal theLoss
		final double myAbsLoss = theLoss * this.LGDMid_Sum;
		if (sortedList.size() > 1) {
			// find the Loss
			if (myAbsLoss < Double.valueOf(sortedList.get(0))) {
				return 0.0;
			}
			if (myAbsLoss > Double.valueOf(sortedList
					.get(sortedList.size() - 1))) {
				return 1.0d;
			}
			final double mysize = sortedList.size();
			for (int i = 1; i < sortedList.size(); i++) {
				if (myAbsLoss < Double.valueOf(sortedList.get(i))) {
					return Double.valueOf(i) / mysize;
				}

			}
			return -1.0; // cannot get here
		} else {
			return -1.0;
		}
	}

	public double Quantil(double theNiveau) { // returns the theNiveau-quantile
		if (sortedList.size() > 1) {
			if (theNiveau > 1.0) {
				theNiveau *= 0.01;
			}
			final double myPos = (theNiveau * sortedList.size());
			final int iHigh = (int) Math.ceil(myPos);
			final int iLow = (int) Math.floor(myPos);
			final double myFrac = myPos - iLow;
			if (iHigh >= sortedList.size()) {
				return -1.0;
			} else {
				return (myFrac * (sortedList.get(iHigh)) + (1.0 - myFrac)
						* (sortedList.get(iLow)));
			}
		} else {
			return (-1.0 / 0.0);
			// throw ...
		}
	}

	public void ReOrganise() {
		final ArrayList<Double> myResultList = new ArrayList<Double>();
		final ArrayList<Double> myResultListInfo = new ArrayList<Double>();
		if (unsortedList.size() <= 0) {// leer
			return;
		}
		SortUList(); // the unsortedLists are now sorted...

		int mySPosition = 0;
		int myUPosition = 0;
		double mySVal = 0.0;
		double myUVal = 0.0;
		int myIndex = 0;

		do {
			if (mySPosition >= sortedList.size()) {
				// just append unsorted list + ready
				for (int i = myUPosition; i < unsortedList.size(); i++) {
					myResultList.add(unsortedList.get(i));
					myResultListInfo.add(unsortedListInfo.get(i));
					myIndex++;
				}
				break;
			}
			if (myUPosition >= unsortedList.size()) {
				// just append sorted list + ready
				for (int i = mySPosition; i < sortedList.size(); i++) {
					myResultList.add(sortedList.get(i));
					myResultListInfo.add(sortedListInfo.get(i));
					myIndex++;
				}
				break;
			}
			mySVal = sortedList.get(mySPosition);
			myUVal = unsortedList.get(myUPosition);
			if (mySVal > myUVal) {
				myResultList.add(unsortedList.get(myUPosition));
				myResultListInfo.add(unsortedListInfo.get(myUPosition));
				myUPosition++;
			} else {
				myResultList.add(sortedList.get(mySPosition));
				myResultListInfo.add(sortedListInfo.get(mySPosition));
				mySPosition++;
			}
			myIndex++;

		} while (true);
		sortedList = myResultList;
		sortedListInfo = myResultListInfo;
		unsortedList = new ArrayList<Double>();
		unsortedListInfo = new ArrayList<Double>();
	}

	public void ReOrganise_old() {
		SortUList(); // the unsortedLists are now sorted...

		for (int i = 0; i < unsortedList.size(); i++) {
			final int myWhere = where2add(unsortedList.get(i));
			if (myWhere == sortedList.size()) {
				sortedList.add(unsortedList.get(i));
				sortedListInfo.add(unsortedListInfo.get(i));
			} else {
				sortedList.add(myWhere, unsortedList.get(i));
				sortedListInfo.add(myWhere, unsortedListInfo.get(i));
			}
		}
		unsortedList = new ArrayList<Double>();
		unsortedListInfo = new ArrayList<Double>();
	}

	private void SortUList() {
		if (unsortedList.size() > 0) {
			SortUListFT(0, unsortedList.size() - 1);
		}

	}

	private void SortUListFT(final int theLo, final int theHi) {
		int myI = theLo;
		int myJ = theHi;
		final int x = (theLo + theHi) / 2;
		final double myX = unsortedList.get(x);
		do {
			while ((double) unsortedList.get(myI) < myX) {
				myI++;
			}
			while ((double) unsortedList.get(myJ) > myX) {
				myJ--;
			}
			if (myI <= myJ) {
				swap(myI, myJ);
				myI++;
				myJ--;
			}
		} while (myI <= myJ);
		if (theLo < myJ) {
			SortUListFT(theLo, myJ);
		}
		if (myI < theHi) {
			SortUListFT(myI, theHi);
		}

	}

	public void Store(final double theValue, final double theAdditionalInfo) {
		unsortedList.add(theValue);
		unsortedListInfo.add(theAdditionalInfo);
		Summe += theValue;
		QSumme += (theValue * theValue);
	}

	private void swap(final int theA, final int theB) {
		final double myO = unsortedListInfo.get(theB);
		final double myD = unsortedList.get(theB);

		unsortedList.set(theB, unsortedList.get(theA));
		unsortedListInfo.set(theB, unsortedListInfo.get(theA));

		unsortedList.set(theA, myD);
		unsortedListInfo.set(theA, myO);

	}

	public double Variance() {
		final int wieViele = this.sortedList.size() + this.unsortedList.size();
		return ((this.QSumme / (wieViele) - Math.pow(this.ExpectedValueSimu(),
				2)));
	}

	private int where2add(final double theValue) {
		final int myMaxVaL = sortedList.size();

		if (myMaxVaL == 0) {
			return 0;
		}

		if (theValue >= (double) sortedList.get(myMaxVaL - 1)) {
			return myMaxVaL;
		} else if (theValue <= (double) sortedList.get(0)) {
			return 0;
		}
		return where2add(theValue, 0, myMaxVaL);
	}

	// public void DumpDistribution(String thePath, String theVasPath, double
	// theLGD, double theVasP, double theVasRho)
	// {
	// BufferedWriter myF = new BufferedWriter(new FileWriter(thePath));
	// ReOrganise ();
	//
	// myF.write ("try; Loss ; GlobalFactor\n" );
	// for(int i=0;i< this.sortedList.size(); i++)
	// {
	// if (sortedListInfo.get(i) == null)
	// myF.write(i).write(sortedList.get(i));
	// else
	// myF.WriteLine ("{0:N0} ;{1:N0} ; {2:S}" , i,sortedList.get(i), (string)
	// sortedListInfo.get(i));
	// }
	// myF.close();
	//
	// if (theVasPath.size() > 1)
	// { //write the vasicek approx
	// myF = new StreamWriter(theVasPath);
	// int myTries = sortedList.size();
	// myF.WriteLine ("RelLoss; EmpProb ; Vasi" );
	// double myRelLoss =0.0;
	// double myCurrentD = 0.0;
	// double myMaxD = 0.0;
	// double myMaxD50 = 0.0;
	// double myMaxD90 = 0.0;
	// double myMaxD99 = 0.0;
	//
	// for( myRelLoss =0.0 ;myRelLoss<= 0.5; myRelLoss += 0.0001)
	// {
	// double myEmpP = Probability(myRelLoss);
	// double myVasP = NormalApprox.VasicekF(myRelLoss, theVasP , theVasRho );
	// myF.WriteLine ("{0:N5} ; {1:N5}; {2:N5}" , myRelLoss,myEmpP,myVasP);
	//
	// myCurrentD = Math.abs ( myEmpP-myVasP);
	// if (myCurrentD > myMaxD )
	// myMaxD = myCurrentD ;
	// if (myVasP>= 0.5)
	// {
	// if (myCurrentD > myMaxD50 )
	// myMaxD50 = myCurrentD ;
	// }
	// if (myVasP>= 0.9)
	// {
	// if (myCurrentD > myMaxD90 )
	// myMaxD90 = myCurrentD ;
	// }
	// if (myVasP>= 0.99)
	// {
	// if (myCurrentD > myMaxD99 )
	// myMaxD99 = myCurrentD ;
	// }
	// }
	//
	// myF.Flush();
	// myF.Close();
	// this.D = myMaxD ;
	// this.D_50 = myMaxD50;
	// this.D_90 = myMaxD90;
	// this.D_99 = myMaxD99;
	//
	// }
	//
	//
	// }

	private int where2add(final double theValue, final int theLow,
			final int theHigh) {
		int myLow = theLow;
		int myHigh = theHigh;
		int myTest = myLow + (myHigh - myLow) / 2;

		while (myHigh - myLow > 1) {
			if (theValue <= (double) sortedList.get(myTest)) {
				myHigh = myTest;
			} else {
				myLow = myTest;
			}
			myTest = myLow + (myHigh - myLow) / 2;
		}
		return myHigh;
	}
}
