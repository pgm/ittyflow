package sample;

public interface ITransitions {

	/* (non-Javadoc)
	 * @see sample.Transitions#userClickedCancel(sample.AssayRun)
	 */
	public AssayRunState userClickedCancel(AssayRun assayRun);

	/* (non-Javadoc)
	 * @see sample.Transitions#userSelectedPlateMaps(sample.AssayRun, java.lang.String[], boolean)
	 */
	public AssayRunState userSelectedPlateMaps(AssayRun assayRun,
			String[] plateMaps, boolean includeControlsAtBeginning);

	/* (non-Javadoc)
	 * @see sample.Transitions#signalRecheckPlates(sample.AssayRun)
	 */
	public AssayRunState signalRecheckPlates(AssayRun assayRun);

	/* (non-Javadoc)
	 * @see sample.Transitions#userClickedContinue(sample.AssayRun)
	 */
	public AssayRunState userClickedContinue(AssayRun assayRun);

	/* (non-Javadoc)
	 * @see sample.Transitions#userSubmittedVerification(sample.AssayRun, java.lang.String, java.lang.String[])
	 */
	public AssayRunState userSubmittedVerification(AssayRun assayRun,
			String cassetteBarcode, String[] plateBarcodes);

	/* (non-Javadoc)
	 * @see sample.Transitions#cellarioCompletedOrders(sample.AssayRun)
	 */
	public AssayRunState cellarioCompletedOrders(AssayRun assayRun);

	public AssayRunState entered(AssayRun assayRun);

}