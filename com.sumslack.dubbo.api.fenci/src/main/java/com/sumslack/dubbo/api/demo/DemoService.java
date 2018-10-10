package com.sumslack.dubbo.api.demo;

import com.sumslack.common.anno.IDubboService;
/*
 * 如果不想写对外API的Controller服务，必须继承IDubboService接口
 */
public interface DemoService extends IDubboService{
	public String hello(String name);
}
