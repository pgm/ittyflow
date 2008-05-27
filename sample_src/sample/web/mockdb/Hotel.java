package sample.web.mockdb;

import java.util.ArrayList;
import java.util.List;

public class Hotel extends DbEntity {
	String description;
	List<Room> rooms = new ArrayList<Room>();
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public String getDescription() {
		return description;
	}

	public Hotel(String description) {
		super();
		this.description = description;
		rooms.add(new Room("Luxury suite"));
		rooms.add(new Room("Economy suite"));
	}

}
