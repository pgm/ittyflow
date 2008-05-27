package sample;

public enum AssayRunState {
    /**
     * Indicates that the run is waiting for compound 
     * plate format groups to be selected
     */
    Waiting_For_Map_Groups("Waiting for Map Groups", "1"),
    
    /**
     * Indicates that some of the run's compound plate 
     * format groups need plates to be created
     */
    Waiting_For_Plates("Waiting For Plates", "1"),
    
    /**
     * Indicates that all plate maps selected for the run have active plates
     * associated and that the user can start the run. 
     */
    Ready_To_Start("Ready", "1"),
    
    /**
     * Indicates that the plates and cassettes must be loaded into
     * incubators before the run.
     */             
    Plate_Placement_Before_Run("Waiting for Plates to be Loaded Before Run", "2"),
    
    /**
     * Indicates that the plates and cassettes must be automatically loaded into
     * incubators before the run.
     */
    Waiting_For_Automated_Load("Waiting for Plates to be Automatically Loaded Before Run", "2"),
    
    /**
     * Indicates that all plates required for a run need to be
     * user-scanned and exactly match those required to execute the run.
     */
    Verify_Before_Run("Waiting for Plate Verification Before Run", "3"),
    /**
     * Waiting for the user to perform the setup protocol
     */
    Waiting_For_Setup("Waiting for Setup", "4"),
    /**
     * Waiting for the user to create Cellario Orders
     */
    Waiting_For_Order_Run("Waiting for Cellario Orders", "5"),
    /**
     * Indicates that the run is in progress.
     */
    Running("Cellario Run Orders In Progress", "5"),
    /**
     * Indicates that the Cellario System is in paused state.
     */
    Paused("Paused", "5"),
    /**
     * Waiting for the user to perform the cleanup protocol
     */
    Waiting_For_Cleanup("Waiting for Cleanup", "6"),
    /**
     * Indicates that the plates and cassettes must be unloaded from
     * incubators after the run.
     */
    Plate_Placement_After_Run("Waiting for Plates to be Unloaded After Run", "7"),
    
    /**
     * Indicates that the plates and cassettes must be automatically unloaded from
     * incubators after the run.
     */
    Waiting_For_Automated_Unload("Waiting for Plates to be Automatically Unloaded After Run", "7"),
    
    /**
     * Indicates that all plates required to be returned after a run 
     * have been user-scanned and exactly match those required return
     * operations 
     */
    Verify_After_Run("Waiting for Plate Verification After Run", "8"),
    
    /**
     *  Run was executed and all cleanup/plate returns 
     *  successfully performed
     */
    Completed("Completed", null),
    
    /**
     * Indicates that the run has been cancelled either by the user or because
     * of an error.
     */
    Cancelled("Cancelled", null);

    private String description;
    private String stepNumber; 

    private AssayRunState(String description, String stepNumber)
    {
            this.description = description;
            this.stepNumber = stepNumber;  
    }	
    
    public String getDescription() {
    	return description;
    }
    
    public String getStepNumber() {
    	return stepNumber;
    }
}
