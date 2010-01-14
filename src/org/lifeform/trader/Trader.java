package org.lifeform.trader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lifeform.backdata.BackTestTraderAssistant;
import org.lifeform.configuration.Defaults;
import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;
import org.lifeform.position.OpenOrder;
import org.lifeform.position.PositionManager;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.ErrorListener;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.strategy.Strategy;

import com.ib.client.Contract;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;

/**
 * This class acts as a "wrapper" in the IB's API terminology.
 */
public class Trader extends IBWrapperAdapter {
	private Report eventReport;
	private final TraderAssistant traderAssistant;
	private final List<ErrorListener> errorListeners = new ArrayList<ErrorListener>();
	private volatile boolean isPendingHistRequest;
	private boolean _portfolioSyncEnabled;

	// / Constructor used for downloading data hitory or doing real/paper
	// trading.
	public Trader() throws Exception {
		_portfolioSyncEnabled = PreferencesHolder.getInstance().getBool(
				Defaults.PortfolioSync);
		traderAssistant = new TraderAssistant(this);
		eventReport = Dispatcher.getReporter();
		traderAssistant.connect();
		if (!traderAssistant.isRealAccountDisclaimerAccepted()) {
			throw new Exception(
					"You may restart TWS and login to a paper trading (simulated) account.");
		}
		traderAssistant.timeSyncCheck();

	}

	// / Constructor used for backtesting and optimizing
	public Trader(final String histDataFileName) throws Exception {
		_portfolioSyncEnabled = false;
		eventReport = Dispatcher.getReporter();
		if (null == eventReport) {
			Dispatcher.setReporter(histDataFileName + ".report");
			eventReport = Dispatcher.getReporter();
		}
		traderAssistant = new BackTestTraderAssistant(this, histDataFileName);
	}

	public void addErrorListener(final ErrorListener errorListener) {
		if (!errorListeners.contains(errorListener)) {
			errorListeners.add(errorListener);
		}
	}

	public synchronized void removeErrorListener(final ErrorListener errorListener) {
		errorListeners.remove(errorListener);
	}

	private void fireError(final int errorCode, final String errorMsg) {
		for (ErrorListener errorListener : errorListeners) {
			errorListener.error(errorCode, errorMsg);
		}
	}

	public TraderAssistant getAssistant() {
		return traderAssistant;
	}

	public boolean getIsPendingHistRequest() {
		return isPendingHistRequest;
	}

	public void setIsPendingHistRequest(final boolean isPendingHistRequest) {
		this.isPendingHistRequest = isPendingHistRequest;
	}

	@Override
	public void updateAccountValue(final String key, final String value, final String currency,
			final String accountName) {
		try {
			if (key.equalsIgnoreCase("AccountCode")) {
				synchronized (this) {
					traderAssistant.setAccountCode(value);
					notifyAll();
				}
			}
		} catch (Throwable t) {
			// Do not allow exceptions come back to the socket -- it will cause
			// disconnects
			eventReport.report(t);
		}
	}

	@Override
	public void updateNewsBulletin(final int msgId, final int msgType, final String message,
			final String origExchange) {
		String newsBulletin = "Msg ID: " + msgId + " Msg Type: " + msgType
				+ " Msg: " + message + " Exchange: " + origExchange;
		eventReport.report(newsBulletin);
	}

	@Override
	public void historicalData(final int reqId, final String date, final double open,
			final double high, final double low, final double close, final int volume, final int count,
			final double WAP, final boolean hasGaps) {
		try {
			QuoteHistory qh = traderAssistant.getStrategy(reqId)
					.getQuoteHistory();
			if (date.startsWith("finished")) {
				qh.setIsHistRequestCompleted(true);
				String msg = qh.getStrategyName()
						+ ": Historical data request finished. ";
				msg += "Bars returned:  " + qh.size();

				eventReport.report(msg);
				synchronized (this) {
					isPendingHistRequest = false;
					notifyAll();
				}
			} else {
				Strategy strategy = traderAssistant.getStrategy(reqId);
				long priceBarDate = (Long.parseLong(date) + strategy
						.getBarSize().toSeconds()) * 1000;
				PriceBar priceBar = new PriceBar(priceBarDate, open, high, low,
						close, volume);
				qh.addHistoricalPriceBar(priceBar);
				if (priceBarDate <= System.currentTimeMillis()) { // is the bar
					// completed?
					strategy.validateIndicators();
				}
			}
		} catch (Throwable t) {
			// Do not allow exceptions come back to the socket -- it will cause
			// disconnects
			eventReport.report(t);
		}
	}

	@Override
	public void execDetails(final int orderId, final Contract contract, final Execution execution) {
		try {
			Map<Integer, OpenOrder> openOrders = traderAssistant
					.getOpenOrders();
			OpenOrder openOrder = openOrders.get(orderId);
			if (openOrder != null) {
				openOrder.add(execution);
				if (openOrder.isFilled()) {
					Strategy strategy = openOrder.getStrategy();
					PositionManager positionManager = strategy
							.getPositionManager();
					positionManager.update(openOrder);
					openOrders.remove(orderId);
				}
			}
		} catch (Throwable t) {
			// Do not allow exceptions come back to the socket -- it will cause
			// disconnects
			eventReport.report(t);
		}
	}

	@Override
	public void realtimeBar(final int strategyId, final long time, final double open,
			final double high, final double low, final double close, final long volume, final double wap,
			final int count) {
		try {
			QuoteHistory qh = traderAssistant.getStrategy(strategyId)
					.getQuoteHistory();
			qh.update(open, high, low, close, volume);
		} catch (Throwable t) {
			// Do not allow exceptions come back to the socket -- it will cause
			// disconnects
			eventReport.report(t);
		}
	}

	@Override
	public void updatePortfolio(final Contract contract, final int position,
			final double marketPrice, final double marketValue, final double averageCost,
			final double unrealizedPNL, final double realizedPNL, final String accountName) {
		if (_portfolioSyncEnabled) {
			traderAssistant.updatePortfolio(contract, position, marketPrice,
					marketValue, averageCost, unrealizedPNL, realizedPNL,
					accountName);
		}
	}

	@Override
	public void error(final Exception e) {
		eventReport.report(e.toString());
	}

	@Override
	public void error(final String error) {
		eventReport.report(error);
	}

	@Override
	public void openOrder(final int orderId, final Contract contract, final Order order,
			final OrderState orderState) {
		boolean isOrderFilled = !(traderAssistant.getOpenOrders()
				.containsKey(orderId));
		boolean isValidCommission = (orderState.m_commission != Double.MAX_VALUE);
		if (isOrderFilled && isValidCommission) {
			BigDecimal commission = new BigDecimal(orderState.m_commission);
			commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
			double roundedCommission = commission.doubleValue();
			String msg = "Commission for order " + orderId + ": "
					+ orderState.m_commissionCurrency + " " + roundedCommission;
			// TODO: in trading mode, account for commissions in P&L calcs
			eventReport.report(msg);
		}
	}

	@Override
	public void error(final int id, final int errorCode, final String errorMsg) {
		try {
			String msg = errorCode + ": " + errorMsg;
			eventReport.report(msg);

			// handle errors 1101 and 1102
			boolean isConnectivityRestored = (errorCode == 1101 || errorCode == 1102);
			if (isConnectivityRestored) {
				eventReport
						.report("Checking for executions while TWS was disconnected from the IB server.");
				traderAssistant.requestExecutions();
			}

			fireError(errorCode, errorMsg);// send a notification to all error
			// listeners
		} catch (Throwable t) {
			// Do not allow exceptions come back to the socket -- it will cause
			// disconnects
			eventReport.report(t);
		}
	}

	@Override
	public void nextValidId(final int orderId) {
		traderAssistant.setOrderID(orderId);
	}

	@Override
	synchronized public void currentTime(final long time) {
		traderAssistant.setServerTime(time);
		notifyAll();
	}

}
