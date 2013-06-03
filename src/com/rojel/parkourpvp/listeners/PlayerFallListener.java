package com.rojel.parkourpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.ChatColor

import com.rojel.parkourpvp.data.PlayerData;
import com.rojel.parkourpvp.data.PlayerState;
import com.rojel.parkourpvp.managers.PlayerManager;

public class PlayerFallListener implements Listener {
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		PlayerData player = PlayerManager.getData(event.getPlayer());
		
		if(player.getState() == PlayerState.IN_GAME && player.getPlayer().getLocation().getY() < 0) {
			player.getPlayer().teleport(player.getRoom().getSpawn());
			player.getPlayer().sendMessage(ChatColor.DARK_AQUA + "You fell off the world. Try not to do that too often.");
		}
	}
}
