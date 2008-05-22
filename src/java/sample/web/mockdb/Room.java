package sample.web.mockdb;

public class Room extends DbEntity {
	String description;
	public Room(String s) {
		this.description = s;
	}
	
	public String getDescription() {
		return description;
	}
}
