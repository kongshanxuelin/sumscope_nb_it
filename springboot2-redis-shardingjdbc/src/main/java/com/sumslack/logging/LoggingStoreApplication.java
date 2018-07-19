package com.sumslack.logging;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LoggingStoreApplication {

	static ConfigurableApplicationContext context;
	public static ConfigurableApplicationContext getContext() {
		return context;
	}
	public static void main(String[] args) {
		context =  new SpringApplicationBuilder()
				.sources(LoggingStoreApplication.class)
				.listeners(new MyApplicationContext())
				.run(args);
		
	}
}
