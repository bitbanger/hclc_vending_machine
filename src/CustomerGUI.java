public class CustomerGUI
{
	/**
	 * Main method, displays the GUI for the vending machine specified on the
	 * command line.
	 * @param args Command line arguments:
	 * <ol>
	 * <li>ID of the vending machine</li>
	 * </ol>
	 **/
	public static void main(String[] args) throws Exception
	{
		GUIUtilities.setNativeLookAndFeel();
		BaseGUI base = new BaseGUI("Login Screen");
		CustomerMachinePickerScreen controller = new CustomerMachinePickerScreen();
		CustomerMachinePickerScreenGUI gui = new CustomerMachinePickerScreenGUI(controller, base);
		base.pushContentPanel(gui);
		base.displayGUI();
	}
}
