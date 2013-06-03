package com.rojel.parkourpvp;

import org.bukkit.plugin.java.JavaPlugin;

import com.rojel.parkourpvp.listeners.PlayerListener;
import com.rojel.parkourpvp.listeners.SignListener;
import com.rojel.parkourpvp.managers.PlayerManager;
import com.rojel.parkourpvp.managers.RoomManager;

public class ParkourPVP extends JavaPlugin {
	private static ParkourPVP plugin;
	
	public void onEnable() {
		plugin = this;
		
		PlayerManager.reset();
		RoomManager.loadFromFile();
		
		getServer().getPluginManager().registerEvents(new SignListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}
	
	public void onDisable() {
		RoomManager.saveToFile();
	}
	
	public static ParkourPVP getPlugin() {
		return plugin;
	}
}
