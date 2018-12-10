package com.sumslack.rxjava;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class TestGroupBy {
	public static void main(String[] args) {
		Observable.range(1, 8)
		.groupBy(new Function<Integer,String>(){
			@Override
			public String apply(Integer t) throws Exception {
				return t%2 == 0 ?"偶数组":"奇数组"; 
			}
		}).subscribe(s -> {
			if(s.getKey().equals("奇数组")) {
				//s.subscribe( i -> System.out.println(i));
				s.reduce(new BiFunction<Integer,Integer,Integer>(){
					@Override
					public Integer apply(Integer t1, Integer t2) throws Exception {
						return t1+t2;
					}
				}).subscribe(t -> System.out.println("奇数求和："+t));
			}
		});
	}
}
