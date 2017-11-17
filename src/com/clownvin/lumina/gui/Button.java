package com.clownvin.lumina.gui;

import com.clownvin.lumina.graphics.Window;
import com.clownvin.lumina.math.MathUtil;
import com.clownvin.lumina.res.Animation;

public class Button extends GUIComponent {
	public static final String PRESSED = "pressed";
	public static final String MOUSE_OVER = "mouseover";
	public static final Runnable NO_ACTION = new Runnable() {

		@Override
		public void run() {
			System.err.println("No action set");
		}

	};

	private Runnable action;

	public Button(String texture, int x, int y, int width, int height, GUIComponent parent, Binding binding) {
		this(texture, x, y, width, height, parent, binding, NO_ACTION);
	}

	public Button(String texture, int x, int y, int width, int height, GUIComponent parent, Binding binding,
			Runnable action) {
		super(texture, x, y, width, height, parent, binding, true);
		addAnimation(PRESSED, new Animation(Integer.MAX_VALUE, new int[] { 2 }));
		addAnimation(MOUSE_OVER, new Animation(Integer.MAX_VALUE, new int[] { 1 }));
		this.action = action;
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public void onCursorPos(double xpos, double ypos) {
		if (MathUtil.inside(xpos, ypos, getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
			startAnimation(MOUSE_OVER);
		} else {
			startAnimation(IDLE);
		}
	}

	@Override
	public void onKey(int key, int scancode, int action, int mods) {
		return;
	}

	@Override
	public void onMouseButton(int button, int action, int mods) {
		if (action == 2 || !MathUtil.inside(Window.getCursorX(), Window.getCursorY(), getX(), getY(),
				getX() + getWidth(), getY() + getHeight()))
			return;
		if (action == 1)
			startAnimation(PRESSED);
		else {
			this.action.run();
			startAnimation(MOUSE_OVER);
		}
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

}
