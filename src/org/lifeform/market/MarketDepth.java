package org.lifeform.market;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lifeform.util.NumberFormatterFactory;

/**
 * Holds history of market snapshots for a trading instrument.
 */
public class MarketDepth {
	private final LinkedList<MarketDepthItem> bids, asks;
	private final LinkedList<Double> balances;
	private double averageBalance;
	private double midPointPrice;
	private final DecimalFormat df2;

	public MarketDepth() {
		bids = new LinkedList<MarketDepthItem>();
		asks = new LinkedList<MarketDepthItem>();
		balances = new LinkedList<Double>();
		df2 = NumberFormatterFactory.getNumberFormatter(2);
	}

	public void reset() {
		bids.clear();
		asks.clear();
		balances.clear();
	}

	private int getCumulativeSize(final LinkedList<MarketDepthItem> items) {
		Set<Double> uniquePriceLevels = new HashSet<Double>();
		int cumulativeSize = 0;

		for (MarketDepthItem item : items) {
			uniquePriceLevels.add(item.getPrice());
			cumulativeSize += item.getSize();
		}

		return cumulativeSize / uniquePriceLevels.size();
	}

	synchronized public void update(final int position,
			final MarketDepthOperation operation, final MarketDepthSide side, final double price,
			final int size) {
		List<MarketDepthItem> items = (side == MarketDepthSide.Bid) ? bids
				: asks;
		int levels = items.size();

		switch (operation) {
		case Insert:
			if (position <= levels) {
				items.add(position, new MarketDepthItem(size, price));
			}
			break;
		case Update:
			if (position < levels) {
				MarketDepthItem item = items.get(position);
				item.setSize(size);
				item.setPrice(price);
			}
			break;
		case Delete:
			if (position < levels) {
				items.remove(position);
			}
			break;
		}

		if (operation == MarketDepthOperation.Update) {
			if (!bids.isEmpty() && !asks.isEmpty()) {
				int cumulativeBid = getCumulativeSize(bids);
				int cumulativeAsk = getCumulativeSize(asks);
				double totalDepth = cumulativeBid + cumulativeAsk;

				double balance = 100d * (cumulativeBid - cumulativeAsk)
						/ totalDepth;
				balances.add(balance);
				midPointPrice = (bids.getFirst().getPrice() + asks.getFirst()
						.getPrice()) / 2;
			}
		}
	}

	synchronized public MarketSnapshot getMarketSnapshot(final long time) {
		if (midPointPrice == 0) {
			// This is normal. It happens at the very start when market depth is
			// initializing.
			return null;
		}

		if (!balances.isEmpty()) {
			double multiplier = 2. / (balances.size() + 1.);
			for (double balance : balances) {
				averageBalance += (balance - averageBalance) * multiplier;
			}
		}

		double oneSecondBalance = Double.valueOf(df2.format(averageBalance));
		MarketSnapshot marketSnapshot = new MarketSnapshot(time,
				oneSecondBalance, midPointPrice);
		balances.clear();

		return marketSnapshot;
	}
}
