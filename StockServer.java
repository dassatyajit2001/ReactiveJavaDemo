package com.reactive.demo;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class StockServer {

	/**
	 * the main method that calls the processRequest to get the data from StockFetcher
	 * @param symbols
	 * @return
	 */
	public static Observable<StockInfo> getFeed(List<String> symbols) {
		return Observable.create(subscriber -> processRequest(subscriber, symbols));

	}

	/**
	 * This calls the service to fetch the price and sleeps for some delay
	 * @param subscriber
	 * @param symbols
	 */
	private static void processRequest(final Subscriber<? super StockInfo> subscriber, List<String> symbols) {

		System.out.println("processing");
		// int x=0;

		while (!subscriber.isUnsubscribed()) {

			// while(true)
			symbols.stream().map(m -> StockerFetcher.fetch(m)).forEach(stockInfo -> subscriber.onNext(stockInfo));
			sleep(5000);
			System.out.println();
		}
	}

	
	/**
	 * Sleep method
	 * @param ms
	 */
	public static void sleep(long ms) {
		try {
			Thread.currentThread().sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
