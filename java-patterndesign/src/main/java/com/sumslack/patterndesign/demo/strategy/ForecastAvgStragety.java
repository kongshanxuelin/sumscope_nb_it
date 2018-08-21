package com.sumslack.patterndesign.demo.strategy;

public class ForecastAvgStragety implements ForecastStrategy{

	@Override
	public double execute(double[] pointers) {
		double sum = 0;
		for(double d : pointers) {
			sum += d;
		}
		return sum/pointers.length;
	}

}
