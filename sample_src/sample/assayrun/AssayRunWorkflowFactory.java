package sample.assayrun;

import com.github.ittyflow.Workflow;

public class AssayRunWorkflowFactory {
	
	public static Workflow<AssayRunState, ITransitions> makeWorkflow() {
		Workflow<AssayRunState, ITransitions> workflow = 
			new Workflow<AssayRunState, ITransitions>(AssayRun.class, ITransitions.class);
		
		workflow.addListener(AssayRunState.Waiting_For_Map_Groups, new Transitions() {
			public AssayRunState userSelectedPlateMaps(AssayRun assayRun, 
					String[] plateMaps, boolean includeControlsAtBeginning) {
				return AssayRunState.Waiting_For_Plates;
			}
		});
		
		workflow.addListener(AssayRunState.Waiting_For_Plates, new Transitions() {
			public AssayRunState signalRecheckPlates(AssayRun assayRun) {
				return AssayRunState.Waiting_For_Plates;
			}

			public AssayRunState entered(AssayRun assayRun) {
				if(assayRun.platesAreReady())
					return AssayRunState.Ready_To_Start;
				
				// if we're still waiting for plates, stay in this state
				return null;
			}
			
		});
		
		workflow.addListener(AssayRunState.Ready_To_Start, new Transitions() {
			public AssayRunState userClickedContinue(AssayRun assayRun) {
				if(assayRun.isUsingSTX1000()) {
					return AssayRunState.Waiting_For_Automated_Load;
				} else {
					return AssayRunState.Plate_Placement_Before_Run;
				}
			}
		});
		
		workflow.addListener(AssayRunState.Plate_Placement_Before_Run, new Transitions() {
			public AssayRunState userClickedContinue(AssayRun assayRun) {
				return AssayRunState.Verify_Before_Run;
			}
		});
		
		workflow.addListener(AssayRunState.Waiting_For_Automated_Load, new Transitions() {
			public AssayRunState cellarioCompletedOrders(AssayRun assayRun) {
				return AssayRunState.Waiting_For_Setup;
			}
		});
		
		workflow.addListener(AssayRunState.Verify_Before_Run, new Transitions() {
			public AssayRunState userSubmittedVerification(AssayRun assayRun, String cassetteBarcode, String[] plateBarcodes) {
				return AssayRunState.Waiting_For_Setup;
			}
		});
		
		workflow.addListener(AssayRunState.Waiting_For_Setup, new Transitions() {
			public AssayRunState userClickedContinue(AssayRun assayRun) {
				return AssayRunState.Waiting_For_Order_Run;
			}
		});
		
		workflow.addListener(AssayRunState.Waiting_For_Order_Run, new Transitions() {
			public AssayRunState cellarioCompletedOrders(AssayRun assayRun) {
				return AssayRunState.Waiting_For_Cleanup;
			}
			public AssayRunState userClickedCancel(AssayRun assayRun) {
				return AssayRunState.Cancelled;
			}
		});

		workflow.addListener(AssayRunState.Waiting_For_Cleanup, new Transitions() {
			public AssayRunState userClickedContinue(AssayRun assayRun) {
				if(assayRun.isUsingSTX1000()) { 
					return AssayRunState.Waiting_For_Automated_Unload;
				} else {
					return AssayRunState.Plate_Placement_After_Run;
				}
			}
		});

		workflow.addListener(AssayRunState.Plate_Placement_After_Run, new Transitions() {
			public AssayRunState userClickedContinue(AssayRun assayRun) {
				return AssayRunState.Verify_After_Run;
			}
		});

		workflow.addListener(AssayRunState.Verify_After_Run, new Transitions() {
			public AssayRunState userClickedContinue(AssayRun assayRun) {
				return AssayRunState.Completed;
			}
		});
		
		workflow.addListener(AssayRunState.Waiting_For_Automated_Unload, new Transitions() {
			public AssayRunState cellarioCompletedOrders(AssayRun assayRun) {
				return AssayRunState.Completed;
			}
		});

		workflow.addTerminalState(AssayRunState.Completed);
		
		workflow.addTerminalState(AssayRunState.Cancelled);
		
		return workflow;
	}
}
