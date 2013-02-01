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
	private JButton emptyRowButton;

	/**
	 * The button that allows the manager to commit changes.
	 **/
	private ConditionButton commitChangesButton;

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
		this.controller = controller;
		this.master = master;

		vmButtons = new VMLayoutPanel(controller.listRows(), true); 
		stockableItems = new JList(controller.listItems().toArray());
		
		
		changeRowButton = new ConditionButton("Change Row");
	//	emptyRowButton = new JButton("Empty Row");
	//	commitChangesButton = new JButton("Commit Changes");
	//	logoutButton = new JButton("Return to home screen");

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

		// Horizontal layout for bottom controls
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		// Spacing between label and buttons
		bottomPanel.add(Box.createGlue());

		// Return Home button
		logoutButton = new JButton("Return Home");
		bottomPanel.add(logoutButton);

		// Spacing between buttons
		bottomPanel.add(Box.createRigidArea(new Dimension(10,0)));

		// Confim Changes button
		commitChangesButton = new ConditionButton("Confirm Changes");
		bottomPanel.add(commitChangesButton);

		// Spacing between buttons
		bottomPanel.add(Box.createRigidArea(new Dimension(10,0)));

		// Empty Row button
		emptyRowButton = new JButton("Empty Row");
		bottomPanel.add(emptyRowButton);

		// Spacing between buttons
		bottomPanel.add(Box.createRigidArea(new Dimension(10,0)));

		// Change Row button
		changeRowButton = new ConditionButton("Change Row");
		bottomPanel.add(changeRowButton);
	}

	/**
	 * Handles any of the buttons being clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		JButton source = (JButton)event.getSource();
		if (source == logoutButton)
		{
			master.getStatusBar().clearStatus();
			master.popContentPanel();
			
		}
		else
		{
			Pair<Integer, Integer> selected = vmButtons.getSelectedRow();
			if (selected == null)
			{
				master.getStatusBar().setStatus("You haven't selected an item yet!", StatusBar.STATUS_BAD_COLOR);
			}

			//String result = controller.tryPurchase(selected);
		//	if (result.equals("GOOD"))
		//	{
		//		master.
		//	}
		
		}
	}
}
