package sample.assayrun;

import com.github.ittyflow.Workflow;

public class VizMain {
	public static void main(String args[]) {
		Workflow<AssayRunState, ITransitions> workflow = AssayRunWorkflowFactory.makeWorkflow();
//		new VisualizerUI().visualize(workflow);
	}
}
