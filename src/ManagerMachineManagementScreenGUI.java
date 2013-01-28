import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;

/**
 * Screen that allows the manager to manage machines
 * @author Matthew Koontz
 **/
public class ManagerMachineManagementScreenGUI extends JPanel implements ActionListener
{
	/**
	 * Controller for this view.
	 **/
	private ManagerMachineManagementScreen controller;

	/**
	 * Frame this panel is contained in.
	 **/
	private BaseGUI master;

	/**
	 * Button that adds a machine to the system.
	 **/
	private JButton addMachineButton;

	/**
	 * Deactivates the selected machine.
	 **/
	private ConditionButton deactivateButton;

	/**
	 * Reactivates the selected machine.
	 **/
	private ConditionButton reactivateButton;

	/**
	 * Changes the location of the selected machine.
	 **/
	private ConditionButton locationButton;

	/**
	 * List of machines.
	 **/
	private JList machineList;

	/**
	 * Button that brings the user back to home.
	 **/
	private JButton exitButton;

	/**
	 * Creates the panel with the given controller and frame.
	 * @param controller The controller for this view.
	 * @param master The BaseGUI instance this panel is contained in.
	 **/
	public ManagerMachineManagementScreenGUI(ManagerMachineManagementScreen controller, BaseGUI master)
	{
		this.controller = controller;
		this.master = master;

		master.getStatusBar().clearStatus();

		addMachineButton = new JButton("Add Machine");
		deactivateButton = new ConditionButton("Deactivate Machine");
		reactivateButton = new ConditionButton("Reactivate Machine");
		locationButton = new ConditionButton("Change Machine Location");
		machineList = new JList();
		exitButton = new JButton("Back To Home");

		addComponents();
	}
	
	/**
	 * Adds the components to the screen.
	 **/
	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		add(buttonPanel);

		buttonPanel.add(Box.createGlue());
		buttonPanel.add(addMachineButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(deactivateButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(reactivateButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(locationButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createGlue());

		add(Box.createGlue());
		
		machineList.setListData(new Vector<VendingMachine>(controller.listMachinessAll()));
		machineList.setMaximumSize(new Dimension((int)machineList.getPreferredSize().getHeight(), Integer.MAX_VALUE));
		add(machineList);
	}

	/**
	 * Handles the buttons getting pressed.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{

	}
}
