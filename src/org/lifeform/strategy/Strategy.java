package org.lifeform.strategy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.lifeform.chart.ChartableIndicator;
import org.lifeform.chart.indicator.Indicator;
import org.lifeform.market.BarSize;
import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;
import org.lifeform.optimizer.StrategyParams;
import org.lifeform.position.Position;
import org.lifeform.position.PositionManager;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.time.TradingInterval;
import org.lifeform.time.TradingSchedule;

import com.ib.client.Contract;

/**
 * Base class for all classes that implement trading strategies.
 */

public abstract class Strategy {
	private static final String NOT_APPLICABLE = "N/A";

	private List<String> strategyReportHeaders;
	private StrategyParams params;

	protected final QuoteHistory quoteHistory;
	private int Id;
	private final DecimalFormat nf2, nf5;
	private Report strategyReport;
	protected Report eventReport;
	private final List<Object> strategyReportColumns = new ArrayList<Object>();

	private boolean onlyRTHPriceBars, isActive;
	private Calendar backTestCalendar;
	private Contract contract;

	private BarSize barSize;
	private final TradingSchedule tradingSchedule;
	private final List<ChartableIndicator> indicators;
	private final PositionManager positionManager;
	private final String name;
	protected int position;
	private boolean hasValidIndicators;

	/**
	 * Framework calls this method when a new strategy-specified bar becomes
	 * available.
	 */
	public abstract void onBar();

	/**
	 * Framework calls this method when a new 5-second bar becomes available.
	 */
	public void onMarketChange() {
	}

	/**
	 * Framework calls this method to obtain strategy parameter ranges.
	 */
	public abstract StrategyParams initParams();

	/**
	 * Framework calls this method to obtain strategy trading time interval
	 */
	public abstract TradingInterval initTradingInterval() throws Exception;

	public Strategy() {
		strategyReportHeaders = new ArrayList<String>();
		strategyReportHeaders.add("Date");
		strategyReportHeaders.add("Last Bar");
		strategyReportHeaders.add("Position");
		strategyReportHeaders.add("Trades");
		strategyReportHeaders.add("Avg Fill Price");
		strategyReportHeaders.add("Trade P&L");
		strategyReportHeaders.add("Total P&L");
		strategyReportHeaders.add("Trades distribution");

		name = getClass().getSimpleName();
		tradingSchedule = new TradingSchedule(this);
		indicators = new ArrayList<ChartableIndicator>();
		params = new StrategyParams();
		positionManager = new PositionManager(this);
		quoteHistory = new QuoteHistory(name);

		nf2 = (DecimalFormat) NumberFormat.getNumberInstance();
		nf2.setMaximumFractionDigits(2);
		nf5 = (DecimalFormat) NumberFormat.getNumberInstance();
		nf5.setMaximumFractionDigits(5);

		eventReport = Dispatcher.getReporter();
		isActive = true;
	}

	public void setReport(final Report strategyReport) {
		this.strategyReport = strategyReport;
	}

	public List<String> getStrategyReportHeaders() {
		return strategyReportHeaders;
	}

	public boolean isActive() {
		return isActive;
	}

	public boolean hasValidIndicators() {
		return hasValidIndicators;
	}

	public int getPosition() {
		return position;
	}

	public void closeOpenPositions() {
		position = 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ").append(name).append(" [");
		sb.append(contract.m_symbol).append("-");
		sb.append(contract.m_secType).append("-");
		sb.append(contract.m_exchange).append("-");
		sb.append(barSize.toString()).append("]");

		return sb.toString();
	}

	public void update() {
		PositionManager pm = getPositionManager();
		boolean hasTraded = pm.getHasTraded();

		strategyReportColumns.clear();
		strategyReportColumns.add(quoteHistory.getLastPriceBar().getClose());
		strategyReportColumns.add(positionManager.getPosition());
		strategyReportColumns.add(positionManager.getTrades());
		strategyReportColumns.add(hasTraded ? nf5.format(positionManager
				.getAvgFillPrice()) : NOT_APPLICABLE);
		strategyReportColumns.add(hasTraded ? nf5.format(positionManager
				.getProfitAndLoss()) : NOT_APPLICABLE);
		strategyReportColumns.add(nf5.format(positionManager
				.getTotalProfitAndLoss()));
		strategyReportColumns.add(hasTraded ? getTradeDistribution()
				: NOT_APPLICABLE);

		String fieldBreak = eventReport.getRenderer().getFieldBreak();
		String msg = getName() + ": state updated" + fieldBreak;
		msg += "Last bar:  " + quoteHistory.getLastPriceBar() + fieldBreak;

		for (ChartableIndicator chartableIndicator : indicators) {
			String formattedValue = NOT_APPLICABLE;
			if (!chartableIndicator.isEmpty()) {
				synchronized (nf2) {
					formattedValue = nf2.format(chartableIndicator
							.getIndicator().getValue());
				}
			}
			strategyReportColumns.add(formattedValue);
			msg += chartableIndicator.getName() + ": " + formattedValue + " ";
		}

		eventReport.report(msg);
		strategyReport.report(strategyReportColumns, getCalendar());
	}

	public String getTradeDistribution() {
		StringBuilder msg = new StringBuilder();
		double profittableTradeMeanValue = positionManager
				.getProfitableTradeMeanValue();
		double profitableStandardDeviation = positionManager
				.getProfitableTradeStandardDeviation();
		double unprofittableTradeMeanValue = positionManager
				.getUnprofitableTradeMeanValue();
		double unprofitableStandardDeviation = positionManager
				.getUnprofitableTradeStandardDeviation();

		msg.append(profittableTradeMeanValue > 0.1 ? nf2
				.format(profittableTradeMeanValue) : nf5
				.format(profittableTradeMeanValue));
		msg.append(" ~ ");
		msg.append(profitableStandardDeviation > 0.1 ? nf2
				.format(profitableStandardDeviation) : nf5
				.format(profitableStandardDeviation));
		msg.append(" (");
		msg.append(positionManager.getPercentProfitable());
		msg.append("%) ");
		msg.append(unprofittableTradeMeanValue < -0.1 ? nf2
				.format(unprofittableTradeMeanValue) : nf5
				.format(unprofittableTradeMeanValue));
		msg.append(" ~ ");
		msg.append(unprofitableStandardDeviation > 0.1 ? nf2
				.format(unprofitableStandardDeviation) : nf5
				.format(unprofitableStandardDeviation));

		return msg.toString();
	}

	public void setParams(final StrategyParams params) {
		this.params = params;
	}

	public StrategyParams getParams() {
		return params;
	}

	public PositionManager getPositionManager() {
		return positionManager;
	}

	public TradingSchedule getTradingSchedule() {
		return tradingSchedule;
	}

	private void setTradingInterval(final TradingInterval tradingInterval)
			throws Exception {
		tradingSchedule.setTradingInterval(tradingInterval);
		backTestCalendar = Calendar.getInstance();
		TimeZone tz = getTradingSchedule().getTimeZone();
		backTestCalendar.setTimeZone(tz);
	}

	protected void addIndicator(final String name, final Indicator indicator, final int chart) {
		ChartableIndicator chartableIndicator = new ChartableIndicator(name,
				indicator, chart);
		indicators.add(chartableIndicator);
		strategyReportHeaders.add(chartableIndicator.getName());
	}

	public List<ChartableIndicator> getIndicators() {
		return indicators;
	}

	public void setBarSize(final BarSize barSize) {
		this.barSize = barSize;
	}

	protected void setStrategy(final Contract contract, final BarSize barSize,
			final boolean onlyRTHPriceBars) throws Exception {
		this.contract = contract;
		this.barSize = barSize;
		this.onlyRTHPriceBars = onlyRTHPriceBars;

		String exchange = contract.m_exchange;
		boolean isForex = exchange.equalsIgnoreCase("IDEAL")
				|| exchange.equalsIgnoreCase("IDEALPRO");
		quoteHistory.setIsForex(isForex);
		setTradingInterval(initTradingInterval());
	}

	public QuoteHistory getQuoteHistory() {
		return quoteHistory;
	}

	public Calendar getCalendar() {
		boolean isTradeMode = (Dispatcher.getMode() == Dispatcher.Mode.TRADE);
		return isTradeMode ? tradingSchedule.getCalendar() : backTestCalendar;
	}

	public BarSize getBarSize() {
		return barSize;
	}

	public void setId(final int Id) {
		this.Id = Id;
	}

	public int getId() {
		return Id;
	}

	public Contract getContract() {
		return contract;
	}

	public boolean getOnlyRTHPriceBars() {
		return onlyRTHPriceBars;
	}

	public String getName() {
		return name;
	}

	public PriceBar getLastPriceBar() {
		return quoteHistory.getLastPriceBar();
	}

	public void updateIndicators() {
		for (ChartableIndicator chartableIndicator : indicators) {
			Indicator indicator = chartableIndicator.getIndicator();
			indicator.calculate();
			indicator.addToHistory(indicator.getDate(), indicator.getValue());
		}
	}

	public void validateIndicators() {
		hasValidIndicators = false;
		boolean isQuoteHistoryValid = quoteHistory.isValid();
		if (isQuoteHistoryValid) {
			try {
				updateIndicators();
				hasValidIndicators = true;
			} catch (ArrayIndexOutOfBoundsException aie) {
				String message = "Quote history length is insufficient to calculate the indicator";
				quoteHistory.getValidationMessages().add(message);
			} catch (Exception e) {
				eventReport.report(e);
			}
		} else {
			String msg = name + ": PriceBar history is invalid: "
					+ quoteHistory.getValidationMessages();
			eventReport.report(getName() + ": " + msg);
		}
	}

	public boolean canTrade() {
		boolean canTrade = true;

		boolean timeToClose = tradingSchedule.isTimeToClose();
		if (timeToClose && (positionManager.getPosition() != 0)) {
			position = 0;
			String msg = "End of trading interval. Closing current position.";
			eventReport.report(getName() + ": " + msg);
			canTrade = false;
		}

		if (!tradingSchedule.isTimeToTrade()) {
			canTrade = false;
		}

		return canTrade;
	}

	public long getTimeSinceLastPosition() {
		long secondsSinceLastPosition = 0;
		PositionManager positionManager = getPositionManager();
		List<Position> positionHistory = positionManager.getPositionsHistory();
		if (!positionHistory.isEmpty()) {
			Position lastPosition = positionHistory
					.get(positionHistory.size() - 1);
			long lastPositionTime = lastPosition.getDate();
			long timeNow = getCalendar().getTimeInMillis();
			secondsSinceLastPosition = (timeNow - lastPositionTime) / 1000L;
		}
		return secondsSinceLastPosition;
	}

	public void report(final String message) {
		if (strategyReport == null)
			return;

		strategyReportColumns.clear();
		strategyReportColumns.add(message);
		strategyReport.report(strategyReportColumns, getCalendar());
	}
}
