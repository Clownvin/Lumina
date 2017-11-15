package com.clownvin.lumina.res;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.clownvin.lumina.graphics.Shader;

import static org.lwjgl.opengl.GL11.*;

public final class ResourceManager {
	public static final String RESOURCE_PATH = "./res/";
	public static final String TEXTURE_PATH = "textures/";

	private static final Hashtable<String, Texture> textureTable = new Hashtable<>();
	private static final Hashtable<String, Shader> shaderTable = new Hashtable<>();

	public static String readShader(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(RESOURCE_PATH + fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
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
		return shaderSource.toString();
	}

	public static Shader getShader(String name) {
		Shader shader = shaderTable.get(name);
		if (shader == null) {
			shader = new Shader(name);
			shaderTable.put(name, shader);
		}
		return shader;
	}

	public static Texture getTexture(String name) {
		Texture texture = textureTable.get(name);
		if (texture == null) {
			texture = loadTexture(name);
			textureTable.put(name, texture);
		}
		return texture;
	}

	public static Texture loadTexture(String name, String format) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(RESOURCE_PATH + TEXTURE_PATH + name + "." + format));
		} catch (IOException e) {
			e.printStackTrace(); // TODO return blank texture instead of just crashing
			System.exit(1);
		}
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		int[] pixels_raw = new int[width * height * 4];
		pixels_raw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);

		ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = pixels_raw[i * height + j];
				pixels.put((byte) ((pixel >> 16) & 0xFF)); // RED
				pixels.put((byte) ((pixel >> 8) & 0xFF)); // GREEN
				pixels.put((byte) (pixel & 0xFF)); // BLUE
				pixels.put((byte) ((pixel >> 24) & 0xFF)); // ALPHA
			}
		}

		pixels.flip();

		int id = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, id);

		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		Texture texture = new Texture(id, width, height);
		textureTable.put(name, texture);
		return texture;
	}

	public static Texture loadTexture(String name) {
		return loadTexture(name, "png");
	}
}
