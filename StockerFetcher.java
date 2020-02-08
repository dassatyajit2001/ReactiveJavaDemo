package com.reactive.demo;

import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockerFetcher {

	public static StockInfo fetch(String symbol) {
		// TODO Auto-generated method stub
		Stock stock=null;		
			try {
				stock = YahooFinance.get(symbol);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new StockInfo(symbol,stock);
	}

	
}
