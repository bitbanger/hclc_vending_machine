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
		BaseGUI base = new BaseGUI("Restocker Machine Picker Screen");
		RestockerMachinePickerScreen controller = new  RestockerMachinePickerScreen();
		RestockerMachinePickerScreenGUI gui = new RestockerMachinePickerScreenGUI(controller, base);
		base.pushContentPanel(gui);
		base.displayGUI();
	}
}
