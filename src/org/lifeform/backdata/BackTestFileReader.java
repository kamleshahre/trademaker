package org.lifeform.backdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.lifeform.configuration.Defaults;
import org.lifeform.core.AppException;
import org.lifeform.market.MarketSnapshot;
import org.lifeform.market.MarketSnapshotFilter;
import org.lifeform.market.PriceBar;
import org.lifeform.service.Dispatcher;

/**
 * Reads and validates a data file containing historical price bars. The data
 * file is used for backtesting and optimization of trading strategies.
 */
public class BackTestFileReader {
	public final static int COLUMNS = 4;
	private static final String PROPERTY_FILE_EXT = ".props";
	private final double spread, slippage, commission;
	private final List<PriceBar> priceBars = new ArrayList<PriceBar>();
	private final Properties properties = new Properties();
	private int columns;
	private Date currentDateTime;
	private final String fileName;
	private BufferedReader reader;
	private long snapshotCount, firstMarketLine, lineNumber;
	private static final String LINE_SEP = System.getProperty("line.separator");
	private volatile boolean cancelled;
	private MarketSnapshotFilter filter;

	public void setFilter(final MarketSnapshotFilter mssFilter) {
		filter = mssFilter;
	}

	public List<PriceBar> getPriceBars() {
		return priceBars;
	}

	public double getBidAskSpread() {
		return spread;
	}

	public double getSlippage() {
		return slippage;
	}

	public double getCommission() {
		return commission;
	}

	public long getSnapshotCount() {
		return snapshotCount;
	}

	public BackTestFileReader(final String fileName) throws Exception {
		String line = null;
		int lineNumber = 0;
		this.fileName = fileName;

		File propertyFile = new File(fileName + PROPERTY_FILE_EXT);
		if (propertyFile.exists())
			properties.load(new FileInputStream(propertyFile));
		else
			properties.load(new FileInputStream(fileName));

		try {
			spread = getPropAsDouble("bidAskSpread");
			if (spread < 0) {
				throw new AppException("\"" + "Bid/Ask spread" + "\""
						+ " must be greater or equal to 0.");
			}

			slippage = getPropAsDouble("slippage");
			if (slippage < 0) {
				throw new AppException("\"" + "Slippage" + "\""
						+ " must be greater or equal to 0.");
			}

			commission = getPropAsDouble("commission");
			if (commission < 0) {
				throw new AppException("\"" + "Commission" + "\""
						+ " must be greater or equal to 0.");
			}

			columns = getPropAsInt("columns");
			int dateColumn = getPropAsColumn("dateColumn");
			int timeColumn = getPropAsColumn("timeColumn");
			int openColumn = getPropAsColumn("openColumn");
			int highColumn = getPropAsColumn("highColumn");
			int lowColumn = getPropAsColumn("lowColumn");
			int closeColumn = getPropAsColumn("closeColumn");

			boolean hasVolume = exists("volumeColumn");
			int volumeColumn = hasVolume ? getPropAsColumn("volumeColumn") : 0;

			String separator = getPropAsString("separator");
			String dateFormat = getPropAsString("dateFormat");
			String timeFormat = getPropAsString("timeFormat");
			String timeZone = getPropAsString("timeZone");

			TimeZone tz = TimeZone.getTimeZone(timeZone);
			if (!tz.getID().equals(timeZone)) {
				String msg = "The specified time zone " + "\"" + timeZone
						+ "\"" + " does not exist."
						+ Defaults.getLineSeperator();
				msg += "Examples of valid time zones: "
						+ " America/New_York, Europe/London, Asia/Singapore.";
				throw new Exception(msg);
			}

			DateTimeZone jodaTimeZone = DateTimeZone.forID(timeZone);
			DateTimeFormatter jodaDateFormat = DateTimeFormat
					.forPattern(dateFormat + timeFormat);
			jodaDateFormat = jodaDateFormat.withZone(jodaTimeZone);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat
					+ timeFormat);
			simpleDateFormat.setTimeZone(tz);

			// Enforce strict interpretation of date and time formats
			simpleDateFormat.setLenient(false);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			Dispatcher.getReporter().report("Loading historical data file");

			while ((line = reader.readLine()) != null) {
				lineNumber++;
				boolean isComment = line.startsWith("#");
				boolean isProperty = line.contains("=");
				boolean isBlankLine = (line.trim().length() == 0);

				if (isComment || isProperty || isBlankLine) {
					continue;
				}
				StringTokenizer st = new StringTokenizer(line, separator);

				int tokenCount = st.countTokens();
				if (tokenCount != columns) {
					String msg = "The descriptor defined " + columns
							+ " columns, ";
					msg += "but line #" + lineNumber + " contains "
							+ tokenCount + " columns";
					throw new Exception(msg);
				}

				int tokenNumber = 0;
				Map<Integer, String> tokens = new HashMap<Integer, String>();
				while (st.hasMoreTokens()) {
					tokenNumber++;
					tokens.put(tokenNumber, st.nextToken());
				}

				String dateToken = tokens.get(dateColumn);
				String timeToken = tokens.get(timeColumn);

				org.joda.time.DateTime jodaDate = jodaDateFormat
						.parseDateTime(dateToken + timeToken);
				Date dateTime = new Date(jodaDate.getMillis());
				long date = dateTime.getTime();
				double open = Double.parseDouble(tokens.get(openColumn));
				double high = Double.parseDouble(tokens.get(highColumn));
				double low = Double.parseDouble(tokens.get(lowColumn));
				double close = Double.parseDouble(tokens.get(closeColumn));
				int volume = hasVolume ? Integer.parseInt(tokens
						.get(volumeColumn)) : 0;

				if (currentDateTime != null) {
					if (dateTime.before(currentDateTime)) {
						String msg = "Date-time of this bar is before the date-time of the previous bar.";
						throw new Exception(msg);
					}
				}

				boolean isInvalidPriceBar = (open <= 0) || (high <= 0)
						|| (low <= 0) || (close <= 0);

				if (isInvalidPriceBar) {
					throw new Exception(
							"Open, High, Low, and Close must be greater than zero.");
				}

				if (low > high) {
					throw new Exception("Low must be less or equal to high.");
				}

				if (close < low || close > high) {
					throw new Exception("Close must be between low and high.");
				}

				if (open < low || open > high) {
					throw new Exception("Open must be between low and high.");
				}

				PriceBar priceBar = new PriceBar(date, open, high, low, close,
						volume);
				priceBars.add(priceBar);
				currentDateTime = dateTime;

			}
			reader.close();

		} catch (Exception e) {
			String msg = "";
			if (lineNumber > 0) {
				msg = "Problem parsing line #" + lineNumber + ": " + line
						+ Defaults.getLineSeperator();
			}

			String description = e.getMessage();
			if (description == null) {
				description = e.toString();
			}
			msg += description;
			throw new Exception(msg);
		}
	}

	private String getPropAsString(final String property) throws Exception {
		String propValue = (String) properties.get(property);
		if (propValue == null) {
			String msg = "Property \"" + property
					+ "\" is not defined in the historical data file.";
			throw new Exception(msg);
		}

		return propValue;
	}

	private int getPropAsInt(final String property) throws Exception {
		String propValue = getPropAsString(property);
		int value;
		try {
			value = Integer.parseInt(propValue);
		} catch (NumberFormatException nfe) {
			String msg = "Value " + propValue + " of property " + property
					+ " is not an integer.";
			throw new Exception(msg);
		}

		return value;
	}

	private double getPropAsDouble(final String property) throws Exception {
		String propValue = getPropAsString(property);
		double value;
		try {
			value = Double.parseDouble(propValue);
		} catch (NumberFormatException nfe) {
			String msg = "Value " + propValue + " of property " + property
					+ " is not a number.";
			throw new Exception(msg);
		}

		return value;
	}

	private int getPropAsColumn(final String property) throws Exception {
		int column = getPropAsInt(property);
		if (column > columns) {
			String msg = "Total number of columns is " + columns + ", ";
			msg += "but property \"" + property + "\" specifies column "
					+ column;
			throw new Exception(msg);
		}

		return column;
	}

	private boolean exists(final String property) {
		String propValue = (String) properties.get(property);
		return propValue != null;
	}

	public void scan() throws Exception {
		String line;
		try {
			while ((line = reader.readLine()) != null && !cancelled) {
				lineNumber++;
				boolean isComment = line.startsWith("#");
				boolean isProperty = line.contains("=");
				boolean isBlankLine = (line.trim().length() == 0);
				boolean isMarketDepthLine = !(isComment || isProperty || isBlankLine);
				if (isMarketDepthLine) {
					MarketSnapshot marketSnapshot = toMarketDepth(line);
					if (filter == null || filter.accept(marketSnapshot)) {
						snapshotCount++;
						if (firstMarketLine == 0) {
							firstMarketLine = lineNumber;
						}
					}
				}

				if (isProperty) {
					if (line.startsWith("timeZone")) {
						setTimeZone(line);
					}
				}
			}

			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName)));
			for (int lineCount = 1; lineCount < firstMarketLine; lineCount++) {
				reader.readLine();
			}
			lineNumber = firstMarketLine;

		} catch (IOException ioe) {
			throw new Exception("Could not read data file");
		}

	}

	SimpleDateFormat sdf = new SimpleDateFormat("MMddyy,HHmmss");

	private void setTimeZone(final String line) throws Exception {
		String timeZone = line.substring(line.indexOf('=') + 1);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		if (!tz.getID().equals(timeZone)) {
			String msg = "The specified time zone " + "\"" + timeZone + "\""
					+ " does not exist." + "\n";
			msg += "Examples of valid time zones: "
					+ " America/New_York, Europe/London, Asia/Singapore.";
			throw new Exception(msg);
		}
		// Enforce strict interpretation of date and time formats
		sdf.setLenient(false);
		sdf.setTimeZone(tz);
	}

	private long previousTime;

	public MarketSnapshot next() {
		String line = "";
		MarketSnapshot marketSnapshot = null;

		try {
			while (marketSnapshot == null) {
				line = reader.readLine();

				if (line == null) {
					reader.close();
					break;
				} else {
					marketSnapshot = toMarketDepth(line);
					lineNumber++;
					if (filter == null || filter.accept(marketSnapshot)) {
						previousTime = marketSnapshot.getTime();
					} else {
						marketSnapshot = null;
					}
				}
			} // while
		} catch (Exception e) {
			String errorMsg = "";
			if (lineNumber > 0) {
				errorMsg = "Problem parsing line #" + lineNumber + LINE_SEP;
				errorMsg += line + LINE_SEP;
			}
			String description = e.getMessage();
			if (description == null) {
				description = e.toString();
			}
			errorMsg += description;
			throw new RuntimeException(errorMsg);
		}

		return marketSnapshot;
	}

	private MarketSnapshot toMarketDepth(final String line) throws Exception {
		if (sdf == null) {
			String msg = "Property " + "\"timeZone\""
					+ " is not defined in the data file." + LINE_SEP;
			throw new Exception(msg);
		}

		StringTokenizer st = new StringTokenizer(line, ",");

		int tokenCount = st.countTokens();
		if (tokenCount != COLUMNS) {
			String msg = "The line should contain exactly " + COLUMNS
					+ " comma-separated columns.";
			throw new Exception(msg);
		}

		String dateToken = st.nextToken();
		String timeToken = st.nextToken();
		long time;
		try {
			time = sdf.parse(dateToken + "," + timeToken).getTime();
		} catch (ParseException pe) {
			throw new Exception("Could not parse date/time in " + dateToken
					+ "," + timeToken);
		}

		if (previousTime != 0) {
			if (time <= previousTime) {
				String msg = "Timestamp of this line is before or the same as the timestamp of the previous line.";
				throw new Exception(msg);
			}
		}

		double balance = Double.parseDouble(st.nextToken());
		double price = Double.parseDouble(st.nextToken());
		return new MarketSnapshot(time, balance, price);
	}
}
