package com.clownvin.lumina.graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public final class RenderUtil {
	private static int staticTexPointer, indicesPointer;

	public static void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public static FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}

	public static int getIndicesPointer() {
		return indicesPointer;
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	public static int getStaticTexPointer() {
		return staticTexPointer;
	}

	public static void init() {
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
		setupModel();
	}

	public static void setupModel() {
		float[] uv = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
		int[] indices = new int[] { 0, 1, 2, 2, 3, 0 };

		staticTexPointer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, staticTexPointer);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(uv), GL_STATIC_DRAW);

		indicesPointer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesPointer);

		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
		buffer.put(indices).flip();

		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
}
