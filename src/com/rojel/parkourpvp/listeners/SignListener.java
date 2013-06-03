package com.rojel.parkourpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rojel.parkourpvp.data.Room;
import com.rojel.parkourpvp.data.RoomState;
import com.rojel.parkourpvp.managers.RoomManager;
import com.rojel.pluginsignapi.events.PluginSignClickEvent;
import com.rojel.pluginsignapi.events.PluginSignUpdateEvent;

public class SignListener implements Listener {
	@EventHandler
	public void onSignUpdate(PluginSignUpdateEvent event) {
		if(event.getPlugin().equalsIgnoreCase("ppvp")) {
			if(event.getPurpose().equalsIgnoreCase("join")) {
				Room room = RoomManager.getRoom(event.getData());
				if(room != null) {
					if(room.getState() == RoomState.WAITING)
						event.setLine(0, "§aJoin room");
					else if(room.getState() == RoomState.ENDING)
						event.setLine(0, "§eEnding");
					else if(room.getState() == RoomState.RUNNING)
						event.setLine(0, "§cRunning");
					
					event.setLine(1, room.getName());
					event.setLine(2, "§o" + room.getPlayerCount() + "/" + Room.MAX_PLAYERS + " players");
				} else {
					event.setLine(0, event.getData());
					event.setLine(1, "§cDOES NOT");
					event.setLine(2, "§cEXIST");
				}
			} else if(event.getPurpose().equalsIgnoreCase("leave")) {
				Room room = RoomManager.getRoom(event.getData());
				if(room != null) {
					event.setLine(1, "§aLeave");
					event.setLine(2, room.getName());
				} else {
					event.setLine(0, event.getData());
					event.setLine(1, "§cDOES NOT");
					event.setLine(2, "§cEXIST");
				}
			}
		}
	}
	
	@EventHandler
	public void onSignClick(PluginSignClickEvent event) {
		if(event.getPlugin().equalsIgnoreCase("ppvp")) {
			
		}
	}
}
