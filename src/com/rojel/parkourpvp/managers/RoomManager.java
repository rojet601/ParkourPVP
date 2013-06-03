package com.rojel.parkourpvp.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rojel.parkourpvp.data.Room;


public class RoomManager {
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
	}
	
	public static void removeRoom(Room room) {
		rooms.remove(room.getName());
	}
	
	public static void removeRoom(String name) {
		rooms.remove(name);
	}
	
	public static List<Room> getRooms() {
		ArrayList<Room> roomList = new ArrayList<Room>();
		for(Room room : rooms.values()) {
			roomList.add(room);
		}
		
		return roomList;
	}
	
	public static void saveToFile() {
		
	}
	
	public static void loadFromFile() {
		
	}
}
