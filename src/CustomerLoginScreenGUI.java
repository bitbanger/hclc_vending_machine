import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.BorderLayout;

/**
 * GUI for the customer login screen.
 * @author Matthew Koontz
 **/
public class CustomerLoginScreenGUI extends BaseGUI
{
	/**
	 * Controller instance for this screen.
	 **/
	private CustomerLoginScreen controller;

	/**
	 * Login by customer id button.
	 **/
	private JButton loginButton;

	/**
	 * Login as cash customer button.
	 **/
	private JButton cashButton;

	/**
	 * Creates and displays the GUI.
	 * @param controller The controller instance to use.
	 **/
	public CustomerLoginScreenGUI(CustomerLoginScreen controller)
	{
		super("Customer Login Screen");
		this.controller = controller;
	}

	/**
	 * Builds and displays the GUI.
	 **/
	public void addComponents()
	{
		// Components will be laid out vertically in the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		BaseGUI.setProperBorder(mainPanel);

		// Align the components to the left side
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);

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
		JTextField idTextField = new JTextField(20);
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
		loginButton = new JButton("Login");
		loginButton.setEnabled(false);
		loginButtonPanel.add(loginButton);
		loginPanel.add(loginButtonPanel);

		// Add the login panel to main panel
		loginPanel.setMaximumSize(loginPanel.getPreferredSize());
		mainPanel.add(loginPanel);

		// Gap between above and pay with cash button
		mainPanel.add(Box.createRigidArea(new Dimension(0,20)));

		// Pay with cash button
		cashButton = new JButton("Pay with cash");
		mainPanel.add(cashButton);

		// Add the main panel to the screen
		add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Main method, displays the GUI for the vending machine specified on the
	 * command line.
	 * @param args Command line arguments:
	 * <ol>
	 * <li>ID of the vending machine</li>
	 * </ol>
	 **/
	public static void main(String[] args)
	{
		GUIUtilities.setNativeLookAndFeel();
		CustomerLoginScreen controller = CustomerLoginScreen.buildInstance(Integer.parseInt(args[0]));
		CustomerLoginScreenGUI gui = new CustomerLoginScreenGUI(controller);
		gui.displayGUI();
	}
}
