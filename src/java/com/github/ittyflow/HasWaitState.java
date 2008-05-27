package com.github.ittyflow;

public interface HasWaitState<W> {
	public W getWaitState();
	public void setWaitState(W state);
}
