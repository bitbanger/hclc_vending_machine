import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import java.awt.GridLayout;

/**
 * Content panel for the manger's alter layout screen.
 * @author Piper Chester
 **/
public class ManagerAlterLayoutScreenGUI extends JPanel implements ActionListener
{
	/**
	 * The controller for this screen.
	 **/
	private ManagerAlterLayoutScreen controller;

	/**
	 * Panel that allows a user to select an item.
	 */
	private VMLayoutPanel vmButtons;

	/**
	 * The frame this panel is in.
	 **/
	private BaseGUI master;

	/**
	 * The list of available items.
	 */
	private JList stockableItems;

	/**
	 * The button that allows the manager to change row.
	 **/
	private ConditionButton changeRowButton;

	/**
	 * The button that allows the manager to empty row.
	 **/
	private ConditionButton emptyRowButton;

	/**
	 * The button that allows the manager to commit changes.
	 **/
	private JButton commitChangesButton;

	/**
	 * The button that allows the manager to log out.
	 **/
	private JButton logoutButton;

	/**
	 * Creates a new manager home screen panel with the given controller and
	 * BaseGUI.
	 * @param controller The controller for this home screen.
	 * @param master The BaseGUI this panel will be inside.
	 **/
	public ManagerAlterLayoutScreenGUI(ManagerAlterLayoutScreen controller, BaseGUI master)
	{
		master.getStatusBar().clearStatus();
		this.controller = controller;
		this.master = master;

		master.setTitle("Layout Management");

		vmButtons = new VMLayoutPanel(controller.listRows(), true); 
		stockableItems = new JList(controller.listItems().toArray());
	
		changeRowButton = new ConditionButton("Change Row");
		emptyRowButton = new ConditionButton("Empty Row");
		commitChangesButton = new JButton("Commit Layout Changes");
		logoutButton = new JButton("Return to Home Screen");

		addComponents();
		addLogic();
	}

	/**
	 * Adds logic to the components.
	 **/
	private void addLogic()
	{
		// Only show the change row button when an item and a row is selected
		changeRowButton.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				return !stockableItems.isSelectionEmpty() && vmButtons.getSelectedRow() != null;
			}
		});

		// Make the change row button 'watch' the list and layout
		changeRowButton.watch(stockableItems);
		changeRowButton.watch(vmButtons);

		// Only show the empty row button when a row is selected
		emptyRowButton.addCondition(new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				return vmButtons.getSelectedRow() != null;
			}
		});

		// Make the empty row button 'watch' the layout
		emptyRowButton.watch(vmButtons);

		// Add action listeners to all of the buttons
		logoutButton.addActionListener(this);
		commitChangesButton.addActionListener(this);
		emptyRowButton.addActionListener(this);
		changeRowButton.addActionListener(this);
	}

	/**
	 * Lays out the components on the panel.
	 **/
	private void addComponents()
	{
		// Sets the layout to a vertical layout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		add(new JScrollPane(stockableItems));

		add(Box.createGlue());
		add(Box.createRigidArea(new Dimension(20, 0)));

		JPanel rightSide = new JPanel();
		rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
		add(rightSide);
		
		// Adds the vending machine layout panel
		rightSide.add(vmButtons);
		
		// Spacing between panel and bottom controls
		rightSide.add(Box.createGlue());
		rightSide.add(Box.createRigidArea(new Dimension(0,20)));

		// Panel for bottom controls
		JPanel bottomPanel = new JPanel();
		rightSide.add(bottomPanel);

		// Grid layout for bottom controls
		bottomPanel.setLayout(new GridLayout(2,2,3,3));

		// Empty Row button
		bottomPanel.add(emptyRowButton);
		
		// Confirm Changes button
		bottomPanel.add(commitChangesButton);

		// Change Row button
		bottomPanel.add(changeRowButton);

		// Return Home button
		bottomPanel.add(logoutButton);
	}

	/**
	 * Handles any of the buttons being clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		JButton source = (JButton)event.getSource();

		// Logout button clicked
		if (source == logoutButton)
		{
			master.getStatusBar().clearStatus();
			master.popContentPanel();
			master.setTitle("Home Screen");
		}

		// Change row button clicked
		else if (source == changeRowButton)
		{
			// Try the row change
			Pair<Integer, Integer> selected = vmButtons.getSelectedRow();
			int result = controller.queueRowChange(selected, (FoodItem)stockableItems.getSelectedValue());

			// Set the status bar to an appropriate message depending on the
			// result.
			if (result == 0)
				master.getStatusBar().setStatus("Row changed successfully", StatusBar.STATUS_GOOD_COLOR);
			else if (result == 1)
				master.getStatusBar().setStatus("Item expires too soon. Cannot add item.", StatusBar.STATUS_BAD_COLOR);
			else
				master.getStatusBar().setStatus("An error occurred while trying to change row.", StatusBar.STATUS_BAD_COLOR);

			// Refresh the content of the VM layout panel
			vmButtons.refreshContent(controller.listRows());
		}

		// Empty row button clicked
		else if (source == emptyRowButton)
		{
			// Try to empty the row
			Pair<Integer, Integer> selected = vmButtons.getSelectedRow();
			int result = controller.queueRowChange(selected, null);

			// Set the status bar to an appropriate message depending on the
			// result.
			if (result == 0)
				master.getStatusBar().setStatus("Row emptied successfully", StatusBar.STATUS_GOOD_COLOR);
			else
				master.getStatusBar().setStatus("An error occurred while trying to empty the row.", StatusBar.STATUS_BAD_COLOR);

			// Refresh the content of the VM layout panel
			vmButtons.refreshContent(controller.listRows());
		}

		// Commit changes clicked
		else if (source == commitChangesButton)
		{
			// Try to commit the changes
			if (controller.commitRowChanges())
			{
				// Display a success message
				master.getStatusBar().setStatus("Changes committed successfully!", StatusBar.STATUS_GOOD_COLOR);
			}
			else
			{
				// Display an error message
				master.getStatusBar().setStatus("An error occurred while attempting to commit the changes", StatusBar.STATUS_BAD_COLOR);
			}
			master.popContentPanel();
		}
	}
}
