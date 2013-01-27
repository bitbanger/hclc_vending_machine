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

	private BaseGUI master;

	private JTextField idTextField;

	/**
	 * Creates the panel with the given controller instance.
	 * @param controller The controller instance to use.
	 **/
	public CustomerLoginScreenGUI(CustomerLoginScreen controller, BaseGUI master)
	{
		this.controller = controller;
		this.master = master;
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
		idTextField = new JTextField(20);
		idTextField.setMaximumSize(idTextField.getPreferredSize());
		idPanel.add(idTextField);

		// Add box id label and text box to login panel
		idPanel.setMaximumSize(idPanel.getPreferredSize());
		loginPanel.add(idPanel);

		// Gap between customer id text box and login button
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel to align the login button to the right
		JPanel loginButtonPanel = new JPanel();
		loginButtonPanel.setLayout(new BoxLayout(loginButtonPanel, BoxLayout.X_AXIS));

		// Aligns the button to the right
		loginButtonPanel.add(Box.createGlue());

		// Add the login button
		final JTextField tempField = idTextField;
		loginButton = new ConditionButton("Login")
		{
			/**
			 * The button should only be enabled if the idTextField has content.
			 * {@inheritDoc}
			 **/
			@Override
			public boolean checkCondition()
			{
				return tempField.getText().length() > 0;
			}
		};
		loginButton.addActionListener(this);
		loginButtonPanel.add(loginButton);
		loginPanel.add(loginButtonPanel);
		
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

		// Add the login panel to main panel
		loginPanel.setMaximumSize(loginPanel.getPreferredSize());
		this.add(loginPanel);

		// Gap between above and pay with cash button
		this.add(Box.createRigidArea(new Dimension(0,20)));

		// Pay with cash button
		cashButton = new JButton("Pay with cash");
		this.add(cashButton);
	}

	/**
	 * Handles the login and cash customer buttons getting pressed.
	 * @param event Contains information regarding the event that caused this
	 * action.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == loginButton)
		{
			CustomerPurchaseScreen next = controller.tryLogin(Integer.parseInt(idTextField.getText()));
			if (next == null)
			{
			}
			else
			{
				CustomerPurchaseScreenGUI nextGUI = new CustomerPurchaseScreenGUI(next, master);
				master.pushContentPanel(nextGUI);
			}
		}
	}
}
