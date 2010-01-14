package org.lifeform.market;

/**
 */
public class MarketSnapshot {
	private final long time;
	private final double balance;
	private final double price;

	public MarketSnapshot(final long time, final double balance, final double price) {
		this.time = time;
		this.balance = balance;
		this.price = price;
	}

	public double getBalance() {
		return balance;
	}

	public long getTime() {
		return time;
	}

	public double getPrice() {
		return price;
	}

	public String toString() {
		StringBuilder marketDepth = new StringBuilder();
		marketDepth.append("time: ").append(getTime());
		marketDepth.append(" balance: ").append(balance);
		marketDepth.append(" price: ").append(price);

		return marketDepth.toString();
	}

}
