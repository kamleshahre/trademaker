package org.lifeform.opentick;

import java.util.List;

import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;

import com.opentick.OTClient;
import com.opentick.OTError;
import com.opentick.OTMessage;
import com.opentick.OTOHLC;
import com.opentick.OTTrade;

/**
 */
public class OpenTickClient extends OTClient {
	private final OTBackDataDownloader downloader;
	private final Report eventReport;

	public OpenTickClient(final OTBackDataDownloader downloader) {
		this.downloader = downloader;
		eventReport = Dispatcher.getReporter();

		addHost("feed1.opentick.com", 10010);
		addHost("feed1.opentick.com", 10015);
		addHost("feed2.opentick.com", 10010);
	}

	@Override
	public void onRestoreConnection() {
		eventReport.report("OpenTick connection restored");
	}

	@Override
	public void onError(final OTError error) {
		eventReport.report("OpenTick Error: " + error);
		downloader.error(error);
	}

	@Override
	public void onStatusChanged(final int status) {
		eventReport.report("OpenTick login status " + status);
	}

	@Override
	public void onLogin() {
		eventReport.report("Logged in to OpenTick server");
		downloader.getExchanges();
	}

	@Override
	public void onMessage(final OTMessage message) {
		eventReport.report("OpenTick Message: " + message);
		String description = message.getDescription();
		if (description.equalsIgnoreCase("End of data")) {
			downloader.responseCompleted();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onListExchanges(final List exchanges) {
		eventReport.report("Received list of " + exchanges.size()
				+ " OpenTick exchanges");
		exchanges.remove(0);
		downloader.setExchanges(exchanges);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onListSymbols(final List symbols) {
		eventReport.report("Received list of " + symbols.size()
				+ " OpenTick symbols for specified exchange");
		downloader.setSecurities(symbols);
	}

	@Override
	public void onHistOHLC(final OTOHLC bar) {
		downloader.addBar(bar);
	}

	@Override
	public void onHistTrade(final OTTrade trade) {
		if (trade.getPrice() > 0) {
			downloader.addTrade(trade);
		}
	}

}
