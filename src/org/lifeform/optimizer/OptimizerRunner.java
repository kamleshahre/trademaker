package org.lifeform.optimizer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.lifeform.backdata.BackTestFileReader;
import org.lifeform.market.MarketSnapshot;
import org.lifeform.report.Report;
import org.lifeform.strategy.Strategy;
import org.lifeform.util.AppUtil;
import org.lifeform.util.ComputationalTimeEstimator;
import org.lifeform.util.NumberFormatterFactory;

/**
 * Runs a trading strategy in the optimizer mode using a data file containing
 * historical market depth.
 */
public abstract class OptimizerRunner implements Runnable {
	protected final List<OptimizationResult> optimizationResults;
	protected final StrategyParams strategyParams;
	protected long snapshotCount;
	protected boolean cancelled;
	protected final int availableProcessors;

	private static final int MAX_SAVED_RESULTS = 100;// max number of results in
	// the optimization
	// results file
	private final Constructor<?> strategyConstructor;
	private final ScheduledExecutorService progressExecutor,
			resultsTableExecutor;
	private ExecutorService optimizationExecutor;
	private final NumberFormat nf2, nf0, gnf0;
	private final String strategyName;
	private final int minTrades;
	private final OptimizerDialog optimizerDialog;
	private ResultComparator resultComparator;
	private ComputationalTimeEstimator timeEstimator;
	private final List<MarketSnapshot> snapshots;
	private long completedSteps, totalSteps;
	private String totalStrategiesString;
	private long previousResultsSize;

	class ProgressRunner implements Runnable {
		public void run() {
			if (completedSteps > 0) {
				showFastProgress(completedSteps, "Optimizing "
						+ totalStrategiesString + " strategies");
			}
		}
	}

	class ResultsTableRunner implements Runnable {
		public void run() {
			int size = optimizationResults.size();
			if (size > previousResultsSize) {
				optimizerDialog.setResults(optimizationResults);
				previousResultsSize = size;
			}
		}
	}

	OptimizerRunner(final OptimizerDialog optimizerDialog, final Strategy strategy,
			final StrategyParams params) throws Exception {
		this.optimizerDialog = optimizerDialog;
		this.strategyName = strategy.getName();
		this.strategyParams = params;
		optimizationResults = new ArrayList<OptimizationResult>();
		snapshots = new LinkedList<MarketSnapshot>();
		nf2 = NumberFormatterFactory.getNumberFormatter(2);
		nf0 = NumberFormatterFactory.getNumberFormatter(0);
		gnf0 = NumberFormatterFactory.getNumberFormatter(0, true);
		availableProcessors = Runtime.getRuntime().availableProcessors();

		Class<?> clazz;
		try {
			clazz = Class.forName(strategy.getClass().getName());
		} catch (ClassNotFoundException cnfe) {
			throw new Exception("Could not find class "
					+ strategy.getClass().getName());
		}
		Class<?>[] parameterTypes = new Class[] { StrategyParams.class };

		try {
			strategyConstructor = clazz.getConstructor(parameterTypes);
		} catch (NoSuchMethodException nsme) {
			throw new Exception("Could not find strategy constructor for "
					+ strategy.getClass().getName());
		}

		resultComparator = new ResultComparator(optimizerDialog
				.getSortCriteria());
		minTrades = optimizerDialog.getMinTrades();
		progressExecutor = Executors.newSingleThreadScheduledExecutor();
		resultsTableExecutor = Executors.newSingleThreadScheduledExecutor();
		optimizationExecutor = Executors
				.newFixedThreadPool(availableProcessors);
	}

	public Strategy getStrategyInstance(final StrategyParams params) throws Exception {
		try {
			return (Strategy) strategyConstructor.newInstance(params);
		} catch (InvocationTargetException ite) {
			throw new Exception(ite.getCause());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	protected abstract void optimize() throws Exception;

	protected void setTotalSteps(final long totalSteps) {
		this.totalSteps = totalSteps;
		if (timeEstimator == null) {
			timeEstimator = new ComputationalTimeEstimator(System
					.currentTimeMillis(), totalSteps);
		}
		timeEstimator.setTotalIterations(totalSteps);
	}

	protected void setTotalStrategies(final long totalStrategies) {
		this.totalStrategiesString = gnf0.format(totalStrategies);
	}

	public int getMinTrades() {
		return minTrades;
	}

	public List<MarketSnapshot> getSnapshots() {
		return snapshots;
	}

	public void addResults(final List<OptimizationResult> results) {
		synchronized (optimizationResults) {
			optimizationResults.addAll(results);
			Collections.sort(optimizationResults, resultComparator);
		}
	}

	void execute(final Queue<StrategyParams> tasks) throws Exception {
		if (tasks.size() != 0) {
			Set<Callable<List<OptimizationResult>>> workers = new HashSet<Callable<List<OptimizationResult>>>();
			for (int worker = 0; worker < availableProcessors; worker++) {
				// Callable<List<OptimizationResult>> optimizerWorker = new
				// OptimizerWorker(
				// this, tasks);
				// workers.add(optimizerWorker);
			}

			try {
				// this blocks until all workers are done
				List<Future<List<OptimizationResult>>> futureResults = optimizationExecutor
						.invokeAll(workers);
			} catch (InterruptedException ie) {
				throw new Exception(ie);
			}
		}
	}

	public void cancel() {
		optimizerDialog.showProgress("Stopping optimization...");
		cancelled = true;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	private void saveToFile() throws Exception {
		if (optimizationResults.size() == 0) {
			return;
		}

		Report.enable();
		String fileName = strategyName + "Optimizer";
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
		StrategyParams params = optimizationResults.iterator().next()
				.getParams();
		for (StrategyParam param : params.getAll()) {
			otpimizerReportHeaders.add(param.getName());
		}

		for (PerformanceMetric performanceMetric : PerformanceMetric.values()) {
			otpimizerReportHeaders.add(performanceMetric.getName());
		}
		optimizerReport.report(otpimizerReportHeaders);

		int maxIndex = Math.min(MAX_SAVED_RESULTS, optimizationResults.size());
		for (int index = 0; index < maxIndex; index++) {
			OptimizationResult optimizationResult = optimizationResults
					.get(index);
			params = optimizationResult.getParams();

			List<Object> columns = new ArrayList<Object>();
			for (StrategyParam param : params.getAll()) {
				columns.add(param.getValue());
			}

			columns.add(optimizationResult.getTrades());
			columns.add(nf0.format(optimizationResult.getNetProfit()));
			columns.add(nf0.format(optimizationResult.getMaxDrawdown()));
			columns.add(nf2.format(optimizationResult.getProfitFactor()));
			columns.add(nf0.format(optimizationResult.getKellyCriterion()));
			columns.add(nf2.format(optimizationResult.getPerformanceIndex()));

			optimizerReport.report(columns);
		}
		Report.disable();
	}

	private void showFastProgress(final long counter, final String text) {
		// String remainingTime = (counter == totalSteps) ? "00:00:00"
		// : timeEstimator.getTimeLeft(counter);
		optimizerDialog.setProgress(counter, totalSteps, text);
	}

	public synchronized void iterationsCompleted(final long iterationsCompleted) {
		completedSteps += iterationsCompleted;
	}

	protected Queue<StrategyParams> getTasks(final StrategyParams params) {
		for (StrategyParam param : params.getAll()) {
			param.setValue(param.getMin());
		}

		Queue<StrategyParams> tasks = new LinkedBlockingQueue<StrategyParams>();

		boolean allTasksAssigned = false;
		while (!allTasksAssigned && !cancelled) {
			StrategyParams strategyParamsCopy = new StrategyParams(params);
			tasks.add(strategyParamsCopy);

			StrategyParam lastParam = params.get(params.size() - 1);
			lastParam.setValue(lastParam.getValue() + lastParam.getStep());

			for (int paramNumber = params.size() - 1; paramNumber >= 0; paramNumber--) {
				StrategyParam param = params.get(paramNumber);
				if (param.getValue() > param.getMax()) {
					param.setValue(param.getMin());
					if (paramNumber == 0) {
						allTasksAssigned = true;
						break;
					} else {
						int prevParamNumber = paramNumber - 1;
						StrategyParam prevParam = params.get(prevParamNumber);
						prevParam.setValue(prevParam.getValue()
								+ prevParam.getStep());
					}
				}
			}
		}

		return tasks;
	}

	public void run() {
		try {
			optimizationResults.clear();
			optimizerDialog.setResults(optimizationResults);
			optimizerDialog.enableProgress();
			optimizerDialog.showProgress("Scanning historical data file...");
			BackTestFileReader backTestFileReader = new BackTestFileReader(
					optimizerDialog.getFileName());
			backTestFileReader.setFilter(optimizerDialog.getDateFilter());
			backTestFileReader.scan();
			snapshotCount = backTestFileReader.getSnapshotCount();

			MarketSnapshot marketSnapshot;
			long count = 0;
			String progressMessage = "Loading historical data file: ";
			while ((marketSnapshot = backTestFileReader.next()) != null) {
				snapshots.add(marketSnapshot);
				count++;
				if (count % 50000 == 0) {
					optimizerDialog.setProgress(count, snapshotCount,
							progressMessage);
				}
				if (cancelled) {
					return;
				}
			}

			optimizerDialog.showProgress("Starting optimization ...");
			progressExecutor.scheduleWithFixedDelay(new ProgressRunner(), 0, 1,
					TimeUnit.SECONDS);
			resultsTableExecutor.scheduleWithFixedDelay(
					new ResultsTableRunner(), 0, 30, TimeUnit.SECONDS);
			long start = System.currentTimeMillis();
			optimize();
			long end = System.currentTimeMillis();
			progressExecutor.shutdownNow();
			resultsTableExecutor.shutdownNow();

			optimizerDialog.setResults(optimizationResults);

			if (!cancelled) {
				optimizerDialog.showProgress("Saving optimization results ...");
				saveToFile();
				long totalTimeInSecs = (end - start) / 1000;
				showFastProgress(totalSteps, "Optimization");
				AppUtil.showMessage("Optimization completed successfully in "
						+ totalTimeInSecs + " seconds.");
			}
		} catch (Throwable t) {
			AppUtil.showError(t);
		} finally {
			progressExecutor.shutdownNow();
			resultsTableExecutor.shutdownNow();
			optimizationExecutor.shutdownNow();
			optimizerDialog.signalCompleted();
		}
	}
}
