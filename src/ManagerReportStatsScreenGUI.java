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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.table.TableRowSorter;

/**
 * Manager GUI screen to view sale statistics by machine, customer, and specific item.
 * 
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class ManagerReportStatsScreenGUI extends JPanel implements ActionListener, ListSelectionListener {
	
	/** Controller instance to allow this screen's manipulation of the model */
	private ManagerReportStatsScreen controller;
	
	/** BaseGUI container for this panel */
	private BaseGUI master;
	
	/** Graphical list of all machines */
	private JTable machineList;
	
	/** Graphical list of all customers */
	private JTable customerList;
	
	/** Graphical list of all food items */
	private JTable itemList;
	
	/** The data feeding the machine list */
	private TableModel machineData;
	
	/** The data feeding the customer list */
	private TableModel customerData;
	
	/** The data feeding the item list */
	private TableModel itemData;
	
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
		master.getStatusBar().clearStatus();
		this.controller = controller;
		this.master = master;
		
		master.getStatusBar().clearStatus();
		addComponents();
	}
	
	/** Lays out components on the JPanel */
	public void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		machineData = new AbstractTableModel() {
			private String[] columnNames = {"State", "ZIP Code", "Stocking Interval", "Active?"};
			public String getColumnName(int col) { return columnNames[col]; }
			public int getColumnCount() { return columnNames.length; }
			public int getRowCount() { return controller.listMachines().size(); }
			public Object getValueAt(int row, int col) {
				VendingMachine machine = controller.listMachines().get(row);
				
				Object retVal = null;
				
				switch(col) {
					case 0:
						retVal = machine.getLocation().getState();
						break;
					case 1:
						retVal = machine.getLocation().getZipCode();
						break;
					case 2:
						retVal = machine.getStockingInterval();
						break;
					case 3:
						retVal = (machine.isActive() ? "Yes" : "No");
						break;
				}
				
				return retVal;
			}
			
			@Override
			public Class getColumnClass(int col) {
				if (col == 1 || col == 2)
					return Integer.class;
				else
					return super.getColumnClass(col);
			}
			
		};
		
		customerData = new AbstractTableModel() {
			private String[] columnNames = {"Name", "Account Balance"};
			public String getColumnName(int col) { return columnNames[col]; }
			public int getColumnCount() { return columnNames.length; }
			public int getRowCount() { return controller.listCustomers().size(); }
			public Object getValueAt(int row, int col) {
				final Customer customer = controller.listCustomers().get(row);
				
				Object retVal = null;
				
				switch(col) {
					case 0:
						retVal = customer.getName();
						break;
					case 1:
						retVal = new MoneyInteger(customer.getMoney());
						break;
				}
				
				return retVal;
			}
			
			@Override
			public Class getColumnClass(int col) {
				if (col == 1)
					return MoneyInteger.class;
				else
					return super.getColumnClass(col);
			}
			
		};
		
		itemData = new AbstractTableModel() {
			private String[] columnNames = {"Item Name", "Price", "Days to Expiration"};
			public String getColumnName(int col) { return columnNames[col]; }
			public int getColumnCount() { return columnNames.length; }
			public int getRowCount() { return controller.listCustomers().size(); }
			public Object getValueAt(int row, int col) {
				final FoodItem item = controller.listFoodItems().get(row);
				
				Object retVal = null;
				
				switch(col) {
					case 0:
						retVal = item.getName();
						break;
					case 1:
						retVal = new MoneyInteger(item.getPrice());
						break;
					case 2:
						retVal = item.getFreshLength();
				}
				
				return retVal;
			}
			
			@Override
			public Class getColumnClass(int col) {
				if (col == 1)
					return MoneyInteger.class;
				else if (col == 2)
					return Integer.class;
				else
					return super.getColumnClass(col);
			}
			
		};
		
		// Create a JList of all the machines in the database
		ArrayList<VendingMachine> machines = controller.listMachines();
		String[] machineStrings = new String[machines.size()];
		for(int i = 0; i < machines.size(); ++i) {
			machineStrings[i] = machines.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		
		machineList = new JTable(machineData);
		JLabel machineLabel = new JLabel("Machines:");
		machineList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		machineList.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a JList of all the customers in the database
		ArrayList<Customer> customers = controller.listCustomers();
		String[] customerStrings = new String[customers.size()];
		for(int i = 0; i < customers.size(); ++i) {
			customerStrings[i] = customers.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		customerList = new JTable(customerData);
		JLabel customerLabel = new JLabel("Customers:");
		customerList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		customerList.setAlignmentX(LEFT_ALIGNMENT);
		
		// Create a JList of all the food items in the database
		ArrayList<FoodItem> items = controller.listFoodItems();
		String[] itemStrings = new String[items.size()];
		for(int i = 0; i < items.size(); ++i) {
			itemStrings[i] = items.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		itemList = new JTable(itemData);
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
		machineList.getSelectionModel().addListSelectionListener(this);
		customerList.getSelectionModel().addListSelectionListener(this);
		itemList.getSelectionModel().addListSelectionListener(this);
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
		buttonPanel.add(Box.createHorizontalStrut(10)); // Creates a space between the two buttons
		buttonPanel.add(showTransactionsButton);
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		// Add everything to the visible panel
		this.add(machineLabel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		/*TableRowSorter<TableModel> machineSorter = new TableRowSorter<TableModel>(machineList.getModel());
		machineList.setRowSorter(machineSorter);*/
		machineList.setAutoCreateRowSorter(true);
		this.add(new JScrollPane(machineList));

		add(Box.createRigidArea(new Dimension(0, 20)));

		this.add(customerLabel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		customerList.setAutoCreateRowSorter(true);
		this.add(new JScrollPane(customerList));

		add(Box.createRigidArea(new Dimension(0, 20)));

		this.add(itemLabel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		itemList.setAutoCreateRowSorter(true);
		this.add(new JScrollPane(itemList));

		add(Box.createRigidArea(new Dimension(0, 50)));

		this.add(buttonPanel);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == showTransactionsButton) {
			ManagerReportStatsScreenViewTransactionsByModelBaseObjectGUI transactionsScreen = new ManagerReportStatsScreenViewTransactionsByModelBaseObjectGUI(controller, master, selectedModelObj);
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
		
		if(event.getSource() == machineList.getSelectionModel()) {
			int index = machineList.convertRowIndexToModel(((DefaultListSelectionModel)event.getSource()).getLeadSelectionIndex());
			
			customerList.clearSelection();
			itemList.clearSelection();
			
			selectedModelObj = controller.listMachines().get(index);
			
		} else if(event.getSource() == customerList.getSelectionModel()) {
			int index = customerList.convertRowIndexToModel(((DefaultListSelectionModel)event.getSource()).getLeadSelectionIndex());
			
			machineList.clearSelection();
			itemList.clearSelection();
			
			selectedModelObj = controller.listCustomers().get(index);
			
		} else if(event.getSource() == itemList.getSelectionModel()) {
			int index = itemList.convertRowIndexToModel(((DefaultListSelectionModel)event.getSource()).getLeadSelectionIndex());
			
			machineList.clearSelection();
			customerList.clearSelection();
			
			selectedModelObj = controller.listFoodItems().get(index);
		}
	}
}

