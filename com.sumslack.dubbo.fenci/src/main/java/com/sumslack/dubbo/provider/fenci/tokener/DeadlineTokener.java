package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.List;
import java.util.regex.Pattern;

import com.hankcs.hanlp.seg.common.Term;

/**
 * 识别期限
 * @author Administrator
 *
 */
public class DeadlineTokener extends AbsTokener {
	
    private static final Pattern PATTERN = Pattern.compile("((?:[\\d|零|一|二|三|四|五|六|七|八|九|十|百|千|万|亿|\\.]+\\-?[\\d|零|一|二|三|四|五|六|七|八|九|十|百|千|万|亿|\\.]*(d|D|m|M|y|Y|个?月|天|日|年))*)?");

	@Override
	public void analysis(List<Term> termList, RequestTokener req,TokenerChain chain) {
		reg(termList,PATTERN,req);
		chain.doAnalysis(termList, req,chain);
	}

	@Override
	public String pos() {
		return "term";
	}

}
