package com.clownvin.lumina.res;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import com.clownvin.lumina.graphics.RenderUtil;

public class AnimatedTexture extends Texture {

	private final int spriteWidth, spriteHeight, texWidth, texHeight, texPointer;
	private int lastFrameIndex = 0;

	public AnimatedTexture(int id, String name, int spriteWidth, int spriteHeight, int texWidth, int texHeight) {
		super(id, name);
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		texPointer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texPointer);
		glBufferData(GL_ARRAY_BUFFER, RenderUtil.createBuffer(getTexVertices(0)), GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void bind(int sampler, int frameIndex) {
		if (sampler < 0 || sampler > 31) {
			System.err.println("Sampler out of range");
			new Exception().printStackTrace();
			return;
		}
		if (frameIndex != lastFrameIndex) {
			updateTexVertices(frameIndex);
			lastFrameIndex = frameIndex;
		}
		glActiveTexture(GL_TEXTURE0 + sampler);
		glBindTexture(GL_TEXTURE_2D, id);
		glBindBuffer(GL_ARRAY_BUFFER, texPointer);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	}

	public float[] getTexVertices(int frame) {
		float tW = (float) spriteWidth / texWidth;
		float tH = (float) spriteHeight / texHeight;
		int spritesPerRow = texWidth / spriteWidth;
		float tX = (frame % spritesPerRow) * tW;
		float tY = (frame / spritesPerRow + 1) * tH;
		return new float[] { tX, tY, tX + tW, tY, tX + tW, tY + tH, tX, tY + tH };
	}

	public void updateTexVertices(int frame) {
		glBindBuffer(GL_ARRAY_BUFFER, texPointer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, RenderUtil.createBuffer(getTexVertices(frame)));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
