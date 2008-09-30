package sample.altsyntax;

import com.github.ittyflow.Execution;

public class WizardExecution implements Execution<WaitState> {
	WaitState waitState = WaitState.STARTED;

	public WaitState getWaitState() {
		return waitState;
	}

	public void setWaitState(WaitState waitState) {
		this.waitState = waitState;
	}
	
}
