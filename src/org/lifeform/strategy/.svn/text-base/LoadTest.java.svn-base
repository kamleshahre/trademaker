package org.lifeform.strategy;

import org.lifeform.market.BarSize;
import org.lifeform.optimizer.StrategyParams;
import org.lifeform.time.TradingInterval;
import org.lifeform.util.ContractFactory;

import com.ib.client.Contract;

/**
 * This is a strategy whose only purpose is to test JST components under load.
 * It makes a trade every minute.
 */
public class LoadTest extends Strategy {

	public LoadTest(final StrategyParams params) throws Exception {
		Contract contract = ContractFactory.makeFutureContract("ES", "GLOBEX");
		setStrategy(contract, BarSize.Min1, false);
	}

	@Override
	public StrategyParams initParams() {
		return new StrategyParams();
	}

	@Override
	public TradingInterval initTradingInterval() throws Exception {
		return new TradingInterval("00:16", "23:44", "America/New_York", false);
	}

	@Override
	public void onBar() {
		if (position == 0) {
			position = 1;
		} else {
			position = -position;
		}
	}
}
