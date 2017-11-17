package test;

import org.joml.Vector2f;

import com.clownvin.lumina.Game;
import com.clownvin.lumina.LuminaEngine;
import com.clownvin.lumina.entity.Entity;
import com.clownvin.lumina.graphics.Camera;
import com.clownvin.lumina.graphics.Window;
import com.clownvin.lumina.gui.Button;
import com.clownvin.lumina.gui.ChatBox;
import com.clownvin.lumina.gui.GUIComponent.Binding;
import com.clownvin.lumina.gui.GUIManager;
import com.clownvin.lumina.gui.TitleScreen;
import com.clownvin.lumina.input.KeyListener;
import com.clownvin.lumina.input.MouseListener;
import com.clownvin.lumina.res.ResourceManager;
import com.clownvin.lumina.world.WorldManager;

public class TestGame extends Game {

	public static float moveAmount = 2.0f / 64.0f;

	public static void main(String[] args) {
		TestGame game = new TestGame();
		LuminaEngine.getEngine().start(game);
	}

	public Entity player;

	public boolean moving = false;

	public boolean running = false;
	public Vector2f directionVector = new Vector2f(0.0f, 0.0f);
	public Vector2f movementVector = new Vector2f(0.0f, 0.0f);
	public TitleScreen titleScreen;
	public final KeyListener keyListener = new KeyListener() {

		@Override
		public void onKey(int key, int scancode, int action, int mods) {
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
			case 0x20: // SPACEBAR
				if (action == 1)
					LuminaEngine.setPause(!LuminaEngine.isPaused());
				break;
			default:
				System.out
						.println("key: " + key + ", scancode: " + scancode + ", action: " + action + ", mods: " + mods);
				break;
			}
			if (directionVector.length() != 0) {
				directionVector.normalize(movementVector);
				movementVector.mul(moveAmount * ((mods & 0x01) == 1 ? 2 : 1), movementVector);
			} else {
				movementVector = new Vector2f(0.0f, 0.0f);
			}
		}

	};
	public final MouseListener mouseListener = new MouseListener() {

		@Override
		public void onCursorPos(double xpos, double ypos) {
			System.out.println("x: " + xpos + ", y: " + ypos);
		}

		@Override
		public void onMouseButton(int button, int action, int mods) {
			switch (button) {
			case 0: // LEFT CLICK
				titleScreen.setVisible(false);
				LuminaEngine.setShowWorld(true);
				break;
			default:
				System.out.println("button: " + button + ", action: " + action + ", mods: " + mods);
				break;

			}
		}

	};

	public TestGame() {

	}

	@Override
	public String getTitle() {
		return "Lumina Test";
	}

	@Override
	public void setup() {
		Window.addKeyListener(keyListener);
		Window.addMouseListener(mouseListener);
		LuminaEngine.setTargetFPS(60.0d);
		LuminaEngine.setGlobalImageScale(64);
		LuminaEngine.setShowWorld(false);
		LuminaEngine.setShowGUI(true);
		titleScreen = new TitleScreen("title1");
		ResourceManager.loadTexture("button1", 64, 64);
		GUIManager.addGUIComponent(new ChatBox("chatbox1", 500, 200));
		GUIManager.addGUIComponent(new Button("button1", 0, 0, 192, 64, null, Binding.CUSTOM));
		GUIManager.addGUIComponent(titleScreen);
		WorldManager.loadWorld("test");
		player = new Entity("test1", 0, 0, true, true);
		WorldManager.getWorld().addEntity(player);
	}

	@Override
	public void update() {
		Camera.move(new Vector2f(0.0f, 0.0f).sub(movementVector));
		player.move(movementVector);
	}
}
