package com.rojel.parkourpvp.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;

import com.rojel.parkourpvp.ParkourPVP;
import com.rojel.parkourpvp.data.Room;

public class Serializer {
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
		map.put("void", room.getVoidLevel());
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
		
		double voidLevel = (double) map.get("void");
		
		Room room = new Room(name);
		room.setLobby(lobby);
		room.setGoal(goal);
		room.setVoidLevel(voidLevel);
		
		for(Location spawn : spawnList)
			room.addSpawn(spawn);
		
		return room;
	}
}
