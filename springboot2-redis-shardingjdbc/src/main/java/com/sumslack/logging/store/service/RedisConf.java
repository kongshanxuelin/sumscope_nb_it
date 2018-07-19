package com.sumslack.logging.store.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@ConfigurationProperties(prefix="spring.redis")
public class RedisConf {
	public static final class POOL_KEY {
		public static final String MaxActive = "max-active";
		public static final String MaxWait = "max-wait";
		public static final String MaxIdle = "max-idle";
		public static final String MinIdle = "min-idle";
	}
	private String host;
	private int port;
	private String password;
	private int timeout;
	private Map<String, Integer> pool = new HashMap<>();
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public Map<String, Integer> getPool() {
		return pool;
	}
	public void setPool(Map<String, Integer> pool) {
		this.pool = pool;
	}
	
	
	
}
