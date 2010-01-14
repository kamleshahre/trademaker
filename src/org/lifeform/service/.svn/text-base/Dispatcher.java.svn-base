package org.lifeform.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.lifeform.gui.dialog.TradingModeDialog;
import org.lifeform.optimizer.OptimizerDialog;
import org.lifeform.report.Report;
import org.lifeform.trader.Trader;

/**
 * Acts as the dispatcher of the services.
 */
public class Dispatcher {
	public enum Mode {
		TRADE, BACK_TEST, OPTIMIZATION
	}

	private static final List<ModelListener> listeners = new ArrayList<ModelListener>();
	private static Report eventReport;
	private static Trader trader;
	private static Mode mode;
	private static CountDownLatch activeStrategies;

	static{
		try{
			eventReport = new Report("report.txt");
		} catch(Exception e) {
		}
	}

	public static void setReporter(final String eventReportFileName)
			throws IOException, Exception {
		eventReport = new Report(eventReportFileName);
	}

	public static void addListener(final ModelListener listener) {
		listeners.add(listener);
	}

	public static Trader getTrader() {
		return trader;
	}

	public static Report getReporter() {
		return eventReport;
	}

	public static Mode getMode() {
		return mode;
	}

	public static void exit() {
		if (trader != null) {
			trader.getAssistant().disconnect();
		}
		System.exit(0);
	}

	public static void setTradingMode() throws Exception {
		Dispatcher.mode = Mode.TRADE;
		Report.enable();
		trader = new Trader();

	}

	public static void setBackTestingMode(final TradingModeDialog tradingModeDialog)
			throws Exception {
		if (trader != null) {
			trader.getAssistant().disconnect();
		}
		Dispatcher.mode = Mode.BACK_TEST;
		if (tradingModeDialog.isReportEnabled()) {
			Report.enable();
		} else {
			Report.disable();
		}

		trader = new Trader(tradingModeDialog.getFileName());

	}

	public static void switchMode(final OptimizerDialog od) throws Exception {
		if (trader != null) {
			trader.getAssistant().disconnect();
		}
		Dispatcher.mode = Mode.OPTIMIZATION;
		// Disable all reporting when JST runs in optimization mode. The
		// optimizer runs
		// thousands of strategies, and the amount of data to report would be
		// enormous.
		Report.disable();
		trader = new Trader(od.getFileName());

	}

	public static void fireModelChanged(final ModelListener.Event event, final Object value) {
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			ModelListener listener = listeners.get(i);
			listener.modelChanged(event, value);
		}
	}

	public static void setActiveStrategies(final int numberOfStrategies) {
		activeStrategies = new CountDownLatch(numberOfStrategies);
		fireModelChanged(ModelListener.Event.STRATEGIES_START, null);
	}

	public static void strategyCompleted() {
		activeStrategies.countDown();
		if (activeStrategies.getCount() == 0) {
			fireModelChanged(ModelListener.Event.STRATEGIES_END, null);
		}
	}

}
