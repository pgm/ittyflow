package sample.altsyntax;

import org.junit.Test;


public class TestWizard {
	@Test
	public void testWizard()
	{
		WizardWorkflow ww = new WizardWorkflow();
		WizardExecution we = new WizardExecution();
		ww.signal(we).clickedDone(null);
		ww.signal(we).clickedDone(null);
		ww.signal(we).clickedDone(null);
	}
}
