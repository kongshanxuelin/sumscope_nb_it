package com.sumslack.patterndesign.demo.command;

public class DivCommandImpl extends AbsCommand{
	private Cals cals = null;
	private double v;

	public DivCommandImpl(Cals cals) {
		this.cals = cals;
	}

	@Override
	public double undo() {
		return cals.div(1/this.v);
	}

	@Override
	public double exec(double v) {
		this.v = v;
		return op(cals,this,v);
	}

}
