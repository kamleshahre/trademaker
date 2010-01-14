package org.lifeform.strategy;

import org.lifeform.chart.indicator.ForceIndex;
import org.lifeform.chart.indicator.Indicator;
import org.lifeform.chart.indicator.Slope;
import org.lifeform.market.BarSize;
import org.lifeform.optimizer.StrategyParams;
import org.lifeform.time.TradingInterval;
import org.lifeform.util.ContractFactory;

import com.ib.client.Contract;

/**
 * This sample strategy trades AAPL stock using the slope of Force Index as an
 * indicator. When the slope is upward, the strategy buys. Otherwise, it sells
 * short. This is a trend following system.
 */
public class SlopeOfForce extends Strategy {

	// Technical indicators
	private final Indicator forceIndexInd, slopeOfForceInd;

	// Strategy parameters names
	private static final String PERIOD_LENGTH = "trend length";
	private static final String THRESHOLD = "threshold";

	// Strategy parameters values
	private final int periodLength, threshold;

	public SlopeOfForce(final StrategyParams params) throws Exception {
		// Define the contract to trade
		Contract contract = ContractFactory.makeStockContract("AAPL", "SMART",
				"USD");
		setStrategy(contract, BarSize.Min5, false);

		// Initialize strategy parameter values. If the strategy is running in
		// the optimization
		// mode, the parameter values will be taken from the "params" object.
		// Otherwise, the
		// "params" object will be empty and the parameter values will be
		// initialized to the
		// specified default values.
		periodLength = (int) params.get(PERIOD_LENGTH, 50);
		threshold = (int) params.get(THRESHOLD, 8000);

		// Instantiate technical indicators
		forceIndexInd = new ForceIndex(quoteHistory);
		slopeOfForceInd = new Slope(forceIndexInd, periodLength);

		// Specify the title and the chart number for each indicator
		// "0" = the same chart as the price chart; "1+" = separate subchart
		// (below the price chart)
		addIndicator("Force Index", forceIndexInd, 1);
		addIndicator("Slope of Force", slopeOfForceInd, 2);
	}

	/**
	 * Returns min/max/step values for each strategy parameter. This method is
	 * invoked by the strategy optimizer to obtain the strategy parameter
	 * ranges.
	 */
	@Override
	public StrategyParams initParams() {
		StrategyParams params = new StrategyParams();
		params.add(PERIOD_LENGTH, 10, 200, 10);
		params.add(THRESHOLD, 0, 10000, 100);
		return params;
	}

	/**
	 * Define the trading interval and the time zone for that interval
	 */
	@Override
	public TradingInterval initTradingInterval() throws Exception {
		return new TradingInterval("9:45", "15:45", "America/New_York", false);
	}

	/**
	 * This method is invoked by the framework when a new bar is completed and
	 * the technical indicators are recalculated. This is where the strategy
	 * itself should be defined.
	 */
	@Override
	public void onBar() {
		double slopeOfForce = slopeOfForceInd.getValue();
		if (slopeOfForce > threshold) {
			position = 10;
		} else if (slopeOfForce < -threshold) {
			position = -10;
		}
	}
}
