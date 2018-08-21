package com.sumslack.patterndesign.demo.command;

public class Cals {
	private double num = 0;
	public double add(double v) {num+=v;return num;}
	public double minus(double v) {num-=v;return num;}
	public double mul(double v) {num*=v;return num;}
	public double div(double v) {num/=v;return num;}	
}
