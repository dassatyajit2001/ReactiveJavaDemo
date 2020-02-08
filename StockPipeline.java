package com.reactive.demo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Subscriber;

public class StockPipeline {
	public static final BlockingQueue<StockInfo> queue=new LinkedBlockingQueue<>(10);
	public static Observable<StockInfo> getFeed() {
		return Observable.create(subscriber->processRequest(subscriber));		
	}

	/**
	 * main method to read the data from the queue
	 * @param subscriber
	 */
	private static void processRequest(final Subscriber<? super StockInfo> subscriber) {
	
		System.out.println("Preparing to read data from queue");

		while(!subscriber.isUnsubscribed())
			queue.stream()
			.forEach(stockInfo->
				readAndFlushDataFromQueue(subscriber, stockInfo));
	}

	/**
	 * This method reads the data and removes from the queue because it is a bounded queue
	 * @param subscriber
	 * @param stockInfo
	 */
	private static void readAndFlushDataFromQueue(final Subscriber<? super StockInfo> subscriber, StockInfo stockInfo) {
		subscriber.onNext(stockInfo);
		queue.poll();
	}
	

}
