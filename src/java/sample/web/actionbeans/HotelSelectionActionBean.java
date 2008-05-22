package sample.web.actionbeans;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import sample.web.mockdb.Hotel;

public class HotelSelectionActionBean extends WorkflowActionBean {
	private String hotelId;

	public List<Hotel> getHotels() {
    	return dbSession.getAllVenues();
    }

    @DefaultHandler
    public Resolution defaultHandler() {
    	return new ForwardResolution("/hotelSelection.jsp");
    }
	
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	
	public Resolution selectHotel() {
		Hotel hotel = dbSession.get(Hotel.class, hotelId);
		signal().choseHotel(null, hotel);

		return nextResolution();
	}
}
