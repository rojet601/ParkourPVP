package com.rojel.parkourpvp.data;

import org.bukkit.entity.Player;

import com.rojel.parkourpvp.ParkourPVP;

public class PlayerData {
	private Player player;
	private PlayerState state;
	private int points;
	private Room room;
	
	public PlayerData(Player player) {
		this.player = player;
		this.state = PlayerState.NOT_IN_GAME;
		this.points = 0;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PlayerState getState() {
		return state;
	}
	
	public void setState(PlayerState state) {
		this.state = state;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void addPoint() {
		points++;
	}
	
	public void resetPoints() {
		points = 0;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void joinRoom(Room room) {
		this.state = PlayerState.IN_GAME;
		this.points = 0;
		this.room = room;
		player.teleport(room.getLobby());
	}
	
	public void leaveRoom() {
		this.state = PlayerState.NOT_IN_GAME;
		this.points = 0;
		this.room = null;
		player.teleport(ParkourPVP.getLobby());
	}
}
