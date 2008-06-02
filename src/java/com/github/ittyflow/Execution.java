package com.github.ittyflow;

public interface Execution<W> {
	public W getWaitState();
	public void setWaitState(W state);
}
