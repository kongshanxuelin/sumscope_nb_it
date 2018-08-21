package com.sumslack.patterndesign.demo.strategy;
@FunctionalInterface
public interface ForecastStrategy {
	public double execute(double[] pointers);
}
