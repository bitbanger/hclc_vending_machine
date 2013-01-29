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



public class ManagerReportStatsScreenGUI extends JPanel implements ActionListener, ListSelectionListener {
	
	/** Controller instance to allow this screen's manipulation of the model */
	private ManagerReportStatsScreen controller;
	
	/** BaseGUI container for this panel */
	private BaseGUI master;
	
	/** Graphical list of all machines */
	private JList machineList;
	
	/** Graphical list of all customers */
	private JList customerList;
	
	/** Graphical list of all food items */
	private JList itemList;
	
	/** Button used to show transactions related to the selected element */
	private JButton showTransactionsButton;
	
	/** Button used to return to the manager home screen */
	private JButton returnHomeButton;
	
	/** The model object whose transactions we're going to look up */
	private ModelBase selectedModelObj;
	
	/**
	 * Constructor for this screen.
	 * 
	 * @param controller	Controller instance for this screen
	 * @param master		BaseGUI container for this panel
	 */
	public ManagerReportStatsScreenGUI(ManagerReportStatsScreen controller, BaseGUI master) {
		this.controller = controller;
		this.master = master;
		
		master.getStatusBar().clearStatus();
		addComponents();
	}
	
	/** Lays out components on the JPanel */
	public void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a JList of all the machines in the database
		ArrayList<VendingMachine> machines = controller.listMachines();
		String[] machineStrings = new String[machines.size()];
		for(int i = 0; i < machines.size(); ++i) {
			machineStrings[i] = machines.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		machineList = new JList(machineStrings);
		JLabel machineLabel = new JLabel("Machines:");
		machineList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		machineList.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a JList of all the customers in the database
		ArrayList<Customer> customers = controller.listCustomers();
		String[] customerStrings = new String[customers.size()];
		for(int i = 0; i < customers.size(); ++i) {
			customerStrings[i] = customers.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		customerList = new JList(customerStrings);
		JLabel customerLabel = new JLabel("Customers:");
		customerList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		customerList.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a JList of all the food items in the database
		ArrayList<FoodItem> items = controller.listFoodItems();
		String[] itemStrings = new String[items.size()];
		for(int i = 0; i < items.size(); ++i) {
			itemStrings[i] = items.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		itemList = new JList(itemStrings);
		JLabel itemLabel = new JLabel("Items:");
		itemList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		itemList.setAlignmentX(LEFT_ALIGNMENT);
		setAlignmentX(LEFT_ALIGNMENT);
		
		// Create the show transactions button and add ourselves as an action listener
		showTransactionsButton = new JButton("Show Transactions");
		showTransactionsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		showTransactionsButton.setAlignmentX(LEFT_ALIGNMENT);
		
		// Only one thing should be selected at a time
		machineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Add all listeners
		machineList.addListSelectionListener(this);
		customerList.addListSelectionListener(this);
		itemList.addListSelectionListener(this);
		showTransactionsButton.addActionListener(this);
		
		// Initially disable the show transactions button
		showTransactionsButton.setEnabled(false);
		
		returnHomeButton = new JButton("Return to Home Screen");
		returnHomeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnHomeButton.setAlignmentX(LEFT_ALIGNMENT);
		returnHomeButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(returnHomeButton);
		buttonPanel.add(showTransactionsButton);
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		// Add everything to the visible panel
		this.add(Box.createGlue());
		this.add(machineLabel);
		this.add(machineList);
		this.add(Box.createGlue());
		this.add(customerLabel);
		this.add(customerList);
		this.add(Box.createGlue());
		this.add(itemLabel);
		this.add(itemList);
		this.add(Box.createGlue());
		this.add(buttonPanel);
		this.add(Box.createGlue());
		
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == showTransactionsButton) {
			ManagerReportStatsScreenViewTransactionsByModelBaseObjectGUI transactionsScreen = new ManagerReportStatsScreenViewTransactionsByModelBaseObjectGUI(controller, master, selectedModelObj);
			master.getStatusBar().clearStatus();
			master.pushContentPanel(transactionsScreen);
		} else if(event.getSource() == returnHomeButton) {
			master.getStatusBar().clearStatus();
			master.popContentPanel();
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		// I'm doing this unnecessary check under the assumption that there's more overhead in the setEnabled method
		if(!showTransactionsButton.isEnabled()) {
			showTransactionsButton.setEnabled(true);
		}
		
		int index = ((JList)event.getSource()).getLeadSelectionIndex();
		
		if(event.getSource() == machineList) {
			customerList.clearSelection();
			itemList.clearSelection();
			
			selectedModelObj = controller.listMachines().get(index);
			
		} else if(event.getSource() == customerList) {
			machineList.clearSelection();
			itemList.clearSelection();
			
			selectedModelObj = controller.listCustomers().get(index);
			
		} else if(event.getSource() == itemList) {
			machineList.clearSelection();
			customerList.clearSelection();
			
			selectedModelObj = controller.listFoodItems().get(index);
		}
	}
}