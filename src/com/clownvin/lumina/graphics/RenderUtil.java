package com.clownvin.lumina.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjglx.BufferUtils;

public final class RenderUtil {
	private static int staticTexPointer, indicesPointer;

	public static int getStaticTexPointer() {
		return staticTexPointer;
	}
	public static int getIndicesPointer() {
		return indicesPointer;
	}

	public static void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public static void initGraphics() {
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

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	public static FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
}
