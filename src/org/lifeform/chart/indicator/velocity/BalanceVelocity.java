package org.lifeform.chart.indicator.velocity;

import org.lifeform.chart.indicator.Indicator;
import org.lifeform.chart.indicator.depth.DepthBalance;
import org.lifeform.market.MarketBook;

/**
 * Velocity of balance
 */
public class BalanceVelocity extends Indicator {
	private final double fastMultiplier, slowMultiplier;
	private double fast, slow;
	private DepthBalance depthBalance;

	public BalanceVelocity(final int fastPeriod, final int slowPeriod) {
		fastMultiplier = 2. / (fastPeriod + 1.);
		slowMultiplier = 2. / (slowPeriod + 1.);
	}

	@Override
	public void setMarketBook(final MarketBook marketBook) {
		super.setMarketBook(marketBook);

		depthBalance = new DepthBalance();
		depthBalance.setMarketBook(marketBook);
	}

	@Override
	public double calculate() {
		depthBalance.calculate();
		double depthBalanceValue = depthBalance.getValue();
		fast += (depthBalanceValue - fast) * fastMultiplier;
		slow += (depthBalanceValue - slow) * slowMultiplier;

		value = fast - slow;

		return value;
	}

	@Override
	public void reset() {
		fast = slow = 0;
		value = 0;
	}
}
