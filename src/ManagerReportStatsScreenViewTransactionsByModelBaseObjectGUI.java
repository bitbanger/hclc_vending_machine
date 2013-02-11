import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

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
	private JList transactionList;
	
	/** Button to return to the main stats screen */
	private JButton returnToMainScreenButton;
	
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
		
		if(transactions.size()==0)
			master.getStatusBar().setStatus("No transactions matched query", StatusBar.STATUS_WARN_COLOR);
		
		String[] transactionStrings = new String[transactions.size()];
		for(int i = 0; i < transactions.size(); ++i) {
			transactionStrings[i] = transactions.get(i).toString().substring(ModelBase.ID_SPACES);
		}
		
		transactionList = new JList(transactionStrings);
		transactionList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		transactionList.setAlignmentX(LEFT_ALIGNMENT);
		
		returnToMainScreenButton = new JButton("Go Back to Stats Screen");
		//returnToMainScreenButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnToMainScreenButton.setAlignmentX(LEFT_ALIGNMENT);
		returnToMainScreenButton.addActionListener(this);
		
		this.add(transactionList);
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
