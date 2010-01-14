package org.lifeform.pricer;

import java.util.Calendar;

import org.lifeform.market.Market;
import org.lifeform.product.LongShort;
import org.lifeform.product.Product;
import org.lifeform.time.DateUtil;
import org.lifeform.time.Frequency;

public class Forward extends Pricer {

	@Override
	public PricingResult price(final Product product, final Calendar valuationDate,
			final Market market) {
		PricingResult result = new PricingResult(product, valuationDate);

		boolean isBuy = product.getLongShort() == LongShort.Long;
		org.lifeform.product.Forward fwd = (org.lifeform.product.Forward) product;
		double price = fwd.getPrice().getValue();
		double notional = fwd.getPrincipal();
		double fwdPrice = fwd.getForwardPrice();
		if (DateUtil.isMaturityDate(valuationDate, product)) {
			double settlementAmount = isBuy ? (price - fwdPrice) * notional
					: (fwdPrice - price) * notional;
			result.put(PricingResult.Type.CashSettlement, settlementAmount);
		}

		return result;
	}

	/**
	 * method to return the delivery price of a new forward contract
	 * 
	 * @param spotprice
	 *            is the spot price of the underlying asset
	 *@param maturity
	 *            is the time (in years as a decimal) to maturity of the
	 *            contract
	 *@param currentime
	 *            is the start time of the new contract
	 */
	public static double delpriceNoinc(final double spotprice, final double maturity,
			final double reporate) {
		Frequency f = new Frequency(Frequency.Type.CNT);
		return (spotprice * Rates.PV(reporate, maturity, f));
	}

	public static double fpriceNoinc(final double spotprice, final double maturity,
			final double currentime, final double deliveryprice, final double reporate) {
		return (spotprice - (Rates.PV(reporate, (maturity - currentime),
				deliveryprice, Frequency.CNT)));
	}

	public static double fpriceInc(final double spotprice, final double maturity,
			final double currentime, final double reporate, final double period, final double dividend) {
		double income = 0.0;
		income = maturity == 1.0 ? Rates.PV(reporate, 1.0, dividend,
				Frequency.CNT) : 0.0;// last
		// value
		double limit = 0.0;

		limit = (maturity - currentime);// Assumes that later start times will
		// floor the pv of dividend payments
		double time = (period / 12.0);
		double increment = time;
		while (time < limit) {
			income += Rates.PV(reporate, time, dividend, Frequency.CNT);
			time = time + increment;
		}
		return ((spotprice - income) * (Rates.PV(reporate,
				(maturity - currentime), Frequency.CNT)));
	}

	public static double fpriceInc(final double spotprice, final double maturity,
			final double currentime, final double[] reporate, final double period, final double dividend) {
		double income = 0.0;
		double time = (period / 12.0);
		double increment = time;
		for (double r : reporate) {
			income += Rates.PV(r, time, dividend, Frequency.CNT);
			time = time + increment;
		}
		return ((spotprice - income) * (Rates.PV(
				reporate[(reporate.length - 1)], (maturity - currentime),
				Frequency.CNT)));

	}

	public static double fvalueInc(final double spotprice, final double maturity,
			final double currentime, final double reporate, final double period, final double dividend,
			final double deliveryprice) {
		double income = 0.0;
		income = maturity == 1.0 ? Rates.PV(reporate, 1.0, dividend,
				Frequency.CNT) : 0.0;// last
		// value
		double limit = 0.0;

		limit = (maturity - currentime);// Assumes that later start times will
		// floor the pv of dividend payments
		double time = (period / 12.0);
		double increment = time;
		while (time < limit) {
			income += Rates.PV(reporate, time, dividend, Frequency.CNT);
			time = time + increment;
		}

		return ((spotprice - income) - (deliveryprice * Rates.PV(reporate,
				(maturity - currentime), Frequency.CNT)));
	}

	public static double fvalueInc(final double spotprice, final double maturity,
			final double currentime, final double[] reporate, final double period,
			final double dividend, final double deliveryprice) {
		double income = 0.0;
		double time = (period / 12.0);
		double increment = time;
		for (double r : reporate) {
			income += Rates.PV(r, time, dividend, Frequency.CNT);
			time = time + increment;

		}
		return (spotprice - (income + (deliveryprice * Rates.PV(
				reporate[(reporate.length - 1)], (maturity - currentime),
				Frequency.CNT))));
	}

	public static double fvaluegen(final double fprice, final double delivprice,
			final double maturity, final double currentime, final double reporate) {
		return ((fprice - delivprice) * Rates.PV(reporate,
				(maturity - currentime), Frequency.CNT));
	}

	public static double fpriceDyld(final double spotprice, final double maturity,
			final double currentime, final double reporate, final double dividendyld) {
		return (spotprice * Rates.PV((reporate - dividendyld),
				(maturity - currentime), new Frequency(Frequency.Type.CNT)));
	}

	public static double fvalueDyld(final double spotprice, final double maturity,
			final double currentime, final double reporate, final double dividendyld,
			final double deliveryprice) {
		return ((fpriceDyld(spotprice, maturity, currentime, reporate,
				dividendyld) - (deliveryprice)) * Rates.PV(reporate,
				(maturity - currentime), Frequency.CNT));
	}

}
