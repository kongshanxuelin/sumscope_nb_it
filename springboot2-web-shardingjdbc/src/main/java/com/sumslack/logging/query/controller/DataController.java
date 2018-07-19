package com.sumslack.logging.query.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sumslack.logging.query.service.DBService;

@RestController
@RequestMapping(value="/")
public class DataController {
	@Autowired
	private DBService dBService;
	
	@RequestMapping("/")
	public String home() {
		return "hello world";
	}
	
	@RequestMapping(value="/data",method=RequestMethod.GET)
	public Map data(HttpServletRequest request){
		String project = request.getParameter("prj");
		int start = Integer.parseInt(request.getParameter("start").toString());
		int pagesize = 10;
		Map retMap = new HashMap();
		retMap.put("list", dBService.queryLogging(project,start,pagesize));
		return retMap;
	}
}
