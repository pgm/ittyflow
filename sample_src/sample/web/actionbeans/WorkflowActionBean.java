package sample.web.actionbeans;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import sample.web.flow.CheckoutWorkflow;
import sample.web.flow.ITransitions;
import sample.web.infrastructure.Container;
import sample.web.infrastructure.Inject;
import sample.web.mockdb.CheckoutExecution;
import sample.web.mockdb.DbSession;

public class WorkflowActionBean implements ActionBean  {
	protected CheckoutWorkflow checkoutWorkflow;
	protected CheckoutExecution checkoutTask;
	protected DbSession dbSession;
	ActionBeanContext actionBeanContext;
	
	@Inject
	public void setCheckoutWorkflow(CheckoutWorkflow checkoutWorkflow) {
		this.checkoutWorkflow = checkoutWorkflow;
	}
	
	@Inject
	public void setDbSession(DbSession dbSession) {
		this.dbSession = dbSession;
	}
	
	public void setCheckoutTask(CheckoutExecution checkoutTask) {
		this.checkoutTask = checkoutTask;
	}
	
	public ITransitions signal() {
		return this.checkoutWorkflow.signal(checkoutTask);
	}
	
	public static Resolution redirectToTask(CheckoutExecution task) {
		RedirectResolution resolution = new RedirectResolution(task.getWaitState().getActionBean());
		
		resolution.addParameter("taskId", task.getId());
		
		return resolution;
	}

	public ActionBeanContext getContext() {
		return this.actionBeanContext;
	}

	public void setContext(ActionBeanContext arg0) {
		this.actionBeanContext = arg0;
	}
	
	public Resolution nextResolution() {
		return redirectToTask(checkoutTask);
	}

	private String taskId;
	
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String id) {
		taskId = id;
	}
	
	public CheckoutExecution getTask() { 
		return this.checkoutTask;
	}
	
    @Before
    public void injectBeforeExecute() {
    	Container.getContainer(getContext().getServletContext()).injectDependancies(this);
    	
    	// not sure why but setTaskId doesn't seem to be called by stripes...  so do it ourselves
//    	String taskId = this.getContext().getRequest().getParameter("taskId");
 //   	setTaskId(taskId);

		this.checkoutTask = dbSession.get(CheckoutExecution.class, taskId);
    }
    
}
