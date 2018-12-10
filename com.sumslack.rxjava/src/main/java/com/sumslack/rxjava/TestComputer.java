package com.sumslack.rxjava;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.schedulers.Schedulers;

public class TestComputer {
	private static final int MAX_I = 210000000;
	
	private static void spendTime(long preTime) {
		System.out.println("花费：" + (System.currentTimeMillis() - preTime) + " 毫秒");
	}
	
	private static void spendTime(long preTime,String str) {
		System.out.println("[" + str + "] 花费：" + (System.currentTimeMillis() - preTime) + " 毫秒");
	}
	private static ExecutorService eService = Executors.newCachedThreadPool();
	public static void main(String[] args) throws Exception{
		
		int[] ss = new int[MAX_I];
		for(int i=1;i<=MAX_I;i++) {
			ss[i-1] = i;
		}
		
		
		long c = System.currentTimeMillis();
		System.out.println(xx(0,MAX_I));
		spendTime(c,"顺序执行");
		
		final long cc5 = System.currentTimeMillis();
		Observable.range(1, MAX_I).map(new Function<Integer, Double>() {
			@Override
			public Double apply(Integer t) throws Exception {
				return Math.sqrt(t);
			}
		}).reduce((i,j)->i+j)
		.subscribeOn(Schedulers.computation())
		.subscribe(s -> {
			spendTime(cc5,"Observable直接算");
		});
		
		final long cc = System.currentTimeMillis();
		CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> {
			return xx(0,MAX_I/2);
		});
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> {
        	return xx(MAX_I/2,MAX_I);
        });
        cf1.thenCombine(cf2,  (i,j)->{
        	System.out.println(""+(i+j));
            spendTime(cc,"CompletableFuture");
        	return i+j;
        });
               
        //也可以用：CompletableFuture.allOf(cf1,cf2).join();
        c = System.currentTimeMillis();
        Double dd = Arrays.stream(ss).mapToDouble(d -> Math.sqrt(d)).reduce(0d,Double::sum);
        System.out.println(dd);
        spendTime(cc,"stream");
        
        c = System.currentTimeMillis();
        Double dd2 = Arrays.stream(ss).parallel().mapToDouble(d -> Math.sqrt(d)).reduce(0d,Double::sum);
        System.out.println(dd2);
        spendTime(cc,"parallel stream");
        
        final long cc2 = System.currentTimeMillis();
        Observable.fromArray(0,1,2)
        .flatMap(new io.reactivex.functions.Function<Integer,ObservableSource<Double>>(){
			@Override
			public ObservableSource<Double> apply(Integer t) throws Exception {
				if(t%3==0) {
					return Observable.just(t)
                        .subscribeOn(Schedulers.computation())
                        .map(new Function<Integer, Double>() {
							@Override
							public Double apply(Integer t) throws Exception {
								return xx(0,MAX_I/3);
							}
						});
				}else if(t%3==1) {
					return Observable.just(t)
	                        .subscribeOn(Schedulers.computation())
	                        .map(new Function<Integer, Double>() {
								@Override
								public Double apply(Integer t) throws Exception {
									return xx(MAX_I/3,MAX_I*2/3);
								}
							});
				}else {
					return Observable.just(t)
	                        .subscribeOn(Schedulers.computation())
	                        .map(new Function<Integer, Double>() {
								@Override
								public Double apply(Integer t) throws Exception {
									return xx(MAX_I*2/3,MAX_I);
								}
							});
				}
			}
        })
        .reduce(new BiFunction<Double, Double, Double>() {
			@Override
			public Double apply(Double t1, Double t2) throws Exception {
				return t1+t2;
			}
		})
        .subscribe( s->{
			System.out.println(s);
			spendTime(cc2,"Observable");
        });
        
        
        
        
        Thread.sleep(100000);
        
        
        
//		Observable.fromArray(ss).groupBy.subscribe(s -> {
//			System.out.println(s);
//			spendTime(cc);
//		});
	}
	
	private static double xx(int start,int end) {
		double sum = 1;
		for(int i=start;i<end;i++) {
			sum += Math.sqrt(i+1);
		}
		return sum;
	}
}
