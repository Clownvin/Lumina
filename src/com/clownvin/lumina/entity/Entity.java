package com.clownvin.lumina.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import com.clownvin.lumina.graphics.RenderUtil;
import com.clownvin.lumina.res.ResourceManager;

public class Entity {
	
	protected float x, y;
	protected int vertexPointer;
	
	public Entity() {
		this(0.0f, 0.0f);
	}
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		vertexPointer = glGenBuffers();
	}
	
	protected float[] getVertices() {
		return new float[] {
				-0.5f + x, 0.5f + y,
				0.5f + x, 0.5f + y,
				0.5f + x, -0.5f + y,
				-0.5f + x, -0.5f + y
		};
	}
	
	public String getTexture() {
		return "test1";
	}
	
	public void render() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		ResourceManager.getTexture(getTexture()).bind(0);

		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, RenderUtil.getTexPointer());
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, RenderUtil.getIndicesPointer());
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
}
