package sample;

import org.apache.log4j.Logger;

import flow.HasWaitState;
import flow.Workflow;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class);
	
	public static class WaitState {

		final String name;
		
		public WaitState(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		public String toString() {
			return getName();
		}
		
	}
	
	public static class GotoState implements TransitionSet {
		WaitState state;
		
		public GotoState(WaitState state ) {
			this.state = state;
		}
		
		public WaitState signalAdvance(SampleTask taskState) {
			return state;
		}
		
		public WaitState enteredS2(SampleTask taskState) {
			log.info("entered s2!.  Jumping directly to next state");
			return state;
		}
	}
	
	public static interface TransitionSet {
		public WaitState signalAdvance(SampleTask task);
	}
	
	public static class SampleTask implements HasWaitState<WaitState> {
		WaitState waitState;
		
		public SampleTask(WaitState initialState) {
			setWaitState(initialState);
		}
		
		public WaitState getWaitState() {
			return waitState;
		}

		public void setWaitState(WaitState state) {
			waitState = state;
		}
		
		public String toString() {
			return waitState.toString();
		}
		
		public String getWaitStateName() {
			return waitState.getName();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WaitState s1 = new WaitState("s1");
		WaitState s2 = new WaitState("s2");
		WaitState s3 = new WaitState("s3");
		WaitState s4 = new WaitState("s4");

		TransitionSet gotoStateS1 = new GotoState(s1);
		TransitionSet gotoStateS2 = new GotoState(s2);
		TransitionSet gotoStateS3 = new GotoState(s3);
		TransitionSet gotoStateS4 = new GotoState(s4);
		
		Workflow<WaitState, TransitionSet> w = new Workflow<WaitState, TransitionSet>(SampleTask.class, TransitionSet.class);
		w.addListener(s1,gotoStateS2);
		w.addListener(s2,gotoStateS3);
		w.addListener(s3,gotoStateS4);
		w.addListener(s4,gotoStateS1);
		
		SampleTask sampleTaskImpl = new SampleTask(s1);
		
		log.debug(sampleTaskImpl);
		w.signal(sampleTaskImpl).signalAdvance(null);
		log.debug(sampleTaskImpl);
		w.signal(sampleTaskImpl).signalAdvance(null);
		log.debug(sampleTaskImpl);
		w.signal(sampleTaskImpl).signalAdvance(null);
		log.debug(sampleTaskImpl);
		w.signal(sampleTaskImpl).signalAdvance(null);
		log.debug(sampleTaskImpl);
		w.signal(sampleTaskImpl).signalAdvance(null);
		log.debug(sampleTaskImpl);
	}

}
