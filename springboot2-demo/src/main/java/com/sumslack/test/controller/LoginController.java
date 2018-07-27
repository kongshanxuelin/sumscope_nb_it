package com.sumslack.test.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sumslack.test.request.User;

@RestController()
@RequestMapping("/api/v2")
public class LoginController {

	public static LoadingCache<String, Optional<User>> userCache = CacheBuilder.newBuilder()
        .maximumSize(10000)
        .expireAfterWrite(24, TimeUnit.HOURS) //这个方法是根据某个键值对被创建或值被替换后多少时间移除
        .build(
        	//当获取的缓存值不存在或已过期时	
            new CacheLoader<String, Optional<User>>() {
            	// 当大量线程用相同的key获取缓存值时，只会有一个线程进入load方法，而其他线程则等待，直到缓存值被生成。这样也就避免了缓存穿透的危险
            	@Override
                public Optional<User> load(String key) {
            		//如果缓存中，没有跨域去数据库查找
            		return Optional.fromNullable(null);
                }
        });
	
	@GetMapping(value="login",produces= {MediaType.APPLICATION_JSON_UTF8_VALUE,MediaType.APPLICATION_XML_VALUE})
	public @ResponseBody Map login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		Map retMap = new HashMap();
		retMap.put("ret", false);
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(pwd)) {
			retMap.put("msg", "密码错误!");
		}
		if(pwd.equals("123456")) {
			User user = new User();
			user.setUsername(username);
			user.setNick("nick"+username);
			user.setPassword(pwd);
			String token = new Date().getTime()+"";
			userCache.put(token,Optional.fromNullable(user));
			retMap.put("token", token);
			retMap.put("user", user);
			retMap.put("msg", "登录成功！");
		}
		
		return retMap;
	}
	
	@RequestMapping(value = "reg", method = RequestMethod.GET)
	public @ResponseBody Map reg(@Valid User user) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		//TODO:注册到数据库代码，略
		retMap.put("user", user);
		return retMap;
	}
	
	@RequestMapping(value = "change-password", method = RequestMethod.GET)
	public @ResponseBody Map changePassword(
			@RequestParam(value="pwd-old",required=true) String pwd1,
			@RequestParam(value="pwd-new",required=true) String pwd2) {
		Map retMap = new HashMap();
		retMap.put("ret", false);
		if(!StringUtils.isEmpty(pwd1) && !StringUtils.isEmpty(pwd2) && pwd1.equals(pwd2)) {
			retMap.put("ret", true);
		}
		return retMap;
	}
	
	@RequestMapping(value = "user/{u}", method = RequestMethod.GET)
	public @ResponseBody Map getUser(@PathVariable(value="u",required=true) String username) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		retMap.put("u", username);
		return retMap;
	}
}
