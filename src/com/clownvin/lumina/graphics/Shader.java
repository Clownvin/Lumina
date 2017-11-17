package com.clownvin.lumina.graphics;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import com.clownvin.lumina.res.ResourceManager;

public class Shader {
	private static final String SHADER_PATH = "shader/";

	private int program;
	private int vs;
	private int fs;

	public Shader(String name) {
		program = glCreateProgram();
		vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs, ResourceManager.readShader(SHADER_PATH + name + ".vs"));
		glCompileShader(vs);
		if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(vs));
			System.exit(1);
		}
		fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs, ResourceManager.readShader(SHADER_PATH + name + ".fs"));
		glCompileShader(fs);
		if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(fs));
			System.exit(1);
		}
		glAttachShader(program, vs);
		glAttachShader(program, fs);

		glBindAttribLocation(program, 0, "vertices");
		glBindAttribLocation(program, 1, "textures");

		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
	}

	public void bind() {
		glUseProgram(program);
	}

	public void setUniform(String name, Object value) {
		Integer pointer = glGetUniformLocation(program, name);
		if (pointer == -1) {
			System.err.println("Failed to get uniform location...");
			System.exit(1);
		}
		if (value instanceof Integer) {
			glUniform1i(pointer, (int) value);
		} else if (value instanceof Float) {
			glUniform1f(pointer, (float) value);
		} else if (value instanceof Matrix4f) {
			FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
			((Matrix4f) value).get(buffer);
			glUniformMatrix4fv(pointer, false, buffer);
		} else {
			System.err.println("No case for uniform type.");
		}
	}
}
