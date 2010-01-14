package org.lifeform.optimizer;

/**
 * Optimization result.
 */
public class OptimizationResult extends Result {
	public OptimizationResult(final StrategyParams params, final double totalProfit,
			final double maxDrawdown, final int trades, final double profitFactor, final double kelly,
			final String tradeDistribution) {
		super(params, totalProfit, maxDrawdown, trades, profitFactor, kelly,
				tradeDistribution);
	}

	public OptimizationResult(final StrategyParams params,
			final PerformanceManager performanceManager) {
		this(params, performanceManager.getNetProfit(), performanceManager
				.getMaxDrawdown(), performanceManager.getTrades(),
				performanceManager.getProfitFactor(), performanceManager
						.getKellyCriterion(), "");
	}

	public StrategyParams getParams() {
		return params;
	}

	public double getNetProfit() {
		return profitFactor;
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

	public double getKellyCriterion() {
		return kelly;
	}

	public double getPerformanceIndex() {
		return Double.NaN;
	}
}
