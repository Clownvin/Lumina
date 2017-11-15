package com.clownvin.lumina;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class Log {
	private static BufferedWriter writer;
	private static boolean debug = false;

	static {
		try {
			writer = new BufferedWriter(new FileWriter("./log.txt"));
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setDebug(boolean debug) {
		Log.debug = debug;
		if (debug)
			logD("Debug output enabled.\n");
		else
			log("Debug output disabled.\n");
	}

	public static void log(String message) {
		System.out.print(message);
		try {
			writer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logE(String message) {
		System.err.print(message);
		try {
			writer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logD(String message) {
		if (!debug)
			return;
		message = "[DEBUG] " + message;
		System.out.print(message);
		try {
			writer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
