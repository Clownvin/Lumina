package com.clownvin.lumina.task;

public abstract class Task implements Runnable {
	protected volatile boolean finished = false;
	
	public void finish() {
		finished = true;
		synchronized (this) {
			this.notifyAll();
		}
	}
}
