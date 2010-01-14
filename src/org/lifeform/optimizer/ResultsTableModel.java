package org.lifeform.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.lifeform.service.TableDataModel;
import org.lifeform.strategy.Strategy;

/**
 * Optimization results table model
 */
public class ResultsTableModel extends TableDataModel {
	private static final long serialVersionUID = -6339646324020232706L;
	private static final String[] SCHEMA = { "P&L", "Max drawdown", "Trades",
			"Profit Factor", "Kelly", "Trade Distribution" };

	public ResultsTableModel() {
		setSchema(SCHEMA);
	}

	public void updateSchema(final Strategy strategy) {
		List<String> paramNames = new ArrayList<String>();
		for (StrategyParam param : strategy.getParams().getAll()) {
			paramNames.add(param.getName());
		}

		for (String paramName : SCHEMA) {
			paramNames.add(paramName);
		}

		setSchema(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public Class<?> getColumnClass(final int c) {
		return Double.class;
	}

	@Override
	public boolean isCellEditable(final int row, final int col) {
		return false;
	}

	@Override
	public synchronized Object getValueAt(final int row, final int column) {
		return super.getValueAt(row, column);
	}

	public synchronized void setResults(final List<Result> results) {

		removeAllData();

		for (Result result : results) {
			Object[] item = new Object[getColumnCount() + 1];

			StrategyParams params = result.getParams();

			int index = -1;
			for (StrategyParam param : params.getAll()) {
				item[++index] = param.getValue();
			}

			item[++index] = result.getTotalProfit();
			item[++index] = result.getMaxDrawdown();
			item[++index] = result.getTrades();
			item[++index] = result.getProfitFactor();
			item[++index] = result.getKelly();
			item[++index] = result.getTradeDistribution();

			addRow(item);
		}
	}
}
