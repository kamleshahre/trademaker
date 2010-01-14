package org.lifeform.pricer;

import org.lifeform.market.vol.Volatility;

public class Duration {
	private double percentchange;
	private double dolduration;
	private double modmdyrs;
	private double mdyrs;
	private double frequency = 2.0;

	public Duration() {
		this(2.0);
	}

	public Duration(final double couponfreq) {
		this.frequency = couponfreq;

	}

	private void setMDyears(final double mdperiods) {
		this.mdyrs = mdperiods / frequency;
	}

	public double getMDyears() {
		return mdyrs;
	}

	public double getMDmodyrs() {
		return modmdyrs;
	}

	private void setMDmodyrs(final double mdyears, final double discvalue) {
		this.modmdyrs = mdyears / discvalue;
	}

	private void setDduration(final double modurationyrs, final double price) {
		this.dolduration = ((modurationyrs * price) / 1e4);

	}

	public double getDolduration() {
		return dolduration;
	}

	private void setPerchange(final double moduration) {
		this.percentchange = -moduration;
	}

	public double getPerchange(final double basispoints) {
		return 100 * (percentchange * basispoints);
	}

	/** Requires the yield and coupon as a decimal value */
	public double duration(double yield, double period, final double parprice,
			double coupon) {

		double val = 0;
		Volatility v = new Volatility(parprice, frequency);
		double bondprice = v
				.Bpricing((yield * 100.0), period, (coupon * 100.0));// require
		// yield
		// and
		// coupon
		// as a
		// percentage
		yield = yield / frequency;
		coupon = coupon / frequency;
		int n = (int) (period * frequency);
		val = (n * (Rates.PVs(yield, parprice, n) / bondprice));// face value
		// discounted..
		for (int i = 1; i < (n + 1); i++) {
			val += ((Rates.PV(yield, (coupon * parprice), i)) / bondprice);// individual
			// period
			// cash
			// flows
		}
		setMDyears(val);
		setMDmodyrs(getMDyears(), (1 + yield));
		setDduration(getMDmodyrs(), bondprice);
		setPerchange(getMDmodyrs());
		return val;
	}

}
