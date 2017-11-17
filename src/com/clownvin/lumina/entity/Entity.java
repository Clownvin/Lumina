package com.clownvin.lumina.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.Hashtable;

import org.joml.Vector2f;

import com.clownvin.lumina.Log;
import com.clownvin.lumina.graphics.RenderUtil;
import com.clownvin.lumina.res.Animation;
import com.clownvin.lumina.res.ResourceManager;

public class Entity {
	
	public static final String IDLE = "idle";
	
	protected final boolean animated;
	protected final boolean dynamic;
	protected boolean visible = true;
	protected float x, y, width, height;
	protected int vertexPointer;
	protected String texture;
	protected String animation = IDLE;
	protected final Hashtable<String, Animation> animationTable;
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Entity(String texture, float x, float y, final boolean dynamic, final boolean animated) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		vertexPointer = glGenBuffers();
		this.dynamic = dynamic;
		this.animated = animated;
		if (animated) {
			animationTable = new Hashtable<>();
			addAnimation(IDLE, new Animation(Integer.MAX_VALUE, new int[] {14}));
		} else
			animationTable = null;
		if (dynamic) {
			glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
			glBufferData(GL_ARRAY_BUFFER, RenderUtil.createBuffer(getVertices()), GL_DYNAMIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		} else {
			glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
			glBufferData(GL_ARRAY_BUFFER, RenderUtil.createBuffer(getVertices()), GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}
	
	public void addAnimation(String name, Animation animation) {
		animationTable.put(name, animation);
	}
	
	public void startAnimation(String name) {
		Animation a = animationTable.get(name);
		if (a == null) {
			Log.logD("No animation in entity for name: "+name+"\n");
			return;
		}
		a.reset();
		animation = name;
	}

	protected float[] getVertices() {
		return new float[] { 
				-0.5f + x, 0.5f + y,
				0.5f + x, 0.5f + y,
				0.5f + x, -0.5f + y,
				-0.5f + x, -0.5f + y };
	}

	public String getTexture() {
		return texture;
	}
	
	public int getFrame() {
		if (animated)
			return animationTable.get(animation).getFrame();
		return 0;
	}
	
	public void move(Vector2f direction) {
		if (!dynamic)
			throw new IllegalStateException("Entity is not marked dynamic, and so cannot move!");
		x += direction.x();
		y += direction.y();
		updateVertices();
	}

	public void setPosition(Vector2f position) {
		if (!dynamic)
			throw new IllegalStateException("Entity is not marked dynamic, and so cannot have it's position set!");
		x = position.x();
		y = position.y();
		updateVertices();
	}

	private void updateVertices() {
		if (!dynamic)
			throw new IllegalStateException("Entity is not marked dynamic, and so cannot update it's vertices!");
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, RenderUtil.createBuffer(getVertices()));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void render() {
		if (animated) 
			animationTable.get(animation).animate();
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		ResourceManager.getTexture(getTexture()).bind(0, getFrame());

		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, RenderUtil.getIndicesPointer());
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
}
