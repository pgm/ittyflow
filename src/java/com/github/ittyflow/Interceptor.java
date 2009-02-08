package com.github.ittyflow;

/**
 * An interface with a single method ( {@link Interceptor#intercept(Runnable)} ) which
 * is used to enable injection of code before and after some other block of code
 * provided by the caller.  (The caller's code is encapsulated in "continuation" 
 * parameter)
 * 
 * @author pgm
 *
 */
public interface Interceptor {
	public void intercept(RunnableWithThrows continuation) throws Throwable;
}
