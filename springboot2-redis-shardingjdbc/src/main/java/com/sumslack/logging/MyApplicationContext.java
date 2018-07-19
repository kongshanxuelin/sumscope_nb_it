package com.sumslack.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumslack.logging.store.service.DBService;
import com.sumslack.logging.store.service.RedisService;
import com.sumslack.logging.store.service.RedisService.Channel;

import redis.clients.jedis.JedisPubSub;

public class MyApplicationContext implements ApplicationListener<ApplicationReadyEvent>{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final long ID_INIT = System.currentTimeMillis();
	public static int INDEX = 0;
	
//	private static DataSource dataSource = null;
//	
//	public static DataSource getDatasource() {
//		if(dataSource == null) {
//			 File yamlFile = null;
//		 	 try {
//		 		 yamlFile = new File(MyApplicationContext.class.getResource("/application.yml").getPath());
//		 		 dataSource = YamlShardingDataSourceFactory.createDataSource(yamlFile);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return dataSource;
//	}
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		 //if(event.getApplicationContext().getParent()==null){   
			
			 ConfigurableApplicationContext ac = event.getApplicationContext();
			 RedisService redisService = ac.getBean(RedisService.class);
			 
			 DBService dbService = ac.getBean(DBService.class);
			 
			 
			 logger.info("##########application started.");
				redisService.initRedis().sub(RedisService.Channel.api_controller2db,new JedisPubSub() {

					@Override
					public boolean isSubscribed() {
						logger.info("###########redis isSubscribed："+Channel.api_controller2db);
						return super.isSubscribed();
					}

					@Override
					public void onMessage(String channel, String message) {
						//TODO: 存入日志统计系统或数据库 {"args":"{}","action":"com.sumslack.consumer.fx.controller.BondController","m":"detailSimple","ip:":"127.0.0.1"}
						logger.info("###########redis onMessage："+channel+" -> " + message);
						JSONObject json = JSON.parseObject(message);
						if(json!=null) {
							String args = json.getString("args");
							String action = json.getString("action");
							String m = json.getString("m");
							String ip = json.getString("ip");
							String agent = json.getString("agent");
							String project = json.getString("prj");
							dbService.addLogging(ID_INIT + ++INDEX, action, m, args, ip, agent, project);							
						}
						
						super.onMessage(channel, message);
					}

					@Override
					public void onSubscribe(String channel, int subscribedChannels) {
						logger.info("###########redis onSubscribe："+channel+" -> " + subscribedChannels);
						super.onSubscribe(channel, subscribedChannels);
					}

					@Override
					public void subscribe(String... channels) {
						logger.info("###########redis subscribe："+channels);
						super.subscribe(channels);
					}

					@Override
					public void unsubscribe() {
						logger.info("###########redis unsubscribe");
						super.unsubscribe();
					}

					@Override
					public void unsubscribe(String... channels) {
						logger.info("###########redis channels："+channels);
						super.unsubscribe(channels);
					}
					
				});
		 //}
		
	}
	
}