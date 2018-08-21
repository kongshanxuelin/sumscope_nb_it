package com.sumslack.patterndesign.demo.chain;

public abstract class RequestHandler {
	private RequestHandler next;
	public abstract void handleRequest(Request req);

	public RequestHandler getRequestHandler() {
		return next;
	}
	public RequestHandler setRequestHandler(RequestHandler next) {
		this.next = next;
		return this;
	}
}
