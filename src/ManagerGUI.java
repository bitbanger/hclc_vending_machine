/**
 * A GUI for restockers.
 * Does that help?
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class ManagerGUI
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
		BaseGUI base = new BaseGUI("Manager GUI");
		ManagerLoginScreen controller = new ManagerLoginScreen();
		ManagerLoginScreenGUI gui = new ManagerLoginScreenGUI(controller, base);
		base.pushContentPanel(gui);
		base.displayGUI();
	}
}
