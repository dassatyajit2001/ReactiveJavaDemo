package com.reactive.demo;

import yahoofinance.Stock;

public class StockInfo {

	public StockInfo(String symbol, Stock stock) {
		super();
		this.symbol = symbol;
		this.stock = stock;
	}

	private String symbol;
	private Stock stock;

	/**
	 * @return the stock
	 */
	public Stock getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(Stock stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "StockInfo [symbol=" + symbol + ", stock=" + stock + "]";
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
