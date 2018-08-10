package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.List;
import java.util.regex.Pattern;

import com.hankcs.hanlp.seg.common.Term;

public class RateTokener extends AbsTokener {
	
    private static final Pattern PATTERN = Pattern.compile("((?:(((A|a){2,3}\\-?\\+?)))*)?");

	@Override
	public void analysis(List<Term> termList, RequestTokener req,TokenerChain chain) {
		reg(termList,PATTERN,req);
		chain.doAnalysis(termList, req, chain);
		formatTerm(termList);
	}

	@Override
	public String pos() {
		return "pj";
	}
	
	private void formatTerm(List<Term> termList) {
		if(termList!=null && termList.size()>0) {
			for(Term m : termList) {
				m.word = m.word.toUpperCase();
			}
		}
	}

}