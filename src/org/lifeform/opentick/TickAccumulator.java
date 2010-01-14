package org.lifeform.opentick;

import com.opentick.OTOHLC;
import com.opentick.OTTrade;

public class TickAccumulator {
	private final int barSize;
	private OTOHLC bar;
	private long cumulativeVolume;

	public TickAccumulator(final int barSize) {
		this.barSize = barSize;
	}

	public OTOHLC accumulate(final OTTrade trade) {
		OTOHLC accumulatedBar = null;

		double price = trade.getPrice();
		long volume = Math.max(0, trade.getVolume() - cumulativeVolume);
		cumulativeVolume = trade.getVolume();
		int time = trade.getTimestamp();

		boolean isBarAccumulated = (bar != null && time >= bar.getTimestamp()
				+ barSize);
		if (isBarAccumulated) {
			accumulatedBar = bar;
		}

		if (bar == null || isBarAccumulated) {
			// Integer division gives us the number of whole periods
			int completedPeriods = time / barSize;
			int barStartTime = completedPeriods * barSize;
			bar = new OTOHLC(0, barStartTime, price, price, price, price,
					volume);
		} else {
			bar.setClosePrice(price);
			bar.setLowPrice(Math.min(price, bar.getLowPrice()));
			bar.setHighPrice(Math.max(price, bar.getHighPrice()));
			bar.setVolume(bar.getVolume() + volume);
		}

		return accumulatedBar;
	}

}
