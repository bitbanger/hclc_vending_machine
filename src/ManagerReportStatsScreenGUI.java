import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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
		showTransactionsButton.setAlignmentX(LEFT_ALIGNMENT);
		
		//machineList.addActionListener(this);
		//customerList.addActionListener(this);
		//itemList.addActionListener(this);
		showTransactionsButton.addActionListener(this);
		
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
		this.add(showTransactionsButton);
		
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == showTransactionsButton) {
			// Show the transaction here
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(event.getSource() == itemList) {
			// Currently unimplemented
		}
	}
}