package com.clownvin.lumina.res;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import com.clownvin.lumina.Log;
import com.clownvin.lumina.graphics.RenderUtil;

public class Texture {
	protected final int id;

	public Texture(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void bind(int sampler, int frame) {
		if (sampler < 0 || sampler > 31) {
			Log.logE("Sampler out of range");
			new Exception().printStackTrace();
			return;
		}
		glActiveTexture(GL_TEXTURE0 + sampler);
		glBindTexture(GL_TEXTURE_2D, id);
		glBindBuffer(GL_ARRAY_BUFFER, RenderUtil.getStaticTexPointer());
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	}
}
