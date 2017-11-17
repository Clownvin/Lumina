package com.clownvin.lumina.math;

public final class MathUtil {
	public static boolean inside(int x, int y, int x1, int y1, int x2, int y2) {
		return x1 <= x && x <= x2 && y1 <= y && y <= y2;
	}

	public static boolean inside(double x, double y, float x1, float y1, float x2, float y2) {
		return x1 <= x && x <= x2 && y1 <= y && y <= y2;
	}
}
