package com.github.ittyflow;

public interface Interceptor {
	public void intercept(Runnable continuation);
}
