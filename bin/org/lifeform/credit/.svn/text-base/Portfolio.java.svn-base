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

import java.util.Hashtable;
import java.util.Vector;

import org.lifeform.configuration.Defaults;
import org.lifeform.excel.Sheet;
import org.lifeform.excel.Workbook;

/**
 * A Portfolio consists of a factor set (factor model), /// a set of exposures
 * and risk entities and means /// to store the results of simulations. /// The
 * Simulate()-method can be used to run one or /// more simulation(s).
 * 
 */
public class Portfolio {
	public FactorModel factors;
	public LossStorage losses;
	public RiskEntity[] riskEntities;
	public Exposure[] exposures;
	public int nrOfHiddenFactors;
	public int nrOfObviousFactors;
	public int nrOfRiskEntities;
	public int nrOfExposures;

	public ContributionStorage bucketFrequency; // counts the freq of pf

	// -loss-buckets

	public Portfolio(final String thebaseDir) {
		if (thebaseDir.toLowerCase().endsWith(".xls")) {
			try {
				InitFromExcel(thebaseDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Portfolio(final String theName, final Object theParm) {
		SpecialInit(theName, theParm, "", 0, 0);
	}

	public Portfolio(final String theName, final Object theParm,
			final String theAdditionalAssetName, final double theAddiPD, final double theAddiSize) {
		SpecialInit(theName, theParm, theAdditionalAssetName, theAddiPD,
				theAddiSize);
	}

	public String CollectStoreString() {
		String myRS = "";
		for (int i = 0; i <= this.factors.hiddenFactors.length; i++)
			myRS += String.valueOf(this.factors.hiddenFactors[i].SimuWert)
					+ ";";
		return myRS;
	}

	public double ExpextedLoss_Analytic() {
		double myEL = 0.0;

		for (int i = 0; i < this.exposures.length; i++) {
			if (exposures[i].belongsToPortfolio)
				myEL += this.exposures[i].Loss_Medium
						* this.exposures[i].RiskEntity.GetPDE1();
		}
		return myEL;
	}

	public void InitFromExcel(final String theExcelPath) throws Exception {
		factors = new FactorModel(true);
		Workbook myXLReader = new Workbook(theExcelPath);
		Sheet sheet = myXLReader.getSheet("Parameters");
		Object[][] table = sheet.getData();
		Defaults.initFromTable(table);

		Sheet re = myXLReader.getSheet("RiskEntry");
		riskEntities = new RiskEntity[re.getRowCount()];
		Vector<Class<?>> mask = new Vector<Class<?>>();
		mask.add(Integer.class);
		mask.add(Double.class);
		mask.add(Double.class);
		mask.add(Double.class);
		mask.add(Double.class);
		mask.add(Double.class);

		Vector<Hashtable<String, Object>> rows = re.getRows();

		int counter = 0;
		for (Hashtable<String, Object> myDR : rows) {
			int myCurrentREID = (Integer) myDR.get("REID");
			String sId = String.valueOf(myCurrentREID);
			double pd = (Double) myDR.get("PD");
			this.riskEntities[counter] = new RiskEntity(sId, sId, pd);
			this.riskEntities[counter].factor.HiddenFactorNrs = new int[3];
			this.riskEntities[counter].factor.HiddenFactorLoads = new double[3];
			this.riskEntities[counter].factor.specificLoad = (Double) myDR
					.get("SpecRisk");
			this.riskEntities[counter].factor.HiddenFactorNrs[0] = 0;
			this.riskEntities[counter].factor.HiddenFactorLoads[0] = (Double) myDR
					.get("S_0");
			this.riskEntities[counter].factor.HiddenFactorNrs[1] = 1;
			this.riskEntities[counter].factor.HiddenFactorLoads[1] = (Double) myDR
					.get("S_1");
			this.riskEntities[counter].factor.HiddenFactorNrs[2] = 2;
			this.riskEntities[counter].factor.HiddenFactorLoads[2] = (Double) myDR
					.get("S_2");
			counter++;
		}

		Sheet exposure = myXLReader.getSheet("Exposure");
		mask = new Vector<Class<?>>();
		mask.add(Integer.class);
		mask.add(Double.class);
		mask.add(String.class);
		mask.add(Double.class);
		mask.add(String.class);
		mask.add(Integer.class);

		exposures = new Exposure[exposure.getRowCount()];
		int myCurrentExp = 0;

		rows = re.getRows();
		for (Hashtable<String, Object> myDR : rows) {
			// int myCurrentExpID = (Integer) myDR.get("ExposureID");
			this.exposures[myCurrentExp] = new Exposure(
					riskEntities[myCurrentExp], true);
			this.exposures[myCurrentExp].Loss_Medium = (Double) myDR.get("LGD");
			this.exposures[myCurrentExp].LGD_type = (String) myDR
					.get("LGDType");
			this.exposures[myCurrentExp].ExposureAmount = (Double) myDR
					.get("Exposure");
			this.exposures[myCurrentExp].Description = (String) myDR
					.get("ExposureID");

			Integer portId = (Integer) myDR.get("Portfolio");
			if (portId != 0)
				this.exposures[myCurrentExp].belongsToPortfolio = true;
			myCurrentExp++;
		}
		losses = new LossStorage(this.ExpextedLoss_Analytic(), this
				.LGDMittel_Summe());
		bucketFrequency = new ContributionStorage();

	}

	public double LGDMittel_Summe() {
		double mySum = 0.0;

		for (int i = 0; i < exposures.length; i++) {
			if (exposures[i].belongsToPortfolio)
				mySum += exposures[i].Loss_Medium;
		}
		return mySum;
	}

	public void Simulate() {
		this.Simulate(1, false);
	}

	public void Simulate(final int theNrSimulations, final boolean theShouldTrack) {
		boolean doTheBlib = Defaults.contains("BLIB");
		int myHowManySinceBlib = 0;
		int myNextBlib = theNrSimulations / 20;

		for (int myHowMany = 0; myHowMany < theNrSimulations; myHowMany++) {
			if (doTheBlib) // show the program is still running
			{
				myHowManySinceBlib++;
				if (myHowManySinceBlib == myNextBlib) {
					myHowManySinceBlib = 0;
				}
			}
			if (myHowMany == 1000) // firstguess
			{
				// 2do??? estimate barriers, initBarrier
			}
			double myCurrentLoss = 0.0;
			factors.Simulate();
			for (int i = 0; i < riskEntities.length; i++) {
				riskEntities[i].Simulate(this.factors);
			}
			for (int i = 0; i < exposures.length; i++) {
				if (exposures[i].belongsToPortfolio)
					myCurrentLoss += exposures[i].Simulate();
			}

			// String myFactorInfo = CollectStoreString();

			this.losses.Store(myCurrentLoss, this.exposures[0].Schuld
					.GetPfLossGrade(myCurrentLoss));

			if (theShouldTrack) { // allocate losses to exposures
				// 2do!!!

				// get PortfolioLossbucket
				int myBucket = this.exposures[0].Schuld
						.GetPfLossGrade(myCurrentLoss);

				// store contributions
				for (int i = 0; i < exposures.length; i++) {
					exposures[i].saveContribution(myBucket);
				}
				this.bucketFrequency.Save(myBucket, 1.0);
			}
		}
	}

	// public void DumpContributions(String thePath) {
	// System.IO.StreamWriter myF = new System.IO.StreamWriter(thePath);
	// String myRiskMeasure = System.Convert
	// .ToString(GlobalSettings.GParms["RISK"]);
	// myRiskMeasure = myRiskMeasure.toUpperCase();
	// double myPortfolioLoss = 0.0;
	// if (myRiskMeasure == "VAR")
	// myPortfolioLoss = this.losses.Quantil(99.0);
	// else
	// myPortfolioLoss = this.losses.ExpectedShortfall(99.0);
	//
	// double myPortfolioContibSum = 0.0;
	// for (int i = 0; i <= this.exposures.length; i++) {
	// if (myRiskMeasure == "VAR")
	// myPortfolioContibSum += this.exposures[i].Schuld
	// .GetVaRContrib();
	// else
	// myPortfolioContibSum += this.exposures[i].Schuld
	// .GetEshContrib();
	// }
	// myF.WriteLine("expo-nr; EL ; ContribRaw; ContribScal");
	// myF.WriteLine("# RiskMeasure: " + myRiskMeasure);
	// myF.WriteLine("# Quantil: ???");
	// double mySkal = myPortfolioLoss / myPortfolioContibSum;
	// for (int i = 0; i <= this.exposures.length; i++) {
	// Exposure myEx = this.exposures[i];
	//
	// double myGuilt;
	// if (myRiskMeasure.toUpperCase() == "VAR")
	// myGuilt = myEx.Schuld.GetVaRContrib();
	// else
	// myGuilt = myEx.Schuld.GetEshContrib();
	// // myF.WriteLine ("{0:N0} ;{1:N7} ; {2:N7} ;{3:N7} ;" ,
	// // i,myEx.EL_analytic(),myGuilt, mySkal*myGuilt);
	// myF.WriteLine("{0:N0} ;{1:N7} ; {2:N7} ;{3:N7} ;"
	// + myEx.Schuld.TestResultStr(), i, myEx.EL_analytic(),
	// myGuilt, mySkal * myGuilt);
	// }
	// myF.Flush();
	// myF.Close();
	// }

	public void SpecialInit(final String theName, final Object theParm,
			final String theAdditionalAssetName, final double theAddiPD, final double theAddiSize) {
		// creates one of the exsample portfolios
		factors = new FactorModel(true);

		// je eine meth. fuer independent, 1-faktor, 2-faktor
		if (theName == "TEST0" || theName == "TEST1" || theName == "TEST2") {
			this.nrOfHiddenFactors = 3;
			this.nrOfObviousFactors = 3;
			this.nrOfRiskEntities = 1143;
			this.nrOfExposures = 1143;
			int[] myRiskEntities = new int[] { 9, 18, 36, 72, 144, 288, 576 };

			if (theAdditionalAssetName != "") {
				this.nrOfRiskEntities = 1144;
				this.nrOfExposures = 1144;

			}

			this.riskEntities = new RiskEntity[this.nrOfRiskEntities];
			this.exposures = new Exposure[this.nrOfRiskEntities];
			int myCurrentRE = -1;
			double p = (Double) theParm;
			double[] myPD = new double[] { p * 0.3, p, p * 7 };
			double[] mySize = new double[] { 1024, 512, 256, 128, 64, 32, 16 };

			for (int mySizeClass = 0; mySizeClass < 7; mySizeClass++) {
				for (int myEntity = 0; myEntity < myRiskEntities[mySizeClass]; myEntity++) {
					myCurrentRE++;
					// 2:1 factor a zu b, 1:1:1 nach Ratings
					this.riskEntities[myCurrentRE] = new RiskEntity(String
							.valueOf(myCurrentRE), String.valueOf(myCurrentRE),
							myPD[myEntity % 3]);
					this.riskEntities[myCurrentRE].factor.HiddenFactorNrs = new int[2];
					this.riskEntities[myCurrentRE].factor.HiddenFactorLoads = new double[2];
					this.riskEntities[myCurrentRE].factor.specificLoad = 0.71;
					this.riskEntities[myCurrentRE].factor.HiddenFactorNrs[0] = 0;
					this.riskEntities[myCurrentRE].factor.HiddenFactorLoads[0] = 0.5;
					this.riskEntities[myCurrentRE].factor.HiddenFactorNrs[1] = 1;
					this.riskEntities[myCurrentRE].factor.HiddenFactorLoads[1] = 0.5;
					if ((myEntity % 9 >= 6) && (theName == "TEST2")) {
						this.riskEntities[myCurrentRE].factor.HiddenFactorNrs[1] = 2;
					}
					if (theName == "TEST0") {
						this.riskEntities[myCurrentRE].factor.HiddenFactorNrs = null;
						this.riskEntities[myCurrentRE].factor.HiddenFactorLoads = null;
						this.riskEntities[myCurrentRE].factor.specificLoad = 1.00;
					}
					this.riskEntities[myCurrentRE].SetPDE1(myPD[myEntity % 3]);
					//
					// exposures:
					this.exposures[myCurrentRE] = new Exposure(
							this.riskEntities[myCurrentRE], true);
					this.exposures[myCurrentRE].Loss_Medium = mySize[mySizeClass];
					this.exposures[myCurrentRE].ExposureAmount = mySize[mySizeClass] * 2.0;
					this.exposures[myCurrentRE].Description = ((myEntity % 9 >= 6) && (theName == "TEST2")) ? "Faktor2"
							: "Faktor1";

				}

			} // end for-loop
			if (theAdditionalAssetName != "") { // enter one additional exposure
				// for tight monitoring!
				this.nrOfRiskEntities++;
				this.nrOfExposures++;
				myCurrentRE++;

				this.riskEntities[myCurrentRE] = new RiskEntity(
						theAdditionalAssetName, String.valueOf(myCurrentRE),
						theAddiPD);
				this.riskEntities[myCurrentRE].factor.HiddenFactorNrs = new int[2];
				this.riskEntities[myCurrentRE].factor.HiddenFactorLoads = new double[2];
				this.riskEntities[myCurrentRE].factor.specificLoad = 0.71;
				this.riskEntities[myCurrentRE].factor.HiddenFactorNrs[0] = 0;
				this.riskEntities[myCurrentRE].factor.HiddenFactorLoads[0] = 0.5;
				this.riskEntities[myCurrentRE].factor.HiddenFactorNrs[1] = 1;
				this.riskEntities[myCurrentRE].factor.HiddenFactorLoads[1] = 0.5;

				this.exposures[myCurrentRE] = new Exposure(
						this.riskEntities[myCurrentRE], true);
				this.exposures[myCurrentRE].Loss_Medium = theAddiSize;
				this.exposures[myCurrentRE].ExposureAmount = theAddiSize * 2.0;

			}
		} // ende der test* - portfolios

		if (losses == null)
			losses = new LossStorage(this.ExpextedLoss_Analytic(), this
					.LGDMittel_Summe());
		this.bucketFrequency = new ContributionStorage();
	}

}
