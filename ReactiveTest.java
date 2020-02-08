package com.reactive.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.schedulers.Schedulers;

public class ReactiveTest {
	public static final Map<String, StockInfo> cache = new ConcurrentHashMap<>(16);

	//subscription List 1
	static final Set<String> request1 = new HashSet<>(Arrays.asList("IBM", "GOOG", "MSFT", "AMZN"));
	//subscription List 2
	static final Set<String> request2 = new HashSet<>(Arrays.asList("KO", "BABA", "CMCSA", "AAPL", "INTC", "PFE"));
	
	//subscription List 3
	static final Set<String> request3 = new HashSet<>(Arrays.asList("IBM", "GOOG", "CMCSA", "AAPL"));

	
	public static void main(String[] args) {

		getAllStocksFromYahooFinance();
		requestForDataWithSpecificSubscription(request1,"client-1 fast Consumer");
		requestForDataWithSpecificSubscription(request2,"client-2 fast Consumer");
//		client3ASlowConsumerRequestForDataWithSpecificSubscription(request3,"client-3 slow Consumer");
		
		// this is not to make the main thread exit
		StockServer.sleep(1000000);
	}

	private static void getAllStocksFromYahooFinance() {
		List<String> symbols = Arrays.asList( "AAPL","KO","GOOG", "MSFT", "AMZN", "BABA", "CMCSA",  "INTC",
				"IBM","PFE");
		Observable<StockInfo> feed = StockServer.getFeed(symbols);
		feed.onErrorResumeNext(throwable -> resumeToNextFunction(symbols, throwable)).
		// filter(stockInfo->stockInfo.getSymbol())
				subscribeOn(Schedulers.io()).subscribe((stockInfo) -> processData(stockInfo));
	}
	
	private static void client3ASlowConsumerRequestForDataWithSpecificSubscription(Set<String> subscriptionList,String clientId) {
		Observable<StockInfo> feed2 = StockPipeline.getFeed();
		feed2.subscribeOn(Schedulers.io()).filter(si -> request2.contains(si.getSymbol())).rebatchRequests(5)
		.subscribe((si) -> {
			//to show delay while consuming
			StockServer.sleep(2000);
			System.out.println(clientId+"  " + si);
		});
		
	}
	
	private static void requestForDataWithSpecificSubscription(Set<String> subscriptionList,String clientId) {
		Observable<StockInfo> feed2 = StockPipeline.getFeed();
		feed2.subscribeOn(Schedulers.io()).filter(si -> subscriptionList.contains(si.getSymbol()))
		.subscribe((si) -> System.out.println(clientId+"  " + si));
	}

	private static Observable<StockInfo> resumeToNextFunction(List<String> symbols, Throwable throwable) {
		System.out.println("inside resumeToNextFunction . I got exception but will continue to get data" + throwable);
		return StockServer.getFeed(symbols);
	}

	private static void processData(StockInfo stockInfo) {
		cache.put(stockInfo.getSymbol(), stockInfo);
		try {
			StockPipeline.queue.put(stockInfo);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Stored in cache" + cache.size() + " " + stockInfo);
	}

}
