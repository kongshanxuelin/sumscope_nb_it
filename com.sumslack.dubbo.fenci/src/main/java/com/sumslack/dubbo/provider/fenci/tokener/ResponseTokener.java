package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.List;

import com.hankcs.hanlp.seg.common.Term;

public class ResponseTokener {
	private String yw;//原文
	private List<Term> termList;
	public String getYw() {
		return yw;
	}
	public void setYw(String yw) {
		this.yw = yw;
	}
	public List<Term> getTermList() {
		return termList;
	}
	public void setTermList(List<Term> termList) {
		this.termList = termList;
	}
	
	
}
