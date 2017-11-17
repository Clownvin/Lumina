package com.clownvin.lumina.res;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import com.clownvin.lumina.graphics.RenderUtil;

public class Texture {
	protected final int id;
	protected final String name;
	protected int usageCount = 0;
	protected boolean released = false;

	public Texture(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public void bind(int sampler, int frame) {
		check();
		if (sampler < 0 || sampler > 31) {
			System.err.println("Sampler out of range");
			new Exception().printStackTrace();
			return;
		}
		glActiveTexture(GL_TEXTURE0 + sampler);
		glBindTexture(GL_TEXTURE_2D, id);
		glBindBuffer(GL_ARRAY_BUFFER, RenderUtil.getStaticTexPointer());
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	}

	protected void check() {
		if (released)
			throw new IllegalStateException("Texture has already been released!");
	}

	public int getId() {
		check();
		return id;
	}

	public String getName() {
		check();
		return name;
	}

	public void stopUsing() {
		check();
		usageCount--;
	}

	public void use() {
		check();
		usageCount++;
	}
}
