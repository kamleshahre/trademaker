package org.lifeform.strategy;

import org.lifeform.chart.indicator.Indicator;
import org.lifeform.chart.indicator.TrendDivergence;
import org.lifeform.market.BarSize;
import org.lifeform.optimizer.StrategyParams;
import org.lifeform.time.TradingInterval;
import org.lifeform.util.ContractFactory;

import com.ib.client.Contract;

/**
 * This sample strategy trades the S&P E-Mini futures contract using a
 * combination of two indicators. One measures a shorter term noise-adjusted
 * trend, and the other one measures a longer term noise-adjusted trend.
 */
public class TrendArbitrage extends Strategy {

	// Technical indicators
	private final Indicator trendDivergenceInd;

	// Strategy parameters names
	private static final String FAST_TREND_LENGTH = "Fast trend length";
	private static final String SLOW_TREND_LENGTH = "Slow trend length";
	private static final String ENTRY = "Entry";
	private static final String EXIT = "Exit";

	// Strategy parameters values
	// private final int fastTrendLength, slowTrendLength;
	private final double entry, exit;

	public TrendArbitrage(final StrategyParams params) throws Exception {
		// Define the contract to trade
		Contract contract = ContractFactory.makeFutureContract("ES", "GLOBEX");
		setStrategy(contract, BarSize.Min5, false);

		// Initialize strategy parameter values. If the strategy is running in
		// the optimization
		// mode, the parameter values will be taken from the "params" object.
		// Otherwise, the
		// "params" object will be empty and the parameter values will be
		// initialized to the
		// specified default values.
		int fastTrendLength = (int) params.get(FAST_TREND_LENGTH, 3);
		int slowTrendLength = (int) params.get(SLOW_TREND_LENGTH, 12);
		entry = params.get(ENTRY, 50);
		exit = params.get(EXIT, 40);

		// Instantiate technical indicators
		trendDivergenceInd = new TrendDivergence(quoteHistory, fastTrendLength,
				slowTrendLength);

		// Specify the title and the chart number for each indicator
		// "0" = the same chart as the price chart; "1+" = separate subchart
		// (below the price chart)
		addIndicator("Trend Divergence", trendDivergenceInd, 1);

	}

	/**
	 * Returns min/max/step values for each strategy parameter. This method is
	 * invoked by the strategy optimizer to obtain the strategy parameter
	 * ranges.
	 */
	@Override
	public StrategyParams initParams() {
		StrategyParams params = new StrategyParams();
		params.add(FAST_TREND_LENGTH, 3, 6, 1);
		params.add(SLOW_TREND_LENGTH, 8, 16, 1);
		params.add(ENTRY, 60, 95, 5);
		params.add(EXIT, 10, 70, 10);
		return params;
	}

	/**
	 * Define the trading interval and the time zone for that interval
	 */
	@Override
	public TradingInterval initTradingInterval() throws Exception {
		return new TradingInterval("0:30", "23:45", "America/New_York", true);
	}

	/**
	 * This method is invoked by the framework when a new bar is completed and
	 * the technical indicators are recalculated. This is where the strategy
	 * itself should be defined.
	 */
	@Override
	public void onBar() {
		double trendDivergence = trendDivergenceInd.getValue();

		int currentPosition = getPositionManager().getPosition();
		boolean target = (currentPosition > 0 && trendDivergence > exit);
		target = target || (currentPosition < 0 && trendDivergence < -exit);

		if (target) {
			position = 0;
		} else {
			if (trendDivergence > entry) {
				position = -1;// slow trend is down, fast trend is up
			} else if (trendDivergence < -entry) {
				position = 1;// slow term trend is up, fast trend is down
			}
		}
	}
}
