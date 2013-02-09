import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

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
	 * Changes the stocking interval of the selected machine.
	 **/
	private ConditionButton intervalButton;

	/**
	 * List of machines.
	 **/
	private JList machineList;

	/**
	 * Button that brings the user back to home.
	 **/
	private JButton exitButton;

	/**
	 * the prior view
	 */
	private ManagerHomeScreenGUI prior;

	/**
	 * Creates the panel with the given controller and frame.
	 * @param controller The controller for this view.
	 * @param master The BaseGUI instance this panel is contained in.
	 **/
	public ManagerMachineManagementScreenGUI(ManagerMachineManagementScreen controller, BaseGUI master, ManagerHomeScreenGUI last)
	{
		this.controller = controller;
		this.master = master;
		prior = last;

		master.setTitle("Machine Management");

		master.getStatusBar().clearStatus();

		addMachineButton = new JButton("Add Machine");
		deactivateButton = new ConditionButton("Deactivate Machine");
		reactivateButton = new ConditionButton("Reactivate Machine");
		locationButton = new ConditionButton("Change Machine Location");
		intervalButton = new ConditionButton("Change Machine Stocking Interval");
		machineList = new JList();
		exitButton = new JButton("Return to Home Screen");

		addComponents();
		addLogic();
		refreshList();
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
		buttonPanel.add(intervalButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createGlue());

		add(Box.createGlue());
		
		machineList.setMaximumSize(new Dimension((int)machineList.getPreferredSize().getHeight(), Integer.MAX_VALUE));
		add(new JScrollPane(machineList));
	}

	/**
	 * Refreshes the data in the list.
	 **/
	public void refreshList()
	{
		machineList.setListData(new Vector<VendingMachine>(controller.listMachinessAll()));
	}

	/**
	 * Adds the logic to the buttons and lists.
	 **/
	private void addLogic()
	{
		// Make final copies for anonymous classes
		final ConditionButton deactivateButtonTemp = deactivateButton;
		final ConditionButton reactivateButtonTemp = reactivateButton;
		final ConditionButton locationButtonTemp = locationButton;
		final ConditionButton intervalButtonTemp = intervalButton;
		final JList machineListTemp = machineList;

		// Add action listeners for buttons
		addMachineButton.addActionListener(this);
		deactivateButton.addActionListener(this);
		reactivateButton.addActionListener(this);
		locationButton.addActionListener(this);
		intervalButton.addActionListener(this);
		exitButton.addActionListener(this);

		// Deactivate button should be enabled iff an active vending machine is selected
		deactivateButton.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				VendingMachine selected = (VendingMachine)machineListTemp.getSelectedValue();
				if (selected == null)
					return false;
				return selected.isActive();
			}
		});

		// Reactivate button should be enabled iff an inactive vending machine is selected
		reactivateButton.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				VendingMachine selected = (VendingMachine)machineListTemp.getSelectedValue();
				if (selected == null)
					return false;
				return !selected.isActive();
			}
		});

		// Location button should be active iff a vending machine is selected
		locationButtonTemp.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				return machineListTemp.getSelectedValue() != null;
			}
		});
		
		// Interval button should be active iff a vending machine is selected
		intervalButtonTemp.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				return machineListTemp.getSelectedValue() != null;
			}
		});

		// Makes changes in the list recheck the conditions
		machineList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent _)
			{
				deactivateButtonTemp.checkAndSetEnabled();
				reactivateButtonTemp.checkAndSetEnabled();
				locationButtonTemp.checkAndSetEnabled();
				intervalButtonTemp.checkAndSetEnabled();
			}
		});
	}

	/**
	 * Handles the buttons getting pressed.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();

		// Deactivate button
		if (source == deactivateButton)
		{
			if (controller.deactivateMachine((VendingMachine)machineList.getSelectedValue()))
				master.getStatusBar().setStatus("Machine deactivated successfully", StatusBar.STATUS_GOOD_COLOR);
			else
				master.getStatusBar().setStatus("An error occurred while trying to deactivate the machine", StatusBar.STATUS_BAD_COLOR);
		}

		// Reactivate button
		else if (source == reactivateButton)
		{
			if (controller.reactivateMachine((VendingMachine)machineList.getSelectedValue()))
				master.getStatusBar().setStatus("Machine reactivated successfully", StatusBar.STATUS_GOOD_COLOR);
			else
				master.getStatusBar().setStatus("An error occurred while trying to reactivate the machine", StatusBar.STATUS_BAD_COLOR);
		}

		// Set location button
		else if (source == locationButton)
		{
			ManagerMachineManagementScreenChangeLocationGUI setLocationPanel = new ManagerMachineManagementScreenChangeLocationGUI(controller, master, (VendingMachine)machineList.getSelectedValue(), this);
			master.pushContentPanel(setLocationPanel);
		}
		
		// Set interval button
		else if (source == intervalButton)
		{
			ManagerMachineManagementScreenChangeIntervalGUI nextGUI = new ManagerMachineManagementScreenChangeIntervalGUI(controller, master, (VendingMachine)machineList.getSelectedValue(), this);
			master.pushContentPanel(nextGUI);
		}

		// Add machine button
		else if (source == addMachineButton)
		{
			ManagerMachineManagementScreenAddMachineGUI nextGUI = new ManagerMachineManagementScreenAddMachineGUI(controller, master, this);
			master.pushContentPanel(nextGUI);
		}

		// Return home button
		else if (source == exitButton)
		{
			master.getStatusBar().clearStatus();
			master.popContentPanel();
			master.setTitle("Home Screen");
			prior.refreshYourself();
		}

		// Update data in list
		refreshList();
	}
}
