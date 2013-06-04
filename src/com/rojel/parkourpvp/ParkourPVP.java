package com.rojel.parkourpvp;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.rojel.parkourpvp.commands.ParkourPVPExecutor;
import com.rojel.parkourpvp.listeners.DamageListener;
import com.rojel.parkourpvp.listeners.GoalListener;
import com.rojel.parkourpvp.listeners.PlayerFallListener;
import com.rojel.parkourpvp.listeners.PlayerListener;
import com.rojel.parkourpvp.listeners.SignListener;
import com.rojel.parkourpvp.managers.PlayerManager;
import com.rojel.parkourpvp.managers.RoomManager;
import com.rojel.parkourpvp.utilities.Serializer;
import com.rojel.pluginsignapi.PluginSignAPI;

public class ParkourPVP extends JavaPlugin {
	private static ParkourPVP plugin;
	private static Location lobby;
	
	public void onEnable() {
		plugin = this;
		
		ConfigurationSection lobbySection = getConfig().getConfigurationSection("lobby");
		if(lobbySection != null)
			lobby = Serializer.deserializeLocation(lobbySection.getValues(false));
		else {
			lobby = new Location(getServer().getWorlds().get(0), 0, 100, 0);
			getServer().getLogger().info("No lobby for ParkourPVP found. Using 0;100;0 in " + lobby.getWorld().getName() + " but you should set the location with /ppvp lobby");
		}
		
		PlayerManager.reset();
		RoomManager.loadFromFile();
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new SignListener(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new GoalListener(), this);
		pm.registerEvents(new PlayerFallListener(), this);
		pm.registerEvents(new DamageListener(), this);
		
		getCommand("ppvp").setExecutor(new ParkourPVPExecutor());
		
		PluginSignAPI.updateSigns();
	}
	
	public void onDisable() {
		RoomManager.saveToFile();
		getConfig().createSection("lobby");
		getConfig().set("lobby", Serializer.serializeLocation(lobby));
		saveConfig();
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
