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
	
	static final String START = "start";
	static final String STOP = "stop";
	static final String MIDDLE = "middle";
	
	public interface ITransitions {
		public String next(State state);
		public String entered(State state);
	}
	
	static public class Transitions implements ITransitions
	{
		@AbstractEvent
		public String next(State state) {
			throw new InvalidEvent();
		}

		@AbstractEvent
		public String entered(State state) {
			throw new InvalidEvent();
		}
	}

	public class State implements Execution<String> {
		String waitState = START;
		
		public String getWaitState() {
			return waitState;
		}

		public void setWaitState(String state) {
			list.add("setWaitState");
			this.waitState = state;
		}
		
		public List list;
	}
	
	@Test
	public void testInterceptor() {
		final List list = new ArrayList();

		Workflow<String,ITransitions> workflow = new Workflow<String,ITransitions>(State.class,
				ITransitions.class, new String[] {START, STOP});

		workflow.addListener(START, new Transitions(){

			/* (non-Javadoc)
			 * @see TestWorkflow.Transitions#next(TestWorkflow.State)
			 */
			@Override
			public String next(State state) {
				list.add("inNext");
				return MIDDLE;
			}
		});

		workflow.addListener(MIDDLE, new Transitions(){

			/* (non-Javadoc)
			 * @see TestWorkflow.Transitions#next(TestWorkflow.State)
			 */
			@Override
			public String entered(State state) {
				list.add("inEntered");
				return STOP;
			}
		});
		
		workflow.addTerminalState(STOP);

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
//		assertEquals("[before, inNext, after]", list.toString());
		assertEquals("[before, inNext, setWaitState, inEntered, setWaitState, after]", list.toString());
		assertEquals(STOP, state.getWaitState());
	}
	

}
