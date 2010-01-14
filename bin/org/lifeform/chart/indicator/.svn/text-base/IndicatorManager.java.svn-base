package org.lifeform.chart.indicator;

import java.util.ArrayList;
import java.util.List;

import org.lifeform.market.MarketBook;

/**
 *
 */
public class IndicatorManager {
	private static final long GAP_SIZE = 60 * 60 * 1000;// 1 hour
	private final List<Indicator> indicators;
	private MarketBook marketBook;
	private boolean hasValidIndicators;
	private long previousSnapshotTime;

	public IndicatorManager() {
		indicators = new ArrayList<Indicator>();
	}

	public void setMarketBook(final MarketBook marketBook) {
		this.marketBook = marketBook;
		for (Indicator indicator : indicators) {
			indicator.setMarketBook(marketBook);
		}
	}

	public String indicatorsState() {
		String indicatorsState = "";
		for (Indicator indicator : indicators) {
			if (!indicatorsState.isEmpty()) {
				indicatorsState += ", ";
			}

			indicatorsState += (int) indicator.getValue();
		}
		return indicatorsState;
	}

	public boolean hasValidIndicators() {
		return hasValidIndicators;
	}

	public void addIndicator(final Indicator indicator) {
		indicators.add(indicator);
	}

	public List<Indicator> getIndicators() {
		return indicators;
	}

	public void updateIndicators() {
		hasValidIndicators = true;
		long lastSnapshotTime = marketBook.getSnapshot().getTime();

		if (lastSnapshotTime - previousSnapshotTime > GAP_SIZE) {
			for (Indicator indicator : indicators) {
				indicator.reset();
			}
		}
		previousSnapshotTime = lastSnapshotTime;

		for (Indicator indicator : indicators) {
			try {
				indicator.calculate();
			} catch (IndexOutOfBoundsException iobe) {
				hasValidIndicators = false;
				// This exception will occur if book size is insufficient to
				// calculate
				// the indicator. This is normal.
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
