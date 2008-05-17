package sample.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sample.AssayRun;
import sample.AssayRunState;
import sample.AssayRunWorkflowFactory;
import sample.ITransitions;
import flow.Workflow;

public class TestSample {
	@Test
	public void testWorkflow() throws Exception {
		CellarioSimulator simulator = new CellarioSimulator();
		
		Workflow<AssayRunState, ITransitions> wf = AssayRunWorkflowFactory.makeWorkflow();

		AssayRun assayRun = new AssayRun();
		assertEquals(AssayRunState.Waiting_For_Map_Groups, assayRun.getWaitState());

		wf.signal(assayRun).userSelectedPlateMaps(null, new String[] {"G001", "G002", "G003"} , false);
		assertEquals(AssayRunState.Ready_To_Start, assayRun.getWaitState());

		wf.signal(assayRun).userClickedContinue(null);
		assertEquals(AssayRunState.Waiting_For_Automated_Load, assayRun.getWaitState());
		
		simulator.simulateOutstandingOrdersOnRun(assayRun);
		assertEquals(AssayRunState.Waiting_For_Setup, assayRun.getWaitState());

		wf.signal(assayRun).userClickedContinue(null);
		assertEquals(AssayRunState.Waiting_For_Order_Run, assayRun.getWaitState());

		simulator.simulateOutstandingOrdersOnRun(assayRun);
		assertEquals(AssayRunState.Waiting_For_Cleanup, assayRun.getWaitState());
	
		wf.signal(assayRun).userClickedContinue(null);
		assertEquals(AssayRunState.Waiting_For_Automated_Unload, assayRun.getWaitState());

		simulator.simulateOutstandingOrdersOnRun(assayRun);
		assertEquals(AssayRunState.Completed, assayRun.getWaitState());
	}
}
