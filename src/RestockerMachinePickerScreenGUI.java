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

/**
 * Content panel for the restocker task machine picker screen.
 * @author Piper Chester
 **/
public class RestockerMachinePickerScreenGUI extends JPanel
{
	/**
	 * Controller instance for this screen.
	 **/
	private RestockerMachinePickerScreen controller;
	

	/**
	 * Array of tasks to perform.
	 */
	private String[] tasks;

	/**
	 * Creates the panel with the given controller instance.
	 * @param controller The controller instance to use.
	 **/
	public RestockerMachinePickerScreenGUI(RestockerMachinePickerScreen controller)
	{
		this.controller = controller;
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
		JLabel idLabel = new JLabel("Requested Changes");
		idPanel.add(idLabel);

		// Gap between label and text box
		idPanel.add(Box.createRigidArea(new Dimension(10, 0)));


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

		
		// Make the id text field notify the login button when it is changed
	//	final ConditionButton temp = loginButton;
	//	idTextField.getDocument().addDocumentListener(new DocumentListener()
	//	{
	//		@Override
	//		public void changedUpdate(DocumentEvent e)
	//		{
	//		}

	//		@Override
	//		public void insertUpdate(DocumentEvent e)
	//		{
	//			temp.checkAndSetEnabled();
	//		}

	//		@Override
	//		public void removeUpdate(DocumentEvent e)
	//		{
	//			temp.checkAndSetEnabled();
	//		}
	//	});

		// Add the login panel to main panel
		loginPanel.setMaximumSize(loginPanel.getPreferredSize());
		this.add(loginPanel);

		// Gap between above and pay with cash button
		this.add(Box.createRigidArea(new Dimension(0,20)));

		// Cancel button
		JButton	cancelButton = new JButton("Cancel");
		this.add(cancelButton);

		// Gap between above and cancel button
		this.add(Box.createRigidArea(new Dimension(50,0)));

		// Done button
		JButton doneButton = new JButton("Done");
		this.add(doneButton);

		JPanel toDoPanel = new JPanel();
		toDoPanel.setLayout(new BoxLayout(toDoPanel, BoxLayout.Y_AXIS));
		toDoPanel.setAlignmentX(RIGHT_ALIGNMENT);

	}
}
