package com.github.ittyflow;

/**
 * Classes which implement the {@link Execution} interface can
 * be used as executions of a given workflow.
 * 
 * @author pgm
 *
 * @param <W> The class representing the wait state
 */
public interface Execution<W> {
	public W getWaitState();
	public void setWaitState(W state);
}
