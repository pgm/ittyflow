package sample.test;

import com.github.ittyflow.Workflow;

import sample.assayrun.AssayRun;
import sample.assayrun.AssayRunState;
import sample.assayrun.AssayRunWorkflowFactory;
import sample.assayrun.ITransitions;

public class CellarioSimulator {
	Workflow<AssayRunState, ITransitions> wf = AssayRunWorkflowFactory.makeWorkflow();

	public void simulateOutstandingOrdersOnRun(AssayRun arun) {
		wf.signal(arun).cellarioCompletedOrders(null);
	}
}
