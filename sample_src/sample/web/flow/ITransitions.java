package sample.web.flow;

import sample.web.mockdb.CheckoutExecution;
import sample.web.mockdb.Hotel;
import sample.web.mockdb.Room;

public interface ITransitions {
	public CheckoutState choseHotel(CheckoutExecution task, Hotel hotel) ;
	public CheckoutState entered(CheckoutExecution task) ;
	public CheckoutState choseRoom(CheckoutExecution task, Room room);
	public CheckoutState enteredBillingDetails(CheckoutExecution task, String billingDetails) ;
	public CheckoutState confirmed(CheckoutExecution task) ;

}