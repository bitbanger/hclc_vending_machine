import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * Manager GUI screen to manage accounts.
 * 
 * @author Lane Lawley <lxl5734@rit.edu>
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class ManagerUserAccountsScreenGUI extends JPanel implements ActionListener, ListSelectionListener {
	
	/** Controller instance to allow this screen's manipulation of the model */
	private ManagerUserAccountsScreen controller;
	
	/** BaseGUI container for this panel. */
	private BaseGUI master;
	
	/** Graphical list of all managers. */
	private JList managerList;
	
	/** Graphical list of all customers. */
	private JList customerList;
	
	/** Button used to edit existing stuff. */
	private ConditionButton editSelectionButton;
	
	/** Button used to make new managers. */
	private JButton newManagerButton;
	
	/** Button used to make new customers. */
	private JButton newCustomerButton;
	
	/**
	 * Constructor for this screen.
	 * 
	 * @param controller	Controller instance for this screen
	 * @param master		BaseGUI container for this panel
	 */
	public ManagerUserAccountsScreenGUI(ManagerUserAccountsScreen controller, BaseGUI master) {
		master.getStatusBar().clearStatus();
		this.controller = controller;
		this.master = master;


		managerList = new JList(controller.listManagers().toArray());
		customerList = new JList(controller.listCustomers().toArray());
		editSelectionButton = new ConditionButton("Edit selected user");
		newManagerButton = new JButton("Create new manager");
		newCustomerButton = new JButton("Create new customer");
		
		master.getStatusBar().clearStatus();
		addComponents();
	}

	/**
	 * Causes us to refresh ourselves with a sigh.
	 */
	public void refreshYourselfYouSmellAwful()
	{
		managerList.setListData(controller.listManagers().toArray());
		customerList.setListData(controller.listCustomers().toArray());
		managerList.clearSelection();
		customerList.clearSelection();
		editSelectionButton.checkAndSetEnabled();
	}
	
	/** Lays out components on the JPanel */
	private void addComponents() {
		JButton returnHomeButton = new JButton("Return to Home Screen");
		//returnHomeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnHomeButton.setAlignmentX(LEFT_ALIGNMENT);
		returnHomeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ignored)
			{
				master.popContentPanel();
			}
		});

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setAlignmentX(tabPane.LEFT_ALIGNMENT);
		add(tabPane);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(returnHomeButton);

		JPanel managerTab = new JPanel();
		managerTab.setBorder(new EmptyBorder(10,10,10,10));
		managerTab.setLayout(new BoxLayout(managerTab, BoxLayout.X_AXIS));
		tabPane.addTab("Managers", managerTab);

		JPanel customerTab = new JPanel();
		customerTab.setBorder(new EmptyBorder(10,10,10,10));
		customerTab.setLayout(new BoxLayout(customerTab, BoxLayout.X_AXIS));
		tabPane.addTab("Customers", customerTab);
		
		// Create a JList of all the machines in the database
		managerList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		// Create a JList of all the customers in the database
		customerList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		// Only one thing should be selected at a time
		managerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Create the show transactions button and add ourselves as an action listener
		editSelectionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	
		newManagerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		newCustomerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		
		// Keep the button appropriate
		editSelectionButton.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition()
			{
				return !managerList.isSelectionEmpty();
			}
		});

		
		// Add all listeners
		managerList.addListSelectionListener(this);
		customerList.addListSelectionListener(this);
		newManagerButton.addActionListener(this);
		newCustomerButton.addActionListener(this);
		editSelectionButton.addActionListener(this);
		
		JButton returnHomeButtonCustomer = new JButton("Return to Home Screen");
		returnHomeButtonCustomer.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnHomeButtonCustomer.setAlignmentX(LEFT_ALIGNMENT);
		returnHomeButtonCustomer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ignored)
			{
				master.popContentPanel();
			}
		});

		JPanel managerLeftPanel = new JPanel();
		managerLeftPanel.setLayout(new GridLayout(0,1,0,20));
		managerTab.add(managerLeftPanel);

		managerLeftPanel.add(newManagerButton);
		managerLeftPanel.add(editSelectionButton);

		managerTab.add(Box.createRigidArea(new Dimension(50, 0)));
		managerTab.add(new JScrollPane(managerList));

		JPanel customerLeftPanel = new JPanel();
		customerLeftPanel.setLayout(new GridLayout(0,1,0,20));
		customerTab.add(customerLeftPanel);

		customerLeftPanel.add(newCustomerButton);
		customerLeftPanel.add(Box.createRigidArea(new Dimension(0,0)));

		customerTab.add(Box.createRigidArea(new Dimension(50, 0)));
		customerTab.add(new JScrollPane(customerList));
		
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource()==newManagerButton) {
			ManagerUserAccountsScreenChangeManagerPasswordGUI nextGUI = new ManagerUserAccountsScreenChangeManagerPasswordGUI(controller, master, null, this);
			master.getStatusBar().clearStatus();
			master.pushContentPanel(nextGUI);
		}
		else if(event.getSource()==newCustomerButton) {
			ManagerUserAccountsScreenCreateCustomerGUI nextGUI = new ManagerUserAccountsScreenCreateCustomerGUI(controller, master, this);
			master.getStatusBar().clearStatus();
			master.pushContentPanel(nextGUI);
		}
		else //changer button thingy
		{
			if(!managerList.isSelectionEmpty()) {
				//master.getStatusBar().setStatus("I would call the manager thingy if I could but I can't");
				ManagerUserAccountsScreenChangeManagerPasswordGUI nextGUI = new ManagerUserAccountsScreenChangeManagerPasswordGUI(controller, master, ((Manager)managerList.getSelectedValue()), this);
				master.getStatusBar().clearStatus();
				master.pushContentPanel(nextGUI);
			}
			else //customerList isn't empty
				master.getStatusBar().setStatus("Customer editing not implemented :(", StatusBar.STATUS_WARN_COLOR);
		}
		
		managerList.clearSelection();
		customerList.clearSelection();
		editSelectionButton.checkAndSetEnabled();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(event.getSource() == managerList)
			customerList.clearSelection();
			
		else //if(event.getSource() == customerList)
			managerList.clearSelection();
		
		editSelectionButton.checkAndSetEnabled();
	}
}
