package sample.web.mockdb;

import sample.web.flow.CheckoutState;
import flow.HasWaitState;

public class CheckoutExecution extends DbEntity implements HasWaitState<CheckoutState>{

	CheckoutState waitState = CheckoutState.WAITING_FOR_HOTEL_SELECTION;
	Hotel hotel = null;
	Room room = null;
	boolean confirmed = false;
	
	public CheckoutState getWaitState() {
		return waitState;
	}

	public void setWaitState(CheckoutState state) {
		this.waitState = state;
	}

	
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
	public Hotel getHotel() {
		return hotel;
	}
	
	public void setRoom(Room room) {
		this.room = room;
		
	}
	
	String billingDetails; 
	
	public void setBillingDetails(String details) {
		this.billingDetails = details;
	}
	
	public void setConfirmed() {
		confirmed = true;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Room getRoom() {
		return room;
	}

	public String getBillingDetails() {
		return billingDetails;
	}


}
