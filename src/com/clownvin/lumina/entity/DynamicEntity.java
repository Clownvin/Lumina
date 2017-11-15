package com.clownvin.lumina.entity;

import static org.lwjgl.opengl.GL15.*;

import org.joml.Vector2f;

import com.clownvin.lumina.graphics.RenderUtil;

public class DynamicEntity extends Entity {
	
	public DynamicEntity() {
		this(0.0f, 0.0f);
	}
	
	public DynamicEntity(float x, float y) {
		super(x, y);
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferData(GL_ARRAY_BUFFER, RenderUtil.createBuffer(getVertices()), GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void move(Vector2f direction) {
		x += direction.x();
		y += direction.y();
		updateVertices();
	}
	
	public void setPosition(Vector2f position) {
		x = position.x();
		y = position.y();
		updateVertices();
	}
	
	private void updateVertices() {
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, RenderUtil.createBuffer(getVertices()));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
