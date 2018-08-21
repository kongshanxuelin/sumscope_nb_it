package com.sumslack.patterndesign.demo.command;

public class App {
	private AbsCommand command;
	
	public void setCommand(AbsCommand command) {  
        this.command = command;  
    }  
	public void compute(AbsCommand cmd,double value) {  
        double i = command.exec(value);  
        System.out.println("执行运算，运算结果为：" + i);  
    }  
	public void undo() {  
		double i = command.undo();  
        System.out.println("执行撤销，运算结果为：" + i);  
    }
	public static void main(String[] args) {
		Cals cal = new Cals();
		App app = new App();
		AbsCommand add = new AddCommandImpl(cal);
		AbsCommand minus = new MinusCommandImpl(cal);
		AbsCommand mul = new MulCommandImpl(cal);
		AbsCommand div = new DivCommandImpl(cal);
		app.setCommand(add);
		app.compute(add,1);
		app.compute(add,2);
		app.setCommand(mul);
		app.compute(mul,3);
		app.undo();
	}
}
