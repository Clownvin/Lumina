package com.clownvin.lumina.gui;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import java.util.ArrayList;
import java.util.List;

import com.clownvin.lumina.LuminaEngine;
import com.clownvin.lumina.entity.Entity;
import com.clownvin.lumina.graphics.RenderUtil;
import com.clownvin.lumina.graphics.Window;
import com.clownvin.lumina.input.KeyListener;
import com.clownvin.lumina.input.MouseListener;
import com.clownvin.lumina.input.WindowListener;

public abstract class GUIComponent extends Entity implements WindowListener, KeyListener, MouseListener {
	public static enum Binding {
		TOP_LEFT, TOP_RIGHT, TOP_MIDDLE, MIDDLE_RIGHT, MIDDLE_LEFT, BOT_LEFT, BOT_RIGHT, BOT_MIDDLE, MIDDLE, CUSTOM;
	}

	private float bX, bY;
	private float fX1, fY1, fX2, fY2;
	private final Binding binding;
	private final GUIComponent parent;
	private final List<GUIComponent> children = new ArrayList<>();
	private boolean hasFocus = false;

	public GUIComponent(String texture, int x, int y, int width, int height, GUIComponent parent, Binding binding,
			boolean animated) {
		super(texture, x, y, true, animated);
		this.parent = parent;
		if (parent != null)
			parent.addChild(this);
		this.width = width;
		this.height = height;
		this.binding = binding;
		recalculate();
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferData(GL_ARRAY_BUFFER, RenderUtil.createBuffer(getVertices()), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		setVisible(true);
		Window.addKeyListener(this);
		Window.addMouseListener(this);
		Window.addWindowSizeListener(this);
	}

	private void addChild(GUIComponent component) {
		children.add(component);
	}

	public final float getHeight() {
		return height;
	}

	public abstract int getLayer();

	@Override
	public float[] getVertices() {
		return new float[] { fX1, fY1, // TR
				fX2, fY1, // TL
				fX2, fY2, // BL
				fX1, fY2 // BR
		};
	}

	public final float getWidth() {
		return width;
	}

	public final float getX() {
		return bX;
	}

	public final float getY() {
		return bY;
	}

	public boolean isFocused() {
		return hasFocus;
	}

	@Override
	public void onWindowResize(int width, int height) {
		recalculate();
	}

	public void recalculate() {
		float w = Window.getWidth(), h = Window.getHeight(), x1 = 0, y1 = 0;
		if (parent != null) {
			w = parent.getWidth();
			h = parent.getHeight();
			x1 = parent.getX();
			y1 = parent.getY();
		}
		switch (binding) {
		case CUSTOM:
			bX = x;
			bY = y;
			break;
		case TOP_LEFT:
			bX = x + x1;
			bY = y + y1;
			break;
		case MIDDLE_LEFT:
			bX = x + x1;
			bY = y + ((h / 2) - (getHeight() / 2));
			break;
		case BOT_LEFT:
			bX = x + x1;
			bY = y + (h - getHeight());
			break;
		case TOP_RIGHT:
			bX = x + (w - getWidth());
			bY = y + y1;
			break;
		case MIDDLE_RIGHT:
			bX = x + (w - getWidth());
			bY = y + ((h / 2) - (getHeight() / 2));
			break;
		case BOT_RIGHT:
			bX = x + (w - getWidth());
			bY = y + (h - getHeight());
			break;
		case TOP_MIDDLE:
			bX = x + ((w / 2) - (getWidth() / 2));
			bY = y + y1;
			break;
		case MIDDLE:
			bX = x + ((w / 2) - (getWidth() / 2));
			bY = y + ((h / 2) - (getHeight() / 2));
			break;
		case BOT_MIDDLE:
			bX = x + ((w / 2) - (getWidth() / 2));
			bY = y + (h - getHeight());
			break;
		default:
			System.err.println("No case for binding: " + binding);
			break;
		}
		fX1 = bX * LuminaEngine.getPixelRatio() + ((-Window.getWidth() / 2) * LuminaEngine.getPixelRatio());
		fY1 = -bY * LuminaEngine.getPixelRatio() + ((Window.getHeight() / 2) * LuminaEngine.getPixelRatio());
		fX2 = fX1 + (width * LuminaEngine.getPixelRatio());
		fY2 = fY1 - (height * LuminaEngine.getPixelRatio());
		updateVertices();
		for (GUIComponent child : children) {
			child.recalculate();
		}
	}

	public void setFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}

	public final void setHeight(int height) {
		this.height = height;
		recalculate();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			Window.addWindowSizeListener(this);
			onWindowResize(Window.getWidth(), Window.getHeight());
		} else
			Window.removeWindowSizeListener(this);
	}

	public final void setWidth(int width) {
		this.width = width;
		recalculate();
	}

	public final void setX(int x) {
		this.x = x;
		recalculate();
	}

	public final void setY(int y) {
		this.y = y;
		recalculate();
	}

	private void updateVertices() {
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, RenderUtil.createBuffer(getVertices()));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
