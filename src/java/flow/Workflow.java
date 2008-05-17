package flow;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import net.sf.cglib.proxy.InvocationHandler;

// a work flow is a product of states and transitions

public class Workflow<W,T> {
	Class<T> transitionClass;
	Class<? extends HasWaitState<W>> wClass;

	Map<String,W> stateByName = new HashMap<String, W>();
	Map<W, List<T>> map = new HashMap<W, List<T>>();
//	Enhancer enhancer = new Enhancer();
	
	public Workflow(Class<? extends HasWaitState<W>> wClass, Class<T> transitionClass) {
		this.transitionClass = transitionClass;
		this.wClass = wClass;
	}
	
	public void addTerminalState(W state) {
		map.put(state, Collections.EMPTY_LIST);
	}
	
	public void addListener(W state, T transitions) {
		List<T> list = map.get(state);
		if(list == null) {
			list = new ArrayList<T>();
			map.put(state, list);
		}
		list.add(transitions);
	}
	
	public void validate() {
		// walk through the transitions for each state and make sure that no
		// transition is handled by more then one handler
	}
	
	public List<T> getTransitionSets(W state) {
		if(!map.containsKey(state))
			throw new RuntimeException("Workflow does not contain "+state);
		return map.get(state);
	}

	public Collection<W> getStates() {
		return map.keySet();
	}
	
	@SuppressWarnings(value={"unchecked"})
	public T signal(final HasWaitState<W> taskState) {

		InvocationHandler handler = new InvocationHandler() {
			public W invokeHandler(String methodName, Class<?> parameterTypes[], Object [] parameters, boolean mustExist) {
				Method transitionMethod = null;
				T transitionSet = null;
				W state = taskState.getWaitState();
				
				if(state == null) {
					throw new RuntimeException("wait state cannot be null");
				}
				
				for(T t : getTransitionSets(state))
				{
					try {
						Class tclass = t.getClass();
						while(!Modifier.isPublic(tclass.getModifiers())) {
							tclass = tclass.getSuperclass();
						}
						transitionMethod = tclass.getMethod(methodName, parameterTypes);
						transitionSet = t;
					} catch (NoSuchMethodException ex) {
					}
					
					if(transitionMethod != null) {
						break;
					}
				}

				if(transitionMethod == null) {
					if(mustExist) {
						throw new RuntimeException("could not find "+methodName+" with parameters "+parameterTypes);
					} else {
						return null;
					}
				}
				
				try {
					return (W)transitionMethod.invoke(transitionSet, parameters);
				} catch (Exception ex) {
					throw new RuntimeException("Could not execute "+transitionMethod, ex);
				}
			}
			
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				
				if ( args[0] != null ) {
					throw new RuntimeException("You cannot provide a state to modify.  This will be automatically populated.  You must provide null as the first argument");
				}

				// copy the task state as the first parameter
				args[0] = taskState;

				W returnedState = invokeHandler(method.getName(), method.getParameterTypes(), args, true);
				
				// if we got a new state
				while(returnedState != null) {
					taskState.setWaitState(returnedState);
					
//					String waitStateName = taskState.getWaitStateName();
//					waitStateName = waitStateName.substring(0,1).toUpperCase()+waitStateName.substring(1);
					
					String newHandlerName = "entered";
					// fix this, the signature is requiring a perfect match...
					returnedState = invokeHandler(newHandlerName, new Class[] {wClass}, new Object[] {taskState}, false );
				}
				
				return taskState.getWaitState();
			}
		};

//		T newProxyInstance = (T) enhancer.create(transitionClass, handler);
		T newProxyInstance = (T) Proxy.newProxyInstance(transitionClass.getClassLoader(), new Class[] {transitionClass}, handler );

		return newProxyInstance;
	}
}
