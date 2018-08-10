package com.sumslack.dubbo.provider.fenci.tokener;

public class Bank {
	private String code;
	private String name;
	private String yname;//原始名称
	public String getYname() {
		return yname;
	}
	public void setYname(String yname) {
		this.yname = yname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Bank(String c,String n,String yname) {
		this.code = c;
		this.name = n;
		this.yname = yname;
	}
	
}
