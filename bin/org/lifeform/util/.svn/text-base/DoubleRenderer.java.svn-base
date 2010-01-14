package org.lifeform.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.table.DefaultTableCellRenderer;

public class DoubleRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = -8076330088197259420L;
	private final DecimalFormat df;
	private static final int SCALE = 4;
	private static int SCALE_10;

	{
		int temp = 10;
		for (int i = 0; i < SCALE; i++) {
			temp = temp * 10;
		}
		SCALE_10 = temp;
	}

	public DoubleRenderer() {
		df = (DecimalFormat) NumberFormat.getNumberInstance();
		df.setMaximumFractionDigits(SCALE);
		df.setGroupingUsed(false);
	}

	@Override
	public void setValue(final Object value) {
		String text = "";
		if (value != null) {
			if (value instanceof Double) {
				if (!Double.isInfinite((Double) value)
						&& !Double.isNaN((Double) value)) {
					double temp = ((Double) value).doubleValue() * SCALE_10;

					temp = Math.floor(temp) / SCALE_10;

					text = df.format(temp);
				} else {
					text = "N/A";
				}
			} else if (value instanceof Integer) {
				text = value.toString();
			} else if (value instanceof String) {
				text = value.toString();
			} else {
				throw new RuntimeException("Could not convert "
						+ value.getClass() + " to a number");
			}
		}
		setHorizontalAlignment(RIGHT);
		setText(text + " ");
	}
}
