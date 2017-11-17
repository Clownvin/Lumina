package com.clownvin.lumina.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.LinkedList;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import com.clownvin.lumina.LuminaEngine;
import com.clownvin.lumina.gui.GUIManager;
import com.clownvin.lumina.input.KeyListener;
import com.clownvin.lumina.input.MouseListener;
import com.clownvin.lumina.input.WindowListener;
import com.clownvin.lumina.res.ResourceManager;
import com.clownvin.lumina.world.WorldManager;

public final class Window {
	public static final int WIDTH = 800, HEIGHT = 800;

	private static long window = NULL;
	private static int width = WIDTH, height = HEIGHT;
	private static String title = "";
	private static GLFWVidMode videoMode = null;
	private static boolean fullscreen = false;
	private static final LinkedList<WindowListener> windowListeners = new LinkedList<>();
	private static final LinkedList<KeyListener> keyListeners = new LinkedList<>();
	private static final LinkedList<MouseListener> mouseListeners = new LinkedList<>();
	private static double cursorX = 0.0d, cursorY = 0.0d;

	protected static final GLFWKeyCallback KEY_CALLBACK = new GLFWKeyCallback() {

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			for (KeyListener listener : keyListeners)
				listener.onKey(key, scancode, action, mods);
		}

	};

	private static final GLFWWindowSizeCallback WINDOW_RESIZE_CALLBACK = new GLFWWindowSizeCallback() {

		@Override
		public void invoke(long window, int width, int height) {
			Window.width = width;
			Window.height = height;
			recalibrate();
			for (WindowListener listener : windowListeners) {
				listener.onWindowResize(width, height);
			}
		}

	};

	private static final GLFWMouseButtonCallback MOUSE_BUTTON_CALLBACK = new GLFWMouseButtonCallback() {

		@Override
		public void invoke(long window, int button, int action, int mods) {
			for (MouseListener listener : mouseListeners) {
				listener.onMouseButton(button, action, mods);
			}
		}

	};

	private static final GLFWCursorPosCallback CURSOR_POS_CALLBACK = new GLFWCursorPosCallback() {

		@Override
		public void invoke(long window, double xpos, double ypos) {
			cursorX = xpos;
			cursorY = ypos;
			for (MouseListener listener : mouseListeners) {
				listener.onCursorPos(xpos, ypos);
			}
		}

	};

	static {
		if (!glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW...");
		videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	}

	public static void addKeyListener(KeyListener listener) {
		keyListeners.add(listener);
	}

	public static void addMouseListener(MouseListener listener) {
		mouseListeners.add(listener);
	}

	public static void addWindowSizeListener(WindowListener listener) {
		windowListeners.add(listener);
	}

	public static void createWindow(String title) {
		if (width > videoMode.width())
			width = videoMode.width();
		if (height > videoMode.height())
			height = videoMode.height();
		if (width <= 0)
			width = 800;
		if (height <= 0)
			height = 600;
		Window.title = title;
		glfwWindowHint(GLFW_SAMPLES, 4);
		window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		if (window == NULL)
			throw new IllegalStateException("Failed to create window...");
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwSetWindowPos(window, (videoMode.width() / 2) - (width / 2) + 1,
				(videoMode.height() / 2) - (height / 2) + 31);
		glfwSetWindowSizeCallback(window, WINDOW_RESIZE_CALLBACK);
		glfwSetKeyCallback(window, KEY_CALLBACK);
		glfwSetMouseButtonCallback(window, MOUSE_BUTTON_CALLBACK);
		glfwSetCursorPosCallback(window, CURSOR_POS_CALLBACK);
		createCapabilities();
		recalibrate();
		glfwShowWindow(window);
	}

	public static void dispose() {
		glfwDestroyWindow(window);
	}

	public static double getCursorX() {
		return cursorX;
	}

	public static double getCursorY() {
		return cursorY;
	}

	public static int getHeight() {
		return height;
	}

	public static int getWidth() {
		return width;
	}

	public static boolean isFullscreen() {
		return fullscreen;
	}

	public static void recalibrate() {
		Camera.resizeMatrix(width, height, LuminaEngine.getGlobalImageScale());
		glViewport(0, 0, width, height);
	}

	public static void removeKeyListener(KeyListener listener) {
		keyListeners.remove(listener);
	}

	public static void removeMouseListener(MouseListener listener) {
		mouseListeners.remove(listener);
	}

	public static void removeWindowSizeListener(WindowListener listener) {
		windowListeners.remove(listener);
	}

	public static void render() {
		RenderUtil.clearScreen();
		if (LuminaEngine.shouldShowWorld()) {
			Camera.updateShader(LuminaEngine.getGlobalShader(), "projection", true);
			Shader shader = ResourceManager.getShader(LuminaEngine.getGlobalShader());
			shader.bind();
			shader.setUniform("sampler", 0);
			WorldManager.renderWorld();
		}
		if (LuminaEngine.shouldShowGUI()) {
			Camera.updateShader(LuminaEngine.getGlobalShader(), "projection", false);
			Shader shader = ResourceManager.getShader(LuminaEngine.getGlobalShader());
			shader.bind();
			shader.setUniform("sampler", 0);
			GUIManager.renderGUI();
		}
		glfwSwapBuffers(window);
	}

	public static void setCursorPosCallback(GLFWCursorPosCallback callback) {
		glfwSetCursorPosCallback(window, callback);
	}

	public static void setFullscreen(boolean fullscreen) {
		boolean recreate = fullscreen != Window.fullscreen;
		Window.fullscreen = fullscreen;
		if (recreate) {
			dispose();
			createWindow(title);
		}
	}

	public static void setKeyCallback(GLFWKeyCallback callback) {
		glfwSetKeyCallback(window, callback);
	}

	public static void setMouseButtonCallback(GLFWMouseButtonCallback callback) {
		glfwSetMouseButtonCallback(window, callback);
	}

	public static void setSize(int width, int height) {
		Window.width = width;
		Window.height = height;
		if (window != NULL)
			glfwSetWindowSize(window, width, height);
	}

	public static boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
}
