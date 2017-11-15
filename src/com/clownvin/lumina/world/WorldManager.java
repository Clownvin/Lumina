package com.clownvin.lumina.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.clownvin.lumina.entity.Entity;
import com.clownvin.lumina.entity.TileEntity;

public final class WorldManager {
	// private static final Hashtable<String, World> worldTable = new Hashtable<>();

	private static World currentWorld;

	public static void renderWorld() {
		currentWorld.renderTiles();
		currentWorld.renderEntities();
	}

	public static World getWorld() {
		return currentWorld;
	}

	public static void addEntity(Entity entity) {
		currentWorld.addEntity(entity);
	}

	public static boolean loadWorld(String worldName) {
		File worldFile = new File("./res/worlds/" + worldName + ".world");
		BufferedReader reader = null;
		String line = "";
		Hashtable<String, String> textures = new Hashtable<>();
		int width = 100, height = 100;
		ArrayList<Entity> tileSet = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(worldFile));
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				if (tokens[0].equalsIgnoreCase("#width")) {
					width = Integer.parseInt(tokens[1]);
				} else if (tokens[0].equalsIgnoreCase("#height")) {
					height = Integer.parseInt(tokens[1]);
				} else if (tokens[0].equalsIgnoreCase("#def") && tokens.length == 3) {
					textures.put(tokens[1], tokens[2]);
				} else if (textures.containsKey(tokens[0]) && tokens.length == 3) {
					float x = Float.parseFloat(tokens[1]);
					float y = Float.parseFloat(tokens[2]);
					tileSet.add(new TileEntity(x, y, textures.get(tokens[0])));
				} else if (textures.containsKey(tokens[0]) && tokens.length >= 5) {
					float x1 = Float.parseFloat(tokens[1]);
					float y1 = Float.parseFloat(tokens[2]);
					float x2 = Float.parseFloat(tokens[3]);
					float y2 = Float.parseFloat(tokens[4]);
					float sizeX = 1.0f;
					float sizeY = 1.0f;
					if (tokens.length == 6) {
						sizeX = sizeY = Float.parseFloat(tokens[5]);
					} else if (tokens.length == 7) {
						sizeX = Float.parseFloat(tokens[5]);
						sizeY = Float.parseFloat(tokens[6]);
					}
					for (float x = x1; x < x2; x += sizeX) {
						for (float y = y1; y < y2; y += sizeY) {
							tileSet.add(new TileEntity(x, y, textures.get(tokens[0])));
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		World world = new World(worldName, width, height);
		world.setTileSet(tileSet);
		currentWorld = world;
		return true;
	}
}
