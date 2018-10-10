package com.sumslack.patterndesign.demo.wanzhuan;

public class Digui {
	public long fact(long n) {
		if(n == 1) {
			return 1;
		}else {
			return n*fact(n-1);
		}
	}
	public long fact(long n,long result) {
		if(n ==1) {
			return result;
		}else {
			return fact(n-1,n*result);
		}
	}
	
	public static void main(String[] args) {
		Digui d = new Digui();
		long s1 = System.currentTimeMillis();
		System.out.println(d.fact(3000));
		long s2= System.currentTimeMillis();
		System.out.println("n*fact(n-1) spent " + (s2-s1) + " ms");
		System.out.println(d.fact(3000));
		long s3= System.currentTimeMillis();
		System.out.println("fact(n-1,n*result) spent " + (s3-s2) + " ms");
		
	}
}
