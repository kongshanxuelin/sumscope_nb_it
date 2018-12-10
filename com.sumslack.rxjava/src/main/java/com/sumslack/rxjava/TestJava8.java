package com.sumslack.rxjava;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class TestJava8 {
	public static void main(String[] args) throws Exception {
		System.out.println(sum(x->x,1,10));
		System.out.println(sum(x->x*x,1,10));
		System.out.println(sum(x->x*x*x,1,10));
		
		Function<Integer,Function<Integer,Function<Integer,Integer>>> fff = x->y->z->(x+y)*z;
		System.out.println(fff.apply(4).apply(5).apply(6));
		
		CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "Hello";
		}).thenAccept(System.out::print);
		System.out.println("End");
		
		Thread.sleep(5000);
		
	}
	
	public static int sum(MyFunc term,int a,int b) {
		int sum = 0;
		for(int i=a;i<=b;i++) {
			sum += term.opera(i);
		}
		return sum;
	}
}



