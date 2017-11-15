package test;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.clownvin.lumina.Game;
import com.clownvin.lumina.LuminaEngine;
import com.clownvin.lumina.Log;
import com.clownvin.lumina.entity.DynamicEntity;
import com.clownvin.lumina.graphics.Camera;
import com.clownvin.lumina.world.WorldManager;

public class TestGame extends Game {

	public static float moveAmount = 0.05f;

	public DynamicEntity player;

	public static void main(String[] args) {
		Log.setDebug(true);
		TestGame game = new TestGame();
		LuminaEngine.getEngine().start(game);
	}

	public TestGame() {

	}

	public boolean moving = false;
	public Vector2f movementVector = new Vector2f(0.0f, 0.0f);

	@Override
	public GLFWKeyCallback getKeyCallback() {
		return new GLFWKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				switch (key) {
				case 0x41: // A
					if (action == 1) {
						movementVector = movementVector.add(moveAmount, 0.0f);
					} else if (action == 0) {
						movementVector = movementVector.sub(moveAmount, 0.0f);
					}
					break;
				case 0x44: // D
					if (action == 1) {
						movementVector = movementVector.add(-moveAmount, 0.0f);
					} else if (action == 0) {
						movementVector = movementVector.sub(-moveAmount, 0.0f);
					}
					break;
				case 0x53: // S
					if (action == 1) {
						movementVector = movementVector.add(0.0f, moveAmount);
					} else if (action == 0) {
						movementVector = movementVector.sub(0.0f, moveAmount);
					}
					break;
				case 0x57: // W
					if (action == 1) {
						movementVector = movementVector.add(0.0f, -moveAmount);
					} else if (action == 0) {
						movementVector = movementVector.sub(0.0f, -moveAmount);
					}
					break;
				case 0x100: // ESCAPE
					LuminaEngine.stop();
					break;
				default:
					Log.log("key: " + key + ", scancode: " + scancode + ", action: " + action + ", mods: " + mods
							+ "\n");
					break;
				}
			}

		};
	}

	@Override
	public GLFWCursorPosCallback getCursorPosCallback() {
		return new GLFWCursorPosCallback() {

			@Override
			public void invoke(long window, double xpos, double ypos) {
				Log.log("x: " + xpos + ", y: " + ypos + "\n");
			}

		};
	}

	@Override
	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				Log.log("button: " + button + ", action: " + action + ", mods: " + mods + "\n");
			}

		};
	}

	@Override
	public String getTitle() {
		return "Lumina Test";
	}

	@Override
	public void onUpdate() {
		Camera.move(movementVector);
		player.move(new Vector2f(0.0f, 0.0f).sub(movementVector));
	}

	@Override
	public void setup() {
		LuminaEngine.setTargetFPS(120.0d);
		LuminaEngine.setGlobalImageScale(64);
		LuminaEngine.setGlobalShader("testshader");
		player = new DynamicEntity(0, 0);
		WorldManager.loadWorld("test");
		WorldManager.getWorld().addEntity(player);
	}

}
