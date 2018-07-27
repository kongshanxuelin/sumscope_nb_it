package com.sumslack.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.alibaba.fastjson.JSON;
import com.sumslack.test.service.RedisService;

public class MyApplicationContext implements ApplicationListener<ApplicationReadyEvent>{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		ConfigurableApplicationContext ac = event.getApplicationContext();
		RedisService redisService = ac.getBean(RedisService.class);
		redisService.subscribe("testdemo", new MessageListener() {
			@Override
			public void onMessage(Message msg, byte[] arg1) {
				System.out.println("sub channel on message:["+new String(msg.getChannel())+"]:"+new String(msg.getBody()));
			}
		});
	}
}
