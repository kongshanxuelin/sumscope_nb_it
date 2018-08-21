package com.sumslack.patterndesign.demo.strategy;

public class MacroForecaster {
	public MacroForecaster(ForecastStrategy strategy) {
		this.strategy = strategy;
	}
	public MacroForecaster() {}
	private ForecastStrategy strategy;

	public ForecastStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(ForecastStrategy strategy) {
		this.strategy = strategy;
	}
	
	public double run(double[] pointers) {
		if(strategy!=null)
			return strategy.execute(pointers);
		return 0d;
	}
	
	public double run(ForecastStrategy strategy,double[] pointers) {
		this.strategy = strategy;
	    return run(pointers);
	}
	
	
	
	public static void main(String[] args) {
		double[] dd = {1,2,3.4,45,23,3};
		MacroForecaster ff = new MacroForecaster();
		double d = ff.run(new ForecastAvgStragety(),dd);
		double d2 = ff.run(new ForecastMaxStragety(),dd);
		System.out.println("forecast avg:"+d+",max:"+d2);
	}
}
