package com.sumslack.rxjava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class TestOperation2 {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		Future<String> future = service.submit(new DataCallable("111"));
		Observable.fromFuture(future,1,TimeUnit.SECONDS).subscribe(s -> System.out.println(s),e->System.out.println("error!"));
	}
	
	static class DataCallable implements Callable<String>{
		private String name;
		public DataCallable(String name) {
			this.name = name;
		}
		@Override
		public String call() throws Exception {
			System.out.println("current Thread "+this.name+":" + Thread.currentThread().getName() );
			Thread.sleep(2000); //模拟耗时操作
			return "data1";
		}
		
	}
}
