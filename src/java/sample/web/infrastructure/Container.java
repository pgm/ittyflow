package sample.web.infrastructure;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import sample.web.flow.CheckoutWorkflow;
import sample.web.mockdb.DbSession;

public class Container {
	public static final String CONTAINER_KEY = "sample.web.infrastructure.Container";
	
	Map<String,Object> objects = new HashMap<String,Object>();
 	
	public static Container getContainer(ServletContext context) {
		Container container = (Container)context.getAttribute(CONTAINER_KEY);
		if(container == null) {
			container = new Container();
			context.setAttribute(CONTAINER_KEY, container);
		}
		return container;
	}
	
	public void register(String name, Object object) {
		objects.put(name, object);
	}
	
	public Object getByName(String name) {
		if(!objects.containsKey(name))
			throw new RuntimeException("no object named "+name);
		return objects.get(name);
	}
	
	public void injectDependancies(Object object) {
		Class<?> clazz = object.getClass();
		while(clazz != null) {
			for(Method method : clazz.getMethods()) {
				Inject inject = method.getAnnotation(Inject.class);
				if(inject != null) {
					String propertyName = getPropertyName(method);
					Object value = getByName(propertyName);
					try {
						method.invoke(object, value);
					} catch (Exception e) {
						throw new RuntimeException("Exception calling "+method.getName(), e);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	public String getPropertyName(Method method) {
		String name = method.getName().substring(3);
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	public Container() {
		// configure the container
		register("checkoutWorkflow", new CheckoutWorkflow());
		// eh, not really right but this is just a mockup
		register("dbSession", new DbSession());
	}


}
