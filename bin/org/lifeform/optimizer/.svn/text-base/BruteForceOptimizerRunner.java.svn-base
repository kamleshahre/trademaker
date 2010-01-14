package org.lifeform.optimizer;

import java.util.Queue;

import org.lifeform.strategy.Strategy;

/**
 */
public class BruteForceOptimizerRunner extends OptimizerRunner {

	public BruteForceOptimizerRunner(final OptimizerDialog optimizerDialog,
			final Strategy strategy, final StrategyParams params) throws Exception {
		super(optimizerDialog, strategy, params);
	}

	@Override
	public void optimize() throws Exception {
		Queue<StrategyParams> tasks = getTasks(strategyParams);
		int taskSize = tasks.size();
		setTotalSteps(snapshotCount * taskSize);
		setTotalStrategies(taskSize);
		execute(tasks);
	}
}
