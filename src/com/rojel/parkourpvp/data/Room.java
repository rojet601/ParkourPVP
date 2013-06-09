package com.rojel.parkourpvp.data;

import java.util.ArrayList;
import java.util.Iterator;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import com.rojel.parkourpvp.ParkourPVP;
import com.rojel.pluginsignapi.PluginSignAPI;

public class Room {
	public static final int MAX_PLAYERS = 4;
	public static final int MIN_PLAYERS = 2;
	public static final int GAME_TIME = 300;
	public static final int LOBBY_TIME = 30;
	public static final int ENDING_TIME_TICKS = 60;
	
	private String name;
	private Location lobby;
	private ArrayList<Location> spawns;
	private Location goal;
	private ArrayList<PlayerData> players;
	private RoomState state;
	private int waitingCounter;
	private int gameCounter;
	private int spawnIndex;
	private double voidLevel;
	
	public Room(String name) {
		this.name = name;
		this.players = new ArrayList<PlayerData>();
		this.spawns = new ArrayList<Location>();
		this.state = RoomState.WAITING;
		this.waitingCounter = LOBBY_TIME;
		this.gameCounter = GAME_TIME;
		voidLevel = 50;
		
		ParkourPVP.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(ParkourPVP.getPlugin(), new BukkitRunnable() {
			@Override
			public void run() {
				if(state == RoomState.WAITING && getPlayerCount() >= MIN_PLAYERS) {
					waitingCounter--;
					
					updateState();
				} else if(state == RoomState.RUNNING) {
					gameCounter--;
					if(gameCounter % 60 == 0)
						sendMessage((gameCounter / 60) + " §3min left.");
					
					updateState();
				}
			}
		}, 0, 20);
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
		Location spawn = spawns.get(spawnIndex);
		spawnIndex++;
		if(spawnIndex >= spawns.size())
			spawnIndex = 0;
		
		return spawn;
	}
	
	public void addSpawn(Location loc) {
		this.spawns.add(loc);
	}
	
	public int getSpawnCount() {
		return spawns.size();
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
	
	public int getWaitingCounter() {
		return waitingCounter;
	}
	
	public int getGameCounter() {
		return gameCounter;
	}
	
	public double getVoidLevel() {
		return voidLevel;
	}
	
	public void setVoidLevel(double voidLevel) {
		this.voidLevel = voidLevel;
	}
	
	public boolean isInRoom(PlayerData player) {
		for(PlayerData data : players)
			if(data == player)
				return true;
		
		return false;
	}
	
	public void joinRoom(PlayerData player) {
		sendMessage(player.getPlayer().getDisplayName() + " §3joined the game.");
		
		players.add(player);
		player.joinRoom(this);
		
		updateState();
	}
	
	public void leaveRoom(PlayerData player) {		
		players.remove(player);
		player.leaveRoom();
		
		sendMessage(player.getPlayer().getDisplayName() + " §3left the game.");
		
		updateState();
	}
	
	public RoomState getState() {
		return state;
	}
	
	private void updateState() {
		if(state == RoomState.WAITING && getPlayerCount() < MIN_PLAYERS) {
			waitingCounter = LOBBY_TIME;
		} else if(state == RoomState.WAITING && waitingCounter <= 0) {
			state = RoomState.RUNNING;
			waitingCounter = LOBBY_TIME;
			
			for(PlayerData player : players) {
				player.getPlayer().teleport(getSpawn());
			}
			
			sendMessage("§3The game has started. Try to reach the goal, but don't let the others knock you off.");
		} else if(state == RoomState.RUNNING && (getWinner() != null || getPlayerCount() <= 1)) {
			PlayerData winner;
			
			if(getWinner() != null)
				winner = getWinner();
			else
				winner = players.get(0);
			
			playerWins(winner);
			
			endGame();
		} else if(state == RoomState.RUNNING && getGameCounter() <= 0) {			
			if(getPlayerWithMostPoints() == null)
				sendMessage("§3Time has run out and no one won.");
			else {
				PlayerData winner = getPlayerWithMostPoints();
				sendMessage("§3Time has run out but §r" + winner.getPlayer().getDisplayName() + " §3was the only one to score a single point.");
				playerWins(winner);
			}
			
			endGame();
		}
		
		PluginSignAPI.updateSigns();
	}
	
	public void playerScores(PlayerData player) {
		player.addPoint();
		if(player.getPoints() == 1)
			sendMessage(player.getPlayer().getDisplayName() + " §3has scored, don't let them score again!");
		
		for(PlayerData data : players) {
			data.getPlayer().teleport(getSpawn());
		}
		
		updateState();
	}
	
	private PlayerData getWinner() {
		for(PlayerData player : players) {
			if(player.getPoints() >= 2)
				return player;
		}
		
		return null;
	}
	
	private PlayerData getPlayerWithMostPoints() {
		PlayerData candidate = null;;
		
		for(PlayerData player : players) {
			if(player.getPoints() == 1)
				if(candidate == null)
					candidate = player;
				else
					return null;
		}
		
		return candidate;
	}
	
	public void sendMessage(String msg) {
		for(PlayerData player : players)
			player.getPlayer().sendMessage(msg);
	}
	
	public boolean isJoinable() {
		if(lobby == null)
			return false;
		if(spawns.size() == 0)
			return false;
		if(goal == null)
			return false;
		if(getPlayerCount() == MAX_PLAYERS)
			return false;
		if(state != RoomState.WAITING)
			return false;
		
		return true;
	}
	
	private void playerWins(PlayerData player) {
		sendMessage(player.getPlayer().getDisplayName() + " §3won the game.");
		
		if(ParkourPVP.getPlugin().getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> economyProvider = ParkourPVP.getPlugin().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			Economy economy = economyProvider.getProvider();
			
			if(economy != null && economy.hasAccount(player.getPlayer().getName())) {
				economy.depositPlayer(player.getPlayer().getName(), 3);
				player.getPlayer().sendMessage("§3You won the round and earned 3 coins.");
			}
		}
	}
	
	private void endGame() {
		gameCounter = GAME_TIME;
		state = RoomState.ENDING;
		
		for(PlayerData player : players)
			player.clearItems();
		
		ParkourPVP.getPlugin().getServer().getScheduler().runTaskLater(ParkourPVP.getPlugin(), new BukkitRunnable() {
			@Override
			public void run() {
				state = RoomState.WAITING;
				Iterator<PlayerData> iterator = players.iterator();
				
				while(iterator.hasNext()) {
					PlayerData player = iterator.next();
					iterator.remove();
					player.leaveRoom();
				}
				
				updateState();
			}
		}, ENDING_TIME_TICKS);
	}
}
