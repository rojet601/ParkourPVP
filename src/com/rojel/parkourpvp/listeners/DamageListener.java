package com.rojel.parkourpvp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.rojel.parkourpvp.data.PlayerData;
import com.rojel.parkourpvp.data.PlayerState;
import com.rojel.parkourpvp.data.RoomState;
import com.rojel.parkourpvp.managers.PlayerManager;

public class DamageListener implements Listener {
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			PlayerData player = PlayerManager.getData((Player) event.getEntity());
			
			if(player.getState() == PlayerState.IN_GAME) {
				event.setDamage(0);
				
				if(player.getRoom().getState() != RoomState.RUNNING || player.isProtected())
					event.setCancelled(true);
			}
		}
	}
}
