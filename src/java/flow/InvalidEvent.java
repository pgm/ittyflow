package flow;

public class InvalidEvent extends RuntimeException {

	private static final long serialVersionUID = 3858207061439810590L;

	public InvalidEvent() {
		super();
	}
	
	public InvalidEvent(String message) {
		super(message);
	}
	
}
