package com.rojel.parkourpvp;

import org.bukkit.plugin.java.JavaPlugin;

public class ParkourPVP extends JavaPlugin {
	private static ParkourPVP plugin;
	
	public void onEnable() {
		plugin = this;
	}
	
	public static ParkourPVP getPlugin() {
		return plugin;
	}
}
