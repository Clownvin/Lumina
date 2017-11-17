package com.clownvin.lumina.gui;

public class ChatBox extends GUIComponent {

	public ChatBox(String texture, int width, int height) {
		super(texture, 0, 0, width, height, null, Binding.BOT_LEFT, false);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public void onCursorPos(double xpos, double ypos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKey(int key, int scancode, int action, int mods) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseButton(int button, int action, int mods) {
		// TODO Auto-generated method stub

	}

}
