package sample;

import flow.InvalidEvent;

public class Transitions implements ITransitions {

	/* (non-Javadoc)
	 * @see sample.Transitions#userClickedCancel(sample.AssayRun)
	 */
	/* (non-Javadoc)
	 * @see sample.ITransitions#userClickedCancel(sample.AssayRun)
	 */
	public AssayRunState userClickedCancel(AssayRun assayRun) {
		throw new InvalidEvent();
	}
	
	/* (non-Javadoc)
	 * @see sample.Transitions#userSelectedPlateMaps(sample.AssayRun, java.lang.String[], boolean)
	 */
	/* (non-Javadoc)
	 * @see sample.ITransitions#userSelectedPlateMaps(sample.AssayRun, java.lang.String[], boolean)
	 */
	public AssayRunState userSelectedPlateMaps(AssayRun assayRun, String[] plateMaps, boolean includeControlsAtBeginning) {
		throw new InvalidEvent();
	}

	/* (non-Javadoc)
	 * @see sample.Transitions#signalRecheckPlates(sample.AssayRun)
	 */
	/* (non-Javadoc)
	 * @see sample.ITransitions#signalRecheckPlates(sample.AssayRun)
	 */
	public AssayRunState signalRecheckPlates(AssayRun assayRun) {
		throw new InvalidEvent();
	}
	
	/* (non-Javadoc)
	 * @see sample.Transitions#userClickedContinue(sample.AssayRun)
	 */
	/* (non-Javadoc)
	 * @see sample.ITransitions#userClickedContinue(sample.AssayRun)
	 */
	public AssayRunState userClickedContinue(AssayRun assayRun) {
		throw new InvalidEvent();
	}

	/* (non-Javadoc)
	 * @see sample.Transitions#userSubmittedVerification(sample.AssayRun, java.lang.String, java.lang.String[])
	 */
	/* (non-Javadoc)
	 * @see sample.ITransitions#userSubmittedVerification(sample.AssayRun, java.lang.String, java.lang.String[])
	 */
	public AssayRunState userSubmittedVerification(AssayRun assayRun, String cassetteBarcode, String[] plateBarcodes) {
		throw new InvalidEvent();
	}

	/* (non-Javadoc)
	 * @see sample.Transitions#cellarioCompletedOrders(sample.AssayRun)
	 */
	/* (non-Javadoc)
	 * @see sample.ITransitions#cellarioCompletedOrders(sample.AssayRun)
	 */
	public AssayRunState cellarioCompletedOrders(AssayRun assayRun) {
		throw new InvalidEvent();
	}

	/* (non-Javadoc)
	 * @see sample.ITransitions#entered(sample.AssayRun)
	 */
	public AssayRunState entered(AssayRun assayRun) {
		return null;
	}
	
	
}
