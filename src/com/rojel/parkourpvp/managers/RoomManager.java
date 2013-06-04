package com.rojel.parkourpvp.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.rojel.parkourpvp.ParkourPVP;
import com.rojel.parkourpvp.data.Room;
import com.rojel.parkourpvp.utilities.Serializer;
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
			roomYML.set(key, Serializer.serializeRoom(room));
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
				Room room = Serializer.deserializeRoom(roomYML.getConfigurationSection(key).getValues(false));
				addRoom(room);
			}
			
			ParkourPVP.getPlugin().getServer().getLogger().info("Loaded " + rooms.size() + " rooms from " + ROOM_FILE);
		}
	}
}
