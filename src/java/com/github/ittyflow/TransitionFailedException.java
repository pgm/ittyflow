package com.github.ittyflow;

public class TransitionFailedException extends RuntimeException {
	private final String transitionName;
	private final Object currentState;
	/**
	 * @param currentState
	 * @param transitionName
	 */
	protected TransitionFailedException(Object currentState,
			String transitionName, Throwable causedBy) {
		super(causedBy);
		this.currentState = currentState;
		this.transitionName = transitionName;
	}
	/**
	 * @return the transitionName
	 */
	public String getTransitionName() {
		return transitionName;
	}
	/**
	 * @return the currentState
	 */
	public Object getCurrentState() {
		return currentState;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "(currentState="+this.currentState+", transition="+this.transitionName+") "+this.getCause().getMessage();
	}
}
