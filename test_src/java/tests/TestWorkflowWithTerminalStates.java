package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import tests.TestWorkflow.State;
import tests.TestWorkflow.Status;
import tests.TestWorkflow.Transitions;

import com.github.ittyflow.Workflow;

public class TestWorkflowWithTerminalStates {
	public static class ThisWorkflow extends Workflow<Status,Transitions>
	{
		public Transitions START = new Transitions(){
			@Override
			public Status next(State state) {
				return Status.MIDDLE;
			}
		};
		
		public Transitions MIDDLE = new Transitions(){
			@Override
			public Status next(State state) {
				return Status.STOP;
			}
		};

		public ThisWorkflow() 
		{
			super(State.class,Transitions.class);			
			this.addTerminalState(Status.STOP, new Transitions() {
				@Override
				public Status next(State state)
				{
					return null;
				}
			} );
			this.setupNonterminalStates();
		}
	};

	@Test
	public void testIgnoreEventsOnTerminalStates()
	{
		ThisWorkflow workflow = new ThisWorkflow();
		
		State state = new State();
		state.list = new ArrayList<String>();
		workflow.signal(state).next(null);
		assertEquals(Status.MIDDLE, state.getWaitState());
		workflow.signal(state).next(null);
		assertEquals(Status.STOP, state.getWaitState());
		workflow.signal(state).next(null);
		assertEquals(Status.STOP, state.getWaitState());
	}
}
