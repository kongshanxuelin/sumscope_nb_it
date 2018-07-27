package com.sumslack.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class RedisService {
	@Autowired
	public RedisTemplate redisTemplate;
	
	public void publish(String channel,Object msg) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn) throws DataAccessException {
				conn.publish(redisTemplate.getStringSerializer().serialize(channel), 
						redisTemplate.getStringSerializer().serialize(JSON.toJSONString(msg)));
				return null;
			}
		});
	}
	
	public void subscribe(String channel,MessageListener msgListener) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn) throws DataAccessException {
				conn.subscribe(msgListener, redisTemplate.getStringSerializer().serialize(channel));
				return null;
			}
		});
	}
}
