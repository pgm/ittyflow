package sample.web.flow;

import sample.web.mockdb.CheckoutExecution;
import sample.web.mockdb.Hotel;
import sample.web.mockdb.Room;

public class Transitions implements ITransitions {
	public CheckoutState choseHotel(CheckoutExecution task, Hotel hotel) {
		task.setHotel(hotel);
		return CheckoutState.WAITING_FOR_ROOM_SELECTION;
	}
	
	public CheckoutState entered(CheckoutExecution task) {
		if(task.getHotel().getRooms().size() == 0) {
			return CheckoutState.NO_ROOM_AVAILIBLE;
		}
		
		return null;
	}

	public CheckoutState choseRoom(CheckoutExecution task, Room room) {
		task.setRoom(room);
		return CheckoutState.WAITING_FOR_BILLING_DETAILS;
	}
	public CheckoutState enteredBillingDetails(CheckoutExecution task, String billingDetails) {
		task.setBillingDetails(billingDetails);
		return CheckoutState.WAITING_FOR_CONFIRMATION;
	}
	public CheckoutState confirmed(CheckoutExecution task) {
		task.setConfirmed();
		return CheckoutState.RESERVATION_SUCCESSFUL;
	}

}
