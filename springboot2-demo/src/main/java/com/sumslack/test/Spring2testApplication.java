package com.sumslack.test;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Spring2testApplication {
	static ConfigurableApplicationContext context;
	public static ConfigurableApplicationContext getContext() {
		return context;
	}
	public static void main(String[] args) {
		//SpringApplication.run(Spring2testApplication.class, args);
		context =  new SpringApplicationBuilder()
				.sources(Spring2testApplication.class)
				//.child(ConsumerFinIndexLaunch.class)
				.bannerMode(Mode.OFF)
				.listeners(new MyApplicationContext())
				.run(args);
	}
}
