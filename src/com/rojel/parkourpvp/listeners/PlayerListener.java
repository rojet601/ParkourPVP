package com.rojel.parkourpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rojel.parkourpvp.data.PlayerData;
import com.rojel.parkourpvp.managers.PlayerManager;

public class PlayerListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PlayerData data = new PlayerData(event.getPlayer());
		PlayerManager.registerPlayer(data);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PlayerManager.unregisterPlayer(PlayerManager.getData(event.getPlayer()));
	}
}
