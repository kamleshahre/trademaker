package org.lifeform.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class NumberFormatterFactory {

	public static DecimalFormat getNumberFormatter(final int maxFractionDigits) {
		return getNumberFormatter(maxFractionDigits, false);
	}

	public static DecimalFormat getNumberFormatter(final int maxFractionDigits,
			final boolean grouping) {
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
				.getNumberInstance();
		DecimalFormatSymbols decimalFormatSeparator = new DecimalFormatSymbols();
		decimalFormatSeparator.setDecimalSeparator('.');
		decimalFormat.setGroupingUsed(grouping);
		decimalFormat.setMinimumFractionDigits(0);
		decimalFormat.setMaximumFractionDigits(maxFractionDigits);
		decimalFormat.setDecimalFormatSymbols(decimalFormatSeparator);

		return decimalFormat;
	}

}
