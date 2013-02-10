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
 **/
public class CustomerLoginScreenGUI extends JPanel implements ActionListener
{
	/**
	 * Controller instance for this screen.
	 **/
	private CustomerLoginScreen controller;

	/**
	 * Login by customer id button.
	 **/
	private ConditionButton loginButton;

	/**
	 * Login as cash customer button.
	 **/
	private JButton cashButton;

	/**
	 * Button to exit the application.
	 */
	private JButton exitButton;

	/**
	 * BaseGUI for this panel.
	 **/
	private BaseGUI master;

	/**
	 * Text field for the customer id.
	 **/
	private NumberField idTextField;

	/**
	 * Creates the panel with the given controller instance.
	 * @param controller The controller instance to use.
	 **/
	public CustomerLoginScreenGUI(CustomerLoginScreen controller, BaseGUI master)
	{
		master.getStatusBar().clearStatus();
		this.controller = controller;
		this.master = master;

		master.setTitle("Login Screen");

		loginButton = new ConditionButton("Login");
		idTextField = new NumberField(NumberField.POSITIVE_Z);

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

		// Label for customer id text box
		JLabel idLabel = new JLabel("Enter ID");
		idPanel.add(idLabel);

		// Gap between label and text box
		idPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// Text field for customer id
		idTextField.setColumns(20);
		idTextField.setMaximumSize(idTextField.getPreferredSize());
		
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

		// Add box id label and text box to login panel
		idPanel.setMaximumSize(idPanel.getPreferredSize());
		idPanel.setAlignmentX(CENTER_ALIGNMENT);
		loginPanel.add(idPanel);

		// Gap between customer id text box and login button
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel to align the login button to the right
		JPanel loginButtonPanel = new JPanel();
		loginButtonPanel.setLayout(new BoxLayout(loginButtonPanel, BoxLayout.X_AXIS));

		// Aligns the button to the right
		loginButtonPanel.add(Box.createGlue());

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
		idTextField.addActionListener(this);
		loginButtonPanel.add(loginButton);
		loginPanel.add(loginButtonPanel);
		
		loginPanel.setAlignmentX(CENTER_ALIGNMENT);
		

		// Add the login panel to main panel
		loginPanel.setMaximumSize(loginPanel.getPreferredSize());
		this.add(loginPanel);

		// Gap between above and pay with cash button
		this.add(Box.createRigidArea(new Dimension(0,20)));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		// Pay with cash button
		cashButton = new JButton("Cash Payment");
		cashButton.addActionListener(this);

		// Gap between cash and exit button

		// Exit button
		exitButton = new JButton("Back to Selector");
		exitButton.addActionListener(this);
		
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(50,0)));
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(cashButton);
		
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		this.add(buttonPanel);
		
	}

	/**
	 * Handles the login and cash customer buttons getting pressed.
	 * @param event Contains information regarding the event that caused this
	 * action.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		// If the login button was pressed
		if (event.getSource() == loginButton || idTextField.areContentsValid() && event.getSource() == idTextField)
		{
			
			// Get the next controller
			CustomerPurchaseScreen next = controller.tryLogin(Integer.parseInt(idTextField.getText()));

			// If the custoker id was not found
			if (next == null)
			{
				master.getStatusBar().setStatus("Customer id not found", StatusBar.STATUS_BAD_COLOR);
			}
			else
			{
				// Clear the id text
				idTextField.clearNumberEntered();

				// Set up the next screen
				CustomerPurchaseScreenGUI nextGUI = new CustomerPurchaseScreenGUI(next, master);

				// Display the next screen
				master.pushContentPanel(nextGUI);
			}
		}
		else if (event.getSource() == cashButton)
		{
			CashCustomerPurchaseScreen next = controller.cashLogin();
			idTextField.clearNumberEntered();
			CashCustomerPurchaseScreenGUI nextGUI = new CashCustomerPurchaseScreenGUI(next, master);
			master.pushContentPanel(nextGUI);
		}
		else if (event.getSource() == exitButton)
		{
			master.getStatusBar().clearStatus();
			master.popContentPanel();
		}
	}
}
