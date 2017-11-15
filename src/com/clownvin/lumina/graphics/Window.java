package com.clownvin.lumina.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import com.clownvin.lumina.LuminaEngine;
import com.clownvin.lumina.res.ResourceManager;
import com.clownvin.lumina.world.WorldManager;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

public final class Window {

	private static long window = NULL;
	private static int width, height;
	private static String title = "";
	private static GLFWVidMode videoMode = null;
	private static boolean fullscreen = false;

	static {
		if (!glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW...");
		videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	}

	public static void createWindow(int width, int height, String title) {
		if (width > videoMode.width())
			width = videoMode.width();
		if (height > videoMode.height())
			height = videoMode.height();
		if (width <= 0)
			width = 800;
		if (height <= 0)
			height = 600;
		Window.width = width;
		Window.height = height;
		Window.title = title;
		glfwWindowHint(GLFW_SAMPLES, 4);
		window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		if (window == NULL)
			throw new IllegalStateException("Failed to create window...");
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwSetWindowPos(window, (videoMode.width() / 2) - (width / 2), (videoMode.height() / 2) - (height / 2));
		glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {

			@Override
			public void invoke(long window, int width, int height) {
				Window.width = width;
				Window.height = height;
				recalibrate();
			}

		});
		createCapabilities();
		recalibrate();
		glfwShowWindow(window);
	}

	public static void setSize(int width, int height) {
		glfwSetWindowSize(window, width, height);
	}

	public static void render() {
		RenderUtil.clearScreen();
		Shader shader = ResourceManager.getShader(LuminaEngine.getGlobalShader());
		shader.bind();
		shader.setUniform("sampler", 0);
		WorldManager.renderWorld();
		glfwSwapBuffers(window);
	}

	public static boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static void dispose() {
		glfwDestroyWindow(window);
	}

	public static void recalibrate() {
		Camera.resizeMatrix(width, height, LuminaEngine.getGlobalImageScale());
		glViewport(0, 0, width, height);
	}

	public static void setKeyCallback(GLFWKeyCallback callback) {
		glfwSetKeyCallback(window, callback);
	}

	public static void setCursorPosCallback(GLFWCursorPosCallback callback) {
		glfwSetCursorPosCallback(window, callback);
	}

	public static void setMouseButtonCallback(GLFWMouseButtonCallback callback) {
		glfwSetMouseButtonCallback(window, callback);
	}

	public static boolean isFullscreen() {
		return fullscreen;
	}

	public static void setFullscreen(boolean fullscreen) {
		if (fullscreen != Window.fullscreen) {
			dispose();
			createWindow(width, height, title);
		}
		Window.fullscreen = fullscreen;
	}
}
