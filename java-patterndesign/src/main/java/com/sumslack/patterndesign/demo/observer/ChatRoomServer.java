package com.sumslack.patterndesign.demo.observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatRoomServer implements Observerable{
	private List<Observer> list;
	private String message;
	public ChatRoomServer() {
		list = new ArrayList<Observer>();
	}
	@Override
	public void registerObserver(Observer o) {
		list.add(o);
	}
	@Override
	public void registerObserver(Observer... o) {
		Collections.addAll(list, o);
	}

	@Override
	public void removeObserver(Observer o) {
		if(!list.isEmpty())
			list.remove(o);
	}

	@Override
	public void notifyObserver() {
		for(int i = 0; i < list.size(); i++) {
            Observer oserver = list.get(i);
            oserver.update(message);
        }
	}
	
	public void sendMessage(String s) {
        this.message = s;
        System.out.println("发送聊天室消息： " + s);
        notifyObserver();
    }

}
