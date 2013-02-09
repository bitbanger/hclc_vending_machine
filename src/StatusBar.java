import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Class for the status bar that exists at the top of all of the GUIs.
 * @author Matthew Koontz
 **/
public class StatusBar extends JPanel
{
	/**
	 * Color the status bar will be when not reporting errors.
	 **/
	public static final Color STATUS_GOOD_COLOR = Color.green;

	/**
	 * Color the status bar will be when reporting errors.
	 **/
	public static final Color STATUS_BAD_COLOR = Color.red;
	
	/**
	 * Color the status bar will be when warning the user.
	 **/
	public static final Color STATUS_WARN_COLOR = Color.yellow;
	
	/**
	 * Priority for messages informing the user that their input is invalid.
	 **/
	public static final int PRIORITY_INVALID_INPUT = 0;
	
	/**
	 * Priority for messages informing the user that their action has been rejected by the system for causing an invalid state.
	 * An example of an invalid state is altering the stocking interval such that items would expire before a visit.
	 **/
	public static final int PRIORITY_REJECTED_CONFIG = 1;

	/**
	 * Label that holds the text for the status bar
	 **/
	private JLabel statusLabel;
	
	/**
	 * The current message's priority
	 **/
	private int currentPriority;

	/**
	 * Creates the status bar with default text and good color
	 **/
	public StatusBar()
	{
		setBackground(STATUS_GOOD_COLOR);
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		statusLabel = new JLabel("status");
		add(statusLabel);
	}
	
	/**
	 * Sets the status text and color of the status bar, as well as the priority of the message.
	 * @param status	The string to display.
	 * @param color		The color of the status bar.
	 * @param priority	The priority of the message.
	 **/
	public void setStatus(String status, Color color, int priority) {
		statusLabel.setText(status);
		setBackground(color);
		currentPriority = priority;
	}

	/**
	 * Sets the status text and color of the status bar.
	 * @param status	The string to display.
	 * @param color		The color of the status bar.
	 **/
	public void setStatus(String status, Color color)
	{
		statusLabel.setText(status);
		setBackground(color);
		currentPriority = 0;
	}
	
	/**
	 * Sets the status text of the status bar, as well as the priority of the message.
	 * @param status	The string to display.
	 * @param priority	The priority of the message.
	 **/
	public void setStatus(String status, int priority) {
		statusLabel.setText(status);
		currentPriority = priority;
	}

	/**
	 * Sets the status text of the status bar.
	 * @param status	The string to display.
	 **/
	public void setStatus(String status)
	{
		statusLabel.setText(status);
		currentPriority = 0;
	}

	/**
	 * Sets the text in the status bar to " ", regardless of priority.
	 **/
	public void clearStatus()
	{
		setStatus(" ", STATUS_GOOD_COLOR);
	}
	
	/**
	 * Sets the text in the status bar to " " if the current message's priority is less than or equal to the given priority.
	 * 
	 * @param priority	The given priority.
	 **/
	public void clearStatus(int priority)
	{
		if(priority >= currentPriority)
		{
			setStatus(" ", STATUS_GOOD_COLOR);
		}
	}

	/**
	 * Sets the color of the status bar.
	 * @param color The color to set the status bar to.
	 **/
	public void setColor(Color color)
	{
		setBackground(color);
	}
}
