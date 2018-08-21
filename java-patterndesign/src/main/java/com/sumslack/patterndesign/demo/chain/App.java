package com.sumslack.patterndesign.demo.chain;

public class App {
	public static void main(String[] args) {
		RequestHandler handler1 = new Hander1();
		RequestHandler handler2 = new Hander2();
		RequestHandler handler3 = new Hander3();
		Request req = new Request("test","abababcdef");
		HandlerChain chain = new HandlerChain(handler1,handler2,handler3);
		chain.run(req);
		System.out.println(""+req.getText());
	}
}
