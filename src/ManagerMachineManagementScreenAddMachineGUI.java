import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.util.ArrayList;

/**
 * Panel that allows a manager to add machines. Used in the machine management
 * GUI.
 * @author Matthew Koontz
 **/
public class ManagerMachineManagementScreenAddMachineGUI extends JPanel implements ActionListener
{
	/**
	 * Controller instance for this view.
	 **/
	private ManagerMachineManagementScreen controller;

	/**
	 * The master GUI this panel is in.
	 **/
	private BaseGUI master;

	/**
	 * The parent GUI for this panel. Used to update parent list upon exit.
	 **/
	private ManagerMachineManagementScreenGUI parent;

	/**
	 * The layout of the machines already in the database. Null if none
	 * exist.
	 **/
	private VMLayout oldLayout;

	/**
	 * The panel that allows the manager to choose a location.
	 **/
	private LocationPickerPanel locationPicker;

	/**
	 * Field for the manager to enter the stocking interval.
	 **/
	private StockingIntervalField stockingIntervalField;

	/**
	 * Field for the manager to enter the number of rows in the vending machine.
	 **/
	private NumberField rowField;

	/**
	 * Field for the manager to enter the number of columns in the vending machine.
	 **/
	private NumberField colField;

	/**
	 * Field for the manager to enter the depth of the vending machine.
	 **/
	private NumberField depthField;

	/**
	 * Button that confirm the addition of the machine.
	 **/
	private ConditionButton confirmButton;

	/**
	 * Cancels the creation of the new machine and brings the manager back to
	 * the previous screen.
	 **/
	private JButton cancelButton;

	/**
	 * Creates a GUI to add a new machine.
	 * @param controller Controller instance for this view.
	 * @param master GUI frame this panel is contained in.
	 * @param parent The ManagerMachineManagementScreenGUI instance that created
	 * this panel.
	 **/
	public ManagerMachineManagementScreenAddMachineGUI(ManagerMachineManagementScreen controller, BaseGUI master, ManagerMachineManagementScreenGUI parent)
	{
		// Set private instance variables
		this.controller = controller;
		this.master = master;
		this.parent = parent;

		// Create GUI components
		locationPicker = new LocationPickerPanel(null);
		stockingIntervalField = new StockingIntervalField(controller, master.getStatusBar());
		rowField = new NumberField(NumberField.POSITIVE_Z);
		colField = new NumberField(NumberField.POSITIVE_Z);
		depthField = new NumberField(NumberField.POSITIVE_Z);
		confirmButton = new ConditionButton("Add Machine");
		cancelButton = new JButton("Cancel");

		// Add components and logic to panel
		addComponents();
		addLogic();
	}

	/**
	 * Adds logic to the components of the GUI.
	 **/
	private void addLogic()
	{
		// Make the condition button enabled iff all of the values on the form
		// are valid.
		confirmButton.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				return stockingIntervalField.areContentsValid() && rowField.areContentsValid() && colField.areContentsValid() && depthField.areContentsValid();
			}
		});

		// Make the confirm button watch all of the components that could change the
		// enabled condition.
		locationPicker.haveWatched(confirmButton, master.getStatusBar());
		confirmButton.watch(stockingIntervalField);
		confirmButton.watch(rowField);
		confirmButton.watch(colField);
		confirmButton.watch(depthField);

		// Set the fields if there is already a table in the database
		ArrayList<VendingMachine> machines = controller.listMachinessAll();
		if (machines.size() == 0)
		{
			oldLayout = null;
			rowField.setText(VMLayout.DEFAULT_HEIGHT+"");
			colField.setText(VMLayout.DEFAULT_WIDTH+"");
			depthField.setText(VMLayout.DEFAULT_DEPTH+"");
		}
		else
		{
			// Set old layout to the first machine's next layout because all
			// machines must have the same next layout.
			VendingMachine machine = machines.get(0);
			oldLayout = machine.getNextLayout();

			// Get the attributes of the layout
			int rows = oldLayout.getRows()[0].length;
			int cols = oldLayout.getRows().length;
			int depth = oldLayout.getDepth();

			// Display the information to the manager.
			rowField.setText(rows+"");
			colField.setText(cols+"");
			depthField.setText(depth+"");

			// Don't allow him to change the information
			rowField.setEnabled(false);
			colField.setEnabled(false);
			depthField.setEnabled(false);
		}

		// Set the action listeners of the buttons
		cancelButton.addActionListener(this);
		confirmButton.addActionListener(this);
	}

	/**
	 * Lays out the components on the panel.
	 **/
	private void addComponents()
	{
		// Set the layout of this panel to a vertical layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Add the location picker
		locationPicker.setAlignmentX(LEFT_ALIGNMENT);
		add(locationPicker);

		//add(Box.createRigidArea(new Dimension(0, 50)));

		// Make the panel to be displayed below the location picker.
		// It will contain the other fields and the confirm and cancel buttons.
		// It will have a vertical layout.
		JPanel rightPanel = new JPanel();
		rightPanel.setAlignmentX(LEFT_ALIGNMENT);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		add(rightPanel);

		// Make the panel to hold the other fields (stocking interval, rows, etc.)
		// It will have a grid bag layout.
		LabeledFieldPanel attributePanel = locationPicker.getZipAndStatePanel();
		attributePanel.setAlignmentX(LEFT_ALIGNMENT);

		// Add the field for stocking interval.
		stockingIntervalField.setColumns(12);
		stockingIntervalField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)stockingIntervalField.getPreferredSize().getHeight()));
		attributePanel.addLabeledTextField("Stocking interval (days):", stockingIntervalField);
		
		// Add the field for rows
		rowField.setColumns(12);
		rowField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)rowField.getPreferredSize().getHeight()));
		attributePanel.addLabeledTextField("Number of rows:", rowField);
		
		// Add the field for columns.
		colField.setColumns(12);
		colField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)colField.getPreferredSize().getHeight()));
		attributePanel.addLabeledTextField("Number of columns:", colField);

		// Add the field for depth.
		depthField.setColumns(12);
		depthField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)depthField.getPreferredSize().getHeight()));
		attributePanel.addLabeledTextField("Depth of machine:", depthField);

		// Space between fields and buttons.
		rightPanel.add(Box.createGlue());

		// Create panel to hold the cancel and confirm buttons.
		JPanel bottomPanel = new JPanel();
		bottomPanel.setAlignmentX(LEFT_ALIGNMENT);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		rightPanel.add(bottomPanel);

		// Helps center align the buttons
		bottomPanel.add(Box.createGlue());

		// Add the cancel button
		bottomPanel.add(cancelButton);

		// Space between buttons.
		bottomPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		// Add the confirmation button.
		bottomPanel.add(confirmButton);

		// Helps center align the buttons
		bottomPanel.add(Box.createGlue());
	}

	/**
	 * Handles the buttons getting pressed.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();

		// If the cancel button was pressed then return to the previous panel.
		if (source == cancelButton)
		{
			master.popContentPanel();
			master.getStatusBar().setStatus("Machine creation canceled", StatusBar.STATUS_WARN_COLOR);
		}

		// If the confirm button was pressed then add the new machine to the
		// database, set an appropriate status, and go back to the previous
		// screen.
		if (source == confirmButton)
		{
			// If the old layout was null then we need to make one from the
			// manager's input.
			if (oldLayout == null)
			{
				try
				{
					oldLayout=new VMLayout(colField.getNumber(), rowField.getNumber(), depthField.getNumber());
				}
				catch(BadArgumentException no)
				{
					ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.FATAL, no);
				}
			}

			// Try adding the machine. If it works then display a success
			// message. If it fails then display an error message.
			if (controller.addMachine(locationPicker.getZipCode(), locationPicker.getState(), locationPicker.getNearbyBusinesses(), stockingIntervalField.getNumber(), oldLayout) != -1)
			{
				master.popContentPanel();
				master.getStatusBar().setStatus("Machine added successfully!", StatusBar.STATUS_GOOD_COLOR);
			}
			else
			{
				master.popContentPanel();
				master.getStatusBar().setStatus("An error occurred while attempting to add the machine", StatusBar.STATUS_BAD_COLOR);
			}
			// Refresh the list on the ManagerMachineManagementScreenGUI and go
			// back to it.
			parent.refreshList();
		}
	}
}
