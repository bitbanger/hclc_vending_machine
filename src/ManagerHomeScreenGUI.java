import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;

/**
 * Content panel for the manger's home screen.
 * @author Matthew Koontz
 **/
public class ManagerHomeScreenGUI extends JPanel implements ActionListener
{
	/**
	 * The controller for this screen.
	 **/
	private ManagerHomeScreen controller;

	/**
	 * The frame this panel is in.
	 **/
	private BaseGUI master;

	/**
	 * The button that brings the manager to the view stats screen.
	 **/
	private ConditionButton statsButton;

	/**
	 * The button that brings the manager to the item management screen.
	 **/
	private JButton stockedItemsButton;

	/**
	 * The button that brings the manager to the alter layout screen.
	 **/
	private ConditionButton layoutButton;

	/**
	 * The button that brings the manager to the manage users screen.
	 **/
	private JButton accountsButton;

	/**
	 * The button that brings the manager to the manage machines screen.
	 **/
	private JButton machinesButton;

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
	public ManagerHomeScreenGUI(ManagerHomeScreen controller, BaseGUI master)
	{
		master.getStatusBar().clearStatus();
		this.controller = controller;
		this.master = master;


		master.getStatusBar().setStatus(String.format("Welcome, %s", controller.getUserName()), StatusBar.STATUS_GOOD_COLOR);

		statsButton = new ConditionButton("View Machine Statistics");
		stockedItemsButton = new JButton("Manage Stocked Items");
		layoutButton = new ConditionButton("Alter Machine Layout");
		accountsButton = new JButton("Manage User Accounts");
		machinesButton = new JButton("Manage Machines");
		logoutButton = new JButton("Log Out");

		addComponents();
	}

	/**
	 * Lays out the components on the panel.
	 **/
	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		statsButton.addCondition(new ConditionButtonCondition()
		{
			/**
			 * The button should only be enabled if there are machines
			 */
			@Override
			public boolean checkCondition()
			{
				return ( !controller.displayVendingMachines().isEmpty() );
			}
		});
		statsButton.addActionListener(this);
		statsButton.checkAndSetEnabled();
		statsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(statsButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		stockedItemsButton.addActionListener(this);
		stockedItemsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(stockedItemsButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		layoutButton.addCondition(new ConditionButtonCondition()
		{
			/**
			 * The button should only be enabled if there are machines
			 */
			@Override
			public boolean checkCondition()
			{
				return !controller.displayVendingMachines().isEmpty();
			}
		});
		layoutButton.addActionListener(this);
		layoutButton.checkAndSetEnabled();
		layoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(layoutButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		accountsButton.addActionListener(this);
		accountsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(accountsButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		machinesButton.addActionListener(this);
		machinesButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(machinesButton);

		add(Box.createRigidArea(new Dimension(0, 20)));

		logoutButton.addActionListener(this);
		logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(logoutButton);
	}

	/**
	 * REFRESH DEM CONDITIONS
	 */
	public void refreshYourself() {
		statsButton.checkAndSetEnabled();
		layoutButton.checkAndSetEnabled();
	}

	/**
	 * Handles any of the buttons being clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == statsButton)
		{
			ManagerReportStatsScreen next = controller.viewStats();
			ManagerReportStatsScreenGUI nextGUI = new ManagerReportStatsScreenGUI(next, master);
			master.pushContentPanel(nextGUI);
		}
		else if (source == stockedItemsButton)
		{
			ManagerStockedItemsScreen next = controller.manageItems();
			ManagerStockedItemsScreenGUI nextGUI = new ManagerStockedItemsScreenGUI(next, master);
			master.pushContentPanel(nextGUI);
		}
		else if (source == layoutButton)
		{
			ManagerAlterLayoutScreen next = controller.alterLayout();
			ManagerAlterLayoutScreenGUI nextGUI = new ManagerAlterLayoutScreenGUI(next, master);
			master.pushContentPanel(nextGUI);
		}
		else if (source == accountsButton)
		{
			ManagerUserAccountsScreen next = controller.manageUsers();
			master.pushContentPanel(new ManagerUserAccountsScreenGUI(next, master));
		}
		else if (source == machinesButton)
		{
			ManagerMachineManagementScreen next = controller.manageMachines();
			ManagerMachineManagementScreenGUI nextGUI = new ManagerMachineManagementScreenGUI(next, master, this);
			master.pushContentPanel(nextGUI);
		}
		else if (source == logoutButton)
		{
			master.getStatusBar().setStatus("Logged out successfully", StatusBar.STATUS_GOOD_COLOR);
			master.popContentPanel();
		}
	}
}
