package com.clownvin.lumina.gui;

import com.clownvin.lumina.graphics.Window;
import com.clownvin.lumina.res.Animation;

public class TitleScreen extends GUIComponent {

	public TitleScreen(String texture) {
		super(texture, 0, 0, Window.getWidth(), Window.getHeight(), null, Binding.CUSTOM, false);
	}
	
	public TitleScreen(String texture, Animation animation) {
		super(texture, 0, 0, Window.getWidth(), Window.getHeight(), null, Binding.CUSTOM, true);
		addAnimation(IDLE, animation);
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
