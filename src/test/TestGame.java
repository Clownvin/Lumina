package test;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.clownvin.lumina.Game;
import com.clownvin.lumina.LuminaEngine;
import com.clownvin.lumina.Log;
import com.clownvin.lumina.entity.Entity;
import com.clownvin.lumina.graphics.Camera;
import com.clownvin.lumina.gui.ChatBox;
import com.clownvin.lumina.gui.GUIManager;
import com.clownvin.lumina.gui.TitleScreen;
import com.clownvin.lumina.res.ResourceManager;
import com.clownvin.lumina.world.WorldManager;

public class TestGame extends Game {

	public static float moveAmount = 2.0f/64.0f;

	public Entity player;

	public static void main(String[] args) {
		Log.setDebug(true);
		TestGame game = new TestGame();
		LuminaEngine.getEngine().start(game);
	}

	public TestGame() {

	}

	public boolean moving = false;
	public Vector2f directionVector = new Vector2f(0.0f, 0.0f);
	public Vector2f movementVector = new Vector2f(0.0f, 0.0f);
	public TitleScreen titleScreen;

	@Override
	public GLFWKeyCallback getKeyCallback() {
		return new GLFWKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				switch (key) {
				case 0x41: // A
					if (action == 1) {
						directionVector = directionVector.add(-moveAmount, 0.0f);
					} else if (action == 0) {
						directionVector = directionVector.sub(-moveAmount, 0.0f);
					}
					break;
				case 0x44: // D
					if (action == 1) {
						directionVector = directionVector.add(moveAmount, 0.0f);
					} else if (action == 0) {
						directionVector = directionVector.sub(moveAmount, 0.0f);
					}
					break;
				case 0x53: // S
					if (action == 1) {
						directionVector = directionVector.add(0.0f, -moveAmount);
					} else if (action == 0) {
						directionVector = directionVector.sub(0.0f, -moveAmount);
					}
					break;
				case 0x57: // W
					if (action == 1) {
						directionVector = directionVector.add(0.0f, moveAmount);
					} else if (action == 0) {
						directionVector = directionVector.sub(0.0f, moveAmount);
					}
					break;
				case 0x100: // ESCAPE
					LuminaEngine.exit();
					break;
				case 0x20: //SPACEBAR
					if (action == 1)
						LuminaEngine.setPause(!LuminaEngine.isPaused());
					break;
				default:
					Log.log("key: " + key + ", scancode: " + scancode + ", action: " + action + ", mods: " + mods
							+ "\n");
					break;
				}
				if (directionVector.length() != 0) {
					directionVector.normalize(movementVector);
					movementVector.mul(moveAmount, movementVector);
				} else {
					movementVector = new Vector2f(0.0f, 0.0f);
				}
			}

		};
	}

	@Override
	public GLFWCursorPosCallback getCursorPosCallback() {
		return new GLFWCursorPosCallback() {

			@Override
			public void invoke(long window, double xpos, double ypos) {
				//Log.log("x: " + xpos + ", y: " + ypos + "\n");
			}

		};
	}

	@Override
	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				switch (button) {
				case 0: //LEFT CLICK
					titleScreen.setVisible(false);
					LuminaEngine.setShowWorld(true);
					break;
				default:
					Log.log("button: " + button + ", action: " + action + ", mods: " + mods + "\n");
					break;
					
				}
			}

		};
	}

	@Override
	public String getTitle() {
		return "Lumina Test";
	}

	@Override
	public void onUpdate() {
		Camera.move(new Vector2f(0.0f, 0.0f).sub(movementVector));
		player.move(movementVector);
	}

	@Override
	public void setup() {
		LuminaEngine.setTargetFPS(60.0d);
		LuminaEngine.setGlobalImageScale(64);
		LuminaEngine.setShowWorld(false);
		LuminaEngine.setShowGUI(true);
		titleScreen = new TitleScreen("title1");
		GUIManager.addGUIComponent(new ChatBox("chatbox1", 500, 200));
		GUIManager.addGUIComponent(titleScreen);
		WorldManager.loadWorld("test");
		player = new Entity("test1", 0, 0, true, true);
		WorldManager.getWorld().addEntity(player);
	}

}
