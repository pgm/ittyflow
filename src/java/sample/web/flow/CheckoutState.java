package sample.web.flow;

import net.sourceforge.stripes.action.ActionBean;
import sample.web.actionbeans.GetBillingInfoActionBean;
import sample.web.actionbeans.HotelSelectionActionBean;
import sample.web.actionbeans.ReservationSuccessfulActionBean;
import sample.web.actionbeans.RoomSelectionActionBean;

public enum CheckoutState {
	WAITING_FOR_HOTEL_SELECTION(HotelSelectionActionBean.class),
	WAITING_FOR_ROOM_SELECTION(RoomSelectionActionBean.class),
	NO_ROOM_AVAILIBLE(null),
	WAITING_FOR_BILLING_DETAILS(GetBillingInfoActionBean.class),
	RESERVATION_SUCCESSFUL(ReservationSuccessfulActionBean.class),
	WAITING_FOR_CONFIRMATION(ReservationSuccessfulActionBean.class);
	
	private final Class<? extends ActionBean> actionBean;
	
	public Class<? extends ActionBean> getActionBean() {
		return actionBean;
	}
	
	private CheckoutState(Class<? extends ActionBean> bean) {
		actionBean = bean;
	}
}
