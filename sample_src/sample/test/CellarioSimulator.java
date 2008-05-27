package sample.test;

import com.github.ittyflow.Workflow;

import sample.AssayRun;
import sample.AssayRunState;
import sample.AssayRunWorkflowFactory;
import sample.ITransitions;

public class CellarioSimulator {
	Workflow<AssayRunState, ITransitions> wf = AssayRunWorkflowFactory.makeWorkflow();

	public void simulateOutstandingOrdersOnRun(AssayRun arun) {
		wf.signal(arun).cellarioCompletedOrders(null);
	}
}
