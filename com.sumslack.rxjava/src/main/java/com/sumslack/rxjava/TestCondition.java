package com.sumslack.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public class TestCondition {
	public static void main(String[] args) throws Exception{
		Observable.just(1,2,3,4)
		//.all(i -> i<10)
		.contains(21)
		.subscribe(s -> System.out.println(s));
		
		Observable.intervalRange(1, 9, 0, 1, TimeUnit.MILLISECONDS)
		.skipUntil(Observable.timer(4, TimeUnit.MILLISECONDS))
		.subscribe(s -> System.out.println(s));;
		
		//直到大于2时才开始发射数据
		Observable.just(1,2,3,4)
		.skipWhile(i -> i<=2)
		.subscribe(s -> System.out.println(s));
		
		System.out.println("takeUntil,但第二个被观察者发送数据或终止时，原被观察者停止发送数据-------");
		Observable.range(1, 10)
		.takeUntil(s -> s==4)
		.subscribe(s -> System.out.println(s));
		System.out.println("takeUntil------");
		
		Thread.sleep(5000);
		
	}
}
