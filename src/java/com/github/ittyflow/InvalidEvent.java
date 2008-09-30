package com.github.ittyflow;

/**
 * An exception to be thrown when a method in the Transition class does not support a particular 
 * event within a specific state.
 */
public class InvalidEvent extends RuntimeException {

	private static final long serialVersionUID = 3858207061439810590L;

	public InvalidEvent() {
		super();
	}
	
	public InvalidEvent(String message) {
		super(message);
	}
	
}
