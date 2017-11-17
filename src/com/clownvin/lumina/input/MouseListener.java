package com.clownvin.lumina.input;

public interface MouseListener {
	
	public void onCursorPos(double xpos, double ypos);
	
	public void onMouseButton(int button, int action, int mods);

}
