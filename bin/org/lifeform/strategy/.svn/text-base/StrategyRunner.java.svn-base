package org.lifeform.strategy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import org.lifeform.market.BarFactory;
import org.lifeform.market.QuoteHistory;
import org.lifeform.market.QuoteHistoryEvent;
import org.lifeform.position.PositionManager;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.ErrorListener;
import org.lifeform.service.ModelListener;
import org.lifeform.trader.Trader;

import com.ib.client.Contract;

/**
 * Runs a trading strategy. There is a one-to-one map between the strategy class
 * and the strategy runner. That is, if 5 strategies are selected to run, there
 * will be 5 instances of the StrategyRunner created.
 */

public class StrategyRunner implements Runnable, ErrorListener {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");
	private final Trader trader;
	private final Strategy strategy;
	private final Report eventReport;
	private final QuoteHistory qh;
	private final LinkedList<QuoteHistoryEvent> quoteHistoryEvents;
	private final PositionManager positionManager;
	private boolean isConnected;

	public void error(final int errorCode, final String errorMsg) {

		if (errorCode == 1100) {// Connectivity between IB and TWS has been
			// lost.
			isConnected = false;
		}

		boolean hasQueryExpired = (errorCode == 320 && errorMsg
				.contains("Invalid Real-time Query"));
		boolean isRestoredWithDataLost = (errorCode == 1101);// "Connectivity between IB and TWS has been restored- data lost."

		if (hasQueryExpired || isRestoredWithDataLost) {
			eventReport.report(strategy.getName()
					+ ": Re-subscribing to real time bars");
			getRealTimeBars();
		}

		boolean isConnectivityRestored = (errorCode == 1101 || errorCode == 1102);
		if (isConnectivityRestored) {
			isConnected = true;
		}
	}

	public StrategyRunner(final Strategy strategy) throws IOException, Exception {
		isConnected = true;
		this.strategy = strategy;
		eventReport = Dispatcher.getReporter();

		Report strategyReport = new Report(strategy.getName());
		strategyReport.report(strategy.getStrategyReportHeaders());
		strategy.setReport(strategyReport);

		trader = Dispatcher.getTrader();
		trader.getAssistant().addStrategy(strategy);
		positionManager = strategy.getPositionManager();
		qh = strategy.getQuoteHistory();
		quoteHistoryEvents = qh.getEvents();
		trader.addErrorListener(this);
		eventReport.report(strategy.getName() + ": strategy started");
	}

	private void getRealTimeBars() {
		Contract contract = strategy.getContract();
		int strategyID = strategy.getId();
		String priceType = strategy.getQuoteHistory().getIsForex() ? "MIDPOINT"
				: "TRADES";
		trader.getAssistant().getRealTimeBars(strategyID, contract, 5,
				priceType, false);
	}

	private void getHistoricalData() throws InterruptedException {
		int onlyRTHPriceBars = strategy.getOnlyRTHPriceBars() ? 1 : 0;
		String barSize = strategy.getBarSize().toIBText();
		String histRequestDuration = strategy.getBarSize()
				.getHistRequestDuration();
		Contract contract = strategy.getContract();
		int strategyID = strategy.getId();
		String timeNow;

		Calendar cal = strategy.getCalendar();

		/*
		 * IB's historical data server detects a "pacing violation" when
		 * multiple requests for the same ticker ending with the same time are
		 * issued, even if these requests are spaced out. Since it's possible
		 * that multiple strategies run at the same time and request the same
		 * historical data, we simply add a few seconds to the "end time" to get
		 * around this limitation. The strategy ID is a unique number, so it
		 * will guarantee that the end time is unique.
		 */
		cal.add(Calendar.SECOND, strategy.getId());

		synchronized (dateFormat) {
			timeNow = dateFormat.format(cal.getTime());
		}

		String priceType = strategy.getQuoteHistory().getIsForex() ? "MIDPOINT"
				: "TRADES";

		trader.getAssistant().getHistoricalData(strategyID, contract, timeNow,
				histRequestDuration, barSize, priceType, onlyRTHPriceBars, 2);
		synchronized (trader) {
			while (!qh.getIsHistRequestCompleted()) {
				// wait until the entire price bar history is returned
				trader.wait();
			}
		}
	}

	public void run() {
		try {

			if (!strategy.getTradingSchedule().isTimeToTrade()) {
				eventReport
						.report(strategy.getName()
								+ ": Waiting for the start of the first trading interval.");
				strategy.getTradingSchedule().waitForTradingInterval();
				eventReport.report(strategy.getName()
						+ ": Trading interval started.");
			}

			getHistoricalData();
			new BarFactory(strategy);
			getRealTimeBars();

			QuoteHistoryEvent quoteHistoryEvent;
			while (strategy.isActive()) {
				synchronized (quoteHistoryEvents) {
					while (quoteHistoryEvents.isEmpty()) {
						quoteHistoryEvents.wait();
					}
					// Events are processed on a "first in, first out" basis
					quoteHistoryEvent = quoteHistoryEvents.removeFirst();
				}
				processEvent(quoteHistoryEvent);
			}

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

	private void processEvent(final QuoteHistoryEvent quoteHistoryEvent) {
		QuoteHistoryEvent.EventType eventType = quoteHistoryEvent.getType();

		if (eventType == QuoteHistoryEvent.EventType.NEW_BAR) {
			Dispatcher.fireModelChanged(ModelListener.Event.STRATEGY_UPDATE,
					strategy);
			// Recalculate and validate indicators even if we are outside of the
			// trading interval.
			// However, don't do it on every snapshot.
			strategy.validateIndicators();
		}

		if (strategy.canTrade()) {
			switch (eventType) {
			case NEW_BAR:
				if (strategy.hasValidIndicators()) {
					strategy.onBar();
				}
				break;
			case MARKET_CHANGE:
				strategy.onMarketChange();
				break;
			}
		}

		if (isConnected && !positionManager.isOrderExecutionPending()) {
			positionManager.trade();
		}

		if (eventType == QuoteHistoryEvent.EventType.NEW_BAR
				&& !positionManager.isOrderExecutionPending()) {
			strategy.update();
		}

	}
}
