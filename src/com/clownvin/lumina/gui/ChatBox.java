package com.clownvin.lumina.gui;

public class ChatBox extends GUIComponent {

	public ChatBox(String texture, int width, int height) {
		super(texture, 0, 0, width, height, null, Binding.BOT_LEFT, false);
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
