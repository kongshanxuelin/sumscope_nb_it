package com.sumslack.dubbo.provider.fenci.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.sumslack.dubbo.api.demo.DemoService;
@Service(version = "1.0.0", timeout = 10000)
public class DemoServiceImpl implements DemoService {
	@Override
	public String hello(String name) {
		return "hello,"+name;
	}
}
