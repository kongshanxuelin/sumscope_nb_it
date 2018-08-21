package com.sumslack.patterndesign.demo.command;

public class AddCommandImpl extends AbsCommand{
	private Cals cals = null;
	private double v;

	public AddCommandImpl(Cals cals) {
		this.cals = cals;
	}

	@Override
	public double undo() {
		return cals.add(-this.v);
	}

	@Override
	public double exec(double v) {
		this.v = v;
		return op(cals,this,v);
	}

}
