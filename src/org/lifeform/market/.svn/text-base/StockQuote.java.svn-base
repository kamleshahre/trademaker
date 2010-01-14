package org.lifeform.market;

import org.lifeform.time.DateUtil;
import org.lifeform.util.Util;

public class StockQuote extends Quote {

	private String market = "NONE";
	private String symbol = "NULL";
	private long volume = 0;
	private double open = 0;
	private double close = 0;
	private double high = 0;
	private double low = 0;


	public StockQuote(final Type type, final double price) {
		super(type, price);
	}

	public StockQuote(final long date, final double open, final double high, final double low,
			final double close, final long volume) {
		super(Type.Price, close);
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	public StockQuote(final double open, final double high, final double low, final double close,
			final long volume) {
		super(Type.Price, close);
		this.date = DateUtil.Now();
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(final double close) {
		this.close = close;
	}

	public void setOpen(final double open) {
		this.open = open;
	}

	public float getChange() {
		return (float) (this.value - open);
	}

	public String getMarket() {
		return market;
	}

	public double getOpen() {
		return open;
	}

	public double getPctChange() {
		return ((this.getChange() / open) * 100);
	}

	public String getSymbol() {
		return symbol;
	}

	public long getVolume() {
		return volume;
	}

	public void setMarket(final String market) {
		this.market = market;
	}

	public void setOpen(final float value) {
		this.open = value;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public void setValue(final float value) {
		this.value = value;
	}

	public void setVolume(final long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {	
		return new StringBuffer(symbol)
			.append(":").append(market)
			.append(" {Close ").append(close)
			.append(", Open ").append(open)
			.append(", High ").append(high)
			.append(", Low ").append(close)
			.append(", Volume ").append(volume)
			.append("}").toString();
	}

	public void setHigh(final double high2) {
		high = high2;
	}

	public void setLow(final double low) {
		this.low = low;
	}

	public static StockQuote fromData(final Object[] data) {
		// [Date, Open, High, Low, Close, Volume, Adj Close]
		long date = DateUtil.fromString(data[0], "yyyy-MM-dd");
		double open = Util.stringToNumber((String) data[1]);
		double high = Util.stringToNumber((String) data[2]);
		double low = Util.stringToNumber((String) data[3]);
		double close = Util.stringToNumber((String) data[4]);
		long volume = Util.stringToLong((String) data[5]);
		double adjClose = Util.stringToNumber((String) data[6]);
		StockQuote sq = new StockQuote(Quote.Type.Price, adjClose);
		sq.setVolume(volume);
		sq.setDate(date);
		sq.setClose(close);
		sq.setOpen(open);
		sq.setHigh(high);
		sq.setLow(low);
		return sq;
	}

	public static StockQuote fromString(final String data, final String seperator) {
		Object[] row = data.split(seperator);
		return fromData(row);
	}

	public static StockQuote fromString(final String data) {
		return fromString(data, ",");
	}
}
