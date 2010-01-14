package org.lifeform.optimizer;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.lifeform.backdata.BackTestStrategyRunner;
import org.lifeform.position.PositionManager;
import org.lifeform.strategy.Strategy;
import org.lifeform.util.AppUtil;

/**
 */
public class OptimizerWorker extends BackTestStrategyRunner {
	private final List<Result> results;
	private final int minTrades;
	private final CountDownLatch remainingTasks;
	private final Constructor<?> strategyConstructor;
	private final LinkedList<StrategyParams> tasks;

	public OptimizerWorker(final Constructor<?> strategyConstructor,
			final LinkedList<StrategyParams> tasks, final List results, final int minTrades,
			final CountDownLatch remainingTasks) throws Exception {
		super(null);
		this.results = results;
		this.minTrades = minTrades;
		this.remainingTasks = remainingTasks;
		this.strategyConstructor = strategyConstructor;
		this.tasks = tasks;
	}

	@Override
	public void run() {
		StrategyParams params;

		try {
			while (true) {
				synchronized (tasks) {
					if (tasks.isEmpty())
						break;
					params = tasks.removeFirst();
				}

				Strategy strategy = (Strategy) strategyConstructor
						.newInstance(params);
				setStrategy(strategy);
				backTest();

				PositionManager positionManager = strategy.getPositionManager();
				int trades = positionManager.getTrades();

				if (trades >= minTrades) {
					double totalPL = positionManager.getTotalProfitAndLoss();
					double profitFactor = positionManager.getProfitFactor();
					double maxDrawdown = positionManager.getMaxDrawdown();
					double kelly = positionManager.getKelly();
					String tradeDistribution = strategy.getTradeDistribution();

					Result result = new Result(params, totalPL, maxDrawdown,
							trades, profitFactor, kelly, tradeDistribution);
					synchronized (results) {
						results.add(result);
					}
				}

				synchronized (remainingTasks) {
					remainingTasks.countDown();
				}

			}
		} catch (Exception t) {
			eventReport.report(t);
			String msg = "Encountered unexpected error while running strategy optimizer: "
					+ t.getMessage();
			AppUtil.showError(t);
		}
	}
}
