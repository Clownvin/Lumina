package com.clownvin.lumina.gui;

import java.util.Hashtable;
import java.util.LinkedList;

public final class GUIManager {
	public static final int MIN_LAYER = 0;
	public static final int MAX_LAYER = 10;

	private static final Hashtable<Integer, LinkedList<GUIComponent>> components = new Hashtable<Integer, LinkedList<GUIComponent>>();

	static {
		for (int i = MIN_LAYER; i <= MAX_LAYER; i++) {
			components.put(i, new LinkedList<GUIComponent>());
		}
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
}
