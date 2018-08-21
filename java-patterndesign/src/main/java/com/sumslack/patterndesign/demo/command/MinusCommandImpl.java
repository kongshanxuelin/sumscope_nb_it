package com.sumslack.patterndesign.demo.command;

public class MinusCommandImpl extends AbsCommand{
	private Cals cals = null;
	private double v;

	public MinusCommandImpl(Cals cals) {
		this.cals = cals;
	}

	@Override
	public double undo() {
		return cals.minus(-this.v);
	}

	@Override
	public double exec(double v) {
		this.v = v;
		return op(cals,this,v);
	}

}
