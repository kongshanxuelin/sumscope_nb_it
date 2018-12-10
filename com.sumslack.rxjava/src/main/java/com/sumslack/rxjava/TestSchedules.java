package com.sumslack.rxjava;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TestSchedules {
	public static void main(String[] args) {
		Observable.just(1,3,2)
		.observeOn(Schedulers.newThread())
		.map(new Function<Integer,String>(){
			@Override
			public String apply(Integer t) throws Exception {
				return "hell:"+t;
			}
		})
		.subscribeOn(Schedulers.single())
		//.observeOn(Schedulers.io())
		.subscribe( s -> System.out.println(s));
	}
}
