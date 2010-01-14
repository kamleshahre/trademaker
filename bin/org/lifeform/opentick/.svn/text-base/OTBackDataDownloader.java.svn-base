package org.lifeform.opentick;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.lifeform.market.PriceBar;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.util.AppUtil;

import com.opentick.OTConstants;
import com.opentick.OTDataEntity;
import com.opentick.OTError;
import com.opentick.OTException;
import com.opentick.OTLoginException;
import com.opentick.OTOHLC;
import com.opentick.OTSymbol;
import com.opentick.OTTrade;

/**
 * Uses OpenTick API to download historical price and volume data for specified
 * exchange and security. Saves the downloaded data in a file as date- and
 * time-stamped OHLCV price-volume bars.
 */
public class OTBackDataDownloader {
	private static final TimeZone tz = TimeZone.getTimeZone("America/New_York");
	private static final String NOT_SPECIFIED = "No description";
	private final Report eventReport;
	private List<PriceBar> priceBars;
	private final OTBackDataDialog backDataDialog;
	private PrintWriter writer;
	private boolean isCancelled;
	private final OpenTickClient openTickClient;
	private TickAccumulator tickAccumulator;
	private boolean responseCompleted;
	private int requestID;
	private long currentBarDate;
	private int barSize;

	class Worker extends Thread {
		public void run() {
			downloadBars();
		}
	}

	public OTBackDataDownloader(final OTBackDataDialog backDataDialog)
			throws OTLoginException {
		eventReport = Dispatcher.getReporter();
		this.openTickClient = new OpenTickClient(this);
		this.backDataDialog = backDataDialog;
		eventReport.report("Logging to OpenTick");
		openTickClient.login(backDataDialog.getUserName(), backDataDialog
				.getPassword());
	}

	public void getExchanges() {
		try {
			eventReport.report("Requested list of OpenTick exchanges");
			openTickClient.requestListExchanges();
		} catch (Throwable t) {
			eventReport.report(t);
			AppUtil.showError(backDataDialog, t.getMessage());
		}
	}

	public void logout() {
		if (openTickClient != null && openTickClient.isLoggedIn()) {
			new Thread(new Runnable() {
				public void run() {
					try {
						openTickClient.logout();
						backDataDialog.close();
					} catch (OTException ote) {
						AppUtil.showError(backDataDialog, ote.getMessage());
					}
				}
			}).start();
		} else {
			backDataDialog.close();
		}
	}

	public void setExchanges(final List<?> exchanges) {
		backDataDialog.setExchanges(exchanges);
	}

	public void addBar(final OTOHLC bar) {
		// use the "right edge of bar" for timestamping convention
		bar.setTimestamp(bar.getTimestamp() + barSize);
		PriceBar priceBar = new PriceBar(bar.getClosePrice());
		priceBars.add(priceBar);
		currentBarDate = priceBar.getDate();
	}

	public void addTrade(final OTTrade trade) {
		OTOHLC bar = tickAccumulator.accumulate(trade);
		if (bar != null) {
			addBar(bar);
		}
	}

	public void getSecurities(final String exchangeCode) throws OTException {
		eventReport.report("Requested list of OpenTick symbols for exchange "
				+ exchangeCode);
		openTickClient.requestListSymbols(exchangeCode);
	}

	public void setSecurities(final List<?> securities) {
		backDataDialog
				.setIndeterminateProgress("Processing securities for exchange "
						+ backDataDialog.getFullExchange());
		List<String> allSecurities = new ArrayList<String>();
		for (Object security1 : securities) {
			OTSymbol security = (OTSymbol) security1;
			String company = security.getCompany();
			if (company.length() > 50) {
				company = company.substring(0, 50);
			}
			if (company == null || company.length() == 0) {
				company = NOT_SPECIFIED;
			}

			allSecurities.add(security.getCode() + ": " + company);
		}

		String[] securitiesArray = allSecurities
				.toArray(new String[allSecurities.size()]);
		backDataDialog.setSecurities(securitiesArray);
	}

	public void responseCompleted() {
		responseCompleted = true;
		requestID = 0;
	}

	public void error(final OTError error) {
		if (!openTickClient.isLoggedIn()) {
			backDataDialog.enableLogin();
		}
		isCancelled = true;
		backDataDialog.closeProgress();
		String msg = "Open Tick error " + error.getCode() + ": "
				+ error.getDescription();
		eventReport.report(msg);
		AppUtil.showError(backDataDialog, msg);
	}

	public void download() {
		new Worker().start();
	}

	private void downloadBars() {
		try {
			barSize = backDataDialog.getBarSize().toSeconds();
			String exchange = backDataDialog.getExchange();
			String security = backDataDialog.getSecurity();
			priceBars = new ArrayList<PriceBar>();
			responseCompleted = false;

			long startDate = backDataDialog.getStartDate().getTimeInMillis();
			int start = (int) (startDate / 1000);

			long endDate = backDataDialog.getEndDate().getTimeInMillis();
			int end = (int) (endDate / 1000);

			OTDataEntity entity = new OTDataEntity(exchange, security);
			if (barSize >= 60) {
				requestID = openTickClient.requestHistData(entity, start, end,
						OTConstants.OT_HIST_OHLC_MINUTELY, barSize / 60);
			} else {
				tickAccumulator = new TickAccumulator(barSize);
				requestID = openTickClient.requestHistTicks(entity, start, end,
						OTConstants.OT_TICK_TYPE_TRADE);
			}
			String msg = "Requested OpenTick historical data for " + exchange
					+ "/" + security + ", requestID: " + requestID;
			eventReport.report(msg);

			backDataDialog.setProgress(0, "Initializing...",
					"Downloading historical data for " + security);
			isCancelled = false;

			// length of the period and last date for progress tracking purposes

			long totalPeriodInMillis = endDate - startDate;
			currentBarDate = startDate;

			while (!responseCompleted && !isCancelled) {
				Thread.sleep(2000);
				double progress = 100 * ((currentBarDate - startDate) / (double) totalPeriodInMillis);
				String progressMsg = "waiting for OpenTick response";
				if (priceBars.size() > 0) {
					progressMsg = priceBars.size() + " bars";
				}
				backDataDialog.setProgress((int) progress, progressMsg,
						"Downloading historical data for " + security);
			}

			if (!isCancelled) {
				String fileName = backDataDialog.getFileName();
				writer = new PrintWriter(new BufferedWriter(new FileWriter(
						fileName, false)));
				writeDescriptior();
				writeBars();
				backDataDialog.setProgress(100, priceBars.size() + " bars",
						"Done");
				msg = priceBars.size()
						+ " bars have been downloaded successfully.";
				eventReport.report(msg);
				AppUtil.showMessage(backDataDialog, msg);
			}
		} catch (Throwable t) {
			eventReport.report(t);
			AppUtil.showError(backDataDialog, t.getMessage());
		} finally {
			if (writer != null) {
				writer.close();
			}
			backDataDialog.signalCompleted();
		}
	}

	public void cancel() {
		try {
			isCancelled = true;
			if (requestID != 0) {
				eventReport.report("Cancelling OpenTick historical request "
						+ requestID);
				openTickClient.cancelHistData(requestID);
			}
		} catch (Throwable t) {
			eventReport.report(t);
			AppUtil.showError(backDataDialog, t.getMessage());
		}
	}

	private void writeDescriptior() {
		writer
				.println("# This dataset is generated by JSystemTrader. Data source: OpenTick");
		writer.println("columns=7");
		writer.println("dateColumn=1");
		writer.println("timeColumn=2");
		writer.println("openColumn=3");
		writer.println("highColumn=4");
		writer.println("lowColumn=5");
		writer.println("closeColumn=6");
		writer.println("volumeColumn=7");
		writer.println("separator=,");
		writer.println("dateFormat=MM/dd/yyyy");
		writer.println("timeFormat=HH:mm:ss");
		writer.println("timeZone=" + tz.getID());
		writer.println();
	}

	private void writeBars() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM/dd/yyyy,HH:mm:ss,");
		dateFormat.setTimeZone(tz);

		long barsWritten = 0;
		int size = priceBars.size();
		for (PriceBar priceBar : priceBars) {
			calendar.setTimeInMillis(priceBar.getDate());
			String dateTime = dateFormat.format(calendar.getTime());
			String line = dateTime + priceBar.getOpen() + ","
					+ priceBar.getHigh() + ",";
			line += priceBar.getLow() + "," + priceBar.getClose();
			line += "," + priceBar.getVolume();
			writer.println(line);
			barsWritten++;
			if (barsWritten % 100 == 0) {
				double progress = (100 * barsWritten) / size;
				backDataDialog.setProgress((int) progress, "",
						"Writing downloaded data to file");
			}
		}
	}
}
