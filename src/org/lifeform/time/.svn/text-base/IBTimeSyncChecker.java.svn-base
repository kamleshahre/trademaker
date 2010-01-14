package org.lifeform.time;

import org.lifeform.configuration.Defaults;
import org.lifeform.util.AppUtil;

/**
 * Utility class to ensure time synchronization between the machine where
 * JSystemTrader is running and the Interactive Brokers' server(s).
 * <p/>
 * It's recommended that a time sync service be running at all times.
 */
public class IBTimeSyncChecker {

	/**
	 * Makes sure that the clock on the machine where JSystemTrader is running
	 * is in sync with the Interactive Brokers server.
	 * 
	 * @param serverTime
	 *            long Time as reported by IB server
	 * 
	 * @throws JSystemTraderException
	 *             If the difference between the two clocks is greater than the
	 *             tolerance
	 */
	public static void timeCheck(final long serverTime) throws Exception {
		long timeNow = System.currentTimeMillis();
		// Difference in seconds between IB server time and local machine's time
		long difference = (timeNow - serverTime) / 1000;

		int tolerance = Integer.parseInt(Defaults.get(Defaults.TimeLagAllowed));

		if (Math.abs(difference) > tolerance) {
			String lineSep = Defaults.getLineSeperator();
			String msg = "This computer's clock is out of sync with the IB server clock."
					+ lineSep;

			msg += Defaults.getLineSeperator() + "IB Server Time: "
					+ DateUtil.toString(serverTime);
			msg += lineSep + "Computer Time: " + DateUtil.toString(timeNow);
			msg += lineSep + "Rounded Difference: " + difference + " seconds";
			msg += lineSep + "Tolerance: " + tolerance + " seconds";
			msg += lineSep + lineSep;
			msg += "Set the machine's clock to the correct time, and restart "
					+ AppUtil.getAppName() + "." + lineSep;
			msg += "A time synchronization service or client is recommended.";

			throw new Exception(msg);
		}
	}
}
