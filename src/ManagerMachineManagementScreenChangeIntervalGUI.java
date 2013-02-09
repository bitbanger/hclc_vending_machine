import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Dimension;

/**
 * Changes the location of a vending machine.
 * 
 * @author Lane Lawley
 */
public class ManagerMachineManagementScreenChangeIntervalGUI extends JPanel implements ActionListener {
	/**
	 * Saves the data.
	 */
	private ConditionButton confirmButton;
	
	/**
	 * Cancels the changes.
	 */
	private JButton cancelButton;
	
	/**
	 * Number field for stocking interval
	 */
	private NumberField stockingField;
	
	/**
	 * Controller instance.
	 */
	private ManagerMachineManagementScreen controller;
	
	/**
	 * Master frame
	 */
	private BaseGUI master;
	
	/**
	 * Machine that is getting its location changed.
	 */
	private VendingMachine machine;
	
	/**
	 * Parent screen.
	 */
	private ManagerMachineManagementScreenGUI parent;
	
	/**
	 * Creates a new ChangeLocationGUI to change the location of the given
	 * vending machine.
	 * 
	 * @param controller
	 *            The controller instance for this view.
	 * @param master
	 *            The master frame.
	 * @param machine
	 *            The machine to change the location for.
	 * @param parent
	 *            The parent screen. Used to refresh the list.
	 */
	public ManagerMachineManagementScreenChangeIntervalGUI(ManagerMachineManagementScreen controller,
			BaseGUI master, VendingMachine machine,
			ManagerMachineManagementScreenGUI parent) {
		this.controller = controller;
		this.master = master;
		this.machine = machine;
		this.parent = parent;
		
		stockingField = new NumberField(NumberField.POSITIVE_Z);
		
		confirmButton = new ConditionButton("Confirm Interval Change");
		cancelButton = new JButton("Cancel Interval Change");
		addComponents();
		addLogic();
	}
	
	/**
	 * Adds logic to the components.
	 */
	private void addLogic() {
		cancelButton.addActionListener(this);
		confirmButton.addActionListener(this);
		confirmButton.addCondition(new ConditionButtonCondition() {
			public boolean checkCondition() {
				return stockingField.areContentsValid() &&
					   controller.stockingIntervalValidity(machine, stockingField.getNumber()) == 0;
			}
		});
		
		stockingField.getDocument().addDocumentListener(new DocumentListener() {
			/** @inheritDoc */
			public void removeUpdate(DocumentEvent arg0) {
				generalUpdate();
			}
			
			/** @inheritDoc */
			public void insertUpdate(DocumentEvent arg0) {
				generalUpdate();
			}
			
			/** @inheritDoc */
			public void changedUpdate(DocumentEvent arg0) {
			}
			
			private void generalUpdate() {
				confirmButton.checkAndSetEnabled();
				
				if(stockingField.areContentsValid()) {
					int validity = controller.stockingIntervalValidity(machine, stockingField.getNumber());
					
					if(validity == -1) {
						master.getStatusBar().setStatus("An error occurred while attempting to change the stocking interval", StatusBar.STATUS_BAD_COLOR, StatusBar.PRIORITY_REJECTED_CONFIG);
					} else if(validity > 0) {
						master.getStatusBar().setStatus("Due to your items, the interval must be at at least " + validity + " days", StatusBar.STATUS_BAD_COLOR, StatusBar.PRIORITY_REJECTED_CONFIG);
					}
				}
			}
		});
	}
	
	/**
	 * Lays out the components.
	 */
	private void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(stockingField);
		
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel bottomPanel = new JPanel();
		add(bottomPanel);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(cancelButton);
		bottomPanel.add(Box.createGlue());
		bottomPanel.add(confirmButton);
	}
	
	/**
	 * Handles buttons getting clicked.
	 * 
	 * @param event
	 *            Contains information regarding the event.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == confirmButton) {
			if(controller.changeMachineStockingInterval(machine, stockingField.getNumber()) == 0) {
				master.getStatusBar().setStatus("Stocking interval changed successfully", StatusBar.STATUS_GOOD_COLOR);
			} else {
				master.getStatusBar().setStatus("An error occurred while attempting to change the stocking interval", StatusBar.STATUS_BAD_COLOR);
			}
			master.popContentPanel();
			parent.refreshList();
		} else if (source == cancelButton) {
			master.popContentPanel();
		}
	}
}
