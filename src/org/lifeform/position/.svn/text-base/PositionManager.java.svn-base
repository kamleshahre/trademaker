package org.lifeform.position;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.lifeform.report.Report;
import org.lifeform.report.format.Format;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.ModelListener;
import org.lifeform.strategy.Strategy;

import com.ib.client.Contract;
import com.ib.client.Order;

/**
 * Position manager keeps track of current positions and P&L.
 */
public class PositionManager {
	private double totalBought, totalSold;
	private final List<Position> positionsHistory;
	private int position;
	protected int trades;
	private double profitAndLoss;
	protected double totalProfitAndLoss;
	private double avgFillPrice;
	protected final Strategy strategy;

	private volatile boolean orderExecutionPending;
	private final DecimalFormat nf4;
	private final Report eventReport;
	private final ProfitAndLossHistory profitAndLossHistory;
	private int profitableTrades, unprofitableTrades;
	private double grossProfit, grossLoss, peakTotalProfitAndLoss, maxDrawdown,
			kelly;
	private StandardDeviation profitableTradeStandardDeviation,
			unprofitableTradeStandardDeviation;

	private boolean hasTraded;

	public PositionManager(final Strategy strategy) {
		this.strategy = strategy;
		profitAndLossHistory = new ProfitAndLossHistory();
		positionsHistory = new ArrayList<Position>();
		nf4 = (DecimalFormat) NumberFormat.getNumberInstance();
		nf4.setMaximumFractionDigits(4);
		eventReport = Dispatcher.getReporter();
		kelly = 0.0;
		profitableTradeStandardDeviation = new StandardDeviation();
		unprofitableTradeStandardDeviation = new StandardDeviation();
	}

	public List<Position> getPositionsHistory() {
		return positionsHistory;
	}

	public int getTrades() {
		return trades;
	}

	public boolean getHasTraded() {
		return hasTraded;
	}

	public int getPercentProfitable() {
		return (int) (((double) profitableTrades / (unprofitableTrades + profitableTrades)) * 100);
	}

	public double getProfitFactor() {
		return Math.abs(grossProfit / grossLoss);
	}

	public double getMaxDrawdown() {
		return maxDrawdown;
	}

	public double getKelly() {
		return kelly;
	}

	public int getPosition() {
		return position;
	}

	// public void setAvgFillPrice(double avgFillPrice) {
	// this.avgFillPrice = avgFillPrice;
	// }

	public double getAvgFillPrice() {
		return avgFillPrice;
	}

	public boolean isOrderExecutionPending() {
		return orderExecutionPending;
	}

	public double getProfitAndLoss() {
		return profitAndLoss;
	}

	public double getTotalProfitAndLoss() {
		return totalProfitAndLoss;
	}

	public ProfitAndLossHistory getProfitAndLossHistory() {
		return profitAndLossHistory;
	}

	public synchronized void update(final OpenOrder openOrder) {

		trades++;
		Order order = openOrder.getOrder();
		String action = order.m_action;

		// There is 4 possible cases
		// * avgFillprice is reseted to 0
		// * avgFillprice get the last order avgFillPrive value
		// * avgFillprice is increased or decreased
		// * avgFillprice is unchanged
		int sharesFilled = openOrder.getSharesFilled();
		double oldPosition = position;
		double orderAvgFillPrice = openOrder.getAvgFillPrice();
		double tradeAmount = orderAvgFillPrice * sharesFilled;

		if (action.equals("SELL")) {
			position -= sharesFilled;
			totalSold += tradeAmount;
		} else if (action.equals("BUY")) {
			position += sharesFilled;
			totalBought += tradeAmount;
		}

		// Case 1: avgFillprice is reseted to 0
		if (position == 0.0) {
			avgFillPrice = 0;
		}
		// Case 2: avgFillprice get the last order avgFillPrive value
		else if (oldPosition == 0.0 || (oldPosition < 0 && position > 0)
				|| (oldPosition > 0 && position < 0)) {
			avgFillPrice = orderAvgFillPrice;
		}
		// Case 3: avgFillprice is increasing or decreasing
		else if ((oldPosition < 0 && position < oldPosition)
				|| (oldPosition > 0 && position > oldPosition)) {
			avgFillPrice = (Math.abs(oldPosition) * avgFillPrice + tradeAmount)
					/ Math.abs(position);
		}
		// Case 4: avgFillprice is unchanged
		// Noting to do

		double positionValue = position * avgFillPrice;
		double previousProfitandLoss = totalProfitAndLoss;
		totalProfitAndLoss = totalSold - totalBought + positionValue;

		profitAndLoss = totalProfitAndLoss - previousProfitandLoss;

		if (profitAndLoss > 0) {
			profitableTrades++;
			grossProfit += profitAndLoss;
			profitableTradeStandardDeviation.increment(profitAndLoss);
		} else {
			unprofitableTrades++;
			grossLoss += profitAndLoss;
			unprofitableTradeStandardDeviation.increment(profitAndLoss);
		}

		if (totalProfitAndLoss > peakTotalProfitAndLoss) {
			peakTotalProfitAndLoss = totalProfitAndLoss;
		}

		double drawdown = peakTotalProfitAndLoss - totalProfitAndLoss;
		if (drawdown > maxDrawdown) {
			maxDrawdown = drawdown;
		}

		if (profitableTrades > 0 && unprofitableTrades > 0) {
			double wfaktor = (double) profitableTrades
					/ (profitableTrades + unprofitableTrades);
			double rfaktor = (double) (grossProfit / profitableTrades)
					/ (Math.abs(grossLoss / unprofitableTrades));
			kelly = wfaktor - (1 - wfaktor) / rfaktor;
		}

		long time = strategy.getCalendar().getTimeInMillis();
		profitAndLossHistory.add(new ProfitAndLoss(time, totalProfitAndLoss));
		positionsHistory.add(new Position(openOrder.getDate(), position,
				avgFillPrice, orderAvgFillPrice));

		if ((Dispatcher.getMode() != Dispatcher.Mode.OPTIMIZATION)) {
			Format renderer = eventReport.getRenderer();
			String fieldBreak = renderer.getFieldBreak();
			String emphasisStart = renderer.getEmphasisStart();
			String emphasisEnd = renderer.getEmphasisEnd();

			String result = (profitAndLoss > 0) ? "gain" : "loss";
			StringBuilder msg = new StringBuilder();
			msg.append(strategy.getName()).append(": ");
			msg.append("Order ").append(openOrder.getId()).append(
					" is filled.  ");

			msg.append("Avg Fill Price: ").append(avgFillPrice).append(".")
					.append(fieldBreak);
			msg.append(emphasisStart).append(" Position changed to: ").append(
					getPosition()).append(emphasisEnd).append(fieldBreak);
			msg.append("This trade P&L: ").append(nf4.format(profitAndLoss))
					.append(" points ").append(result).append(".").append(
							fieldBreak);
			eventReport.report(msg.toString());
			strategy.update();
			Dispatcher.fireModelChanged(ModelListener.Event.STRATEGY_UPDATE,
					strategy);
		}

		this.orderExecutionPending = false;
	}

	public synchronized void update(final PortfolioMirrorItem portfolioMirrorItem) {
		position = portfolioMirrorItem.getPosition();
		avgFillPrice = portfolioMirrorItem.getAverageCost();
	}

	public void trade() {
		assert (orderExecutionPending = false);

		hasTraded = false;
		int newPosition = strategy.getPosition();

		int quantity = newPosition - position;
		if (quantity == 0) {
			return;
		}

		orderExecutionPending = true;
		String action = (quantity > 0) ? "BUY" : "SELL";
		Contract contract = strategy.getContract();
		Dispatcher.getTrader().getAssistant().placeMarketOrder(contract,
				Math.abs(quantity), action, strategy);

		hasTraded = true;
	}

	public double getProfitableTradeMeanValue() {
		if (profitableTrades == 0)
			return 0.0;

		return grossProfit / profitableTrades;
	}

	public double getUnprofitableTradeMeanValue() {
		if (unprofitableTrades == 0)
			return 0.0;

		return grossLoss / unprofitableTrades;
	}

	public double getProfitableTradeStandardDeviation() {
		if (profitableTrades == 0)
			return 0.0;

		return profitableTradeStandardDeviation.getResult();
	}

	public double getUnprofitableTradeStandardDeviation() {
		if (unprofitableTrades == 0)
			return 0.0;

		return unprofitableTradeStandardDeviation.getResult();
	}
}
