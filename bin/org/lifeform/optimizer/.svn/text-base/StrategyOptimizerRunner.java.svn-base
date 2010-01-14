package org.lifeform.optimizer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.lifeform.configuration.Defaults;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.strategy.Strategy;
import org.lifeform.util.AppUtil;
import org.lifeform.util.ComputationalTimeEstimator;

/**
 * Runs a trading strategy in the optimizer mode using a data file containing
 * historical price bars.
 */
public class StrategyOptimizerRunner extends Thread {
	private static final long MAX_ITERATIONS = 50000000L;
	private static final int MAX_RESULTS = 5000;
	private static final long UPDATE_FREQUENCY = 5000L;// milliseconds
	private final List<Result> results;
	private final OptimizerDialog optimizerDialog;
	private final NumberFormat nf2;
	private boolean cancelled;
	private ResultComparator resultComparator;
	private final StrategyParams strategyParams;
	private ComputationalTimeEstimator timeEstimator;
	private final long iterations;
	private final Constructor<?> strategyConstructor;
	private LinkedList<StrategyParams> tasks;

	public StrategyOptimizerRunner(final OptimizerDialog optimizerDialog,
			final String strategyName, final StrategyParams strategyParams)
			throws ClassNotFoundException, NoSuchMethodException {
		this.optimizerDialog = optimizerDialog;
		this.strategyParams = strategyParams;
		iterations = strategyParams.iterations();
		results = Collections.synchronizedList(new ArrayList<Result>());
		nf2 = NumberFormat.getNumberInstance();
		nf2.setMaximumFractionDigits(2);
		Class<?> clazz = Class.forName(strategyName);
		Class<?>[] parameterTypes = new Class[] { StrategyParams.class };
		strategyConstructor = clazz.getConstructor(parameterTypes);
	}

	public void cancel() {
		optimizerDialog.showProgress("Stopping running processes...");
		tasks.clear();
		cancelled = true;
	}

	private void saveToFile(final Strategy strategy) throws IOException, Exception {
		if (results.size() == 0) {
			return;
		}

		Report.enable();
		String fileName = strategy.getName() + "Optimizer";
		Report optimizerReport = new Report(fileName);

		optimizerReport.reportDescription("Strategy parameters:");
		for (StrategyParam param : strategyParams.getAll()) {
			optimizerReport.reportDescription(param.toString());
		}
		optimizerReport
				.reportDescription("Minimum trades for strategy inclusion: "
						+ optimizerDialog.getMinTrades());
		optimizerReport.reportDescription("Back data file: "
				+ optimizerDialog.getFileName());

		List<String> otpimizerReportHeaders = new ArrayList<String>();
		StrategyParams params = results.iterator().next().getParams();
		for (StrategyParam param : params.getAll()) {
			otpimizerReportHeaders.add(param.getName());
		}

		otpimizerReportHeaders.add("Total P&L");
		otpimizerReportHeaders.add("Max Drawdown");
		otpimizerReportHeaders.add("Trades");
		otpimizerReportHeaders.add("Profit Factor");
		otpimizerReportHeaders.add("Kelly");
		otpimizerReportHeaders.add("Trade Distribution");
		optimizerReport.report(otpimizerReportHeaders);

		for (Result result : results) {
			params = result.getParams();

			List<String> columns = new ArrayList<String>();
			for (StrategyParam param : params.getAll()) {
				columns.add(nf2.format(param.getValue()));
			}

			columns.add(nf2.format(result.getTotalProfit()));
			columns.add(nf2.format(result.getMaxDrawdown()));
			columns.add(nf2.format(result.getTrades()));
			columns.add(nf2.format(result.getProfitFactor()));
			columns.add(nf2.format(result.getKelly()));
			columns.add(result.getTradeDistribution());

			optimizerReport.report(columns);
		}
		Report.disable();
	}

	private void showProgress(final long counter) {
		synchronized (results) {
			Collections.sort(results, resultComparator);

			while (results.size() > MAX_RESULTS) {
				results.remove(results.size() - 1);
			}

			optimizerDialog.setResults(results);
		}

		if (counter > 50) {
			// Wait until 50 steps completed. Otherwise, the estimated remaining
			// time will be inaccurate.
			String remainingTime = timeEstimator.getTimeLeft(counter);
			optimizerDialog.setProgress(counter, iterations, remainingTime);
		}
	}

	public void run() {
		try {
			Strategy strategy = (Strategy) strategyConstructor
					.newInstance(new StrategyParams());

			for (StrategyParam param : strategyParams.getAll()) {
				param.setValue(param.getMin());
			}

			if (iterations > MAX_ITERATIONS) {
				optimizerDialog.showMaxIterationsLimit(iterations,
						MAX_ITERATIONS);
				return;
			}

			optimizerDialog.enableProgress();
			int minTrades = optimizerDialog.getMinTrades();
			resultComparator = new ResultComparator(optimizerDialog
					.getSortCriteria());

			boolean allTasksAssigned = false;
			cancelled = false;

			CountDownLatch remainingTasks = new CountDownLatch((int) iterations);

			tasks = new LinkedList<StrategyParams>();

			while (!allTasksAssigned) {
				StrategyParams strategyParamsCopy = new StrategyParams(
						strategyParams);
				tasks.add(strategyParamsCopy);

				StrategyParam lastParam = strategyParams.get(strategyParams
						.size() - 1);
				lastParam.setValue(lastParam.getValue() + lastParam.getStep());

				for (int paramNumber = strategyParams.size() - 1; paramNumber >= 0; paramNumber--) {
					StrategyParam param = strategyParams.get(paramNumber);
					if (param.getValue() > param.getMax()) {
						param.setValue(param.getMin());
						if (paramNumber == 0) {
							allTasksAssigned = true;
							break;
						} else {
							int prevParamNumber = paramNumber - 1;
							StrategyParam prevParam = strategyParams
									.get(prevParamNumber);
							prevParam.setValue(prevParam.getValue()
									+ prevParam.getStep());
						}
					}
				}
			}

			optimizerDialog.showProgress("Distributing the tasks...");
			PreferencesHolder jstProperties = PreferencesHolder.getInstance();
			int maxThreads = Integer.valueOf(jstProperties
					.get(Defaults.OptimizerMaxThread));
			for (int thread = 0; thread < maxThreads; thread++) {
				new OptimizerWorker(strategyConstructor, tasks, results,
						minTrades, remainingTasks).start();
			}

			optimizerDialog.showProgress("Estimating remaining time...");
			long startTime = System.currentTimeMillis();
			timeEstimator = new ComputationalTimeEstimator(startTime,
					iterations);

			long remaining;
			do {
				Thread.sleep(UPDATE_FREQUENCY);
				remaining = remainingTasks.getCount();
				showProgress(iterations - remaining);// results in progress
			} while (remaining != 0 && !cancelled);

			if (!cancelled) {
				showProgress(iterations);// final results
				long endTime = System.currentTimeMillis();
				long totalTimeInSecs = (endTime - startTime) / 1000;
				saveToFile(strategy);
				AppUtil.showMessage(optimizerDialog,
						"Optimization completed successfully in "
								+ totalTimeInSecs + " seconds.");
			}

		} catch (Exception t) {
			Dispatcher.getReporter().report(t);
			AppUtil.showError(optimizerDialog, t.toString());
		} finally {
			optimizerDialog.signalCompleted();
		}
	}
}
