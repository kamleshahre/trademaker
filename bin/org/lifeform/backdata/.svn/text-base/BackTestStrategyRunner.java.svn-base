package org.lifeform.backdata;

import java.io.IOException;
import java.util.Calendar;

import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;
import org.lifeform.position.PositionManager;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.strategy.Strategy;

/**
 * Runs a trading strategy in the backtesting mode using a data file containing
 * historical price bars. There is a one-to-one map between the strategy class
 * and the strategy runner. That is, if 5 strategies are selected to run, there
 * will be 5 instances of the StrategyRunner created.
 */
public class BackTestStrategyRunner extends Thread {
	private Strategy strategy;
	protected final Report eventReport;
	private final BackTestTraderAssistant backTestAssistant;
	private final boolean isOptimized;

	public BackTestStrategyRunner(final Strategy strategy) throws IOException,
			Exception {
		this.strategy = strategy;
		isOptimized = (Dispatcher.getMode() == Dispatcher.Mode.OPTIMIZATION);
		eventReport = Dispatcher.getReporter();
		backTestAssistant = (BackTestTraderAssistant) Dispatcher.getTrader()
				.getAssistant();

		if (!isOptimized) {
			backTestAssistant.addStrategy(strategy);
			eventReport.report(strategy.getName() + ": strategy started");
			Report strategyReport = new Report(strategy.getName());
			strategyReport.report(strategy.getStrategyReportHeaders());
			strategy.setReport(strategyReport);
		}
	}

	protected void setStrategy(final Strategy strategy) {
		this.strategy = strategy;
	}

	protected void backTest() {

		QuoteHistory qh = strategy.getQuoteHistory();
		PositionManager positionManager = strategy.getPositionManager();
		Calendar calendar = strategy.getCalendar();

		for (PriceBar priceBar : backTestAssistant.priceBars) {
			qh.addHistoricalPriceBar(priceBar);
			calendar.setTimeInMillis(priceBar.getDate());

			// recalculate and validate indicators even if we are outside of the
			// trading interval
			strategy.validateIndicators();
			if (strategy.canTrade()) {
				if (strategy.hasValidIndicators()) {
					strategy.onBar();
				}
			}
			positionManager.trade();

			if (!isOptimized) {
				strategy.update();
			}
		}

		// go flat at the end of the test period
		strategy.closeOpenPositions();
		positionManager.trade();
		if (!isOptimized) {
			strategy.update();
		}
	}

	@Override
	public void run() {
		try {
			backTest();
			eventReport.report(strategy.getName() + ": is now inactive.");
		} catch (Exception t) {
			/*
			 * Exceptions should never happen. If an exception of any type
			 * occurs, it would indicate a serious JSystemTrader bug, and there
			 * is nothing we can do to recover at runtime. Report the error for
			 * the "after-run" analysis.
			 */
			eventReport.report(t);
		} finally {
			Dispatcher.strategyCompleted();
		}

	}
}
