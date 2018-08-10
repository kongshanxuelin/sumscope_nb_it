package com.sumslack.dubbo.provider.fenci.tokener;

/**
 * @author Administrator
 *
 */
public class RequestTokener {
	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
	public RequestTokener(String str) {
		this.str = str;
	}
	
}
