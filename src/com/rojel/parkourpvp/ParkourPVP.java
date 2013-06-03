package com.rojel.parkourpvp;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.rojel.parkourpvp.commands.ParkourPVPExecutor;
import com.rojel.parkourpvp.listeners.GoalListener;
import com.rojel.parkourpvp.listeners.PlayerListener;
import com.rojel.parkourpvp.listeners.SignListener;
import com.rojel.parkourpvp.managers.PlayerManager;
import com.rojel.parkourpvp.managers.RoomManager;

public class ParkourPVP extends JavaPlugin {
	private static ParkourPVP plugin;
	private static Location lobby;
	
	public void onEnable() {
		plugin = this;
		lobby = new Location(getServer().getWorlds().get(0), 0, 100, 0);
		
		PlayerManager.reset();
		RoomManager.loadFromFile();
		
		getServer().getPluginManager().registerEvents(new SignListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new GoalListener(), this);
		
		getCommand("ppvp").setExecutor(new ParkourPVPExecutor());
	}
	
	public void onDisable() {
		RoomManager.saveToFile();
	}
	
	public static ParkourPVP getPlugin() {
		return plugin;
	}
	
	public static Location getLobby() {
		return lobby;
	}
	
	public static void setLobby(Location loc) {
		lobby = loc;
	}
}
