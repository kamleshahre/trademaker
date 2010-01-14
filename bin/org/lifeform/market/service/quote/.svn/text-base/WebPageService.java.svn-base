package org.lifeform.market.service.quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import org.lifeform.market.StockQuote;

public class WebPageService {
	protected String seperator = ",";

	public Object[][] getData(final String sURL) {
		Vector<Object[]> data = new Vector<Object[]>();
		InputStream is = null;
		try {
			URL url = new URL(sURL);
			is = url.openStream(); // throws an IOException
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				Object[] row = line.split(seperator);
				data.add(row);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (null != is) {
					is.close();
				}
			} catch (IOException ioe) {
			}
		}
		return data.toArray(new Object[0][0]);
	}

	public static void main(final String[] args) {
		String url = "http://ichart.finance.yahoo.com/table.csv?s=GXP&ignore=.csv";
		WebPageService s = new WebPageService();
		Object[][] data = s.getData(url);
		for (int i = 1; i < data.length; ++i) {
			StockQuote q = StockQuote.fromData(data[i]);
			System.out.println(q.toString());
		}
	}
}
