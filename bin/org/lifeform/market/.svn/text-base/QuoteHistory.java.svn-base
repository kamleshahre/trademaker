package org.lifeform.market;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lifeform.configuration.Defaults;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;

public class QuoteHistory {

	private static final long CONTINUITY_THRESHOLD = 15 * 60 * 1000;// 15
	// minutes
	public static final Report eventReporter = Dispatcher.getReporter();

	private final List<PriceBar> priceBars;
	private final List<String> validationMessages;
	private final String strategyName;
	private boolean isHistRequestCompleted;
	private PriceBar nextBar;
	private boolean isForex;
	private Snapshot snapshot;
	private final LinkedList<QuoteHistoryEvent> events;

	public QuoteHistory(final String strategyName) {
		this.strategyName = strategyName;
		priceBars = new ArrayList<PriceBar>();
		validationMessages = new ArrayList<String>();
		events = new LinkedList<QuoteHistoryEvent>();
	}

	public QuoteHistory() {
		this("BackDataDownloader");
	}

	public LinkedList<QuoteHistoryEvent> getEvents() {
		return events;
	}

	public Snapshot getSnapshot() {
		return snapshot;
	}

	public void setIsForex(final boolean isForex) {
		this.isForex = isForex;
	}

	public boolean getIsForex() {
		return isForex;
	}

	public List<PriceBar> getAll() {
		return priceBars;
	}

	public synchronized void onBar(final long nextBarTime) {
		if (nextBar == null) {
			// The just completed bar never opened, so we assign its OHLC values
			// to the last bar's close
			double lastBarClose = getLastPriceBar().getClose();
			nextBar = new PriceBar(lastBarClose);
		}

		nextBar.setDate(nextBarTime);
		priceBars.add(nextBar);
		nextBar = null;// initialize next bar

		// Send a notification to waiting threads informing them that a new bar
		// has closed
		QuoteHistoryEvent quoteHistoryEvent = new QuoteHistoryEvent(
				QuoteHistoryEvent.EventType.NEW_BAR);
		synchronized (events) {
			events.add(quoteHistoryEvent);
			events.notifyAll();
		}
	}

	public synchronized void update(final double open, final double high,
			final double low, final double close, long volume) {
		if (isForex) {
			volume = 0;// volume is not reported for Forex
		}

		if (nextBar == null) {
			nextBar = new PriceBar(open, high, low, close, volume);
		} else {
			nextBar.setClose(close);
			nextBar.setLow(Math.min(low, nextBar.getLow()));
			nextBar.setHigh(Math.max(high, nextBar.getHigh()));
			nextBar.setVolume(nextBar.getVolume() + volume);
		}

		snapshot = new Snapshot(open, high, low, close, volume);

		QuoteHistoryEvent quoteHistoryEvent = new QuoteHistoryEvent(
				QuoteHistoryEvent.EventType.MARKET_CHANGE);
		synchronized (events) {
			events.add(quoteHistoryEvent);
			events.notifyAll();
		}

	}

	public String getStrategyName() {
		return strategyName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (PriceBar priceBar : priceBars) {
			sb.append(priceBar).append(Defaults.getLineSeperator());
		}

		return sb.toString();
	}

	public boolean isValid() {
		// TODO: validate quote history
		boolean isValid = true;
		validationMessages.clear();
		return isValid;
	}

	public List<String> getValidationMessages() {
		return validationMessages;
	}

	public int size() {
		return priceBars.size();
	}

	public void addHistoricalPriceBar(final PriceBar priceBar) {
		priceBars.add(priceBar);
	}

	public PriceBar getPriceBar(final int index) {
		return priceBars.get(index);
	}

	public int getSize() {
		return priceBars.size();
	}

	public void setIsHistRequestCompleted(final boolean isHistRequestCompleted) {
		this.isHistRequestCompleted = isHistRequestCompleted;
		if (isHistRequestCompleted) {
			if (!priceBars.isEmpty()) {
				long timeDifference = System.currentTimeMillis()
						- getLastPriceBar().getDate();
				if (timeDifference < CONTINUITY_THRESHOLD) {
					nextBar = getLastPriceBar();
					priceBars.remove(priceBars.size() - 1);
				}
			}
		}
	}

	public boolean getIsHistRequestCompleted() {
		return isHistRequestCompleted;
	}

	public PriceBar getLastPriceBar() {
		return priceBars.get(priceBars.size() - 1);
	}

	public PriceBar getFirstPriceBar() {
		return priceBars.get(0);
	}
}
