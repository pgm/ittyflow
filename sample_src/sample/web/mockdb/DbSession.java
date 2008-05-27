package sample.web.mockdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbSession {
	static Map<String, Object> mockDb = new HashMap<String, Object>();
	static int nextId = 1;

	static {
		DbSession session = new DbSession();
		session.save(new Hotel("Ritz"));
		session.save(new Hotel("Day's Inn"));
		session.save(new Hotel("Waldorph"));
		
		for(Hotel h : session.getAllVenues()) {
			for(Room r : h.getRooms()) {
				session.save(r);
			}
		}
	}
	
	public static synchronized String getNextId() {
		String id = Integer.toString(nextId);
		nextId++;
		
		return id;
	}
	
	public void save(DbEntity entity) {
		if(entity.getId() == null) {
			entity.setId(getNextId());
		}
		
		mockDb.put(entity.getId(), entity);
	}
	
	public List<Hotel> getAllVenues() {
		List<Hotel> result = new ArrayList<Hotel>();
		for(Object o : mockDb.values()) {
			if(o instanceof Hotel) {
				result.add((Hotel)o);
			}
		}
		return result;
	}
	
	public <T> T get(Class<T> clazz, String id) {
		return clazz.cast(mockDb.get(id));
	}
}
