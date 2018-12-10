package com.sumslack.rxjava;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class TestObservable {
	public static void main(String[] args) {
		System.out.println("Single");
		Single.just(new Random().nextInt())
		.subscribe(s -> {
			System.out.println(s);
		},e -> {
			System.out.println("Single error.");
		});
		Observable.create(new ObservableOnSubscribe<Response>() {
			@Override
			public void subscribe(ObservableEmitter<Response> e) throws Exception {
				Builder builder = new Builder()
						.url("http://wx.sumslack.com/restful/stat/stat.jhtml")
						.get();
				Request request = builder.build();
				Call call = new OkHttpClient().newCall(request);
				Response response = call.execute();
                e.onNext(response);
			}
		}).map(new Function<Response,String>(){
			@Override
			public String apply(Response r) throws Exception {
				return r.body().string();
			}
		}).subscribe(s -> {
			System.out.println("get url:"+s);
		});
		
	}
}
