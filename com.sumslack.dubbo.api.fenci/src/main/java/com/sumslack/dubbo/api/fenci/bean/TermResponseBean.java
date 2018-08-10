package com.sumslack.dubbo.api.fenci.bean;

import java.io.Serializable;
import java.util.List;

public class TermResponseBean implements Serializable{
	private String yw;//т╜нд
	private List<TermBean> termList;
	public String getYw() {
		return yw;
	}
	public void setYw(String yw) {
		this.yw = yw;
	}
	public List<TermBean> getTermList() {
		return termList;
	}
	public void setTermList(List<TermBean> termList) {
		this.termList = termList;
	}
}
