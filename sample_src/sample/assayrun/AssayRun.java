package sample.assayrun;

import com.github.ittyflow.Execution;

public class AssayRun implements Execution<AssayRunState> {
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
