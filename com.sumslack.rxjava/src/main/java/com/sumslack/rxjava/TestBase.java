package com.sumslack.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class TestBase {
	public static void main(String[] args) {
		Observable crawler1 = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> e) throws Exception {
				e.onNext("从网站1爬来了想要的数据11");
				e.onNext("从网站1爬来了想要的数据12");
				e.onNext("从网站1爬来了想要的数据13");
				//e.onError(new Exception("errror"));
				e.onComplete();
			}
		});
		Observable crawler2 = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> e) throws Exception {
				e.onNext("从网站2爬来了想要的数据21");
				e.onNext("从网站2爬来了想要的数据22");
				e.onNext("从网站2爬来了想要的数据23");
				//e.onError(new Exception("errror"));
				e.onComplete();
			}
		});
		
		Observable.concat(crawler1,crawler2)
		.map(new Function<String, String>() {
			@Override
			public String apply(@NonNull String arg0) throws Exception {
				return "This is result " + arg0;
			}
		})
//		.observeOn(Schedulers.newThread())
//		.subscribeOn(Schedulers.io())
		.subscribe(new Observer<String>() {
			// 第二步：初始化Observer
            private int i;
            private Disposable mDisposable;
			@Override
			public void onComplete() {
				System.out.println("TEST"+"onComplete" );
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("TEST"+"onError : value : " + e.getMessage());
			}

			@Override
			public void onNext(String str) {
				// 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                /*
				if(str.indexOf("2")>=0) {
					mDisposable.dispose();
				}
				*/
				System.out.println("收到数据:"+str);
			}

			@Override
			public void onSubscribe(Disposable d) {
				mDisposable = d;
			}
			
		});
	}
}
