package org.lifeform.backdata;

import org.lifeform.optimizer.StrategyParams;
import org.lifeform.strategy.Strategy;
import org.lifeform.time.TradingInterval;

/**
 * This is simply a fake strategy. Its only purpose is to pretend to be a real
 * one so that it can be passed through the framework.
 */
public class DownloaderStrategy extends Strategy {
	@Override
	public void onBar() {
		// nothing to do
	}

	@Override
	public StrategyParams initParams() {
		return new StrategyParams();
	}

	@Override
	public TradingInterval initTradingInterval() throws Exception {
		return new TradingInterval();
	}

}
