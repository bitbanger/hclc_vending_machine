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
		this.controller = controller;
		this.master = master;

		master.setTitle("User Accounts");

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
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a JList of all the machines in the database
		managerList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		managerList.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a JList of all the customers in the database
		customerList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		customerList.setAlignmentX(LEFT_ALIGNMENT);
		
		// Only one thing should be selected at a time
		managerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Create the show transactions button and add ourselves as an action listener
		editSelectionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		editSelectionButton.setAlignmentX(LEFT_ALIGNMENT);
		newManagerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		newManagerButton.setAlignmentX(LEFT_ALIGNMENT);
		newCustomerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		newCustomerButton.setAlignmentX(LEFT_ALIGNMENT);
		
		// Keep the button appropriate
		editSelectionButton.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition()
			{
				return !managerList.isSelectionEmpty() ^ !customerList.isSelectionEmpty();
			}
		});
		
		// Add all listeners
		managerList.addListSelectionListener(this);
		customerList.addListSelectionListener(this);
		newManagerButton.addActionListener(this);
		newCustomerButton.addActionListener(this);
		editSelectionButton.addActionListener(this);
		
		JButton returnHomeButton = new JButton("Return to Home Screen");
		returnHomeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnHomeButton.setAlignmentX(LEFT_ALIGNMENT);
		returnHomeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ignored)
			{
				master.popContentPanel();
				master.setTitle("Home Screen");
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(editSelectionButton);
		buttonPanel.add(newManagerButton);
		buttonPanel.add(newCustomerButton);
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		// Add everything to the visible panel
		this.add(Box.createGlue());
		this.add(new JLabel("Managers:"));
		this.add(managerList);
		this.add(Box.createGlue());
		this.add(new JLabel("Customers:"));
		this.add(customerList);
		this.add(Box.createGlue());
		this.add(buttonPanel);
		this.add(Box.createGlue());
		this.add(returnHomeButton);
		
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
