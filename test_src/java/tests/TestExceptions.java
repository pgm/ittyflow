package tests;

import static org.junit.Assert.assertEquals;

import java.io.NotSerializableException;

import org.junit.Assert;
import org.junit.Test;

import com.github.ittyflow.Execution;
import com.github.ittyflow.InvalidEvent;
import com.github.ittyflow.TransitionFailedException;
import com.github.ittyflow.Workflow;

public class TestExceptions {
	
	public static enum Status {
		START, STOP
	}
	
	static public class Transitions
	{
		public Status next(State state) {
			throw new InvalidEvent();
		}

		public Status entered(State state) {
			return null;
		}
	}

	public class State implements Execution<Status> {
		Status waitState = Status.START;
		
		public Status getWaitState() {
			return waitState;
		}

		public void setWaitState(Status state) {
			this.waitState = state;
		}
	}

	public static class ThisWorkflow extends Workflow<Status,Transitions>
	{
		public Transitions START = new Transitions(){
			@Override
			public Status next(State state) {
				throw new UnsupportedOperationException("encountered a fake error");
			}
		};
		
		public ThisWorkflow() 
		{
			super(State.class,Transitions.class);			
			this.addTerminalState(Status.STOP);
			this.setupNonterminalStates();
		}
	};
	
	@Test
	public void testInterceptor() {
		ThisWorkflow workflow = new ThisWorkflow();

		State state = new State();
		
		try {
			workflow.signal(state).next(null);
		} 
		catch (TransitionFailedException ex) 
		{
			assertEquals(Status.START, ex.getCurrentState());
			assertEquals("next", ex.getTransitionName());

			Assert.assertTrue(ex.getCause() instanceof UnsupportedOperationException);
			ex.printStackTrace();
			return;
		}
		
		Assert.fail("no exception");
	}

}
