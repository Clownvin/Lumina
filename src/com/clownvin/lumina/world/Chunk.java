package com.clownvin.lumina.world;

import java.util.ArrayList;

import com.clownvin.lumina.entity.Entity;

public class Chunk {
	private final String name;
	private final int width, height;

	private ArrayList<Entity> tileSet = new ArrayList<>();
	private ArrayList<Entity> entities = new ArrayList<>();

	public Chunk(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	public void setTileSet(ArrayList<Entity> tileSet) {
		this.tileSet = tileSet;
	}

	public void renderTiles() {
		for (Entity e : tileSet) {
			e.render();
		}
	}

	public void renderEntities() {
		for (Entity e : entities) {
			e.render();
		}
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getName() {
		return name;
	}
}
