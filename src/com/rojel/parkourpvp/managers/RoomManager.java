package com.rojel.parkourpvp.managers;

import java.util.HashMap;

import com.rojel.parkourpvp.data.Room;


public class RoomManager {
	private static HashMap<String, Room> rooms = new HashMap<String, Room>();
	
	public static Room getRoom(String name) {
		return rooms.get(name);
	}
	
	public static void addRoom(Room room) {
		rooms.put(room.getName(), room);
	}
	
	public static void removeRoom(Room room) {
		rooms.remove(room.getName());
	}
}
