package com.clownvin.lumina.gui;

import java.util.Hashtable;
import java.util.LinkedList;

import com.clownvin.lumina.graphics.Window;
import com.clownvin.lumina.input.KeyListener;
import com.clownvin.lumina.input.MouseListener;

public final class GUIManager implements MouseListener, KeyListener{
	public static final int MIN_LAYER = 0;
	public static final int MAX_LAYER = 10;
	
	private static final GUIManager singleton = new GUIManager();
	
	private static final Hashtable<Integer, LinkedList<GUIComponent>> components = new Hashtable<Integer, LinkedList<GUIComponent>>();
	
	static {
		for (int i = MIN_LAYER; i <= MAX_LAYER; i++) {
			components.put(i, new LinkedList<GUIComponent>());
		}
	}
	
	public static GUIManager getSingleton() {
		return singleton;
	}
	
	private GUIManager() {
		Window.addMouseListener(this);
		Window.addKeyListener(this);
	}
	
	public static void addGUIComponent(GUIComponent component) {
		components.get(component.getLayer()).add(component);
	}
	
	public static void removeGUIComponent(GUIComponent component) {
		components.get(component.getLayer()).remove(component);
	}
	
	public static void renderGUI() {
		for (int i = MIN_LAYER; i <= MAX_LAYER; i++) {
			for (GUIComponent component : components.get(i)) {
				if (!component.isVisible())
					continue;
				component.render();
			}
		}
	}

	@Override
	public void onKey(int key, int scancode, int action, int mods) {
		
	}

	@Override
	public void onCursorPos(double xpos, double ypos) {
		
	}

	@Override
	public void onMouseButton(int button, int action, int mods) {
		
	}
}
