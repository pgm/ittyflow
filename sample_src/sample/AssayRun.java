package sample;

import com.github.ittyflow.HasWaitState;

public class AssayRun implements HasWaitState<AssayRunState> {
	private AssayRunState waitState = AssayRunState.Waiting_For_Map_Groups;

	public AssayRunState getWaitState() {
		return waitState;
	}

	public String getWaitStateName() {
		return waitState.toString();
	}

	public void setWaitState(AssayRunState state) {
		this.waitState = state;
	}

	public boolean platesAreReady() {
		return true;
	}
	
	public boolean isUsingSTX1000() {
		return true;
	}
}
