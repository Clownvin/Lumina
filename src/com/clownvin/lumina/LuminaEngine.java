package com.clownvin.lumina;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import org.lwjgl.glfw.GLFWErrorCallback;

import com.clownvin.lumina.graphics.Camera;
import com.clownvin.lumina.graphics.RenderUtil;
import com.clownvin.lumina.graphics.Window;

public final class LuminaEngine implements Runnable {
	public static final int WIDTH = 800, HEIGHT = 800;

	private static final LuminaEngine engine = new LuminaEngine();

	private static boolean running = false;
	private static boolean keepRunning = false;
	private static double targetFPS = 60.0d;
	private static int globalImageScale = 32;
	private static String globalShader = "testshader";
	private static Game game = null;

	public static LuminaEngine getEngine() {
		return engine;
	}

	public static void setTargetFPS(double targetFPS) {
		if (targetFPS <= 0) {
			throw new IllegalArgumentException("Target FPS must be at least greater than 0.");
		}
		LuminaEngine.targetFPS = targetFPS;
	}

	public static void setGlobalImageScale(int scale) {
		globalImageScale = scale;
	}

	public static int getGlobalImageScale() {
		return globalImageScale;
	}

	public static String getGlobalShader() {
		return globalShader;
	}

	public static void setGlobalShader(String globalShader) {
		LuminaEngine.globalShader = globalShader;
	}

	public static boolean running() {
		return running;
	}

	public static Game getGame() {
		return game;
	}

	private LuminaEngine() {
		GLFWErrorCallback.createPrint(System.err).set();
	}

	public void start(Game game) {
		LuminaEngine.game = game;
		Thread thread = new Thread(engine);
		thread.setName("LuminaEngine");
		thread.start();
	}

	private void setup() {
		Window.createWindow(WIDTH, HEIGHT, game.getTitle());
		Window.setCursorPosCallback(game.getCursorPosCallback());
		Window.setKeyCallback(game.getKeyCallback());
		Window.setMouseButtonCallback(game.getMouseButtonCallback());
		RenderUtil.initGraphics();
		Log.log(RenderUtil.getOpenGLVersion() + "\n");
	}

	private void cleanUp() {
		Window.dispose();
		game.update();
	}

	@Override
	public void run() {
		setup();
		running = keepRunning = true;
		game.setup();
		Window.recalibrate();
		game.start();
		long start = System.nanoTime();
		while (keepRunning) {
			glfwPollEvents();
			game.update();
			Camera.updateCamera();
			render();
			if (Window.shouldClose())
				stop();
			long now = System.nanoTime();
			long sleeptime = (long) ((1000.0d / targetFPS) - ((double) (now - start) / 1000000.0D));
			// System.out.println((1000.0d / 60.0d)+" "+(double)(now - start) / 1000000.0D);
			start = now;
			if (sleeptime < 0)
				continue;
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		running = false;
		cleanUp();
	}

	private void render() {
		Window.render();
	}

	public static void stop() {
		keepRunning = false;
	}
}
