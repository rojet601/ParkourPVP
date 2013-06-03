package com.rojel.parkourpvp.data;

import java.util.ArrayList;

import org.bukkit.Location;

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
		players.add(player);
		player.joinRoom(this);
		
		updateState();
	}
	
	public void leaveRoom(PlayerData player) {
		players.remove(player);
		player.leaveRoom();
		
		updateState();
	}
	
	public RoomState getState() {
		return state;
	}
	
	private void updateState() {
		if(state == RoomState.WAITING && getPlayerCount() == MAX_PLAYERS) {
			state = RoomState.RUNNING;
		}
	}
	
	public void playerScores(PlayerData player) {
		
	}
}
