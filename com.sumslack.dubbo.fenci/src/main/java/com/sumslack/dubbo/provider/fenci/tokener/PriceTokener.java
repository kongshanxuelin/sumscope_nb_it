package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.List;
import java.util.regex.Pattern;

import com.hankcs.hanlp.seg.common.Term;

public class PriceTokener  extends AbsTokener {
	
    private static final Pattern PATTERN = Pattern.compile("((?:([1-9][\\d|,]*\\.[\\d|,]*)|(0\\.\\d*[1-9])%?)*)?");

	@Override
	public void analysis(List<Term> termList, RequestTokener req,TokenerChain chain) {
		//价格的收益率一般低于10.0%
		reg(termList,PATTERN,req,10);
		chain.doAnalysis(termList, req, chain);
	}

	@Override
	public String pos() {
		return "pri";
	}

}
