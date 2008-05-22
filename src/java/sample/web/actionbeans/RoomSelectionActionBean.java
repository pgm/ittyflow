package sample.web.actionbeans;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import sample.web.mockdb.Room;

public class RoomSelectionActionBean extends WorkflowActionBean {
	private String roomId;
	
	public List<Room> getRooms() {
		return checkoutTask.getHotel().getRooms();
	}
    
    public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

    @DefaultHandler
    public Resolution defaultHandler() {
    	return new ForwardResolution("/roomSelection.jsp");
    }
    
	public Resolution reserve() {
		Room room = dbSession.get(Room.class, roomId);
		signal().choseRoom(null, room);

		return nextResolution();
    }
}