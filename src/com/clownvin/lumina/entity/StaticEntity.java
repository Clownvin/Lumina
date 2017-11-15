package com.clownvin.lumina.entity;

import static org.lwjgl.opengl.GL15.*;

import com.clownvin.lumina.graphics.RenderUtil;

public class StaticEntity extends Entity {
	public StaticEntity() {
		this(0.0f, 0.0f);
	}
	
	public StaticEntity(float x, float y) {
		super(x, y);
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferData(GL_ARRAY_BUFFER, RenderUtil.createBuffer(getVertices()), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
