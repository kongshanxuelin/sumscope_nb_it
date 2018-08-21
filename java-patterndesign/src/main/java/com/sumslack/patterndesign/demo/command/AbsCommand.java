package com.sumslack.patterndesign.demo.command;

public abstract class AbsCommand {
	public abstract double exec(double value);
	public abstract double undo();
	protected double op(Cals cals,AbsCommand cmd,double v) {
		Object opObj;
		try {
			if(cmd instanceof AddCommandImpl) {
				return cals.add(v);
			}else if(cmd instanceof MinusCommandImpl) {
				return cals.minus(v);
			}else if(cmd instanceof MulCommandImpl) {
				return cals.mul(v);
			}else if(cmd instanceof DivCommandImpl) {
				return cals.div(v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
}
