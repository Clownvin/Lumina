package com.clownvin.lumina.entity;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.util.Hashtable;

import org.joml.Vector2f;

import com.clownvin.lumina.graphics.RenderUtil;
import com.clownvin.lumina.res.Animation;
import com.clownvin.lumina.res.ResourceManager;
import com.clownvin.lumina.res.Texture;

public class Entity {

	public static final String IDLE = "idle";

	protected final boolean animated;
	protected final boolean dynamic;
	protected boolean visible = true;
	protected float x, y, width, height;
	protected int vertexPointer;
	protected Texture texture;
	protected String animation = IDLE;
	protected final Hashtable<String, Animation> animationTable;

	public Entity(String texture, float x, float y, final boolean dynamic, final boolean animated) {
		this.x = x;
		this.y = y;
		this.texture = ResourceManager.getTexture(texture);
		this.texture.use();
		vertexPointer = glGenBuffers();
		this.dynamic = dynamic;
		this.animated = animated;
		if (animated) {
			animationTable = new Hashtable<>();
			addAnimation(IDLE, new Animation(Integer.MAX_VALUE, new int[] { 0 }));
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

	public void destroy() {
		texture.stopUsing();
	}

	public int getFrame() {
		if (animated)
			return animationTable.get(animation).getFrame();
		return 0;
	}

	public Texture getTexture() {
		return texture;
	}

	protected float[] getVertices() {
		return new float[] { -0.5f + x, 0.5f + y, 0.5f + x, 0.5f + y, 0.5f + x, -0.5f + y, -0.5f + x, -0.5f + y };
	}

	public boolean isVisible() {
		return visible;
	}

	public void move(Vector2f direction) {
		if (!dynamic)
			throw new IllegalStateException("Entity is not marked dynamic, and so cannot move!");
		x += direction.x();
		y += direction.y();
		updateVertices();
	}

	public void render() {
		if (animated)
			animationTable.get(animation).animate();
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		getTexture().bind(0, getFrame());

		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, RenderUtil.getIndicesPointer());
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}

	public void setPosition(Vector2f position) {
		if (!dynamic)
			throw new IllegalStateException("Entity is not marked dynamic, and so cannot have it's position set!");
		x = position.x();
		y = position.y();
		updateVertices();
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void startAnimation(String name) {
		Animation a = animationTable.get(name);
		if (a == null) {
			System.err.println("No animation in entity for name: " + name);
			return;
		}
		a.reset();
		animation = name;
	}

	private void updateVertices() {
		if (!dynamic)
			throw new IllegalStateException("Entity is not marked dynamic, and so cannot update it's vertices!");
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, RenderUtil.createBuffer(getVertices()));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
