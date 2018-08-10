package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.LinkedList;
import java.util.List;

import com.hankcs.hanlp.seg.common.Term;


public class TokenerChain{
	List<AbsTokener> filters = new LinkedList<AbsTokener>();
	int index=0;
	
	public TokenerChain addFilter(AbsTokener f) {
		filters.add(f);
        return this;
    }
	
	public void doAnalysis(List<Term> termList,RequestTokener req,TokenerChain fc) {
		if(index==filters.size()){
			return;
		}
		AbsTokener  f = filters.get(index);
		index++;
		f.analysis(termList, req,fc);
	}
}
