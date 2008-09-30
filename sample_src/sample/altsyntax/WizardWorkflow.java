package sample.altsyntax;

import com.github.ittyflow.Workflow;


public class WizardWorkflow extends Workflow<WaitState, Transitions> {
	public Transitions STARTED = new Transitions() {
		@Override
		public WaitState clickedDone(WizardExecution e) {
			return WaitState.PART1;
		}
	};

	public Transitions PART1 = new Transitions() {
		@Override
		public WaitState clickedDone(WizardExecution e) {
			return WaitState.PART2;
		}
	};

	public Transitions PART2 = new Transitions() {
		@Override
		public WaitState clickedDone(WizardExecution e) {
			return WaitState.FINISHED;
		}
	};

	public WizardWorkflow() 
	{
		super(WizardExecution.class, Transitions.class);
		addTerminalState(WaitState.FINISHED);
		setupNonterminalStates();
	}
}
