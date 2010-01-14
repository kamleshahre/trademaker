package org.lifeform.position;

import org.lifeform.strategy.Strategy;

import com.ib.client.Execution;
import com.ib.client.Order;

/**
 * Encapsulates the order execution information.
 */
public class OpenOrder {
	private final int id;
	private final Order order;
	private final Strategy strategy;
	private long date;
	private int sharesFilled;
	private boolean isFilled;
	private double avgFillPrice;

	public OpenOrder(final int id, final Order order, final Strategy strategy) {
		this.id = id;
		this.order = order;
		this.strategy = strategy;
	}

	public int getId() {
		return id;
	}

	public Order getOrder() {
		return order;
	}

	public void add(final Execution execution) {
		sharesFilled += execution.m_shares;
		avgFillPrice += execution.m_price * execution.m_shares;

		if (sharesFilled == order.m_totalQuantity) {
			avgFillPrice /= sharesFilled;
			date = strategy.getCalendar().getTimeInMillis();
			isFilled = true;
		}
	}

	public void reset() {
		sharesFilled = 0;
		avgFillPrice = 0;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public int getSharesFilled() {
		return sharesFilled;
	}

	public double getAvgFillPrice() {
		return avgFillPrice;
	}

	public long getDate() {
		return date;
	}

}
