package com.rojel.parkourpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ParkourPVPExecutor implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("§3/ppvp room [room] create §rCreates a new room with the name [room]");
			sender.sendMessage("§3/ppvp room [room] lobby §rSets the lobby of [room] to your current position");
			sender.sendMessage("§3/ppvp room [room] spawn §rSets the spawn of [room] to your current position");
			sender.sendMessage("§3/ppvp room [room] goal §rSets the goal of [room] to your current position");
			sender.sendMessage("§3/ppvp room [room] delete §rDeletes the room with the name [room]");
			
			sender.sendMessage("§3/ppvp rooms §rLists all rooms");
			
			sender.sendMessage("§3/ppvp lobby §rSets the room selection lobby to your current position");
			return true;
		}
		
		return false;
	}
}
