package org.lifeform.optimizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.lifeform.configuration.Defaults;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.strategy.Strategy;

/**
 * Runs a trading strategy in the optimizer mode using a data file containing
 * historical market depth.
 */
public class DivideAndConquerOptimizerRunner extends OptimizerRunner {

	public DivideAndConquerOptimizerRunner(final OptimizerDialog optimizerDialog,
			final Strategy strategy, final StrategyParams params) throws Exception {
		super(optimizerDialog, strategy, params);
	}

	public void optimize() throws Exception {
		List<StrategyParams> topParams = new ArrayList<StrategyParams>();
		StrategyParams startingParams = new StrategyParams(strategyParams);
		topParams.add(startingParams);
		int dimensions = strategyParams.size();
		HashSet<String> uniqueParams = new HashSet<String>();

		int maxRange = 0;
		for (StrategyParam param : startingParams.getAll()) {
			maxRange = Math.max(maxRange, (int) (param.getMax() - param
					.getMin()));
		}

		int divider = 3;
		int iterationsRemaining = 1 + (int) (Math.log(maxRange) / Math
				.log(divider / 2.));

		long completedSteps = 0;
		LinkedList<StrategyParams> tasks = new LinkedList<StrategyParams>();
		Queue<StrategyParams> filteredTasks = new LinkedBlockingQueue<StrategyParams>();
		PreferencesHolder prefs = PreferencesHolder.getInstance();
		int chunkSize = 100 * prefs.getInt(Defaults.DivideAndConquerCoverage);
		int numberOfCandidates = Math.max(1, (int) (chunkSize / Math.pow(
				divider, dimensions)));
		int filteredTasksSize;

		do {
			tasks.clear();
			int maxPartsPerDimension = (topParams.size() == 1) ? Math.max(
					divider, (int) Math.pow(chunkSize, 1. / dimensions))
					: divider;

			for (StrategyParams params : topParams) {
				for (StrategyParam param : params.getAll()) {
					int step = Math.max(1, (int) (param.getMax() - param
							.getMin())
							/ (maxPartsPerDimension - 1));
					param.setStep(step);
				}
				tasks.addAll(getTasks(params));
			}

			filteredTasks.clear();
			for (StrategyParams params : tasks) {
				String key = params.getKey();
				if (!uniqueParams.contains(key)) {
					uniqueParams.add(key);
					filteredTasks.add(params);
				}
			}

			long totalSteps = completedSteps + snapshotCount
					* iterationsRemaining * filteredTasks.size();
			setTotalSteps(totalSteps);
			filteredTasksSize = filteredTasks.size();
			setTotalStrategies(filteredTasksSize);
			execute(filteredTasks);

			iterationsRemaining--;
			completedSteps += snapshotCount * filteredTasks.size();

			if (optimizationResults.size() == 0 && !cancelled) {
				throw new Exception(
						"No strategies found within the specified parameter boundaries.");
			}

			topParams.clear();

			int maxIndex = Math.min(numberOfCandidates, optimizationResults
					.size());
			for (int index = 0; index < maxIndex; index++) {
				StrategyParams params = optimizationResults.get(index)
						.getParams();
				for (StrategyParam param : params.getAll()) {
					String name = param.getName();
					double value = param.getValue();
					int displacement = (int) Math.ceil(param.getStep()
							/ (double) divider);
					StrategyParam originalParam = strategyParams.get(name);
					// Don't push beyond the user-specified boundaries
					param.setMin(Math.max(originalParam.getMin(), value
							- displacement));
					param.setMax(Math.min(originalParam.getMax(), value
							+ displacement));
				}
				topParams.add(new StrategyParams(params));
			}

		} while (filteredTasksSize > 0 && !cancelled);
	}
}
