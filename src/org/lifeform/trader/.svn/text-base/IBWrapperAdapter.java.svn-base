package org.lifeform.trader;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;

/**
 * Adapter pattern: provides empty implementation for all the methods in the
 * interface, so that the implementing classes can selectively override only the
 * needed methods.
 */
public class IBWrapperAdapter implements EWrapper {
	public void error(final Exception e) {
	}

	public void error(final String str) {
	}

	public void error(final int id, final int errorCode, final String errorMsg) {
	}

	public void connectionClosed() {
	}

	public void tickPrice(final int tickerId, final int field, final double price,
			final int canAutoExecute) {
	}

	public void tickSize(final int tickerId, final int field, final int size) {
	}

	public void tickOptionComputation(final int tickerId, final int field,
			final double impliedVol, final double delta, final double modelPrice,
			final double pvDividend) {
	}

	public void tickGeneric(final int tickerId, final int tickType, final double value) {
	}

	public void tickString(final int tickerId, final int tickType, final String value) {
	}

	public void tickEFP(final int tickerId, final int tickType, final double basisPoints,
			final String formattedBasisPoints, final double impliedFuture, final int holdDays,
			final String futureExpiry, final double dividendImpact, final double dividendsToExpiry) {
	}

	public void orderStatus(final int orderId, final String status, final int filled,
			final int remaining, final double avgFillPrice, final int permId, final int parentId,
			final double lastFillPrice, final int clientId, final String whyHeld) {
	}

	public void openOrder(final int orderId, final Contract contract, final Order order,
			final OrderState orderState) {
	}

	public void updateAccountValue(final String key, final String value, final String currency,
			final String accountName) {
	}

	public void updatePortfolio(final Contract contract, final int position,
			final double marketPrice, final double marketValue, final double averageCost,
			final double unrealizedPNL, final double realizedPNL, final String accountName) {
	}

	public void updateAccountTime(final String timeStamp) {
	}

	public void nextValidId(final int orderId) {
	}

	public void contractDetails(final ContractDetails contractDetails) {
	}

	public void bondContractDetails(final ContractDetails contractDetails) {
	}

	public void execDetails(final int orderId, final Contract contract, final Execution execution) {
	}

	public void updateMktDepth(final int tickerId, final int position, final int operation,
			final int side, final double price, final int size) {
	}

	public void updateMktDepthL2(final int tickerId, final int position,
			final String marketMaker, final int operation, final int side, final double price, final int size) {
	}

	public void updateNewsBulletin(final int msgId, final int msgType, final String message,
			final String origExchange) {
	}

	public void managedAccounts(final String accountsList) {
	}

	public void receiveFA(final int faDataType, final String xml) {
	}

	public void historicalData(final int reqId, final String date, final double open,
			final double high, final double low, final double close, final int volume, final int count,
			final double WAP, final boolean hasGaps) {
	}

	public void scannerParameters(final String xml) {
	}

	public void scannerData(final int reqId, final int rank,
			final ContractDetails contractDetails, final String distance, final String benchmark,
			final String projection, final String legsStr) {
	}

	public void scannerDataEnd(final int reqId) {
	}

	public void realtimeBar(final int reqId, final long time, final double open, final double high,
			final double low, final double close, final long volume, final double wap, final int count) {
	}

	public void currentTime(final long time) {
	}

}
