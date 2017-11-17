package com.clownvin.lumina.res;

import com.clownvin.lumina.LuminaEngine;

public class Animation {

	private int timeBetweenFrames;
	private int[] frames;
	private int frameCounter = 0;
	private int timeElapsed = 0;
	private long startTime = 0L;

	public Animation(int timeBetweenFrames, int[] frames) {
		this.timeBetweenFrames = timeBetweenFrames;
		this.frames = frames;
	}

	public int animate() {
		timeElapsed += LuminaEngine.gameTimeMillis() - (startTime + timeElapsed);
		if (timeElapsed >= timeBetweenFrames) {
			timeElapsed %= timeBetweenFrames;
			startTime = LuminaEngine.gameTimeMillis();
			frameCounter++;
			frameCounter %= frames.length;
		}
		return frames[frameCounter];
	}

	public int getFrame() {
		return frames[frameCounter];
	}

	public void reset() {
		frameCounter = 0;
		startTime = LuminaEngine.gameTimeMillis();
	}
}
