package com.sumslack.dubbo.provider.fenci.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.sumslack.dubbo.api.fenci.TokenerService;
import com.sumslack.dubbo.api.fenci.bean.TermBean;

@Service(version = "1.0.0", timeout = 10000)
public class TokenerServiceImpl implements TokenerService {
	private Segment segment = HanLP.newSegment();
	@Override
	public List<TermBean> standard(String text) {
		List<Term> terms = segment.seg(text);
		List<TermBean> termList = new ArrayList();
		toBean(terms,termList);
		return termList;
	}
	@Override
	public boolean addDict(String word) {
		try {
			CustomDictionary.add(word);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addDict(String word, String nature) {
		try {
			CustomDictionary.add(word,nature);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	@Override
	public List<String> pickerKeyword(String text, int num) {
		return HanLP.extractKeyword(text, num);
	}
	@Override
	public List<String> pickerDigest(String text,int num){
		return HanLP.extractSummary(text, num);
	}
	@Override
	public List<String> pickerPhrase(String text,int num){
		return HanLP.extractPhrase(text, num);
	}
	
	public boolean removeDict(String word) {
		try {
			CustomDictionary.remove(word);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean config(boolean enableNameRecognize, boolean enableTranslatedNameRecognize,
			boolean enableJapaneseNameRecognize, boolean enablePlaceRecognize, boolean enableOrganizationRecognize) {
		segment.enableNameRecognize(enableNameRecognize);
		segment.enableTranslatedNameRecognize(enableTranslatedNameRecognize);
		segment.enableJapaneseNameRecognize(enableJapaneseNameRecognize);
		segment.enablePlaceRecognize(enablePlaceRecognize);
		segment.enableOrganizationRecognize(enableOrganizationRecognize);
		return true;
	}
	
	private void toBean(List<Term> source, List<TermBean> target) {
		for (Term term : source) {
			TermBean tb = new TermBean();
			if (term.nature != null)
				tb.setNature(term.nature.name());
			if (term.word != null)
				tb.setWord(term.word);
			tb.setFrequency(term.getFrequency());
			tb.setOffset(term.offset);
			target.add(tb);
		}
	}



	
}
