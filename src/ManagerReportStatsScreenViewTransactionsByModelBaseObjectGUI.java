import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.table.TableRowSorter;

import java.util.GregorianCalendar;

/**
 * Screen used by ManagerReportStatsScreenGUI to actually view the selected list of transactions.
 * Please forgive the long class name.
 * 
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class ManagerReportStatsScreenViewTransactionsByModelBaseObjectGUI extends JPanel implements ActionListener {
	/** Controller instance */
	private ManagerReportStatsScreen controller;
	
	/** BaseGUI container for this panel */
	private BaseGUI master;
	
	/** Object whose transactions we want to look up */
	private ModelBase modelBaseObject;
	
	/** Graphical list of all transactions for the object */
	private JTable transactionList;
	
	/** Button to return to the main stats screen */
	private JButton returnToMainScreenButton;
	
	/** The data feeding the transaction list */
	private TableModel transactionData;
	
	/**
	 * Constructor for this screen.
	 * 
	 * @param controller		Controller instance for this screen
	 * @param master			BaseGUI container for this panel
	 * @param modelBaseObject	ModelBase object whose transactions we're looking up
	 */
	public ManagerReportStatsScreenViewTransactionsByModelBaseObjectGUI(ManagerReportStatsScreen controller, BaseGUI master, ModelBase modelBaseObject) {
		this.controller = controller;
		this.master = master;
		this.modelBaseObject = modelBaseObject;
		
		master.getStatusBar().clearStatus();
		addComponents();
	}
	
	/** Lays out components on the JPanel */
	public void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		ArrayList<Transaction> transactions = null;
		
		if(modelBaseObject instanceof VendingMachine) {
			transactions = controller.listMachineSales((VendingMachine)modelBaseObject);
		} else if(modelBaseObject instanceof Customer) {
			transactions = controller.listCustomerSales((Customer)modelBaseObject);
		} else if(modelBaseObject instanceof FoodItem) {
			transactions = controller.listFoodItemSales((FoodItem)modelBaseObject);
		}
		
		final ArrayList<Transaction> finalTransactions = transactions;
		
		transactionData = new AbstractTableModel() {
			private String[] columnNames = {"Time", "Vending Machine", "Customer", "Purchased Item", "Row"};
			public String getColumnName(int col) { return columnNames[col]; }
			public int getColumnCount() { return columnNames.length; }
			public int getRowCount() { return finalTransactions.size(); }
			public Object getValueAt(int row, int col) {
				final Transaction transaction = finalTransactions.get(row);
				
				Object retVal = null;
				
				switch(col) {
					case 0:
						retVal = transaction.getTimestamp().getTime();
						break;
					case 1:
						retVal = transaction.getMachine();
						break;
					case 2:
						retVal = transaction.getCustomer().getName();
						break;
					case 3:
						retVal = transaction.getProduct().getName();
						break;
					case 4:
						retVal = transaction.getRow();
						break;
				}
				
				return retVal;
			}
			
			@Override
			public Class getColumnClass(int col) {
				switch(col) {
					case 0:
						return GregorianCalendar.class;
					case 1:
						return VendingMachine.class;
					case 2:
						return String.class;
					case 3:
						return String.class;
					case 4:
						return Pair.class;
				}
				
				return super.getColumnClass(col);
			}
			
		};
		
		if(transactions.size()==0)
			master.getStatusBar().setStatus("No transactions matched query", StatusBar.STATUS_WARN_COLOR);
		
		String[] transactionStrings = new String[transactions.size()];
		for(int i = 0; i < transactions.size(); ++i) {
			transactionStrings[i] = transactions.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		
		transactionList = new JTable(transactionData);
		transactionList.setAutoCreateRowSorter(true);
		transactionList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		transactionList.setAlignmentX(LEFT_ALIGNMENT);
		
		returnToMainScreenButton = new JButton("Go Back to Stats Screen");
		//returnToMainScreenButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnToMainScreenButton.setAlignmentX(LEFT_ALIGNMENT);
		returnToMainScreenButton.addActionListener(this);
		
		transactionList.getColumnModel().getColumn(0).setPreferredWidth(250);
		transactionList.getColumnModel().getColumn(1).setPreferredWidth(500);
		transactionList.getColumnModel().getColumn(2).setPreferredWidth(150);
		transactionList.getColumnModel().getColumn(3).setPreferredWidth(150);
		JScrollPane transactionScroll = new JScrollPane(transactionList);
		transactionScroll.setAlignmentX(LEFT_ALIGNMENT);
		transactionScroll.setPreferredSize(new Dimension(transactionList.getPreferredSize().width, transactionList.getRowHeight() * (transactionList.getRowCount() + 1)+2));
		this.add(transactionScroll);
		add(Box.createRigidArea(new Dimension(0, 20)));
		this.add(returnToMainScreenButton);
		
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == returnToMainScreenButton) {
			master.getStatusBar().clearStatus();
			master.popContentPanel();
		}
	}
}
