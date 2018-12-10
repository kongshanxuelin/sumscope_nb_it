package com.sumslack.rxjava;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sun.rmi.runtime.Log;

public class TestOperation {
	public static void main(String[] args) {
		//去重
		System.out.println("distinct:");
		Observable.just(1, 1, 1, 2, 2, 3, 4, 5)
        .distinct()
        .doOnNext(s -> {
        	System.out.println("on next before doing sth");
        })
        .subscribe(s -> {
        	System.out.println(s);
        });
		//过滤
		System.out.println("filter:");
		Observable.just(1, 1, 1, 2, 2, 3, 4, 5)
        .filter(s -> s>1)
        .subscribe(s -> {
        	System.out.println(s);
        });
		//按步长分成最大不超过count的被观察者流
		System.out.println("buffer:");
		Observable.just(1, 1, 1, 2, 2, 3, 4, 5)
        .buffer(3,2)
        .subscribe(s -> {
        	System.out.println(s);
        });
		//Timer
		System.out.println("timer:");
		System.out.println("["+new Date()+"] 开始发送:");
		Observable
		.timer(2, TimeUnit.SECONDS)
		.subscribe(s -> {
        	System.out.println("["+new Date()+"]接收："+s);
        });
		
		//interval
		System.out.println("interval:");
		Observable
		.interval(2,1, TimeUnit.SECONDS)
		.subscribe(s -> {
        	System.out.println(s);
        });
		
		//skip 跳过N个开始接收
		//last：观察最后一个值
		//take 至多接收N个
		System.out.println("take:");
		Observable.just(11,12,13,14,56,12,43)
        .take(2)
        .subscribe(s -> {
        	System.out.println(s);
        });
		
		System.out.println("debounce:去除发送间隔时间小于 500 毫秒的发射事件");
		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				emitter.onNext(1); // skip
                Thread.sleep(400);
                emitter.onNext(2); // deliver
                Thread.sleep(505);
                emitter.onNext(3); // skip
                Thread.sleep(100);
                emitter.onNext(4); // deliver
                Thread.sleep(605);
                emitter.onNext(5); // deliver
                Thread.sleep(510);
                emitter.onComplete();
				
			}
		}).debounce(500,TimeUnit.MILLISECONDS)
		.subscribe(s -> {
			System.out.println(s);
		});
		
		System.out.println("merge:concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送");
		Observable.merge(Observable.just(1, 2,6,7), Observable.just(3, 4, 5))
        .subscribe(s -> System.out.println("accept: merge :" + s + "\n" ));
		
		System.out.println("reduce 操作符每次用一个方法处理一个值，可以有一个 seed 作为初始值");
		Observable.just(1, 2, 3,8)
		.reduce(0,new BiFunction<Integer,Integer,Integer>(){
			@Override
			public Integer apply(Integer i1, Integer i2) throws Exception {
				return i1+i2;
			}
			
		}).subscribe(s -> {
			System.out.println("reduce s="+s);
		});
		
		System.out.println("scan与reduce 一致， scan会始终如一地把每一个步骤都输出");
		Observable.just(1, 2, 3,8)
		.scan(new BiFunction<Integer,Integer,Integer>(){
			@Override
			public Integer apply(Integer i1, Integer i2) throws Exception {
				return i1+i2;
			}
			
		}).subscribe(s -> {
			System.out.println("scan s="+s);
		});
		
		//window:按照划分窗口发送给不同被观察者
		Observable
		.interval(1, TimeUnit.SECONDS)
        .take(15) // 最多接收15个
        .window(3, TimeUnit.SECONDS)
        .subscribe(new Consumer<Observable<Long>>() {
            @Override
            public void accept(@NonNull Observable<Long> longObservable) throws Exception {
            	System.out.println("window Sub Divide begin...\n");
                longObservable.subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                System.out.println("window Next:" + aLong + "\n");
                            }
                        });
            }
        });
		System.out.println("defer简单地时候就是每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable。");
		Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return Observable.just(1, 2, 3);
            }
        });
        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                System.out.println("defer : " + integer + "\n");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println( "defer : onError : " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                System.out.println("defer : onComplete\n");
            }
        });
		
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
