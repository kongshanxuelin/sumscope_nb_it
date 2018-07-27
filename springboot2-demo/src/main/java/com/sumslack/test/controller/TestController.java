package com.sumslack.test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v2/test")
public class TestController {
	@GetMapping(value="hello",produces= {MediaType.APPLICATION_JSON_UTF8_VALUE,MediaType.APPLICATION_XML_VALUE})
	public @ResponseBody Map testHello(HttpServletResponse response) {
		Map retMap = new HashMap();
		retMap.put("ret", true);
		retMap.put("msg", "hello world");
		return retMap;
	}
	
	
}
