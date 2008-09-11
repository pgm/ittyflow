package com.github.ittyflow;

import java.io.Serializable;
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
	public static final String ENTERED_EVENT_NAME = "entered";
	
	final Class<T> transitionClass;
	final Class<? extends Execution<W>> executionClass;
	final Class<?> waitStateClass;
	final W waitStates[];
	final Set<W> undefinedWaitStates = new HashSet<W>();
	
	protected Map<String, Method> allMethodsByName = new HashMap<String, Method>();
	protected Map<String,W> stateByName = new HashMap<String, W>();
	protected Map<W, TransitionDispatch<T>> dispatchPerState = new HashMap<W, TransitionDispatch<T>>();

	public Workflow(Class<? extends Execution<W>> executionClass, Class<T> transitionClass, W [] waitStates) {
		for(W waitState : waitStates) {
			this.undefinedWaitStates.add(waitState);
		}
		
		this.waitStates = waitStates;
		this.transitionClass = transitionClass;
		this.executionClass = executionClass;
		
		buildStateIndex();
		
		try {
			this.waitStateClass = executionClass.getMethod("getWaitState").getReturnType();
		} catch (Exception ex) {
			throw new RuntimeException("Could not get the return type of getWaitState from the class "+executionClass.getName(), ex);
		}
		
		for(Method method : this.transitionClass.getMethods()) {
			// make sure the signature is right for an event
			// must return a wait state.   
			if(!method.getReturnType().equals(waitStateClass)) {
				continue;
			}
			
			// skip any methods that were created from erasure or other
			// compilation processes
			if(method.isSynthetic()) {
				continue;
			}

			String methodName = method.getName();
			if(allMethodsByName.containsKey(methodName)) {
				throw new RuntimeException("Two transitions with the same name are not allowed.  In class "+transitionClass.getName()+
						" there at least two methods with the same name ("+methodName+").");
			}

			// and the first parameter must be the execution object
			Class<?>parameterTypes[] = method.getParameterTypes();
			if(parameterTypes.length < 1 || !parameterTypes[0].equals(executionClass)) {
				continue;
			}
			
			allMethodsByName.put(methodName, method);
		}
	}
	
	protected void buildStateIndex() {
		for(W state : waitStates) {
			String name = state.toString();
			if(stateByName.containsKey(name)) {
				throw new RuntimeException("Workflow contains multiple states whose toString method returned "+name);
			}
			stateByName.put(name, state);
		}
	}
	
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
	
	public Collection<TargetedMethod<T>> getTransitionsForState(W state) {
		return dispatchPerState.get(state).getTransitions();
	}
	
	public void addTerminalState(W state) {
		this.undefinedWaitStates.remove(state);

		dispatchPerState.put(state, new TransitionDispatch<T>());
	}

	public void addListener(W state, T transitionSet) {
		this.undefinedWaitStates.remove(state);
		
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
		TransitionDispatch<T> dispatch = dispatchPerState.get(state);
		if(dispatch == null) {
			dispatch = new TransitionDispatch<T>();
			dispatchPerState.put(state, dispatch);
		}
		for(String methodName : allowedTransitions) {
			Method method = this.allMethodsByName.get(methodName);
			if(method == null) {
				throw new RuntimeException("There was an attempt to add a listener for the event named "+methodName+", however the class "+transitionClass.getName()+" has no method by that name");
			}

			dispatch.addTransition(transitions, method);
		}
	}
	
	protected TransitionDispatch<T> getDispatcher(W state) {
		if(!dispatchPerState.containsKey(state))
			throw new RuntimeException("Workflow does not contain state "+state);
		return dispatchPerState.get(state);
	}

	public Collection<W> getStates() {
		return dispatchPerState.keySet();
	}
	
	@SuppressWarnings(value={"unchecked"})
	public T signal(final Execution<W> taskState) {
		// check to make sure the definition of this workflow is complete
		if(this.undefinedWaitStates.size() > 0) {
			throw new RuntimeException("Workflow definition is incomplete.  The following states are undefined: "+this.undefinedWaitStates+".  There should be a call to addTerminalState() or addListener() for each state.");
		}

		InvocationHandler handler = new InvocationHandler() {
			public W invokeHandler(String methodName, Class<?> parameterTypes[], Object [] parameters, boolean mustExist) {
				final W state = taskState.getWaitState();
				
				if(state == null) {
					throw new RuntimeException("wait state cannot be null");
				}
				
				TransitionDispatch dispatch = getDispatcher(state);
				final TargetedMethod transitionMethod = dispatch.getMethod(methodName);
				if(transitionMethod == null) {
					if(mustExist) {
						throw new RuntimeException("could not find "+methodName+" with parameters "+parameterTypes);
					} else {
						return null;
					}
				}
				
				final Object _target = transitionMethod.getTarget();
				final Object [] _parameters = parameters;
				final Object [] retResult = new Object[1];
				
				Runnable cont = new Runnable() {
					public void run() {
						try {
							retResult[0] = transitionMethod.getMethod().invoke(_target, _parameters);
						} catch (Exception ex) {
							throw new RuntimeException("Could not execute "+transitionMethod+". (current state = "+state+")", ex);
						}
					}
				};
				
				// execute signal within interceptors
				cont.run();
				
				return (W)retResult[0];
			}
			
			public Object invoke(final Object proxy, final Method method, final Object[] args)
					throws Throwable {
				final Object retBuffer[] = new Object[1];
				
				if ( args[0] != null ) {
					throw new RuntimeException("You cannot provide a state to modify.  This will be automatically populated.  You must provide null as the first argument");
				}

				Runnable cont = new Runnable() {
					public void run() {
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
						
						retBuffer[0] = taskState.getWaitState();
					}
				};
				
				signalInterceptor.intercept(cont);
				
				return retBuffer[0];
			}
		};

		T newProxyInstance = (T) Proxy.newProxyInstance(transitionClass.getClassLoader(), new Class[] {transitionClass}, handler );

		return newProxyInstance;
	}
	
	public static class SerializableEdges implements Serializable {
		private static final long serialVersionUID = 1562709226833023689L;

		String method;
		String fromState;
		String toState;
	}
	
	public Collection<SerializableEdges> getSummary() {
		throw new RuntimeException("unimp");
	}
	
	public void addInterceptor(final Interceptor interceptor) {
		final Interceptor innerInterceptor = signalInterceptor;
		
		signalInterceptor = new Interceptor() {
			public void intercept(final Runnable continuation) {
				Runnable innerRunnable = new Runnable() {
					public void run() {
						innerInterceptor.intercept(continuation);
					}
				};
				interceptor.intercept(innerRunnable);
			}
		};
	}
	
	Interceptor signalInterceptor = new Interceptor() {
		public void intercept(Runnable continuation) {
			continuation.run();
		}
		
	};
}
