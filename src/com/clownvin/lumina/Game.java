package com.clownvin.lumina;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public abstract class Game implements Runnable {
	public final Object updateSyncLock = new Object();
	
	public void update() {
		synchronized (updateSyncLock) {
			updateSyncLock.notifyAll();
		}
		//TODO Remove when multithreaded
		onUpdate();
	}
	
	public void waitForUpdate() throws InterruptedException {
		synchronized (updateSyncLock) {
			updateSyncLock.wait();
		}
	}
	
	public void start() {
		//TODO Find a way to have multiple threads access opengl synchronized
		//Thread thread = new Thread(this);
		//thread.setName(getTitle()+"_thread");
		//thread.start();
	}
	
	@Override
	public void run() {
		while (LuminaEngine.running()) {
			try {
				waitForUpdate();
			} catch (InterruptedException e) {
				Log.logD("Interrupted while waiting for update...");
			}
			onUpdate();
		}
	}
	
	public abstract String getShader();
	
	public abstract int getSpriteScale();
	
	public abstract void setup();
	
	public abstract void onUpdate();
	
	public abstract String getTitle();
	
	public abstract GLFWKeyCallback getKeyCallback();
	
	public abstract GLFWCursorPosCallback getCursorPosCallback();
	
	public abstract GLFWMouseButtonCallback getMouseButtonCallback();
}
