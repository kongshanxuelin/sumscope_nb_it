package com.sumslack.patterndesign.demo.decorator;

public class IndexFormatterForFinIndexDbGridDecorator extends AbsIndexFormatter{
	private AbsIndexFormatter formatter;
	public IndexFormatterForFinIndexDbGridDecorator(AbsIndexFormatter formmater) {
		this.formatter = formmater;
	}
	@Override
	public String analysis(String content) {
		System.out.println("给content加个包裹");
		String ss = formatter.analysis(content);
		return "wrapper:"+ss;
	}
	
	public static void main(String[] args) {
		AbsIndexFormatter formatter = new IndexFormatterForFinIndex();
		IndexFormatterForFinIndexDbGridDecorator decorator = new IndexFormatterForFinIndexDbGridDecorator(formatter);
		System.out.println(decorator.analysis("test"));
	}
}
