package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import cn.hutool.core.util.StrUtil;

public class BankTokerner extends AbsTokener{
	public static List<Bank> banks = new ArrayList();
	@Override
	public void analysis(List<Term> termList, RequestTokener req,TokenerChain chain) {
		containBank(termList, req);
		chain.doAnalysis(termList, req,chain);
	}

	@Override
	public String pos() {
		return "bank";
	}
	
	
    protected void containBank(List<Term> resultList,RequestTokener req)
    {
    	Set<Term> termList = new HashSet<Term>();
    	//优先全匹配
    	List<Bank> _bankList = banks.stream().filter(bank -> bankFilterStr(req.getStr()).contains(bank.getYname())).sorted((b1,b2) -> b1.getName().length()>b2.getName().length()?-1:1).limit(1).collect(Collectors.toList());
        if(_bankList!=null && _bankList.size()>0) {
        	for(Bank b : _bankList) {
            	resultList.add(new Term(b.getYname(), Nature.fromString(pos())));
        	}
        }else {
        	//寻找匹配字数最多的
	        _bankList = banks.stream().filter(bank -> bankFilterStr(req.getStr()).contains(bank.getName())).sorted((b1,b2) -> lenBank(b1.getName(),bankFilterStr(req.getStr())) > lenBank(b2.getName(),bankFilterStr(req.getStr())) ?1:-1).limit(1).collect(Collectors.toList());
	        if(_bankList!=null && _bankList.size()>0) {
	        	for(Bank b : _bankList) {
	            	resultList.add(new Term(b.getYname(), Nature.fromString(pos())));
	        	}
	        }
        }
    }
    
    private String bankFilterStr(String str) {
    	str = StrUtil.replace(str, "市区", "");
    	return str;
    }
    
    private int lenBank(String s1,String s2) {
    	String max = "";
		for (int i = 0; i < s1.length(); i++) {
			for (int j = i; j < s1.length(); j++) {
				String sub = s1.substring(i, j);
				if ((s2.indexOf(sub) != -1) && sub.length() > max.length()) {
					max = sub;
				}
			}
		}
		return max.length();
    }

}
