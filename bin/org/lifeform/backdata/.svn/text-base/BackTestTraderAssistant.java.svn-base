package org.lifeform.backdata;

import java.util.List;

import org.lifeform.configuration.Defaults;
import org.lifeform.market.PriceBar;
import org.lifeform.position.OpenOrder;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.strategy.Strategy;
import org.lifeform.trader.Trader;
import org.lifeform.trader.TraderAssistant;
import org.lifeform.util.AppUtil;

import com.ib.client.Contract;
import com.ib.client.Execution;
import com.ib.client.Order;

/**
 * Extends regular trader assistant for backtesting purposes
 */
public class BackTestTraderAssistant extends TraderAssistant {
	public List<PriceBar> priceBars;
	private final double spread, slippage, commission;

	public BackTestTraderAssistant(final Trader trader, final String fileName)
			throws Exception {
		super(trader, fileName);

		PreferencesHolder properties = PreferencesHolder.getInstance();

		eventReport.report("Reading back data file");
		BackTestFileReader reader = new BackTestFileReader(fileName);
		priceBars = reader.getPriceBars();
		spread = reader.getBidAskSpread();
		slippage = reader.getSlippage();
		commission = reader.getCommission();

		boolean showNumberOfBars = properties
				.getBool(Defaults.BacktestShowNumberOfBar);
		if (showNumberOfBars) {
			String msg = priceBars.size()
					+ " bars have been read successfully.";
			AppUtil.showMessage(null, msg);
		}
		eventReport.report("Connected to back test");
	}

	public synchronized void placeOrder(final Contract contract, final Order order,
			final Strategy strategy) {
		orderID++;
		openOrders.put(orderID, new OpenOrder(orderID, order, strategy));
		String msg = strategy.getName() + ": Placed order " + orderID;
		eventReport.report(msg);

		double price = strategy.getLastPriceBar().getClose();
		Execution execution = new Execution();
		execution.m_shares = order.m_totalQuantity;

		double aveTransactionCost = slippage + spread / 2d + commission;

		if (order.m_action.equalsIgnoreCase("BUY")) {
			execution.m_price = price + aveTransactionCost;
		}

		if (order.m_action.equalsIgnoreCase("SELL")) {
			execution.m_price = price - aveTransactionCost;
		}

		trader.execDetails(orderID, contract, execution);
	}
}
