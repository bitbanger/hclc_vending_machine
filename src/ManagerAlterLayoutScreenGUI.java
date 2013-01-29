import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	 * The button that allows the manager to change row.
	 **/
	private JButton changeRowButton;

	/**
	 * The button that allows the manager to empty row.
	 **/
	private JButton emptyRowButton;

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
		this.controller = controller;
		this.master = master;

		vmButtons = new VMLayoutPanel(controller.listRows(), false); 
		
		
	//	changeRowButton = new JButton("Change Row");
	//	emptyRowButton = new JButton("Empty Row");
	//	commitChangesButton = new JButton("Commit Changes");
	//	logoutButton = new JButton("Return to home screen");

		addComponents();
	}

	/**
	 * Lays out the components on the panel.
	 **/
	private void addComponents()
	{
		// Sets the layout to a vertical layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Adds the vending machine layout panel
		add(vmButtons);
		
		// Spacing between panel and bottom controls
		add(Box.createGlue());
		add(Box.createRigidArea(new Dimension(0,20)));

		// Panel for bottom controls
		JPanel bottomPanel = new JPanel();
		add(bottomPanel);

		// Horizontal layout for bottom controls
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		// Spacing between label and buttons
		bottomPanel.add(Box.createGlue());

		// Return Home button
		logoutButton = new JButton("Return Home");
		logoutButton.addActionListener(this);
		bottomPanel.add(logoutButton);

		// Spacing between buttons
		bottomPanel.add(Box.createRigidArea(new Dimension(10,0)));

		// Confim Changes button
		commitChangesButton = new JButton("Confirm Changes");
		commitChangesButton.addActionListener(this);
		bottomPanel.add(commitChangesButton);

		
	//	changeRowButton.addActionListener(this);
	//	changeRowButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	//	add(changeRowButton);

	//	add(Box.createRigidArea(new Dimension(0, 20)));

	//	emptyRowButton.addActionListener(this);
	//	emptyRowButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	//	add(emptyRowButton);

	//	add(Box.createRigidArea(new Dimension(0, 20)));
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
