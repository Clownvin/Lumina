package com.clownvin.lumina.gui;

public class ChatBox extends GUIComponent {

	public ChatBox(int width, int height) {
		super(0, 0, width, height, Binding.BOT_LEFT);
	}

	@Override
	public String getTexture() {
		return "chatbox1";
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
