package com.sumslack.test.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sumslack.test.service.IUserService;
import com.sumslack.test.service.RedisService;

@RestController()
@RequestMapping("/api/v2/redis")
public class RedisController {
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private IUserService userService;
	
	@GetMapping(value="publish/{channel}")
	public @ResponseBody Map publish(@PathVariable("channel") String channel) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		Map msg = new HashMap();
		msg.put("msg", "hello中文测~！#:"+new Date());
		redisService.publish(channel, msg);
		return retMap;
	}
	
	@GetMapping(value="dbcache/user")
	public @ResponseBody Map dbCache() {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		retMap.put("user",userService.getUserById("1"));
		return retMap;
	}
}
