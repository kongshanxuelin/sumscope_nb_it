package com.sumslack.patterndesign.demo.chain;

public class Hander2 extends RequestHandler {
	@Override
	public void handleRequest(Request req) {
		System.out.println("处理Handler2");
		req.setText(req.getText().replaceAll("b", ""));
		if(getRequestHandler()!=null)
			getRequestHandler().handleRequest(req);
	}
}