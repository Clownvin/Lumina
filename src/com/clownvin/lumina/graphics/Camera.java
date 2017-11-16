package com.clownvin.lumina.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.clownvin.lumina.res.ResourceManager;

public final class Camera {
	private static Vector3f position = new Vector3f(0, 0, 0);
	private static Matrix4f projection = new Matrix4f();

	public static void resizeMatrix(int width, int height, float scale) {
		projection = new Matrix4f().setOrtho2D(-width / 2, width / 2, -height / 2, height / 2).scale(scale);
	}

	public static Matrix4f getProjection(boolean translated) {
		Matrix4f target = new Matrix4f();
		Matrix4f pos = new Matrix4f();
		if (translated)
			pos = pos.setTranslation(position);
		target = projection.mul(pos, target);
		return target;
	}

	public static Vector3f getPosition() {
		return position;
	}

	public static void setPosition(Vector2f position) {
		Camera.position = new Vector3f(position.x(), position.y(), 0.0f);
	}

	public static void move(Vector2f direction) {
		position = position.add(direction.x(), direction.y(), 0.0f);
	}

	public static void updateShader(String shader, String projection, boolean translated) {
		ResourceManager.getShader(shader).setUniform(projection, getProjection(translated));
	}
}
