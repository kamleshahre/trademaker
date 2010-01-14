package org.lifeform.market;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.strategy.Strategy;

/**
 * Determines when a new bar should be created and signals to QuoteHistory when
 * such times occur. For example, if the strategy subscribes to 5-minute bars,
 * this class will send a signal at every 5-minute interval, such as 10:00,
 * 10:05, 10:10, and so on. There is one instance of BarFactory for every
 * instance of Strategy.
 * <p/>
 * It's important to run a time synchronization software to make sure that the
 * system clock is accurate, so that the signals are sent at the correct time.
 */
public class BarFactory extends TimerTask {
	private static final int MILLIS_IN_SECOND = 1000;
	private static final int DELAY = 750; // Time delay in milliseconds beyond
	// the bar edge.
	private static final Report eventReport = Dispatcher.getReporter();

	private final QuoteHistory qh;
	private final long frequency;
	private long nextBarTime;

	public BarFactory(final Strategy strategy) {
		qh = strategy.getQuoteHistory();
		frequency = strategy.getBarSize().toSeconds() * MILLIS_IN_SECOND;
		// Integer division gives us the number of whole periods
		long completedPeriods = System.currentTimeMillis() / frequency;
		nextBarTime = (completedPeriods + 1) * frequency;
		// delay is to ensure that the last 5 second bar of each period has
		// arrived
		Date start = new Date(nextBarTime + DELAY);

		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(this, start, frequency);
		eventReport.report(strategy.getName() + ": Bar factory started");
	}

	@Override
	public void run() {
		qh.onBar(nextBarTime);
		nextBarTime += frequency;
	}

}
