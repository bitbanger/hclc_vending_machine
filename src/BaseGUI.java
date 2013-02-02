import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.util.Stack;

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
	 * Stack of content panels.
	 **/
	private Stack<JPanel> contentStack;

	/**
	 * Creates this GUI with the given title.
	 * @param title The title for the created window.
	 **/
	public BaseGUI(String title)
	{
		this.title = title;
		contentStack = new Stack<JPanel>();
		statusBar = new StatusBar();
		NumberField.setBar(statusBar);
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
		pack();
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
	 * Pushes the panel content onto the stack of panels and displays it.
	 * @param content The panel to display
	 **/
	public void pushContentPanel(JPanel content)
	{
		try
		{
			JPanel currentPanel = contentStack.peek();
			if (currentPanel != null)
				getContentPane().remove(currentPanel);
		}
		catch (java.util.EmptyStackException ex) {}
		contentStack.push(content);
		displayTop();
	}

	/**
	 * Pops the top off the stack of content panels and displays the next one.
	 **/
	public void popContentPanel()
	{
		JPanel currentPanel = contentStack.peek();
		if (currentPanel != null)
			getContentPane().remove(currentPanel);
		contentStack.pop();
		displayTop();
	}

	/**
	 * Displays the top of the stack.
	 * Pre-conditions: The last content panel to be displayed must have been
	 * removed (i.e. nothing should be currently displayed).
	 **/
	private void displayTop()
	{
		JPanel currentPanel = contentStack.peek();
		currentPanel.setBorder(new EmptyBorder(0,10,10,10));
		getContentPane().add(currentPanel, BorderLayout.CENTER);
		currentPanel.repaint();
		validate();
		pack();
	}
}
