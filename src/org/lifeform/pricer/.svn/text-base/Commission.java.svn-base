package org.lifeform.pricer;

/**
 */
public class Commission {
	private final double rate, minimum, maximumPercent;

	/**
	 * For commissions and fees, see
	 * http://individuals.interactivebrokers.com/en
	 * /accounts/fees/commission.php?ib_entity=llc
	 * 
	 * @param rate
	 *            Commission per contract or per share
	 * @param minimum
	 *            Minimum commission per order
	 * @param maximumPercent
	 *            Maximum commission as percent of the trade amount
	 */
	public Commission(final double rate, final double minimum, final double maximumPercent) {
		this.rate = rate;
		this.minimum = minimum;
		this.maximumPercent = maximumPercent;
	}

	public String toString() {
		return (rate + " per share/contract, " + minimum + " minimum per trade");
	}

	public Commission(final double rate, final double minimum) {
		this(rate, minimum, 0);
	}

	public double getCommission(final int contracts, final double price) {
		double commission = rate * contracts;
		if (maximumPercent > 0) {
			double maximumCommission = contracts * price * maximumPercent;
			commission = Math.min(commission, maximumCommission);
		}

		commission = Math.max(commission, minimum);
		return commission;
	}
}
