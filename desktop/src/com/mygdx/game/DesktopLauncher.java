package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Drop");
		config.setWindowedMode(800,480); //landscape mode on Android
		config.useVsync(true);
		//config.setResizable(false);
		config.setForegroundFPS(60);
		config.setTitle("Wrangler Giddy Up");
		new Lwjgl3Application(new WranglerGiddyUp(), config);
	}
}
