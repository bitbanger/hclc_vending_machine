import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

/**
 * Frame that is used for all GUIs in this project. This helps keep a consistent
 * experience for the user.
 * Includes the status bar and a default size.
 **/
public class BaseGUI extends JFrame
{
	/**
	 * Default width for the window.
	 **/
	private static final int DEFAULT_WIDTH = 600;

	/**
	 * Default height for the window.
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
	 * Current content.
	 **/
	private JPanel contentPanel;

	/**
	 * Creates this GUI with the given title.
	 * @param title The title for the created window.
	 **/
	public BaseGUI(String title)
	{
		this.title = title;
		statusBar = new StatusBar();
		addBaseComponents();
	}

	/**
	 * Adds the status bar and sets the layout to a BorderLayout.
	 **/
	private void addBaseComponents()
	{
		setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		northPanel.add(statusBar);
		northPanel.setBorder(new EmptyBorder(10,10,30,10));
		add(northPanel, BorderLayout.NORTH);
	}


	/**
	 * Displays the GUI
	 **/
	public void displayGUI()
	{
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
	 * Swaps in the given panel as the center panel.
	 * @param content The panel to display
	 **/
	public void setContentPanel(JPanel content)
	{
		if (contentPanel != null)
			getContentPane().remove(contentPanel);
		contentPanel = content;
		contentPanel.setBorder(new EmptyBorder(0,10,10,10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		validate();
	}
}
