/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ernan Hughes - initial API and implementation
 *******************************************************************************/
package org.lifeform.market.service.quote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import org.lifeform.market.Quote;
import org.lifeform.market.QuoteException;

public class Yahoo extends WebPageService implements QuoteSource {
	private String symbol;

	public boolean fetch(final Quote quote) throws QuoteException {
		// symbol = quote.getSymbol();
		String content;

		String u = "http://finance.yahoo.com/d/quotes.csv?s=" + symbol
				+ "&f=snl1d1t1c1ohgv&e=.c";

		try {
			URL url = new URL(u);

			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));

			content = in.readLine();
			in.close();

		} catch (Exception e) {
			throw new QuoteException("Couldn't retrieve quote - " + e);
		}

		StringTokenizer tk = new StringTokenizer(content, ",");
		tk.nextToken(); // symbol
		// quote.setCompany(stripQuotes(tk.nextToken())); // name
		quote.setValue(Float.parseFloat(tk.nextToken())); // value
		tk.nextToken(); // date
		tk.nextToken(); // time
		tk.nextToken(); // net

		try {
			// quote.setOpenPrice(Float.parseFloat(tk.nextToken())); // open
			// price
		} catch (NumberFormatException nfe) {
			// quote.setOpenPrice(0);
		}

		tk.nextToken(); // Daily High
		tk.nextToken(); // Daily Low

		try {
			// quote.setVolume(Long.parseLong(tk.nextToken()));
		} catch (NumberFormatException nfe) {
			// quote.setVolume(0);
		}

		return true;
	}
}
