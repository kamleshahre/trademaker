package org.lifeform.util;

import com.ib.client.Contract;

public class ContractFactory {

	public static Contract makeContract(final String symbol, final String securityType,
			final String exchange, final String expiry, final String currency) {
		Contract contract = new Contract();

		contract.m_symbol = symbol;
		contract.m_secType = securityType;
		contract.m_exchange = exchange;
		contract.m_expiry = expiry;
		contract.m_currency = currency;

		return contract;
	}

	public static Contract makeStockContract(final String symbol, final String exchange,
			final String currency) {
		return makeContract(symbol, "STK", exchange, null, currency);
	}

	public static Contract makeStockContract(final String symbol, final String exchange) {
		return makeStockContract(symbol, exchange, null);
	}

	public static Contract makeFutureContract(final String symbol, final String exchange,
			final String expiry, final String currency) {
		return makeContract(symbol, "FUT", exchange, expiry, currency);
	}

	public static Contract makeFutureContract(final String symbol, final String exchange,
			final String expiry) {
		return makeFutureContract(symbol, exchange, expiry, null);
	}

	public static Contract makeFutureContract(final String symbol, final String exchange) {
		return makeFutureContract(symbol, exchange, MostLiquidContract
				.getMostLiquid());
	}

	public static Contract makeCashContract(final String symbol, final String currency) {
		return makeContract(symbol, "CASH", "IDEALPRO", null, currency);
	}
}
