package com.sumslack.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sumslack.test.entity.User;
import com.sumslack.test.service.IUserService;

@RestController()
@RequestMapping("/api/v2/db")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@GetMapping(value="user/{id}")
	public @ResponseBody Map getUser(@PathVariable("id") String id) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		User user = new User();
		retMap.put("user", userService.selectOne(new EntityWrapper<User>().eq("uid", id)));
		return retMap;
	}
	
	@GetMapping(value="user/save")
	public @ResponseBody Map addUser(User user) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		user.insertOrUpdate();
		return retMap;
	}
	
	@GetMapping(value="user/delete/{id}")
	public @ResponseBody Map deleteUser(@PathVariable("id") String id) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		User user = new User();
		user.setUid(id);
		retMap.put("user", user.deleteById() );
		return retMap;
	}
	
	
}
