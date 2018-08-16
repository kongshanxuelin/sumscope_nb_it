package com.sumslack.dubbo.provider.fenci.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.hankcs.hanlp.seg.common.Term;
import com.sumslack.dubbo.api.fenci.NCDService;
import com.sumslack.dubbo.api.fenci.bean.TermBean;
import com.sumslack.dubbo.api.fenci.bean.TermResponseBean;
import com.sumslack.dubbo.provider.fenci.tokener.BankTokerner;
import com.sumslack.dubbo.provider.fenci.tokener.DeadlineTokener;
import com.sumslack.dubbo.provider.fenci.tokener.PriceShiborTokener;
import com.sumslack.dubbo.provider.fenci.tokener.PriceTokener;
import com.sumslack.dubbo.provider.fenci.tokener.RateTokener;
import com.sumslack.dubbo.provider.fenci.tokener.RequestTokener;
import com.sumslack.dubbo.provider.fenci.tokener.ResponseTokener;
import com.sumslack.dubbo.provider.fenci.tokener.TokenerChain;
import com.sumslack.dubbo.provider.fenci.tokener.VolTokener;

@Service(version = "1.0.0", timeout = 10000)
public class NCDServiceImpl implements NCDService{

	public Map analysis(String text) {
		Map retMap = new HashMap();
		List<ResponseTokener> resultTerm = new ArrayList();
		String[] tlines  = text.split("\n");
		long ss = System.currentTimeMillis();
		if(tlines!=null && tlines.length>0) {
			for(String _line : tlines) {
				if(_line.trim().equals("")) continue;
				TokenerChain chain = new TokenerChain();
				
				chain
				.addFilter(new BankTokerner())
				.addFilter(new PriceShiborTokener())
				.addFilter(new DeadlineTokener())
				.addFilter(new VolTokener())
				.addFilter(new PriceTokener())
				.addFilter(new RateTokener());
				
				List<Term> _resultTerm = new ArrayList();
				RequestTokener req = new RequestTokener(_line);
				chain.doAnalysis(_resultTerm, req, chain);
				ResponseTokener rt = new ResponseTokener();
				rt.setYw(_line);
				rt.setTermList(_resultTerm);
				resultTerm.add(rt);
			}
		}
		ss = System.currentTimeMillis() - ss;
		List<TermResponseBean> list = new ArrayList();
		beanConvert(resultTerm,list);
		retMap.put("ci", list);
		retMap.put("time", ss);
		return retMap;
	}
	
	private void beanConvert(List<ResponseTokener> resultTerm,List<TermResponseBean> target) {
		
		for(ResponseTokener toker : resultTerm) {
			TermResponseBean resp = new TermResponseBean();
			resp.setYw(toker.getYw());
			List<Term> terms = toker.getTermList();
			List<TermBean> termBeanList = new ArrayList();
			for(Term term : terms) {
				TermBean tb = new TermBean();
				if(term.nature!=null)
					tb.setNature(term.nature.name());
				if(term.word!=null)
					tb.setWord(term.word);
				tb.setFrequency(term.getFrequency());
				tb.setOffset(term.offset);
				termBeanList.add(tb);
			}
			resp.setTermList(termBeanList);
			target.add(resp);
		}
	}

}
