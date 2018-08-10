package com.sumslack.dubbo.provider.fenci.tokener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import cn.hutool.core.util.StrUtil;


public abstract class AbsTokener {
	
	/**
	 * 根据正则匹配识别出实现类的特殊词性，并返回截取后的字符串
	 * @param termList
	 * @param str：待识别的原文
	 * @return
	 */
	public abstract void analysis(List<Term> termList,RequestTokener request,TokenerChain chain);
	/**
	 * 设置词性
	 * @return
	 */
	public abstract String pos();
	
	
    protected void reg(List<Term> resultList,Pattern p,RequestTokener req)
    {
    	Set<Term> termList = new HashSet<Term>();
        Matcher matcher = p.matcher(req.getStr());
        int begin = 0;
        int end;
        String ystr = req.getStr();
        String ret = "";
        while (matcher.find())
        {
            end = matcher.start();
            String _str = matcher.group();
            if(!StrUtil.isEmpty(_str)) {
            	resultList.add(new Term(_str, Nature.fromString(pos())));
            }
            begin = matcher.end();
            if(StrUtil.isEmpty(_str)) {
            	ret += ystr.substring(begin,begin+1<=ystr.length()?begin+1:ystr.length());
            }
        }
        req.setStr(ret);
    }
    

    
    protected void reg(List<Term> resultList,Pattern p,RequestTokener req,float maxNum)
    {
    	Set<Term> termList = new HashSet<Term>();
        Matcher matcher = p.matcher(req.getStr());
        int begin = 0;
        int end;
        String ystr = req.getStr();
        String ret = "";
        while (matcher.find())
        {
            end = matcher.start();
            String _str = matcher.group();
            _str = _str.trim();
            if(!StrUtil.isEmpty(_str)) {
            	float _f = getFloat(_str);
            	if(_f>0 && _f<maxNum) {
            		resultList.add(new Term(_str, Nature.fromString(pos())));
            	}
            }
            begin = matcher.end();
            if(StrUtil.isEmpty(_str)) {
            	ret += ystr.substring(begin,begin+1<=ystr.length()?begin+1:ystr.length());
            }
        }
        req.setStr(ret);
    }
    
    private float getFloat(String str) {
    	try {
    		if(str.endsWith("%")) str = str.substring(0, str.length()-1);
    		str = str.replaceAll(",", "");
    		return Float.parseFloat(str);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	return -1;
    }
    
    
}
