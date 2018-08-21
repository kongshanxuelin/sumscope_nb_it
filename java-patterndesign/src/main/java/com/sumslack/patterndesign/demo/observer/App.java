package com.sumslack.patterndesign.demo.observer;

public class App {
	public static void main(String[] args) {
		ChatRoomServer server = new ChatRoomServer();
		Observer user1 = new User("user1");
		Observer user2 = new User("user2");
		Observer user3 = new User("user3");
		server.registerObserver(user1,user2,user3);
		server.sendMessage("hello");
		
		server.removeObserver(user2);
		server.sendMessage("hello2");
		
	}
}
