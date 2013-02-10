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
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	 * Master for this panel.
	 */
	private BaseGUI master;

	/**
	 * List of machines.
	 */
	private JList machines;


	/**
	 * Creates the panel with the given controller instance.
	 * @param controller The controller instance to use.
	 * @param master The frame surrounding the panel
	 **/
	public RestockerMachinePickerScreenGUI(RestockerMachinePickerScreen controller, BaseGUI master)
	{
		master.getStatusBar().clearStatus();
		this.master = master;
		this.controller = controller;

		machines = new JList(controller.listActiveMachines().toArray());
		machines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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

		// Gap between label and text box
		idPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// Add a scroll pane
		JScrollPane scrollPanel = new JScrollPane(machines);
		this.add(scrollPanel);		

	
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

		
		// Add the login panel to main panel
		loginPanel.setMaximumSize(loginPanel.getPreferredSize());
		this.add(loginPanel);

		// Gap between above and pay with cash button
		this.add(Box.createRigidArea(new Dimension(0,20)));

		// Cancel button
		JButton	cancelButton = new JButton("Exit");
		
		// Exits the screen on cancel
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				master.setVisible(false);
				System.exit(1);
			}
		});

		// Gap between above and cancel button
		this.add(Box.createRigidArea(new Dimension(50,0)));

		// Done button
		ConditionButton selectButton = new ConditionButton("Select");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createGlue());  // Creating space between Select and Exit buttons
		buttonPanel.add(selectButton);
		this.add(buttonPanel);

		// Displays task into status bar	
		selectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				try {
					RestockerTaskListScreen next = controller.tryMachine( 
						((VendingMachine)machines.getSelectedValue()).getId());
					if ( next == null )
						master.getStatusBar().setStatus("Vending machine not found", StatusBar.STATUS_BAD_COLOR);
					else {
						RestockerTaskListScreenGUI nextGUI = new RestockerTaskListScreenGUI( next, master );
						master.pushContentPanel( nextGUI );
					}
				} catch (BadStateException impossible) {
					System.err.println("oops?");
					//I can't think of a way around this...
				}
			}
		});

		// Checks the condition of the done button
		final JList machineFinal = machines;
		selectButton.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition(){
				return machineFinal.getSelectedValue() != null;		
			}
		});

		
		// Grays out button if need be
		final ConditionButton selectButtonFinal = selectButton;
		machines.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e){
				selectButtonFinal.checkAndSetEnabled();
			}
		});

		JPanel toDoPanel = new JPanel();
		toDoPanel.setLayout(new BoxLayout(toDoPanel, BoxLayout.Y_AXIS));
		toDoPanel.setAlignmentX(RIGHT_ALIGNMENT);
	}
}
