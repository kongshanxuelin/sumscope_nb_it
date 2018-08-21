package com.sumslack.patterndesign.demo.chain;

public class Hander3 extends RequestHandler {
	@Override
	public void handleRequest(Request req) {
		System.out.println("处理Handler3");
		req.setText(req.getText().replaceAll("c", ""));
		if(getRequestHandler()!=null)
			getRequestHandler().handleRequest(req);
	}
}