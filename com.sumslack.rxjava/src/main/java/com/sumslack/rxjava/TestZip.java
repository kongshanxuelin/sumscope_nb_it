package com.sumslack.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;

public class TestZip {
	public static void main(String[] args) {
		Observable.zip(getStringObservable(),getIntegerObservable(),new BiFunction<String,Integer,String>(){
			@Override
			public String apply(String s, Integer i) throws Exception {
				return s+":"+i;
			}
		}).subscribe(ss -> {
			System.out.println(ss);
		});
		
		Observable.concat(Observable.just("a","b"),Observable.just("c","d","e")).subscribe(ss -> {
			System.out.println(ss);
		});
	}
	
	
	private static Observable<String> getStringObservable(){
		return Observable.create(new ObservableOnSubscribe<String>() {

			@Override
			public void subscribe(ObservableEmitter<String> e) throws Exception {
				if(!e.isDisposed()) {
					e.onNext("A");
					e.onNext("B");
					e.onNext("C");
					e.onNext("D");
				}
			}
		});
	}
	
	private static Observable<Integer> getIntegerObservable(){
		return Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> e) throws Exception {
				if(!e.isDisposed()) {
					e.onNext(1);
					e.onNext(2);
					e.onNext(3);
				}
			}
		});
	}
}
