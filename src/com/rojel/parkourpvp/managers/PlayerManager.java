package com.rojel.parkourpvp.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.rojel.parkourpvp.ParkourPVP;
import com.rojel.parkourpvp.data.PlayerData;

public class PlayerManager {
	private static HashMap<String, PlayerData> players = new HashMap<String, PlayerData>();
	
	public static PlayerData getData(Player player) {
		return players.get(player.getName());
	}
	
	public static void registerPlayer(Player player) {
		players.put(player.getName(), new PlayerData(player));
	}
	
	public static void unregisterPlayer(Player player) {
		players.remove(player.getName());
	}
	
	public static void reset() {
		players.clear();
		for(Player player : ParkourPVP.getPlugin().getServer().getOnlinePlayers()) {
			PlayerManager.registerPlayer(player);
		}
	}
}
