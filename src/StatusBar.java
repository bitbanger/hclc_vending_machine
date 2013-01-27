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
	 * Label that holds the text for the status bar
	 **/
	private JLabel statusLabel;

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
	 * Sets the status text of the status bar.
	 * @param status The string to display
	 **/
	public void setStatus(String status, Color color)
	{
		statusLabel.setText(status);
		setBackground(color);
	}

	/**
	 * Sets the status text of the status bar.
	 * @param status The string to display
	 **/
	public void setStatus(String status)
	{
		statusLabel.setText(status);
	}

	/**
	 * Sets the text in the status bar to ""
	 **/
	public void clearStatus()
	{
		setStatus(" ", STATUS_GOOD_COLOR);
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
