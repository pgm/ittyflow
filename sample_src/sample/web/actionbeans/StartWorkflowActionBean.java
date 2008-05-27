package sample.web.actionbeans;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import sample.web.infrastructure.Container;
import sample.web.infrastructure.Inject;
import sample.web.mockdb.CheckoutExecution;
import sample.web.mockdb.DbSession;

public class StartWorkflowActionBean implements ActionBean {
    private ActionBeanContext context;
    private DbSession dbSession;
    
    public ActionBeanContext getContext() { return context; }
    public void setContext(ActionBeanContext context) { this.context = context; }

    @DefaultHandler
    public Resolution defaultHandler() {
    	CheckoutExecution task = new CheckoutExecution();
    	dbSession.save(task);
    	return WorkflowActionBean.redirectToTask(task);
    }

    @Inject
    public void setDbSession(DbSession dbSession) {
    	this.dbSession = dbSession;
    }
    
    @Before
    public void injectBeforeExecute() {
    	Container.getContainer(getContext().getServletContext()).injectDependancies(this);
    }
}
