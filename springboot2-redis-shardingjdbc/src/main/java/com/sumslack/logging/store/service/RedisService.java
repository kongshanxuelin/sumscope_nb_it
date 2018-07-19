package com.sumslack.logging.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

@Service
public class RedisService {
	public static final class Channel{
		public static final String api_controller2db = "api.controller.stat"; 
	}
	
	@Autowired
	private RedisConf redisConf;

	public static JedisPool pool = null;
	private Jedis jedis = null;
	
	public RedisService initRedis() {
		getRedis();
		return this;
	}
	
	public void publish(final String chanelId,final String body) {
		Jedis jedis = null;
		try{
			jedis = getRedis();
			long i = jedis.publish(chanelId, body);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	public void sub(String channel,JedisPubSub pubsub) {
		Jedis jedis = null;
		try{
			jedis = getRedis();
			jedis.subscribe(pubsub, channel);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	public void put(final String key,Object value){
		Jedis jedis = null;
		if(value == null) return;
		try{
			jedis = getRedis();
			jedis.set(key, JSON.toJSONString(value));
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	public Object get(final String key,Class cls){
		Jedis jedis = null;
		try{
			jedis = getRedis();
			String str = jedis.get(key);
			if(StringUtils.isEmpty(str)){
				return null;
			}
			return JSON.parseObject(key, cls);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		return null;
	}
	
	public String get(final String key){
		Jedis jedis = null;
		try{
			jedis = getRedis();
			String str = jedis.get(key);
			return str;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		return null;
	}
	
	public void putList(final String key,List list){
		Jedis jedis = null;
		if(list == null) return; 
		try{
			jedis = getRedis();
			jedis.set(key, JSON.toJSONString(list));
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	public List getList(final String key,Class cls){
		Jedis jedis = null;
		try{
			jedis = getRedis();
			String str = jedis.get(key);
			if(StringUtils.isEmpty(str)){
				return null;
			}
			return JSON.parseArray(str,cls);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		return null;
	}
	
	
	
	public void forceRestartPool(){
		if(pool!=null) {
			pool.close();
			pool.destroy();
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setJmxEnabled( true );
		config.setBlockWhenExhausted(false);
		config.setMaxIdle(redisConf.getPool().get(RedisConf.POOL_KEY.MaxIdle));
		config.setMaxTotal(redisConf.getPool().get(RedisConf.POOL_KEY.MaxActive));
		config.setMinIdle(redisConf.getPool().get(RedisConf.POOL_KEY.MinIdle));
		config.setMaxWaitMillis(redisConf.getPool().get(RedisConf.POOL_KEY.MaxWait));
		config.setMaxWaitMillis(-1);
		config.setSoftMinEvictableIdleTimeMillis(1800000);
		config.setTestOnBorrow(false);
		config.setTestOnReturn(false);
		config.setTestWhileIdle(true);
		config.setTimeBetweenEvictionRunsMillis(-1);
		boolean borrowOrOprSuccess = true;
		pool = new JedisPool(config, redisConf.getHost(), redisConf.getPort(), redisConf.getTimeout(),redisConf.getPassword());
	}
	
	
	
	
	private Jedis getRedis(){
		if(pool == null){
			
			System.out.println("redis init:%s:%i".format(redisConf.getHost(), redisConf.getPort()));
			JedisPoolConfig config = new JedisPoolConfig();
			//			config.setEvictionPolicyClassName( "org.apache.commons.pool2.impl.DefaultEvictionPolicy" );
			config.setJmxEnabled( true );
			config.setBlockWhenExhausted(false);
			config.setMaxIdle(redisConf.getPool().get(RedisConf.POOL_KEY.MaxIdle));
			config.setMaxTotal(redisConf.getPool().get(RedisConf.POOL_KEY.MaxActive));
			config.setMinIdle(redisConf.getPool().get(RedisConf.POOL_KEY.MinIdle));
			config.setMaxWaitMillis(redisConf.getPool().get(RedisConf.POOL_KEY.MaxWait));
			config.setSoftMinEvictableIdleTimeMillis(1800000);
			config.setTestOnBorrow(false);
			config.setTestOnReturn(false);
			config.setTestWhileIdle(true);
			config.setTimeBetweenEvictionRunsMillis(-1);
			pool = new JedisPool(config, redisConf.getHost(), redisConf.getPort(), redisConf.getTimeout(),redisConf.getPassword());
		}
		jedis = pool.getResource();
		//jedis.auth(REDIS_PWD);
		return jedis;
	}
	
	
}
