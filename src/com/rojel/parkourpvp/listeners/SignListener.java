package com.rojel.parkourpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rojel.pluginsignapi.events.PluginSignClickEvent;
import com.rojel.pluginsignapi.events.PluginSignUpdateEvent;

public class SignListener implements Listener {
	@EventHandler
	public void onSignUpdate(PluginSignUpdateEvent event) {
		if(event.getPlugin().equalsIgnoreCase("parkourpvp")) {
			
		}
	}
	
	@EventHandler
	public void onSignClick(PluginSignClickEvent event) {
		if(event.getPlugin().equalsIgnoreCase("parkourpvp")) {
			
		}
	}
}
