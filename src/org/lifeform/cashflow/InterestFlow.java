package org.lifeform.cashflow;

import java.util.Calendar;
import java.util.Currency;

import org.lifeform.core.Index;
import org.lifeform.market.Market;
import org.lifeform.time.BusinessDayMethod;
import org.lifeform.time.DayCount;
import org.lifeform.time.Period;

public class InterestFlow extends CashFlow {
	final DayCount dayCount;
	final BusinessDayMethod method;
	final Period accrual;
	final Index index;
	final Calendar fixing;
	final Calendar payment;

	public InterestFlow(final Period accrual, final Calendar payment, final Calendar fixing,
			final Index rateIndex, final double amount, final Currency currency, final DayCount dc,
			final BusinessDayMethod method) {
		super(payment, amount, currency);
		this.dayCount = dc;
		this.method = method;
		this.accrual = accrual;
		this.index = rateIndex;
		this.fixing = fixing;
		this.payment = payment;
	}

	public double calculate(final Market market) {
		double fixingDiscount = market.getDf(currency, payment);
		double fwdRate = market.getForwardRate(currency, fixing);
		return fixingDiscount * fwdRate * amount;
	}
}
