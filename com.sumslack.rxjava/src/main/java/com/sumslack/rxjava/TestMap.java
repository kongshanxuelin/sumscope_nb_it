package com.sumslack.rxjava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TestMap {
	public static void main(String[] args) {
		Observable.create(new ObservableOnSubscribe<Integer>() {
	           @Override
	           public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
	               e.onNext(1);
	               e.onNext(2);
	               e.onNext(3);
	               //e.onComplete();
	           }
	           //flatMap是无序的
	       }).concatMap(new Function<Integer, ObservableSource<String>>() {
	           @Override
	           public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
	               List<String> list = new ArrayList<>();
	               for (int i = 0; i < 3; i++) {
	                   list.add("I am value " + integer);
	               }
	               int delayTime = (int) (1 + Math.random() * 10);
	               return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
	           }
	       })
		          // .subscribeOn(Schedulers.newThread())
	               //.observeOn(Schedulers.newThread())
	        .subscribe(s -> {
	        	System.out.println("flatMap : accept : " + s + "\n");
	        });
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
