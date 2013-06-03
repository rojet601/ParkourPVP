package com.rojel.parkourpvp.data;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.rojel.parkourpvp.ParkourPVP;
import com.rojel.pluginsignapi.PluginSignAPI;

public class Room {
	public static final int MAX_PLAYERS = 4;
	
	private String name;
	private Location lobby;
	private Location spawn;
	private Location goal;
	private ArrayList<PlayerData> players;
	private RoomState state;
	
	public Room(String name) {
		this.name = name;
		this.players = new ArrayList<PlayerData>();
		this.state = RoomState.WAITING;
	}
	
	public String getName() {
		return name;
	}
	
	public Location getLobby() {
		return lobby;
	}
	
	public void setLobby(Location loc) {
		this.lobby = loc;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public void setSpawn(Location loc) {
		this.spawn = loc;
	}
	
	public Location getGoal() {
		return goal;
	}
	
	public void setGoal(Location loc) {
		this.goal = loc;
	}
	
	public int getPlayerCount() {
		return players.size();
	}
	
	public boolean isInRoom(PlayerData player) {
		for(PlayerData data : players)
			if(data == player)
				return true;
		
		return false;
	}
	
	public void joinRoom(PlayerData player) {
		sendMessage(player.getPlayer().getDisplayName() + " §3joined the room.");
		
		players.add(player);
		player.joinRoom(this);
		
		updateState();
	}
	
	public void leaveRoom(PlayerData player) {		
		players.remove(player);
		player.leaveRoom();
		
		sendMessage(player.getPlayer().getDisplayName() + " §3left the room.");
		
		updateState();
	}
	
	public RoomState getState() {
		return state;
	}
	
	private void updateState() {
		if(state == RoomState.WAITING && getPlayerCount() == MAX_PLAYERS) {
			state = RoomState.RUNNING;
			
			for(PlayerData player : players) {
				player.getPlayer().teleport(spawn);
			}
			
			sendMessage("§3The game has started. Try to reach the goal, but don't let the other knock you off.");
		} else if((state == RoomState.RUNNING && getWinner() != null) || (state == RoomState.RUNNING && getPlayerCount() <= 1)) {
			PlayerData winner;
			
			if(getWinner() != null)
				winner = getWinner();
			else
				winner = players.get(0);
			
			sendMessage(winner.getPlayer().getDisplayName() + " §3won the game.");
			
			state = RoomState.ENDING;
			
			for(PlayerData player : players) {
				player.resetPoints();
			}
			
			ParkourPVP.getPlugin().getServer().getScheduler().runTaskLater(ParkourPVP.getPlugin(), new BukkitRunnable() {
				@Override
				public void run() {
					state = RoomState.WAITING;
					for(PlayerData player : players)
						player.getPlayer().teleport(lobby);
					
					updateState();
				}
			}, 100);
		}
		
		PluginSignAPI.updateSigns();
	}
	
	public void playerScores(PlayerData player) {
		player.addPoint();
		sendMessage(player.getPlayer().getDisplayName() + " §3scored a point.");
		
		for(PlayerData data : players) {
			data.getPlayer().teleport(spawn);
		}
		
		updateState();
	}
	
	public PlayerData getWinner() {
		for(PlayerData player : players) {
			if(player.getPoints() >= 3)
				return player;
		}
		
		return null;
	}
	
	public void sendMessage(String msg) {
		for(PlayerData player : players)
			player.getPlayer().sendMessage(msg);
	}
	
	public boolean isJoinable() {
		if(lobby == null)
			return false;
		if(spawn == null)
			return false;
		if(goal == null)
			return false;
		if(getPlayerCount() == MAX_PLAYERS)
			return false;
		if(state != RoomState.WAITING)
			return false;
		
		return true;
	}
}
