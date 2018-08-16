package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.List;
import java.util.regex.Pattern;

import com.hankcs.hanlp.seg.common.Term;

public class PriceShiborTokener extends AbsTokener {
	
    private static final Pattern PATTERN = Pattern.compile("((?:(3[mM][\\+|\\-]\\d*))*)?");

	@Override
	public void analysis(List<Term> termList, RequestTokener req,TokenerChain chain) {
		//浙商银行  AAA  6M  3m+9  今日  满
		reg(termList,PATTERN,req);
		chain.doAnalysis(termList, req, chain);
	}

	@Override
	public String pos() {
		return "shibor";
	}

}
