package sample.web.flow;

import com.github.ittyflow.Workflow;

import sample.web.mockdb.CheckoutExecution;
import sample.web.mockdb.Hotel;
import sample.web.mockdb.Room;

public class CheckoutWorkflow extends Workflow<CheckoutState, ITransitions> {

	public CheckoutWorkflow() {
		super(CheckoutExecution.class, ITransitions.class, CheckoutState.values());
		
		addListener(CheckoutState.WAITING_FOR_HOTEL_SELECTION, new Transitions() {
			public CheckoutState choseHotel(CheckoutExecution task, Hotel hotel) {
				task.setHotel(hotel);
				return CheckoutState.WAITING_FOR_ROOM_SELECTION;
			}
		});

		addListener(CheckoutState.WAITING_FOR_ROOM_SELECTION, new Transitions() {
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
		});

		addListener(CheckoutState.WAITING_FOR_BILLING_DETAILS, new Transitions() {
			public CheckoutState enteredBillingDetails(CheckoutExecution task, String billingDetails) {
				task.setBillingDetails(billingDetails);
				return CheckoutState.WAITING_FOR_CONFIRMATION;
			}
		});
		
		addListener(CheckoutState.WAITING_FOR_CONFIRMATION, new Transitions() {
			public CheckoutState confirmed(CheckoutExecution task) {
				task.setConfirmed();
				return CheckoutState.RESERVATION_SUCCESSFUL;
			}
		});
	
		addTerminalState(CheckoutState.RESERVATION_SUCCESSFUL);
	}

//
//                       v----- search again --\	
// select venue -> dosearch -> showUserBestTicket -> waitingForBillingDetails -> done
//                   |
//                   +---> no tickets left
	
	
}
