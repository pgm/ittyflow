package sample.web.infrastructure;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;

public class ContainerInterceptor implements Interceptor {

	public Resolution intercept(ExecutionContext context) throws Exception {
		ActionBean actionBean = context.getActionBean();
		
		// look up the container
		Container container = Container.getContainer(context.getActionBeanContext().getServletContext());

		// perform injections
		container.injectDependancies(actionBean);
		
		Resolution result = context.proceed();
		
		return result;
	}
	
}
