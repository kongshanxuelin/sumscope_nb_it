package com.sumslack.patterndesign.demo.command;

public class MulCommandImpl extends AbsCommand{
	private Cals cals = null;
	private double v;

	public MulCommandImpl(Cals cals) {
		this.cals = cals;
	}

	@Override
	public double undo() {
		return cals.mul(1/this.v);
	}

	@Override
	public double exec(double v) {
		this.v = v;
		return op(cals,this,v);
	}

}
