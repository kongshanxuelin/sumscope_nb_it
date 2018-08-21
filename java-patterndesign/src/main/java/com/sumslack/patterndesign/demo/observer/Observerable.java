package com.sumslack.patterndesign.demo.observer;

public interface Observerable {
	public void registerObserver(Observer... o);
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObserver();
}
