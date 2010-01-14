package org.lifeform.market.vol;

import org.lifeform.pricer.Rates;
import org.lifeform.pricer.RiskFreeYield;

public class Volatility {
	public Volatility() {
		this(1000.0, 2.0);
	}

	public Volatility(final double parvalue, final double coupontimes) {
		this.facevalue = parvalue;
		this.frequency = coupontimes;
	}

	private double mktprice;
	private double mktpricelow;
	private double mktpricenew;
	private double couponvalue;// couponvalue = par value*annual coupon
	// percent/2
	private double facevalue;
	private double frequency;
	private double pv;
	private double par;
	private double relativeprice;
	private double relativepricelow;
	private double upyield;
	private double downyield;
	private double newpriceup;
	private double newpricedown;
	private double currentyield;
	private double currentpvb;

	private void setInitialYldPp(final double yield) {
		currentyield = yield;
	}

	public double getInitialPpYld() {
		return currentyield;
	}

	private void setPpointpriceup(final double price) {
		newpriceup = price;
	}

	private void setPpointpricedown(final double price) {
		newpricedown = price;
	}

	public double getPriceupPp() {
		return newpriceup;
	}

	public double getPricedownPp() {
		return newpricedown;
	}

	private void setdownyieldPp(final double yield) {
		downyield = yield;
	}

	private void setupyieldPp(final double yield) {
		upyield = yield;
	}

	public double getUpPp() {
		return upyield;
	}

	public double getDownPp() {
		return downyield;
	}

	public double getValuePUp() {
		return (Math.abs(getUpPp() - getInitialPpYld()) / 100.0);
	}

	public double getValuePDown() {
		return (Math.abs(getDownPp() - getInitialPpYld()) / 100.0);
	}

	private void setRelativeValue(final double price) {
		relativeprice = price;
	}

	public double getRelativeValue() {
		return relativeprice;
	}

	private void setRelativeValuelow(final double price) {
		relativepricelow = price;
	}

	public double getRelativeValuelow() {
		return relativepricelow;
	}

	private void setCurrentPvb(final double price) {
		currentpvb = price;
	}

	public double getCurrentPvb() {
		return currentpvb;
	}

	/* Price value of a basis point */
	public void pVbPoints(final double yield, final double yearterm, final double coupon,
			final double pointchange) {

		double yieldval;
		mktprice = Bpricing(yield, yearterm, coupon);
		setCurrentPvb(mktprice);
		yieldval = (yield + (pointchange / 100.0));// make basis point
		// adjustment higher
		mktpricenew = Bpricing(yieldval, yearterm, coupon);
		setRelativeValue(Math.abs(mktpricenew - mktprice));
		yieldval = (yield - (pointchange / 100.0));// make basis point
		// adjustment lower
		mktpricelow = Bpricing(yieldval, yearterm, coupon);
		setRelativeValuelow(Math.abs(mktpricelow - mktprice));

	}

	public double Bpricing(final double yield, final double yearterm, final double coupon) {
		couponvalue = ((facevalue * coupon / 100) / frequency);
		pv = (couponvalue * Rates.pvancert((yield / 100.0) / frequency,
				(frequency * yearterm)));
		par = (facevalue * (1.0 / Math.pow(1.0 + (yield / 100.0) / frequency,
				(frequency * yearterm))));
		return (pv + par);
	}

	/**
	 *Spricing method requires rates for each compounding period and the annual
	 * coupon rate
	 */

	public double percentVolatility(final double yield, final double yearterm,
			final double coupon, final double pointchange) {
		pVbPoints(yield, yearterm, coupon, pointchange);// price value of a
		// basis point
		mktprice = Bpricing(yield, yearterm, coupon);
		return ((getRelativeValue() / mktprice) * 100.0);

	}

	/**
	 *Method provides yield values for a percentage point change in par value
	 * Sets via accessor methods the Yield value of a point change, the initial
	 * yield value prior to applying the point change
	 */
	public void yieldForPpoint(final double couponpercent, final double price,
			final double maturity, final double estimate, final double pointvalue) {

		double couponterm = 12.0 / frequency;
		double change = ((facevalue / 100.0) * pointvalue);
		setPpointpricedown(price - change);
		setPpointpriceup(price + change);
		RiskFreeYield bond = new RiskFreeYield(facevalue, couponterm,
				couponpercent, price, maturity, estimate);
		try {
			setInitialYldPp(bond.yieldEstimate());
			setdownyieldPp(Math.abs((bond.yieldEstimate())));
			setupyieldPp(Math.abs((bond.yieldEstimate())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
