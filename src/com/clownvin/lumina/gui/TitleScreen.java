package com.clownvin.lumina.gui;

import com.clownvin.lumina.graphics.Window;

public class TitleScreen extends GUIComponent {
	
	protected final String texture;

	public TitleScreen(String texture) {
		super(0, 0, Window.getWidth(), Window.getHeight(), Binding.CUSTOM);
		this.texture = texture;
	}

	@Override
	public String getTexture() {
		return texture;
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void onWindowResize(int width, int height) {
		this.width = width;
		this.height = height;
		recalculate();
	}

}
