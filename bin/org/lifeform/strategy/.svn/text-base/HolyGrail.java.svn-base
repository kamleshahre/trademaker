package org.lifeform.strategy;

import org.lifeform.chart.indicator.Indicator;
import org.lifeform.chart.indicator.NoiseAdjustedRSI;
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
public class HolyGrail extends Strategy {

	// Technical indicators
	private final Indicator fastTrendInd, slowTrendInd;

	// Strategy parameters names
	private static final String FAST_TREND_LENGTH = "Fast trend length";
	private static final String SLOW_TREND_LENGTH = "Slow trend length";
	private static final String FAST_TREND_STRENGTH = "Fast trend strength";
	private static final String SLOW_TREND_STRENGTH = "Slow trend strength";
	private static final String EXIT = "Exit";

	// Strategy parameters values
	private final int fastTrendLength, slowTrendLength;
	private final double fastTrendStrength, slowTrendStrength, exit;

	public HolyGrail(final StrategyParams params) throws Exception {
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
		fastTrendLength = (int) params.get(FAST_TREND_LENGTH, 3);
		slowTrendLength = (int) params.get(SLOW_TREND_LENGTH, 12);
		fastTrendStrength = params.get(FAST_TREND_STRENGTH, 35);
		slowTrendStrength = params.get(SLOW_TREND_STRENGTH, 89);
		exit = params.get(EXIT, 95);

		// Instantiate technical indicators
		fastTrendInd = new NoiseAdjustedRSI(quoteHistory, fastTrendLength);
		slowTrendInd = new NoiseAdjustedRSI(quoteHistory, slowTrendLength);

		// Specify the title and the chart number for each indicator
		// "0" = the same chart as the price chart; "1+" = separate subchart
		// (below the price chart)
		addIndicator("Fast Trend", fastTrendInd, 1);
		addIndicator("Slow Trend", slowTrendInd, 1);
	}

	/**
	 * Returns min/max/step values for each strategy parameter. This method is
	 * invoked by the strategy optimizer to obtain the strategy parameter
	 * ranges.
	 */
	@Override
	public StrategyParams initParams() {
		StrategyParams params = new StrategyParams();
		params.add(FAST_TREND_LENGTH, 3, 4, 1);
		params.add(SLOW_TREND_LENGTH, 8, 16, 1);
		params.add(FAST_TREND_STRENGTH, 30, 50, 5);
		params.add(SLOW_TREND_STRENGTH, 85, 105, 5);
		params.add(EXIT, 90, 100, 5);
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

		double diff = fastTrend - slowTrend;

		int currentPosition = getPositionManager().getPosition();
		boolean target = (currentPosition > 0 && diff > exit);
		target = target || (currentPosition < 0 && diff < -exit);

		if (target) {
			position = 0;
		} else {
			if (fastTrend > fastTrendStrength && slowTrend < -slowTrendStrength) {
				position = -1;// slow trend is down, fast trend is up
			} else if (fastTrend < -fastTrendStrength
					&& slowTrend > slowTrendStrength) {
				position = 1;// slow term trend is up, fast trend is down
			}
		}
	}
}
