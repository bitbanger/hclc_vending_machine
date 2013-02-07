import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Content panel for the customer login screen.
 * @author Matthew Koontz
 * @author Sol Boucher <slb1566@rit.edu>
 **/
public class ManagerLoginScreenGUI extends JPanel implements ActionListener
{
	/**
	 * Controller instance for this screen.
	 **/
	private ManagerLoginScreen controller;

	/**
	 * Login by customer id button.
	 **/
	private ConditionButton loginButton;

	/**
	 * Button to exit the application.
	 */
	private JButton exitButton;

	/**
	 * BaseGUI for this panel.
	 **/
	private BaseGUI master;

	/**
	 * Numeric field for the manager id.
	 **/
	private NumberField idTextField;

	/**
	 * Obscured field for the manager password.
	 */
	private JPasswordField passTextField;

	/**
	 * Creates the panel with the given controller instance.
	 * @param controller The controller instance to use.
	 **/
	public ManagerLoginScreenGUI(ManagerLoginScreen controller, BaseGUI master)
	{
		this.controller = controller;
		this.master = master;

		loginButton = new ConditionButton("Login");
		idTextField = new NumberField(NumberField.POSITIVE_Z);
		passTextField = new JPasswordField();

		master.getStatusBar().clearStatus();
		addComponents();
	}

	/**
	 * Lays out the components on the panel.
	 **/
	public void addComponents()
	{
		// Components will be laid out vertically in the main panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Align the components to the left side
		this.setAlignmentX(LEFT_ALIGNMENT);

		// Panel to hold id text box and login button
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.setAlignmentX(LEFT_ALIGNMENT);

		// Panel to hold id label and text box
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));

		// Label for manager id text box
		JLabel idLabel = new JLabel("Enter ID:");
		idPanel.add(idLabel);
		idPanel.add(Box.createHorizontalStrut(62)); // Creates space between ID label and text field

		// Gap between label and text box
		idPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// Text field for manager id
		idTextField.setColumns(20);
		idTextField.setMaximumSize(idTextField.getPreferredSize());
		
		// Panel to hold password label and text box
		JPanel passPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
		
		// Label for password text box
		JLabel passLabel = new JLabel("Enter password:");
		passPanel.add(passLabel);
		
		// Gap between label and text box
		passPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		// Text field for manager password
		passTextField.setColumns(20);
		passTextField.setMaximumSize(passTextField.getPreferredSize());
		
		// Make the id text field notify the login button when it is changed
		final ConditionButton temp = loginButton;
		idTextField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void changedUpdate(DocumentEvent e)
			{
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				temp.checkAndSetEnabled();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				temp.checkAndSetEnabled();
			}
		});
		idPanel.add(idTextField);
		passPanel.add(passTextField);

		// Add box id label and text box to login panel
		idPanel.setMaximumSize(idPanel.getPreferredSize());
		loginPanel.add(idPanel);
		
		// Gap between above and pay with cash button
		// Gap between id and password panels
		this.add(Box.createRigidArea(new Dimension(0,20)));

		// Add password label and text box to login panel
		passPanel.setMaximumSize(passPanel.getPreferredSize());
		loginPanel.add(passPanel);
		
		// Gap between customer id text box and login button
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel to align the login button to the right
		JPanel loginButtonPanel = new JPanel();
		loginButtonPanel.setLayout(new BoxLayout(loginButtonPanel, BoxLayout.X_AXIS));

		// Add the login button
		final NumberField tempField = idTextField;
		loginButton.addCondition(new ConditionButtonCondition()
		{
			/**
			 * The button should only be enabled if the idTextField has content.
			 **/
			@Override
			public boolean checkCondition()
			{
				return tempField.areContentsValid();
			}
		});
		loginButton.addActionListener(this);
		loginButtonPanel.add(loginButton);
		loginPanel.add(loginButtonPanel);

		// Add the login panel to main panel
		loginPanel.setMaximumSize(loginPanel.getPreferredSize());
		this.add(loginPanel);
		
		// Add the exit button
		exitButton = new JButton("Exit");
		loginButtonPanel.add(Box.createHorizontalStrut(270));  // Spaces out the buttons
		loginButtonPanel.add(exitButton);
		
		// Add the exit button ActionListener
		exitButton.addActionListener(new ActionListener() 
		{
			/**
			 * If the user clicks the exit buton, the application will exit
			 */
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (event.getSource() == exitButton)
				{
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Handles the login and cash customer buttons getting pressed.
	 * @param event Contains information regarding the event that caused this
	 * action.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		// Get the next controller
		ManagerHomeScreen next = controller.tryLogin(Integer.parseInt(idTextField.getText()), new String(passTextField.getPassword()));
		
		passTextField.setText(""); //clear password
		
		// If the credentials didn't use an auth token
		if (next == null)
		{
			master.getStatusBar().setStatus("Invalid username/password combo", StatusBar.STATUS_BAD_COLOR);
		}
		else
		{
			ManagerHomeScreenGUI nextGUI = new ManagerHomeScreenGUI(next, master);
			idTextField.clearNumberEntered(); //clear ID
			
			master.pushContentPanel(nextGUI);
		}
	}
}
