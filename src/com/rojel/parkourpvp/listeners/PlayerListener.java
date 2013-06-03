package com.rojel.parkourpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rojel.parkourpvp.data.PlayerData;
import com.rojel.parkourpvp.data.PlayerState;
import com.rojel.parkourpvp.managers.PlayerManager;

public class PlayerListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PlayerManager.registerPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PlayerData data = PlayerManager.getData(event.getPlayer());
		
		if(data.getState() != PlayerState.NOT_IN_GAME) {
			data.getRoom().leaveRoom(data);
		}
		
		PlayerManager.unregisterPlayer(event.getPlayer());
	}
}
