package org.lifeform.chart;

import org.jfree.chart.axis.SegmentedTimeline;
import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;

public class MarketTimeLine {
	/**
	 * Gaps less than MAX_GAP will be ignored, gaps greater than MAX_GAP will be
	 * removed
	 */
	private static final long MAX_GAP = 12 * 60 * 60 * 1000;// 12 hours
	private static final long SEGMENT_SIZE = SegmentedTimeline.FIFTEEN_MINUTE_SEGMENT_SIZE;
	private static final long GAP_BUFFER = SEGMENT_SIZE;
	private final QuoteHistory qh;

	public MarketTimeLine(final QuoteHistory qh) {
		this.qh = qh;
	}

	public SegmentedTimeline getNormalHours() {
		SegmentedTimeline timeline = new SegmentedTimeline(SEGMENT_SIZE, 1, 0);
		long previousTime = qh.getFirstPriceBar().getDate();

		for (PriceBar bar : qh.getAll()) {
			long barTime = bar.getDate();
			long difference = barTime - previousTime;
			if (difference > MAX_GAP) {
				timeline.addException(previousTime + GAP_BUFFER, barTime
						- GAP_BUFFER);
			}
			previousTime = barTime;
		}

		return timeline;
	}

	public SegmentedTimeline getAllHours() {
		return new SegmentedTimeline(SegmentedTimeline.DAY_SEGMENT_SIZE, 7, 0);
	}
}
