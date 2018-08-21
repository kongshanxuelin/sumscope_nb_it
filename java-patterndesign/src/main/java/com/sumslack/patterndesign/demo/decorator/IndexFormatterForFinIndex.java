package com.sumslack.patterndesign.demo.decorator;

public class IndexFormatterForFinIndex extends AbsIndexFormatter{
	@Override
	public String analysis(String content) {
		System.out.println("给finindex项目用的数据结构");
		return content;
	}
}
