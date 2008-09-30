import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.ittyflow.Execution;
import com.github.ittyflow.Interceptor;
import com.github.ittyflow.InvalidEvent;
import com.github.ittyflow.Workflow;
import com.github.ittyflow.annotations.AbstractEvent;

public class TestWorkflow {
	
	public static enum Status {
		START, STOP, MIDDLE;
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
			list.add("setWaitState");
			this.waitState = state;
		}
		
		public List list;
	}


	public static class ThisWorkflow extends Workflow<Status,Transitions>
	{
		public Transitions START = new Transitions(){
			@Override
			public Status next(State state) {
				state.list.add("inNext");
				return Status.MIDDLE;
			}
		};
		
		public Transitions MIDDLE = new Transitions(){
			@Override
			public Status entered(State state) {
				state.list.add("inEntered");
				return Status.STOP;
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
		final List list = new ArrayList();

		ThisWorkflow workflow = new ThisWorkflow();
		
		workflow.addInterceptor(new Interceptor() {

			public void intercept(Runnable continuation) {
				list.add("before");
				continuation.run();
				list.add("after");
			}
			
		} );
		
		State state = new State();
		state.list = list;
		workflow.signal(state).next(null);
		assertEquals("[before, inNext, setWaitState, inEntered, setWaitState, after]", list.toString());
		assertEquals(Status.STOP, state.getWaitState());
	}
}
