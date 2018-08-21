package com.sumslack.patterndesign.demo.chain;

public class Hander1 extends RequestHandler {
	@Override
	public void handleRequest(Request req) {
		System.out.println("处理Handler1");
		req.setText(req.getText().replaceAll("a", ""));
		if(getRequestHandler()!=null)
			getRequestHandler().handleRequest(req);
	}

}