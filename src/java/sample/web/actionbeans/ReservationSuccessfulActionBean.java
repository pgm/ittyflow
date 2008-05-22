package sample.web.actionbeans;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import sample.web.flow.CheckoutState;

public class ReservationSuccessfulActionBean  extends WorkflowActionBean {
	@DefaultHandler
	public Resolution defaultHandler() {
		if(this.checkoutTask.getWaitState().equals(CheckoutState.WAITING_FOR_CONFIRMATION))
			return new ForwardResolution("/confirmation.jsp");
		else
			return new ForwardResolution("/reservationSuccessful.jsp");
	}
	
	public Resolution confirm() {
		signal().confirmed(null);
		
		return nextResolution();
	}
}
