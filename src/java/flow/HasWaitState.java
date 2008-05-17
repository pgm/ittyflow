package flow;

public interface HasWaitState<W> {
	public W getWaitState();
	public void setWaitState(W state);
//	public String getWaitStateName();
}
