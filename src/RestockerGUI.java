public class RestockerGUI
{
	/**
	 * Main method, displays the GUI for the restocker specified on the
	 * command line.
	 * @param args Command line arguments:
	 * <ol>
	 * <li>ID of the vending machine</li>
	 * </ol>
	 **/
	public static void main(String[] args) throws Exception
	{
		GUIUtilities.setNativeLookAndFeel();
		BaseGUI base = new BaseGUI("Restocker Task List Screen");
		RestockerMachinePickerScreen controller = RestockerMachinePickerScreen.buildInstance();
		RestockerMachinePickerScreenGUI gui = new RestockerMachinePickerScreenGUI(controller);
		base.pushContentPanel(gui);
		base.displayGUI();
	}
}
