/**
 * Used to hold properties of one portfolio item.
 * All the TWS portfolio is mirrored if the property portofolio.sync is true
 */
package org.lifeform.position;

import com.ib.client.Contract;

/**
 * @author Florent Guiliani <florent@guiliani.fr>
 */
public class PortfolioMirrorItem {

	private final Contract contract;
	private final int position;
	private final double marketPrice, marketValue, averageCost, unrealizedPNL,
			realizedPNL;
	private final String accountName;

	public PortfolioMirrorItem(final Contract contract, final int position,
			final double marketPrice, final double marketValue, final double averageCost,
			final double unrealizedPNL, final double realizedPNL, final String accountName) {
		this.contract = contract;
		this.position = position;
		this.marketPrice = marketPrice;
		this.marketValue = marketValue;
		this.averageCost = averageCost;
		this.unrealizedPNL = unrealizedPNL;
		this.realizedPNL = realizedPNL;
		this.accountName = accountName;
	}

	public Contract getContract() {
		return contract;
	}

	public int getPosition() {
		return position;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public double getMarketValue() {
		return marketValue;
	}

	public double getAverageCost() {
		return averageCost;
	}

	public double getUnrealizedPNL() {
		return unrealizedPNL;
	}

	public double getRealizedPNL() {
		return realizedPNL;
	}

	public String getAccountName() {
		return accountName;
	}

}
