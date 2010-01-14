package org.lifeform.trader;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.lifeform.configuration.Defaults;
import org.lifeform.market.QuoteHistory;
import org.lifeform.position.OpenOrder;
import org.lifeform.position.PortfolioMirrorItem;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.ModelListener;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.strategy.Strategy;
import org.lifeform.time.IBTimeSyncChecker;
import org.lifeform.util.AppUtil;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.ExecutionFilter;
import com.ib.client.Order;

public class TraderAssistant {
	private final String host, advisorAccountID;
	private final int port, clientID;

	/**
	 * Space the sequential historical requests by at least 10 seconds.
	 * 
	 * IB's historical data server imposes limitations on the number of
	 * historical requests made within a certain period of time. At this time,
	 * this limit appears to be 60 requests in 5 minutes.
	 */
	private static final int MAX_REQUEST_FREQUENCY_MILLIS = 10100;
	private EClientSocket socket;
	private final Map<Integer, Strategy> strategies;
	protected final Map<Integer, OpenOrder> openOrders;
	protected final Map<String, PortfolioMirrorItem> portfolioMirror;
	protected Report eventReport;
	private int nextStrategyID;
	protected int orderID;
	private String accountCode;// used to determine if TWS is running against
	// real or paper trading account
	private long serverTime;
	private int serverVersion;
	protected final Trader trader;
	private long lastRequestTime;

	public TraderAssistant(final Trader trader) throws Exception {
		this.trader = trader;

		eventReport = Dispatcher.getReporter();

		// contains strategies and their IDs
		strategies = new HashMap<Integer, Strategy>();
		// contains orders and their IDs
		openOrders = new HashMap<Integer, OpenOrder>();
		// contains portfolio replication
		portfolioMirror = new HashMap<String, PortfolioMirrorItem>();

		PreferencesHolder properties = PreferencesHolder.getInstance();
		boolean isAdvisorAccountUsed = properties
				.getBool(Defaults.AdvisorAccount);
		if (isAdvisorAccountUsed) {
			advisorAccountID = properties.get(Defaults.AdvisorAccountNumber);
		} else {
			advisorAccountID = "";
		}

		host = properties.get(Defaults.Host);
		port = properties.getInt(Defaults.Port);
		clientID = properties.getInt(Defaults.ClientID);
	}

	public TraderAssistant(final Trader trader, final String fileName) throws Exception {
		this(trader);
	}

	public Map<Integer, OpenOrder> getOpenOrders() {
		return openOrders;
	}

	public Strategy getStrategy(final int strategyId) {
		return strategies.get(strategyId);
	}

	public void connect() throws Exception {

		eventReport.report("Connecting to TWS");
		socket = new EClientSocket(trader);

		if (!socket.isConnected()) {
			socket.eConnect(host, port, clientID);
		}
		if (!socket.isConnected()) {
			throw new Exception(
					"Could not connect to TWS. See report for details.");
		}

		// IB Log levels: 1=SYSTEM 2=ERROR 3=WARNING 4=INFORMATION 5=DETAIL
		socket.setServerLogLevel(2);
		socket.reqNewsBulletins(true);
		serverVersion = socket.serverVersion();

		eventReport.report("Connected to TWS");

	}

	public int getServerVersion() {
		return serverVersion;
	}

	public void disconnect() {
		if (socket != null && socket.isConnected()) {
			socket.cancelNewsBulletins();
			socket.eDisconnect();
		}
	}

	/**
	 * While TWS was disconnected from the IB server, some order executions may
	 * have occured. To detect executions, request them explicitly after the
	 * reconnection.
	 */
	public void requestExecutions() {
		try {
			eventReport.report("Requested executions.");
			for (OpenOrder openOrder : openOrders.values()) {
				openOrder.reset();
			}
			socket.reqExecutions(new ExecutionFilter());
		} catch (Throwable t) {
			// Do not allow exceptions come back to the socket -- it will cause
			// disconnects
			eventReport.report(t);
		}
	}

	public void getHistoricalData(final int strategyId, final Contract contract,
			final String endDateTime, final String duration, final String barSize,
			final String whatToShow, final int useRTH, final int formatDate)
			throws InterruptedException {

		// Only one historical data request can hit the socket at a time, so
		// we wait here to be notified that no historical requests are pending.
		synchronized (trader) {
			while (trader.getIsPendingHistRequest()) {
				trader.wait();
			}

			trader.setIsPendingHistRequest(true);
			long elapsedSinceLastRequest = System.currentTimeMillis()
					- lastRequestTime;
			long remainingTime = MAX_REQUEST_FREQUENCY_MILLIS
					- elapsedSinceLastRequest;
			if (remainingTime > 0) {
				Thread.sleep(remainingTime);
			}
		}

		QuoteHistory qh = getStrategy(strategyId).getQuoteHistory();
		qh.setIsHistRequestCompleted(false);

		String msg = qh.getStrategyName() + ": "
				+ "Requested Historical data. ";
		msg += "End time: " + endDateTime;
		eventReport.report(msg);

		lastRequestTime = System.currentTimeMillis();
		socket.reqHistoricalData(strategyId, contract, endDateTime, duration,
				barSize, whatToShow, useRTH, formatDate);
	}

	public void getRealTimeBars(final int strategyId, final Contract contract, final int barSize,
			final String whatToShow, final boolean useRTH) {
		socket.reqRealTimeBars(strategyId, contract, barSize, whatToShow,
				useRTH);
		QuoteHistory qh = getStrategy(strategyId).getQuoteHistory();
		String msg = qh.getStrategyName() + ": " + "Requested real time bars.";
		eventReport.report(msg);
	}

	public synchronized void addStrategy(final Strategy strategy) {
		nextStrategyID++;
		strategy.setId(nextStrategyID);
		strategies.put(nextStrategyID, strategy);

		PortfolioMirrorItem portfolioMirrorItem = portfolioMirror
				.get(_generateContractKey(strategy.getContract()));
		if (portfolioMirrorItem != null) {
			strategy.getPositionManager().update(portfolioMirrorItem);
			Dispatcher.fireModelChanged(ModelListener.Event.STRATEGY_UPDATE,
					strategy);
		}
	}

	public void setAccountCode(final String accountCode) {
		this.accountCode = accountCode;
	}

	public void setServerTime(final long serverTime) {
		this.serverTime = serverTime * 1000L;
	}

	public synchronized void placeOrder(final Contract contract, final Order order,
			final Strategy strategy) {
		try {
			orderID++;
			openOrders.put(orderID, new OpenOrder(orderID, order, strategy));
			socket.placeOrder(orderID, contract, order);
			String msg = strategy.getName() + ": Placed order " + orderID;
			eventReport.report(msg);
		} catch (Throwable t) {
			// Do not allow exceptions come back to the socket -- it will cause
			// disconnects
			eventReport.report(t);
		}
	}

	public void placeMarketOrder(final Contract contract, final int quantity,
			final String action, final Strategy strategy) {
		Order order = new Order();
		order.m_action = action;
		order.m_totalQuantity = quantity;
		order.m_orderType = "MKT";
		if (advisorAccountID.length() != 0)
			order.m_account = advisorAccountID;
		placeOrder(contract, order, strategy);
	}

	public void setOrderID(final int orderID) {
		this.orderID = orderID;
	}

	private long getServerTime() throws Exception {
		socket.reqCurrentTime();
		try {
			synchronized (trader) {
				while (serverTime == 0) {
					trader.wait();
				}
			}
		} catch (InterruptedException ie) {
			throw new Exception(ie);
		}

		return serverTime;
	}

	public void timeSyncCheck() throws Exception {
		// Make sure that system clock and IB server clocks are in sync
		socket.reqCurrentTime();
		IBTimeSyncChecker.timeCheck(getServerTime());
	}

	public boolean isRealAccountDisclaimerAccepted() throws Exception {
		boolean isAccepted = true;
		socket.reqAccountUpdates(true, advisorAccountID);

		try {
			synchronized (trader) {
				while (accountCode == null) {
					trader.wait();
				}
			}
		} catch (InterruptedException ie) {
			throw new Exception(ie);
		}

		socket.reqAccountUpdates(false, advisorAccountID);
		if (!accountCode.startsWith("D")) {
			String lineSep = Defaults.getLineSeperator();
			String warning = "Connected to a real (not simulated) IB account. ";
			warning += "Running " + AppUtil.getAppName() + " against a real"
					+ lineSep;
			warning += "account may cause significant losses in your account. ";
			warning += "Are you sure you want to proceed?";
			int response = JOptionPane.showConfirmDialog(null, warning, AppUtil
					.getAppName(), JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.NO_OPTION) {
				isAccepted = false;
				disconnect();
			}
		}

		return isAccepted;
	}

	public synchronized void updatePortfolio(final Contract contract, final int position,
			final double marketPrice, final double marketValue, final double averageCost,
			final double unrealizedPNL, final double realizedPNL, final String accountName) {
		PortfolioMirrorItem portfolioMirrorItem = new PortfolioMirrorItem(
				contract, position, marketPrice, marketValue, averageCost,
				unrealizedPNL, realizedPNL, accountName);
		portfolioMirror
				.put(_generateContractKey(contract), portfolioMirrorItem);

		for (Strategy strategy : strategies.values()) {
			if (strategy.getContract().m_symbol.equals(portfolioMirrorItem
					.getContract().m_symbol)) {
				strategy.getPositionManager().update(portfolioMirrorItem);
				Dispatcher.fireModelChanged(
						ModelListener.Event.STRATEGY_UPDATE, strategy);
			}
		}
	}

	private String _generateContractKey(final Contract contract) {
		if (contract == null)
			return "nocontract";

		StringBuilder key = new StringBuilder();
		key.append(contract.m_symbol);
		if (contract.m_expiry != null) {
			key.append("/");
			key.append(contract.m_expiry.substring(0, 5));
		}

		return key.toString();
	}

}
