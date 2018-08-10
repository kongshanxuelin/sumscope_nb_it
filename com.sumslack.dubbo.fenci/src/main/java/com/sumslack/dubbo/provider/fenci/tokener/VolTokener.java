package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.List;
import java.util.regex.Pattern;

import com.hankcs.hanlp.seg.common.Term;

public class VolTokener extends AbsTokener {
	
    private static final Pattern PATTERN = Pattern.compile("((?:([1-9]+[0-9|,]*|0)(\\.[\\d]+)?(kw|e|E|千万|万|千|亿)元?)*)?");

	@Override
	public void analysis(List<Term> termList, RequestTokener req,TokenerChain chain) {
		reg(termList,PATTERN,req);
		chain.doAnalysis(termList, req, chain);
	}

	@Override
	public String pos() {
		return "vol";
	}

}