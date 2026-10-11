package com.swingspeed;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SwingConfig {
	public static final float MIN_SPEED = 0.1f;
	public static final float MAX_SPEED = 3.0f;

	public static volatile boolean enabled = true;
	public static volatile float speed = 0.5f;

	private SwingConfig() {}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("swingspeed.properties");
	}

	public static float clamp(float v) {
		return Math.max(MIN_SPEED, Math.min(MAX_SPEED, v));
	}

	public static void load() {
		Path f = file();
		if (!Files.exists(f)) {
			save();
			return;
		}
		try (InputStream in = Files.newInputStream(f)) {
			Properties p = new Properties();
			p.load(in);
			enabled = Boolean.parseBoolean(p.getProperty("enabled", "true"));
			speed = clamp(Float.parseFloat(p.getProperty("speed", "0.5")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try (OutputStream out = Files.newOutputStream(file())) {
			Properties p = new Properties();
			p.setProperty("enabled", Boolean.toString(enabled));
			p.setProperty("speed", Float.toString(speed));
			p.store(out, "Swing Speed settings");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
