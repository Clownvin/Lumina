package com.clownvin.lumina.gui;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

import com.clownvin.lumina.LuminaEngine;
import com.clownvin.lumina.entity.Entity;
import com.clownvin.lumina.graphics.RenderUtil;
import com.clownvin.lumina.graphics.Window;
import com.clownvin.lumina.graphics.WindowSizeListener;

public abstract class GUIComponent extends Entity implements WindowSizeListener {
	public static enum Binding {
		TOP_LEFT, TOP_RIGHT, TOP_MIDDLE, MIDDLE_RIGHT, MIDDLE_LEFT, BOT_LEFT, BOT_RIGHT, BOT_MIDDLE, MIDDLE, CUSTOM;
	}
	
	protected int x, y, width, height;
	private int bX, bY;
	private float fX1, fY1, fX2, fY2;
	private final Binding binding;
	
	public GUIComponent(int x, int y, int width, int height, Binding binding) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.binding = binding;
		recalculate();
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferData(GL_ARRAY_BUFFER, RenderUtil.createBuffer(getVertices()), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		setVisible(true);
	}
	
	public void recalculate() {
		switch (binding) {
		case CUSTOM:
		case TOP_LEFT:
			bX = x;
			bY = y;
			break;
		case MIDDLE_LEFT:
			bX = x;
			bY = y + ((Window.getHeight() / 2) - (getHeight() / 2));
			break;
		case BOT_LEFT:
			bX = x;
			bY = y + (Window.getHeight() - getHeight());
			break;
		case TOP_RIGHT:
			bX = x + (Window.getWidth() - getWidth());
			bY = y;
			break;
		case MIDDLE_RIGHT:
			bX = x + (Window.getWidth() - getWidth());
			bY = y + ((Window.getHeight() / 2) - (getHeight() / 2));
			break;
		case BOT_RIGHT:
			bX = x + (Window.getWidth() - getWidth());
			bY = y + (Window.getHeight() - getHeight());
			break;
		case TOP_MIDDLE:
			bX = x + ((Window.getWidth() / 2) - (getWidth() / 2));
			bY = y;
			break;
		case MIDDLE:
			bX = x + ((Window.getWidth() / 2) - (getWidth() / 2));
			bY = y + ((Window.getHeight() / 2) - (getHeight() / 2));
			break;
		case BOT_MIDDLE:
			bX = x + ((Window.getWidth() / 2) - (getWidth() / 2));
			bY = y + (Window.getHeight() - getHeight());
			break;
		default:
			System.out.println("No case for binding: "+binding);
			break;
		}
		fX1 = bX * LuminaEngine.getPixelRatio() + ((-Window.getWidth() / 2) * LuminaEngine.getPixelRatio());
		fY1 = -bY * LuminaEngine.getPixelRatio() + ((Window.getHeight() / 2) * LuminaEngine.getPixelRatio());
		System.out.println(width+", "+height+", "+LuminaEngine.getPixelRatio());
		fX2 = fX1 + (width * LuminaEngine.getPixelRatio());
		fY2 = fY1 - (height * LuminaEngine.getPixelRatio());
		updateVertices();
	}
	
	private void updateVertices() {
		glBindBuffer(GL_ARRAY_BUFFER, vertexPointer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, RenderUtil.createBuffer(getVertices()));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
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
	
	@Override
	public abstract String getTexture();
	
	public abstract int getLayer();
	
	@Override
	public float[] getVertices() {
		return new float[] { 
				fX1, fY1, //TR
				fX2, fY1, //TL
				fX2, fY2, //BL
				fX1, fY2 //BR 
				};
	}
	
	public final int getX() {
		return bX;
	}
	
	public final void setX(int x) {
		this.x = x;
		recalculate();
	}
	
	public final int getWidth() {
		return width;
	}
	
	public final void setWidth(int width) {
		this.width = width;
		recalculate();
	}
	
	public final int getHeight() {
		return height;
	}
	
	public final void setHeight(int height) {
		this.height = height;
		recalculate();
	}
	
	public final int getY() {
		return bY;
	}
	
	public final void setY(int y) {
		this.y = y;
		recalculate();
	}
	
	@Override
	public void onWindowResize(int width, int height) {
		recalculate();
	}
}
