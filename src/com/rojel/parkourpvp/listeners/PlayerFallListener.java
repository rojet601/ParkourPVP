package com.rojel.parkourpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.rojel.parkourpvp.data.PlayerData;
import com.rojel.parkourpvp.data.PlayerState;
import com.rojel.parkourpvp.data.RoomState;
import com.rojel.parkourpvp.managers.PlayerManager;

public class PlayerFallListener implements Listener {
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		PlayerData player = PlayerManager.getData(event.getPlayer());
		
		if(player.getState() == PlayerState.IN_GAME && player.getPlayer().getLocation().getY() < player.getRoom().getVoidLevel() && player.getRoom().getState() != RoomState.WAITING) {
			player.getPlayer().teleport(player.getRoom().getSpawn());
			player.getPlayer().sendMessage("�3You fell out of the world. Try to not do that too often. You have 5 seconds protection.");
			player.setProtected();
		}
	}
}
