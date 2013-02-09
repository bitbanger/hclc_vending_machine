import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Changes the location of a vending machine.
 * @author Matthew Koontz
 **/
public class ManagerMachineManagementScreenChangeLocationGUI extends JPanel implements ActionListener
{
	/**
	 * Saves the data.
	 **/
	private JButton confirmButton;

	/**
	 * Cancels the changes.
	 **/
	private JButton cancelButton;

	/**
	 * Controller instance.
	 **/
	private ManagerMachineManagementScreen controller;

	/**
	 * Master frame
	 **/
	private BaseGUI master;

	/**
	 * Machine that is getting its location changed.
	 **/
	private VendingMachine machine;

	/**
	 * Allows the user to select a location.
	 **/
	private LocationPickerPanel locationPicker;

	/**
	 * Parent screen.
	 **/
	private ManagerMachineManagementScreenGUI parent;

	/**
	 * Creates a new GUI to change the location of the given vending machine.
	 * @param controller The controller instance for this view.
	 * @param master The master frame.
	 * @param machine The machine to change the location for.
	 * @param parent The parent screen. Used to refresh the list.
	 **/
	public ManagerMachineManagementScreenChangeLocationGUI(ManagerMachineManagementScreen controller, BaseGUI master, VendingMachine machine, ManagerMachineManagementScreenGUI parent)
	{
		this.controller=controller;
		this.master = master;
		this.machine = machine;
		this.parent = parent;

		confirmButton = new JButton("Confirm Location Change");
		cancelButton = new JButton("Cancel Location Change");

		locationPicker = new LocationPickerPanel(machine.getLocation());
		addComponents();
		addLogic();
	}

	/**
	 * Adds logic to the components.
	 **/
	private void addLogic()
	{
		cancelButton.addActionListener(this);
		confirmButton.addActionListener(this);
	}

	/**
	 * Lays out the components.
	 **/
	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createGlue());

		JPanel innerLayout = new JPanel();
		innerLayout.setLayout(new BoxLayout(innerLayout, BoxLayout.Y_AXIS));

		innerLayout.add(Box.createGlue());

		innerLayout.add(locationPicker);
		locationPicker.setAlignmentX(CENTER_ALIGNMENT);

		innerLayout.add(Box.createRigidArea(new Dimension(0, 50)));

		JPanel bottomPanel = new JPanel();
		innerLayout.add(bottomPanel);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(Box.createGlue());
		bottomPanel.add(cancelButton);
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		bottomPanel.add(confirmButton);
		bottomPanel.add(Box.createGlue());
		bottomPanel.setAlignmentX(CENTER_ALIGNMENT);

		innerLayout.setMaximumSize(innerLayout.getPreferredSize());

		innerLayout.setAlignmentY(CENTER_ALIGNMENT);

		add(innerLayout);

		add(Box.createGlue());
	}

	/**
	 * Handles buttons getting clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == confirmButton)
		{
			if (controller.changeMachineLocation(machine, locationPicker.getZipCode(), locationPicker.getState(), locationPicker.getNearbyBusinesses()))
				master.getStatusBar().setStatus("Location changed successfully", StatusBar.STATUS_GOOD_COLOR);
			else
				master.getStatusBar().setStatus("An error occurred while attempting to change the location", StatusBar.STATUS_BAD_COLOR);
			master.popContentPanel();
			parent.refreshList();
		}
		else if (source == cancelButton)
		{
			master.popContentPanel();
		}
	}
}
