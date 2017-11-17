package com.clownvin.lumina;

public abstract class Game {
	public final Object updateSyncLock = new Object();
	
	public abstract void setup();

	public abstract void update();

	public abstract String getTitle();
}
