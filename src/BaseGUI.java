import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

/**
 * Base GUI for all GUIs in this project.
 * Includes the status bar and a default size.
 **/
public abstract class BaseGUI extends JFrame
{
	/**
	 * Default width for windows.
	 **/
	private static final int DEFAULT_WIDTH = 600;

	/**
	 * Default height for windows.
	 **/
	private static final int DEFAULT_HEIGHT = 700;

	/**
	 * Status bar for this window.
	 **/
	private StatusBar statusBar;

	/**
	 * Title of this window.
	 **/
	private String title;

	/**
	 * Creates this GUI with the given title.
	 * @param title The title of the created GUI
	 **/
	public BaseGUI(String title)
	{
		this.title = title;
		statusBar = new StatusBar();
	}

	/**
	 * Adds the status bar and sets the layout to a BorderLayout.
	 **/
	public void addBaseComponents()
	{
		setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		northPanel.add(statusBar);
		northPanel.setBorder(new EmptyBorder(10,10,30,10));
		add(northPanel, BorderLayout.NORTH);
	}


	/**
	 * Adds the components to the window (by calling addBaseComponents and
	 * addComponents()) before displaying the GUI.
	 **/
	public void displayGUI()
	{
		addBaseComponents();
		addComponents();

		setVisible(true);
		setTitle(title);
		setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Gets the status bar associated with this GUI
	 * @return The status bar
	 **/
	public StatusBar getStatusBar()
	{
		return statusBar;
	}

	/**
	 * Adds the components specific to this GUI
	 **/
	public abstract void addComponents();

	/**
	 * Sets the border of the given panel to 10 pixels on all sides but the top.
	 * This looks nice for the center panel.
	 * @param panel The panel that is getting a border change.
	 **/
	public static void setProperBorder(JPanel panel)
	{
		panel.setBorder(new EmptyBorder(0,10,10,10));
	}
}
