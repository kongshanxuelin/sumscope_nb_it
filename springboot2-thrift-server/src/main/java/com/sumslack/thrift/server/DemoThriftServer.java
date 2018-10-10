package com.sumslack.thrift.server;

import com.sumscope.thrift.annotation.ThriftService;

@ThriftService("demo")
public class DemoThriftServer {
    public String hello(String name){
        return "hello thrift," + name;
    }
}
