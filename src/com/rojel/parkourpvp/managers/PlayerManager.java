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
	
	public static void registerPlayer(PlayerData player) {
		players.put(player.getPlayer().getName(), player);
	}
	
	public static void unregisterPlayer(PlayerData player) {
		players.remove(player.getPlayer().getName());
	}
	
	public static void reset() {
		players.clear();
		for(Player player : ParkourPVP.getPlugin().getServer().getOnlinePlayers()) {
			PlayerData data = new PlayerData(player);
			PlayerManager.registerPlayer(data);
		}
	}
}
