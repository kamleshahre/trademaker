package org.lifeform.chart.indicator;

import java.util.ArrayList;
import java.util.List;

import org.lifeform.market.MarketBook;
import org.lifeform.market.QuoteHistory;

/**
 * Base class for all classes implementing technical indicators.
 */
public abstract class Indicator {
	protected double value;
	protected MarketBook marketBook;
	protected QuoteHistory qh;
	private final List<IndicatorValue> history;
	protected Indicator parent;

	public abstract double calculate();// must be implemented in subclasses.

	public Indicator() {
		history = new ArrayList<IndicatorValue>();
	}

	public Indicator(final QuoteHistory qh) {
		this();
		this.qh = qh;
	}

	public Indicator(final Indicator parent) {
		this();
		this.parent = parent;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" value: ").append(value);
		return sb.toString();
	}

	public double getValue() {
		return value;
	}

	public long getDate() {
		if (qh != null) {
			return qh.getLastPriceBar().getDate();
		} else {
			List<IndicatorValue> parentHistory = parent.getHistory();
			return parentHistory.get(parentHistory.size() - 1).getDate();
		}
	}

	public void addToHistory(final long date, final double value) {
		history.add(new IndicatorValue(date, value));
	}

	public List<IndicatorValue> getHistory() {
		return history;
	}

	public void reset() {

	}

	public void setMarketBook(final MarketBook marketBook) {
		this.marketBook = marketBook;
	}

	public String getName() {
		return toString();
	}
}
