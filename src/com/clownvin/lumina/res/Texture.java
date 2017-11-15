package com.clownvin.lumina.res;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import com.clownvin.lumina.Log;

public class Texture {
	private final int id, width, height;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind(int sampler) {
		if (sampler < 0 || sampler > 31) {
			Log.logE("Sampler out of range");
			new Exception().printStackTrace();
			return;
		}
		glActiveTexture(GL_TEXTURE0 + sampler);
		glBindTexture(GL_TEXTURE_2D, id);
	}
}
