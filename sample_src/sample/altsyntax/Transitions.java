package sample.altsyntax;

import com.github.ittyflow.InvalidEvent;

public class Transitions {
	public WaitState entered(WizardExecution e) {
		return null;
	}
	
	public WaitState clickedDone(WizardExecution e) {
		throw new InvalidEvent();
	}
	
	public WaitState clickedCancel(WizardExecution e) {
		throw new InvalidEvent();
	}
}
