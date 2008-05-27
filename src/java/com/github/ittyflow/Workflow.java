package com.github.ittyflow;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.ittyflow.annotations.AbstractEvent;

public class Workflow<W,T> {
	final Class<T> transitionClass;
	final Class<? extends HasWaitState<W>> executionClass;
	final Class<?> waitStateClass;
	
	Map<String, Method> allMethodsByName = new HashMap<String, Method>();
	Map<String,W> stateByName = new HashMap<String, W>();
	Map<W, TransitionDispatch<T>> map = new HashMap<W, TransitionDispatch<T>>();

	@SuppressWarnings("serial")
	static class TransitionAlreadyDefinedException extends RuntimeException {
	}
	
	public static class TargetedMethod<T> {
		final Method method;
		final T target;
		public Method getMethod() {
			return method;
		}
		public T getTarget() {
			return target;
		}
		public TargetedMethod(Method method, T target) {
			super();
			this.method = method;
			this.target = target;
		}
	}
	
	static class TransitionDispatch<T>{
		Map<String, TargetedMethod<T>> transitions = new HashMap<String,TargetedMethod<T>>();
		
		public void addTransition(T target, Method method) {
			String name = method.getName();
			if(transitions.containsKey(name)) {
				throw new TransitionAlreadyDefinedException();
			}
			
			transitions.put(name, new TargetedMethod<T>(method,target));
		}
		
		public TargetedMethod<T> getMethod(String name) {
			return transitions.get(name);
		}
		
		public Collection<TargetedMethod<T>> getTransitions() {
			return transitions.values();
		}
	}
	
	public static final String ENTERED_EVENT_NAME = "entered";
	
	public Workflow(Class<? extends HasWaitState<W>> wClass, Class<T> transitionClass) {
		this.transitionClass = transitionClass;
		this.executionClass = wClass;
		try {
			this.waitStateClass = executionClass.getMethod("getWaitState").getReturnType();
		} catch (Exception ex) {
			throw new RuntimeException("Could not get the return type of getWaitState from the class "+executionClass.getName(), ex);
		}
		
		for(Method method : this.transitionClass.getMethods()) {
			String methodName = method.getName();
			if(allMethodsByName.containsKey(methodName)) {
				throw new RuntimeException("Two transitions with the same name are not allowed.  In this class at least two methods with the same name ("+methodName+") were found");
			}

			// make sure the signature is right for an event
			// must return a wait state
			if(!method.getReturnType().equals(waitStateClass)) {
				continue;
			}

			// and the first parameter must be the execution object
			Class<?>parameterTypes[] = method.getParameterTypes();
			if(parameterTypes.length < 1 || !parameterTypes[0].equals(wClass)) {
				continue;
			}
			
			allMethodsByName.put(methodName, method);
		}
	}
	
	public Collection<TargetedMethod<T>> getTransitionsForState(W state) {
		return map.get(state).getTransitions();
	}
	
	public void addTerminalState(W state) {
		map.put(state, new TransitionDispatch<T>());
	}

	public void addListener(W state, T transitionSet) {
		Set <String> methodNames = new HashSet<String>();
		Class<?> clazz = transitionSet.getClass();

		while(clazz != null && !clazz.equals(transitionClass)) {
			for(Method method : clazz.getMethods()) {
				// skip if this is marked as abstract
				if (method.getAnnotation(AbstractEvent.class) != null)
					continue;
				
				String methodName = method.getName();
	
				// if the method is also in the base class, then we 
				// consider this a real event method.
				if(allMethodsByName.containsKey(methodName)) {
					methodNames.add(method.getName());
				}
			}
			clazz = clazz.getSuperclass();
		}
		
		String methodNameArray[] = methodNames.toArray(new String[methodNames.size()]);
		addListener(state, transitionSet, methodNameArray);
	}
	
	public void addListener(W state, T transitions, String[] allowedTransitions) {
		TransitionDispatch<T> dispatch = map.get(state);
		if(dispatch == null) {
			dispatch = new TransitionDispatch<T>();
			map.put(state, dispatch);
		}
		for(String methodName : allowedTransitions) {
			Method method = this.allMethodsByName.get(methodName);
			if(method == null) {
				throw new RuntimeException("There was an attempt to add a listener for the event named "+methodName+", however the class "+transitionClass.getName()+" has no method by that name");
			}

			dispatch.addTransition(transitions, method);
		}
	}
	
	public void validate() {
		// walk through the transitions for each state and make sure that no
		// transition is handled by more then one handler
	}
	
	public TransitionDispatch<T> getDispatcher(W state) {
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
				W state = taskState.getWaitState();
				
				if(state == null) {
					throw new RuntimeException("wait state cannot be null");
				}
				
				TransitionDispatch dispatch = getDispatcher(state);
				TargetedMethod transitionMethod = dispatch.getMethod(methodName);
				if(transitionMethod == null) {
					if(mustExist) {
						throw new RuntimeException("could not find "+methodName+" with parameters "+parameterTypes);
					} else {
						return null;
					}
				}
				
				try {
					return (W)transitionMethod.getMethod().invoke(transitionMethod.getTarget(), parameters);
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
					
					String newHandlerName = ENTERED_EVENT_NAME;
	
					// fix this, I think the signature is requiring a perfect match...
					returnedState = invokeHandler(newHandlerName, new Class[] {executionClass}, new Object[] {taskState}, false );
				}
				
				return taskState.getWaitState();
			}
		};

		T newProxyInstance = (T) Proxy.newProxyInstance(transitionClass.getClassLoader(), new Class[] {transitionClass}, handler );

		return newProxyInstance;
	}
}
