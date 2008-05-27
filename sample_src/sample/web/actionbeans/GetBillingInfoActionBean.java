package sample.web.actionbeans;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

public class GetBillingInfoActionBean extends WorkflowActionBean {
	private String billingInfo;
	
    public void setBillingInfo(String billingInfo) {
    	this.billingInfo = billingInfo;
	}

    @DefaultHandler
    public Resolution defaultHandler() {
    	return new ForwardResolution("/billingDetails.jsp");
    }
    
	public Resolution submit() {
		signal().enteredBillingDetails(null, billingInfo);
		
		return nextResolution();
    }
}