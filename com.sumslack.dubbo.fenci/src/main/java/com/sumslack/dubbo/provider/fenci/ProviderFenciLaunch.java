package com.sumslack.dubbo.provider.fenci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.utility.LexiconUtility;

@SpringBootApplication
public class ProviderFenciLaunch {
	static {
		HanLP.Config.enableDebug();
		HanLP.Config.Normalization = true;
		Nature natureTerm = Nature.create("term");//期限
		LexiconUtility.setAttribute("term", natureTerm);
		Nature natureVol = Nature.create("vol");//数量
		LexiconUtility.setAttribute("vol", natureVol);
		Nature naturePri = Nature.create("pri");//价格
		LexiconUtility.setAttribute("pri", naturePri);
		Nature naturePj = Nature.create("pj");//评级
		LexiconUtility.setAttribute("pj", naturePj);
		
		LexiconUtility.setAttribute("AAA+", naturePj);
		LexiconUtility.setAttribute("AAA", naturePj);
		LexiconUtility.setAttribute("AAA-", naturePj);
		LexiconUtility.setAttribute("AA+", naturePj);
		LexiconUtility.setAttribute("AA", naturePj);
		LexiconUtility.setAttribute("AA-", naturePj);
		
		Nature natureBank = Nature.create("bank");//机构
		LexiconUtility.setAttribute("bank", natureBank);
	}
    public static void main(String[] args) {
        SpringApplication.run(ProviderFenciLaunch.class,args);        
    }
}
