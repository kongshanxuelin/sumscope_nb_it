package com.sumslack.dubbo.api.fenci.bean;

import java.io.Serializable;


public class TermBean implements Serializable{
    private String word;
    private String nature;
    private int offset;
    private int frequency;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
    
}
