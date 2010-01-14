package org.lifeform.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableDataModel extends AbstractTableModel {
	private static final long serialVersionUID = 6727100364298459876L;
	private String[] schema;
	private final List<Object> rows;

	public TableDataModel() {
		rows = new ArrayList<Object>();
	}

	public void addRow(final Object[] item) {
		rows.add(item);
		fireTableDataChanged();
	}

	@Override
	public void setValueAt(final Object value, final int row, final int col) {
		Object[] changedItem = (Object[]) rows.get(row);
		changedItem[col] = value;
		fireTableCellUpdated(row, col);
	}

	protected void removeAllData() {
		rows.clear();
		fireTableDataChanged();
	}

	protected void removeRow(final int row) {
		rows.remove(row);
		fireTableDataChanged();
	}

	public void setSchema(final String[] schema) {
		this.schema = schema;
		fireTableStructureChanged();
	}

	public Object getValueAt(final int row, final int column) {
		Object[] item = (Object[]) rows.get(row);
		return item[column];
	}

	protected Object[] getRow(final int row) {
		return (Object[]) rows.get(row);
	}

	@Override
	public String getColumnName(final int index) {
		return schema[index];
	}

	public int getRowCount() {
		return rows.size();
	}

	public int getColumnCount() {
		return (schema == null) ? 0 : schema.length;
	}
}
