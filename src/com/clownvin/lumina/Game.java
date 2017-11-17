package com.clownvin.lumina;

public abstract class Game {
	public final Object updateSyncLock = new Object();

	public abstract String getTitle();

	public abstract void setup();

	public abstract void update();
}
