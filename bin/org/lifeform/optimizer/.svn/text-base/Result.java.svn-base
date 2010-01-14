package org.lifeform.optimizer;

/**
 * Optimization results table model.
 */
public class Result {
	protected final double totalProfit, maxDrawdown, profitFactor, kelly;
	protected final int trades;
	protected final String tradeDistribution;
	protected final StrategyParams params;

	public Result(final StrategyParams params, final double totalProfit,
			final double maxDrawdown, final int trades, final double profitFactor, final double kelly,
			final String tradeDistribution) {
		this.params = params;
		this.totalProfit = totalProfit;
		this.maxDrawdown = maxDrawdown;
		this.trades = trades;
		this.profitFactor = profitFactor;
		this.kelly = kelly;
		this.tradeDistribution = tradeDistribution;
	}

	public StrategyParams getParams() {
		return params;
	}

	public double getTotalProfit() {
		return totalProfit;
	}

	public double getMaxDrawdown() {
		return maxDrawdown;
	}

	public int getTrades() {
		return trades;
	}

	public double getProfitFactor() {
		return profitFactor;
	}

	public double getKelly() {
		return kelly;
	}

	public String getTradeDistribution() {
		return tradeDistribution;
	}
}
