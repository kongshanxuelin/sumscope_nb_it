package com.sumslack.rxjava;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.schedulers.Schedulers;

public class TestMicro {
	private static String[] ss = {"商品详情微服务","商品评论微服务","推荐商品微服务"};
	private static long rand() {
		///return Math.abs(new Random().nextInt()) % 1000;
		return 250;
	}
	
	private static void spendTime(long preTime) {
		System.out.println("花费：" + (System.currentTimeMillis() - preTime) + " 毫秒");
	}
	
	public static void main(String[] args) throws Exception {
		long c = 0L;
//		long c = System.currentTimeMillis();
//		System.out.println("顺序执行:");
//		System.out.println(service("商品详情微服务")+service("商品评论微服务")+service("推荐商品微服务"));
//		spendTime(c);
		
//		c = System.currentTimeMillis();	
//		CompletableFuture<String> ss = CompletableFuture.supplyAsync(() -> service("商品详情微服务"))
//		.thenApply(s -> service(s + "商品评论微服务"))
//		.thenApply(s -> service(s + "推荐商品微服务"));
//		System.out.println("使用JDK8的Future:");
//		System.out.println(ss.get());
//		spendTime(c);
		
//		final long cc = System.currentTimeMillis();	
//		CompletableFuture<String> s1 = CompletableFuture.supplyAsync(() -> service("商品详情微服务"));
//		CompletableFuture<String> s2 = CompletableFuture.supplyAsync(() -> service("商品评论微服务"));
//		CompletableFuture<String> s3 = CompletableFuture.supplyAsync(() -> service("推荐商品微服务"));
//		s1.thenCombine(s2, (i,j)->{
//			return i+j;
//		}).thenCombine(s3, (i,j)->{
//			System.out.println("使用JDK8的并行编程:");
//			System.out.println(i+j);
//			spendTime(cc);
//			return i+j;
//		});
		
//		c = System.currentTimeMillis();	
//		ExecutorService executorService2 = Executors.newCachedThreadPool();
//		executorService2.submit(() -> service("商品详情微服务"));
//		executorService2.submit(() -> service("商品评论微服务"));
//		executorService2.submit(() -> service("推荐商品微服务"));
//		System.out.println("线程池方式：");
//		spendTime(c);
		
		final long cc2 = System.currentTimeMillis();	
//		Observable<String> o1 = Observable.fromCallable(() -> service("商品详情微服务"));
//		Observable<String> o2 = Observable.fromCallable(() -> service("商品评论微服务"));
//		Observable<String> o3 = Observable.fromCallable(() -> service("推荐商品微服务"));
//		Observable.concat(o1,o2,o3).flatMap(new Function<String,Obser>).subscribe(s->{
//			System.out.print(s);
//		},e->{
//			System.out.println("error");
//		},new Action() {
//			@Override
//			public void run() throws Exception {
//				spendTime(cc2);
//			}
//		});

		
		Observable.range(0,3)
		.flatMap(new Function<Integer, ObservableSource<String>>() {
			@Override
			public ObservableSource<String> apply(Integer t) throws Exception {
				return Observable.just(t)
				.subscribeOn(Schedulers.from(Executors.newFixedThreadPool(1)))
				.map(new Function<Integer, String>() {
					@Override
					public String apply(Integer t) throws Exception {
						return service(ss[t]);
					}
				});
			}
		})
		.reduce((s1,s2)->s1+s2)
		.subscribe(s -> {
			System.out.println("Observable：\r\n" + s);
			spendTime(cc2);
		});
		
		
		//Round-Robin算法
        final long cc8 = System.currentTimeMillis();
        final AtomicInteger batch = new AtomicInteger(0);
        int threadNum = 5;
        final ExecutorService s5 = Executors.newFixedThreadPool(threadNum);
        final Scheduler sch = Schedulers.from(s5);
        //ParallelFlowable<String> pp = Flowable.just(service(ss[0]),service(ss[1]),service(ss[2])).parallel();
        ParallelFlowable<Integer> pp = Flowable.range(1, 210000000).parallel();
        pp.runOn(Schedulers.computation())
        .sequential()
        .map(new Function<Integer,Double>(){
			@Override
			public Double apply(Integer t) throws Exception {
				return Math.sqrt(t);
			}
        })
        .reduce((i,j)->i+j)
        .subscribe(s -> {
        	System.out.println(Thread.currentThread().getName()+ "(ss):"+s);
        },e->{
        	
        },new Action() {
			@Override
			public void run() throws Exception {
				System.out.println("ParallelFlowable...");
				spendTime(cc8);
			}
		});
	
		
		
		Thread.sleep(8000);
	}
	
	
	
	private static String service(String srvName){
		try {
			Thread.sleep(rand());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return srvName+"\r\n";
	}
}
