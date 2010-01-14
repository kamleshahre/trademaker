package org.lifeform.backdata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.lifeform.configuration.Defaults;
import org.lifeform.market.BarSize;
import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.ErrorListener;
import org.lifeform.strategy.Strategy;
import org.lifeform.trader.Trader;
import org.lifeform.util.AppUtil;

import com.ib.client.Contract;

/**
 * Retrieves historical price and volume data for a specified security and saves
 * it as date- and time-stamped OHLCV price bars. Up to one year of intraday
 * historical data can be downloaded for stocks, futures, forex, and indices.
 */
public class BackDataDownloader extends Thread implements ErrorListener {
	private final int requestId;

	private final Trader trader;
	private final Report eventReport;
	private final String fileName;
	private final BarSize barSize;
	private final QuoteHistory qh;
	private final List<PriceBar> priceBars;
	private final BackDataDialog backDataDialog;
	private final Contract contract;
	private PrintWriter writer;
	private final boolean rthOnly;
	private boolean firstBarReached;
	private boolean isCancelled;
	private Calendar firstDate, lastDate;

	public BackDataDownloader(final BackDataDialog backDataDialog, final Contract contract,
			final BarSize barSize, final boolean rthOnly, final String fileName) {
		this.backDataDialog = backDataDialog;
		this.contract = contract;
		this.barSize = barSize;
		this.rthOnly = rthOnly;
		this.fileName = fileName;
		priceBars = new ArrayList<PriceBar>();

		setPeriodBoundaries();
		eventReport = Dispatcher.getReporter();
		trader = Dispatcher.getTrader();
		Strategy strategy = new DownloaderStrategy();
		strategy.setBarSize(barSize);
		trader.getAssistant().addStrategy(strategy);
		requestId = strategy.getId();
		qh = strategy.getQuoteHistory();

		trader.addErrorListener(this);
	}

	@Override
	public void run() {
		try {

			download();

			if (!isCancelled) {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(
						fileName, false)));
				writeDescriptior();
				writeBars();
				backDataDialog.setProgress(100, "Done");
				backDataDialog.signalCompleted();
				String msg = priceBars.size()
						+ " bars have been downloaded successfully.";
				eventReport.report(msg);
				AppUtil.showMessage(backDataDialog, msg);
			}
		} catch (Exception t) {
			eventReport.report(t);
			AppUtil.showError(backDataDialog, t.getMessage());
		} finally {
			if (writer != null) {
				writer.close();
			}
			backDataDialog.signalCompleted();
			qh.setIsHistRequestCompleted(true);
			trader.setIsPendingHistRequest(false);
			trader.removeErrorListener(this);
		}
	}

	public void error(final int errorCode, final String errorMsg) {

		firstBarReached = (errorCode == 162 && errorMsg
				.contains("HMDS query returned no data"));
		if (firstBarReached) {
			firstBarReached = true;
			qh.setIsHistRequestCompleted(true);
			synchronized (trader) {
				trader.setIsPendingHistRequest(false);
				trader.notifyAll();
			}
			return;
		}

		if (errorCode == 162 || errorCode == 200 || errorCode == 321) {
			cancel();
			String msg = "Could not complete back data download."
					+ Defaults.getLineSeperator() + "Cause: " + errorMsg;
			AppUtil.showError(backDataDialog, msg);
		}
	}

	private void download() throws InterruptedException {
		backDataDialog.setProgress(0, "Downloading:");
		int onlyRTHPriceBars = rthOnly ? 1 : 0;

		// "trades" are not reported for Forex, only "midpoint".
		String infoType = contract.m_exchange.equalsIgnoreCase("IDEALPRO") ? "MIDPOINT"
				: "TRADES";
		Calendar cal = (Calendar) lastDate.clone();

		isCancelled = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String endTime = dateFormat.format(cal.getTime());

		// length of the period and last date for progress tracking purposes
		long totalMillis = cal.getTimeInMillis() - firstDate.getTimeInMillis();
		long lastDateMillis = cal.getTimeInMillis();

		while (cal.after(firstDate)) {
			// Make historical data request
			trader.getAssistant().getHistoricalData(requestId, contract,
					endTime, barSize.getHistRequestDuration(),
					barSize.toIBText(), infoType, onlyRTHPriceBars, 2);

			// Wait until the response is returned
			synchronized (trader) {
				while (!qh.getIsHistRequestCompleted()) {
					// wait until the entire price bar history is returned
					trader.wait();
				}
			}

			// For certain contracts, the data is not available as far back as
			// the beginning of the requested interval. If that's the case,
			// simply
			// bail out and use the data set that was received so far.
			if (firstBarReached || isCancelled || qh.size() <= 1) {
				return;
			}

			// Use the timestamp of the first bar in the received
			// block of bars as the "end time" for the next historical data
			// request.
			long firstBarMillis = qh.getFirstPriceBar().getDate();
			cal.setTimeInMillis(firstBarMillis);
			endTime = dateFormat.format(cal.getTime());

			// Add the just received block of bars to the cumulative set of
			// bars and clear the current block for the next request
			List<PriceBar> allBars = qh.getAll();
			priceBars.addAll(0, allBars);
			allBars.clear();
			qh.setIsHistRequestCompleted(false);

			// show the download progress
			double progress = 100 * ((lastDateMillis - firstBarMillis) / (double) totalMillis);
			backDataDialog.setProgress((int) progress, "Downloading:");
		}

		long firstBar = firstDate.getTimeInMillis();
		while (priceBars.size() > 0 && priceBars.get(0).getDate() < firstBar) {
			priceBars.remove(0);
		}
	}

	private void setPeriodBoundaries() {
		firstDate = backDataDialog.getStartDate();
		lastDate = backDataDialog.getEndDate();

		if (contract.m_expiry != null) {
			String expiration = contract.m_expiry;
			int expirationYear = Integer.valueOf(expiration.substring(0, 4));
			int expirationMonth = Integer.valueOf(expiration.substring(4, 6));
			Calendar expirationDate = Calendar.getInstance();

			expirationDate.set(Calendar.YEAR, expirationYear);
			expirationDate.set(Calendar.MONTH, expirationMonth - 1);
			expirationDate.set(Calendar.WEEK_OF_MONTH, 3);
			expirationDate.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			if (lastDate.after(expirationDate)) {
				contract.m_includeExpired = true;
				lastDate = expirationDate;
			}
		}
	}

	public void cancel() {
		isCancelled = true;
		qh.setIsHistRequestCompleted(true);
		synchronized (trader) {
			trader.setIsPendingHistRequest(false);
			trader.notifyAll();
		}
		trader.removeErrorListener(this);
	}

	private void writeDescriptior() {
		writer
				.println("# This dataset is generated by JSystemTrader. Data source: Interactive Brokers.");
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
		writer.println("timeZone=" + TimeZone.getDefault().getID());
		writer.println();
	}

	private void writeBars() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM/dd/yyyy,HH:mm:ss,");

		long barsWritten = 0;
		int size = priceBars.size();
		for (PriceBar priceBar : priceBars) {
			String dateTime = dateFormat.format(new Date(priceBar.getDate()));
			String line = dateTime + priceBar.getOpen() + ","
					+ priceBar.getHigh() + ",";
			line += priceBar.getLow() + "," + priceBar.getClose();
			line += "," + priceBar.getVolume();
			writer.println(line);
			barsWritten++;
			if (barsWritten % 100 == 0) {
				double progress = (100 * barsWritten) / size;
				backDataDialog.setProgress((int) progress, "Writing to file:");
			}
		}
	}
}
