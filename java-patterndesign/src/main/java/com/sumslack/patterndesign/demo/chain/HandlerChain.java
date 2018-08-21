package com.sumslack.patterndesign.demo.chain;

public class HandlerChain {
	private RequestHandler[] handlers;
	public HandlerChain(RequestHandler ...handlers) {
		this.handlers = handlers;
	}
	
	public void run(Request req) {
		for(int i=0;i<handlers.length;i++) {
			if(i<handlers.length-1)
				handlers[i].setRequestHandler(handlers[i+1]);
		}
		if(handlers.length>=1) {
			handlers[0].handleRequest(req);
		}
	}
}
