package com.rojel.parkourpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.rojel.parkourpvp.data.PlayerData;
import com.rojel.parkourpvp.data.PlayerState;
import com.rojel.parkourpvp.data.RoomState;
import com.rojel.parkourpvp.managers.PlayerManager;

public class GoalListener implements Listener {
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		PlayerData player = PlayerManager.getData(event.getPlayer());
		
		if(player.getState() == PlayerState.IN_GAME) {
			if(player.getRoom().getState() == RoomState.RUNNING && player.getPlayer().getLocation().distance(player.getRoom().getGoal()) < 1)
				player.getRoom().playerScores(player);
		}
	}
}
