package com.rojel.parkourpvp.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.rojel.parkourpvp.ParkourPVP;
import com.rojel.parkourpvp.data.Room;
import com.rojel.pluginsignapi.PluginSignAPI;


public class RoomManager {
	public static final String ROOM_FILE = "rooms.yml";
	
	private static HashMap<String, Room> rooms = new HashMap<String, Room>();
	
	public static Room getRoom(String name) {
		return rooms.get(name);
	}
	
	public static void createRoom(String name) {
		Room room = new Room(name);
		
		addRoom(room);
	}
	
	public static void addRoom(Room room) {
		rooms.put(room.getName(), room);
		
		PluginSignAPI.updateSigns();
	}
	
	public static void removeRoom(Room room) {
		rooms.remove(room.getName());
		
		PluginSignAPI.updateSigns();
	}
	
	public static void removeRoom(String name) {
		rooms.remove(name);
		
		PluginSignAPI.updateSigns();
	}
	
	public static List<Room> getRooms() {
		ArrayList<Room> roomList = new ArrayList<Room>();
		for(Room room : rooms.values()) {
			roomList.add(room);
		}
		
		return roomList;
	}
	
	public static void saveToFile() {
		String dataPath = ParkourPVP.getPlugin().getDataFolder().getPath();
		File dataDirectory = new File(dataPath);
		if(!dataDirectory.exists())
			dataDirectory.mkdir();
		
		String filePath = dataPath + File.separator + ROOM_FILE;
		File roomFile = new File(filePath);
		
		try {
			roomFile.delete();
			roomFile.createNewFile();
		} catch(IOException e) {
			ParkourPVP.getPlugin().getServer().getLogger().info("An error occured during the creation of " + ROOM_FILE);
			e.printStackTrace();
		}
		
		FileConfiguration roomYML = YamlConfiguration.loadConfiguration(roomFile);
		
		for(String key : rooms.keySet()) {
			Room room = rooms.get(key);
			
			roomYML.createSection(key);
			roomYML.set(key, serializeRoom(room));
		}
		
		try {
			roomYML.save(roomFile);
		} catch(IOException e) {
			ParkourPVP.getPlugin().getServer().getLogger().info("An error occured while saving " + ROOM_FILE);
			e.printStackTrace();
		}
		
		ParkourPVP.getPlugin().getLogger().info("Saved " + rooms.size() + " rooms to " + ROOM_FILE);
	}
	
	public static void loadFromFile() {
		rooms = new HashMap<String, Room>();
		
		String dataPath = ParkourPVP.getPlugin().getDataFolder().getPath();
		File dataDirectory = new File(dataPath);
		if(!dataDirectory.exists())
			dataDirectory.mkdir();
		
		String filePath = dataPath + File.separator + ROOM_FILE;
		File roomFile = new File(filePath);
		
		if(!roomFile.exists()) {
			ParkourPVP.getPlugin().getServer().getLogger().info("No " + ROOM_FILE + " found. Creating it at " + filePath);
			try {
				roomFile.createNewFile();
			} catch(IOException e) {
				ParkourPVP.getPlugin().getServer().getLogger().info("An error occured during the creation of " + ROOM_FILE);
				e.printStackTrace();
			}
		} else {
			FileConfiguration roomYML = YamlConfiguration.loadConfiguration(roomFile);
			
			for(String key : roomYML.getKeys(false)) {				
				Room room = deserializeRoom(roomYML.getConfigurationSection(key).getValues(false));
				addRoom(room);
			}
			
			ParkourPVP.getPlugin().getServer().getLogger().info("Loaded " + rooms.size() + " rooms from " + ROOM_FILE);
		}
	}
	
	public static Map<String, Object> serializeLocation(Location loc) {
		if(loc != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("world", loc.getWorld().getName());
			map.put("x", loc.getX());
			map.put("y", loc.getY());
			map.put("z", loc.getZ());
			map.put("pitch", loc.getPitch());
			map.put("yaw", loc.getYaw());
			
			return map;
		} else
			return null;
	}
	
	public static Location deserializeLocation(Map<String, Object> map) {
		if(map != null) {
			World world = ParkourPVP.getPlugin().getServer().getWorld((String) map.get("world"));
			double x = (double) map.get("x");
			double y = (double) map.get("y");
			double z = (double) map.get("z");
			String pitchString = "" + (double) map.get("pitch");
			String yawString = "" + (double) map.get("yaw");
			float pitch = Float.parseFloat(pitchString);
			float yaw = Float.parseFloat(yawString);
			
			Location loc = new Location(world, x, y, z);
			loc.setPitch(pitch);
			loc.setYaw(yaw);
			
			return loc;
		} else
			return null;
	}
	
	public static Map<String, Object> serializeRoom(Room room) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", room.getName());
		map.put("lobby", serializeLocation(room.getLobby()));
		map.put("goal", serializeLocation(room.getGoal()));
		
		HashMap<String, Object> spawns = new HashMap<String, Object>();
		for(int i = 0; i < room.getSpawnCount(); i++) {
			Location spawn = room.getSpawn();
			spawns.put("spawn" + i, serializeLocation(spawn));
		}
		
		map.put("spawns", spawns);
		
		return map;
	}
	
	public static Room deserializeRoom(Map<String, Object> map) {
		String name = (String) map.get("name");
		MemorySection lobbySection = (MemorySection) map.get("lobby");
		Location lobby = null;
		if(lobbySection != null)
			lobby = deserializeLocation((lobbySection.getValues(false)));
		
		MemorySection goalSection = (MemorySection) map.get("goal");
		Location goal = null;
		if(goalSection != null)
			goal = deserializeLocation((goalSection.getValues(false)));
		
		MemorySection spawnSection = (MemorySection) map.get("spawns");
		Map<String, Object> spawnMap = spawnSection.getValues(false);
		ArrayList<Location> spawnList = new ArrayList<Location>();
		
		for(Object singleSpawnSection : spawnMap.values()) {
			Map<String, Object> singleSpawnMap = ((MemorySection) singleSpawnSection).getValues(false);
			Location singleSpawn = deserializeLocation(singleSpawnMap);
			spawnList.add(singleSpawn);
		}
		
		Room room = new Room(name);
		room.setLobby(lobby);
		room.setGoal(goal);
		
		for(Location spawn : spawnList)
			room.addSpawn(spawn);
		
		return room;
	}
}
