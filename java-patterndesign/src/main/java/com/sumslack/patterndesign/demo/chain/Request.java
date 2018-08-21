package com.sumslack.patterndesign.demo.chain;

public class Request {
	private String requestType;
	private String text;
	private boolean handled;

	public Request(final String requestType, final String requestDescription) {
		this.requestType = requestType;
		this.text = requestDescription;
	}

	public void markHandled() {
		this.handled = true;
	}

	public boolean isHandled() {
		return this.handled;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
