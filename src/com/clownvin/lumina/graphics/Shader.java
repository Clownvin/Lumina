package com.clownvin.lumina.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import com.clownvin.lumina.Log;
import com.clownvin.lumina.res.ResourceManager;

public class Shader {
	private static final String SHADER_PATH = "shader/";
	
	private int program;
	private int vs;
	private int fs;
	
	public Shader(String name) {
		program = glCreateProgram();
		vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs, ResourceManager.readShader(SHADER_PATH+name+".vs"));
		glCompileShader(vs);
		if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
			Log.logE(glGetShaderInfoLog(vs)+"\n");
			System.exit(1);
		}
		fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs, ResourceManager.readShader(SHADER_PATH+name+".fs"));
		glCompileShader(fs);
		if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
			Log.logE(glGetShaderInfoLog(fs)+"\n");
			System.exit(1);
		}
		glAttachShader(program, vs);
		glAttachShader(program, fs);
		
		glBindAttribLocation(program, 0, "vertices");
		glBindAttribLocation(program, 1, "textures");
		
		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
			Log.logE(glGetProgramInfoLog(program)+"\n");
			System.exit(1);
		}
		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
			Log.logE(glGetProgramInfoLog(program)+"\n");
			System.exit(1);
		}
	}
	
	public void setUniform(String name, Object value) {
		Integer pointer = glGetUniformLocation(program, name);
		if (pointer == -1) {
			Log.logE("Failed to get uniform location...\n");
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
			Log.logE("No case for uniform type.\n");
		}
	}
	
	public void bind() {
		glUseProgram(program);
	}
}
