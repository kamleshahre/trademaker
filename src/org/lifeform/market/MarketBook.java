package org.lifeform.market;

import java.util.TimeZone;

import org.lifeform.backdata.BackTestFileWriter;

/**
 * Holds history of market snapshots for a trading instrument.
 */
public class MarketBook {
	private MarketSnapshot marketSnapshot;
	private final MarketDepth marketDepth;
	private final String name;
	private final TimeZone timeZone;
	private BackTestFileWriter backTestFileWriter;

	public MarketBook(final String name, final TimeZone timeZone) {
		this.name = name;
		this.timeZone = timeZone;
		marketDepth = new MarketDepth();
	}

	public MarketBook() {
		this(null, null);
	}

	public MarketDepth getMarketDepth() {
		return marketDepth;
	}

	public void saveSnapshot(final MarketSnapshot marketSnapshot) {
		if (backTestFileWriter == null) {
			try {
				backTestFileWriter = new BackTestFileWriter(name, timeZone,
						true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		backTestFileWriter.write(marketSnapshot);
	}

	public boolean isEmpty() {
		return marketSnapshot == null;
	}

	public void setSnapshot(final MarketSnapshot marketSnapshot) {
		this.marketSnapshot = marketSnapshot;
	}

	public MarketSnapshot getSnapshot() {
		return marketSnapshot;
	}

	public MarketSnapshot getNextMarketSnapshot(final long time) {
		return marketDepth.getMarketSnapshot(time);
	}
}
