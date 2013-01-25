import javax.swing.UIManager;

/**
 * Provides static methods that perform various GUI related tasks.
 * @author Matthew Koontz
 **/
public class GUIUtilities
{
	/**
	 * Private empty constructor to prevent creation.
	 **/
	private GUIUtilities() {}

	/**
	 * Sets the look and feel to the system's look and feel.
	 **/
	public static void setNativeLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex) {}
	}
}
