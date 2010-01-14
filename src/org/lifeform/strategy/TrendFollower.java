package org.lifeform.strategy;

import org.lifeform.chart.indicator.EMA;
import org.lifeform.chart.indicator.Indicator;
import org.lifeform.market.BarSize;
import org.lifeform.optimizer.StrategyParams;
import org.lifeform.time.TradingInterval;
import org.lifeform.util.ContractFactory;

import com.ib.client.Contract;

/**
 * This sample strategy trades S&P E-Mini futures using moving average crossover
 * as an indicator. When the fast EMA is above the slow EMA, it buys. Otherwise,
 * it sells short. This is a trend following system. The moving averages are
 * calculated from the priceBar history of the 5-minute bars.
 */
public class TrendFollower extends Strategy {

	// Technical indicators
	private final Indicator fastTrendInd, slowTrendInd;

	// Strategy parameters names
	private static final String FAST_TREND_LENGTH = "Fast trend length";
	private static final String SLOW_TREND_LENGTH = "Slow trend length";

	// Strategy parameters values
	private final int fastTrendLength, slowTrendLength;

	public TrendFollower(final StrategyParams params) throws Exception {
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
		fastTrendLength = (int) params.get(FAST_TREND_LENGTH, 32);
		slowTrendLength = (int) params.get(SLOW_TREND_LENGTH, 128);

		// Instantiate technical indicators
		fastTrendInd = new EMA(quoteHistory, fastTrendLength);
		slowTrendInd = new EMA(quoteHistory, slowTrendLength);

		// Specify the title and the chart number for each indicator
		// "0" = the same chart as the price chart; "1+" = separate subchart
		// (below the price chart)
		addIndicator("Fast Trend", fastTrendInd, 0);
		addIndicator("Slow Trend", slowTrendInd, 0);
	}

	/**
	 * Returns min/max/step values for each strategy parameter. This method is
	 * invoked by the strategy optimizer to obtain the strategy parameter
	 * ranges.
	 */
	@Override
	public StrategyParams initParams() {
		StrategyParams params = new StrategyParams();
		params.add(FAST_TREND_LENGTH, 5, 50, 1);
		params.add(SLOW_TREND_LENGTH, 5, 50, 1);
		return params;
	}

	/**
	 * Define the trading interval and the time zone for that interval
	 */
	@Override
	public TradingInterval initTradingInterval() throws Exception {
		return new TradingInterval("10:00", "15:40", "America/New_York", false);
	}

	/**
	 * This method is invoked by the framework when a new bar is completed and
	 * the technical indicators are recalculated. This is where the strategy
	 * itself should be defined.
	 */
	@Override
	public void onBar() {
		double fastTrend = fastTrendInd.getValue();
		double slowTrend = slowTrendInd.getValue();

		if (fastTrend > slowTrend) {
			position = 1;
		} else if (fastTrend < slowTrend) {
			position = -1;
		}
	}
}
