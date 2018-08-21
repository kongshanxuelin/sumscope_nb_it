package com.sumslack.patterndesign.demo.strategy;

public class ForecastMaxStragety implements ForecastStrategy{

	@Override
	public double execute(double[] pointers) {
		double d = Double.MIN_VALUE;
		for(double dd : pointers) {
			if(dd>d) {
				d = dd;
			}
		}
		return d;
	}

}
