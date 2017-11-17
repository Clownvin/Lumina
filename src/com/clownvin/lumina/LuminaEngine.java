package com.clownvin.lumina;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import org.lwjgl.glfw.GLFWErrorCallback;

import com.clownvin.lumina.graphics.RenderUtil;
import com.clownvin.lumina.graphics.Window;
import com.clownvin.lumina.res.ResourceManager;

public final class LuminaEngine implements Runnable {
	private static final LuminaEngine engine = new LuminaEngine();

	private static boolean running = false;
	private static boolean keepRunning = false;
	private static double targetFPS = 60.0d;
	private static float globalImageScale = 32;
	private static String globalShader = "testshader";
	private static Game game = null;
	private static float pixelRatio = 1.0f/32.0f;
	private static boolean showWorld = false;
	private static boolean showGUI = false;
	private static boolean paused = false;
	private static long gameTime = 0L;

	public static LuminaEngine getEngine() {
		return engine;
	}
	
	public static long gameTimeMillis() {
		return gameTime;
	}

	public static void setTargetFPS(double targetFPS) {
		if (targetFPS <= 0) {
			throw new IllegalArgumentException("Target FPS must be at least greater than 0.");
		}
		LuminaEngine.targetFPS = targetFPS;
	}
	
	public static boolean isPaused() {
		return paused;
	}
	
	public static void setPause(boolean paused) {
		LuminaEngine.paused = paused;
	}

	public static void setGlobalImageScale(float scale) {
		globalImageScale = scale;
		pixelRatio = 1.0f/scale;
	}

	public static float getGlobalImageScale() {
		return globalImageScale;
	}
	
	public static float getPixelRatio() {
		return pixelRatio;
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
		Window.createWindow(game.getTitle());
		Window.setCursorPosCallback(game.getCursorPosCallback());
		Window.setKeyCallback(game.getKeyCallback());
		Window.setMouseButtonCallback(game.getMouseButtonCallback());
		RenderUtil.initGraphics();
		game.setup();
		Window.recalibrate();
		ResourceManager.getShader(globalShader);
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
		game.start();
		long start;
		while (keepRunning) {
			start = System.nanoTime();
			glfwPollEvents();
			float millisecondsPerFrame = (float) (1000.0d / targetFPS);
			if (!paused) {
				game.update();
				render();
				gameTime += millisecondsPerFrame;
			}
			if (Window.shouldClose())
				exit();
			long now = System.nanoTime();
			long sleeptime = (long) (millisecondsPerFrame - ((double) (now - start) / 1000000.0D));
			//System.out.println("Target FPS: "+targetFPS+", "+(1000.0d / targetFPS)+"-"+((double) (now - start) / 1000000.0D)+"="+sleeptime);
			if (sleeptime <= 0) {
				continue;
			}
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

	public static void exit() {
		keepRunning = false;
	}

	public static boolean shouldShowWorld() {
		return showWorld;
	}

	public static void setShowWorld(boolean showWorld) {
		LuminaEngine.showWorld = showWorld;
	}

	public static boolean shouldShowGUI() {
		return showGUI;
	}

	public static void setShowGUI(boolean showGUI) {
		LuminaEngine.showGUI = showGUI;
	}
}
