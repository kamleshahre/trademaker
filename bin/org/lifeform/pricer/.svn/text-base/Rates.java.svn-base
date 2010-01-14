package org.lifeform.pricer;

import org.lifeform.time.DayCount;
import org.lifeform.time.Frequency;

public class Rates {
	public static double PV(final double[] discounts, final double[] cashflows) {
		double pv = 0;
		for (int i = 0, n = cashflows.length; i < n; i++) {
			double discount = discounts[i];
			double flow = cashflows[i];
			pv += (Math.exp(-(i + 1) * Math.log(1.0 + (discount / 100.0))) * flow);
		}
		return pv;
	}

	public static double PV(final double rate, final double years, final Frequency freq) {
		return PV(rate, years, 1.0, freq);
	}

	public static double PV(final double rate, final double years, final double amount,
			final Frequency freq) {
		switch (freq.getType()) {
		case CNT: {
			return Math.exp(rate * years) * amount;
		}
		default:
			return 0;
		}
	}

	public static double pVonecash(final double[] discounts, final double cashflow) {
		int i = 0;
		double pv = 0;
		for (double d : discounts) {
			pv += (Math.exp(-(i + 1) * Math.log(1.0 + (d / 100.0))) * cashflow);
			i++;
		}
		return pv;
	}

	public static double PV(final double rate, final double[] cashflows) {
		double sum = 0;
		for (int i = 0, index = 1; i < cashflows.length; i++, index++) {
			sum += (cashflows[i] * (Math.exp(-(double) index
					* Math.log(1.0 + rate))));
		}
		return sum;
	}

	public static double PV(final double rate, final double cash, final int period) {
		double sum = 0;
		for (int i = 0, index = 1; i < period; i++, index++) {
			sum += (cash * (Math.exp(-(double) index * Math.log(1.0 + rate))));
		}
		return sum;
	}

	public static double rateToDf(final double rate, final double t) {
		return Math.exp(-rate * t);
	}

	public static double PVw(final double r, final double cash, final int period) {
		return (period * cash * (Math.exp(-(double) period * Math.log(1.0 + r))));
	}

	public static double PVs(final double r, final double cash, final int period) {
		return (cash * (Math.exp(-(double) period * Math.log(1.0 + r))));
	}

	public static double PV(final double rate, final double period) {
		double sum = 0;
		int i = (int) period;
		double periodOffset = period - i;
		int index = periodOffset == 0.0 ? 1 : 0;
		for (i = 0; i < period; i++, index++) {
			sum += (1.0 / (Math.pow((1.0 + rate), (index + periodOffset))));
		}
		return sum;
	}

	public static double dfToFV(final double df1, final double df2, final double t1, final double t2) {
		return (Math.log(df1 / df2) / (t2 - t1));
	}

	public static double rateToFV(final double r1, final double r2, final double t1, final double t2) {
		return (r2 * (t2 / (t2 - t1))) - (r1 * (t1 / (t2 - t1)));
	}

	public static double dfToYield(final double df, final double t) {
		return -Math.log(df) / t;
	}

	public static double discreteToContinuous(final double periods, final double rate) {
		return (periods * (Math.log(1.0 + (rate / periods))));
	}

	public static double continuousToDiscrete(final double periods, final double rate) {
		return (periods * (Math.exp(rate / periods) - 1.0));
	}

	public static double effectiveRate(final double rate, final double periods) {
		return Math.pow((1 + (rate / periods)), periods) - 1;
	}

	public static double effectiveAnnualRate(final double rate, final double realRate) {
		return (Math.pow((1 + rate / realRate), realRate) - 1);
	}

	public static double erate(final double intr, final double convertp) {
		return Math.pow((1 + (intr / convertp)), convertp) - 1;
	}

	public static double forceInterest(final double intr) {
		return Math.log(1 + intr);
	}

	/**
	 * annuity certain- cumulative amounts annually-in arrears
	 * 
	 * @param intr
	 *            the input interest rate
	 * @param n
	 *            the number of payments per annum
	 * @return the multiplier rate
	 */
	public static double ancertain(final double intr, final double n) {
		return ((Math.pow((1 + intr), n) - 1) / intr);// annuity
		// certain..cumulative
		// amount for a series
		// of payments at n ints
		// at a given int rate
		// payed in arrears
	}

	/**
	 * computes an annuity certain-series of payments in advance
	 * 
	 * @param intr
	 *            input interest rate
	 * @param n
	 *            number of conversion periods per annum
	 * @return the multiplier rate
	 */
	public static double ancertainAd(final double intr, final double n) {
		return (((Math.pow((1 + intr), n) - 1) / intr) * (1 + intr));// annuity
		// certain..cumulative
		// amount
		// for a
		// series
		// of
		// payments
		// at n
		// ints
		// at a
		// given
		// int
		// rate
		// payed in advance
	}

	/**
	 * present value of an annuity certain --in arrears
	 * 
	 * @param intr
	 *            the input interest rate per conversion period
	 * @param n
	 *            number of conversions
	 * @return the multiplier rate
	 */
	public static double pvancert(final double intr, final double n) {
		return (1.0 - (1 / Math.pow((1 + intr), n))) / intr; // PV of an annuity
		// certain
		// payed in arrears
	}

	/**
	 * present value of an annuity certain in advance
	 * 
	 * @param intr
	 *            the input interest rate
	 * @param n
	 *            the number of conversions per annum * years
	 * @return The multiplier rate
	 */
	public static double pvancertAd(final double intr, final double n) {
		return ((1 + intr) * (1.0 - (1 / Math.pow((1 + intr), n))) / intr); // PV
		// of
		// an
		// annuity
		// certain
		// in
		// advance

	}

	/**
	 * present value of an infinite number of payments with each payment growing
	 * 1+g in succession
	 * 
	 * @param intr
	 *            the interest rate (flat)
	 * @param growth
	 *            the growth rate between payments
	 * @param value
	 *            initial value of payment
	 * @return the present value for the interest rate
	 */
	public static double pvainfprog(final double intr, final double growth, final double value) {
		return value / intr - growth;// pv of an ininite number of payments od d
		// each /each being grown by 1+g
		// (multiplier)
		// if growth =0, this is a perpetuity
	}

	/**
	 * computes the present value of an annuity with successive payments a
	 * multiple
	 * 
	 * @param intr
	 *            the input flat rate of interest
	 * @param n
	 *            the growth multiplier
	 * @return present value of the annuity
	 */
	public static double pvanmult(final double intr, final double n) {
		double value = 1 / (1 + intr);
		return ((pvancertAd(intr, n)) - (n * Math.pow(value, n))) / intr;// pv
		// of
		// an
		// increasing
		// annuity
		// with
		// each
		// period
		// being
		// a
		// multiple
		// of
		// n
		// per
		// period

	}

	/**
	 * Computes Effective present interest rate
	 * 
	 * @param annualintr
	 *            The effective annual interest rate
	 * @param p
	 *            the present flat rate
	 * @return the effective present rate
	 */
	public static double effectintp(final double annualintr, final double p) {
		return Math.pow((1 + annualintr), (1 / p)) - 1;// given the effective
		// annual int rate gives
		// the effective p rate
	}

	/**
	 * computes the effective annaul rate for a present rate
	 * 
	 * @param nomnualintr
	 *            the nominal interest rate
	 * @param p
	 *            the present rate
	 * @return the effective annaul rate
	 */
	public static double effectann(final double nomnualintr, final double p) {

		return (Math.pow((1 + nomnualintr / p), p) - 1);// given a nominal rate
		// returns the effctive
		// annual rate for p
	}

	public static double dollarInterest(final double term, final double rate, final DayCount dc) {
		return dollarInterest(term, rate, 1.0, dc);
	}

	public static double dollarInterest(final double term, final double rate,
			final double amount, final DayCount dc) {
		return rate * (term / dc.getYearDays()) * amount;
	}
}
