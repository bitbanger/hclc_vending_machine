import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Component;

/**
 * Changes the stocking interval of a vending machine.
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
	private StockingIntervalField stockingField;
	
	/**
	 * Controller instance.
	 */
	private ManagerMachineManagementScreen controller;
	
	/**
	 * Master frame
	 */
	private BaseGUI master;
	
	/**
	 * Machine that is getting its stocking interval changed.
	 */
	private VendingMachine machine;
	
	/**
	 * Parent screen.
	 */
	private ManagerMachineManagementScreenGUI parent;
	
	/**
	 * Creates a new GUI to change the stocking interval of the given
	 * vending machine.
	 * 
	 * @param controller
	 *            The controller instance for this view.
	 * @param master
	 *            The master frame.
	 * @param machine
	 *            The machine to change the interval for.
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
		
		stockingField = new StockingIntervalField(controller, master.getStatusBar());
		stockingField.setText(String.valueOf(machine.getStockingInterval()));
		
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
				return stockingField.areContentsValid();
			}
		});
		confirmButton.watch(stockingField);
	}
	
	/**
	 * Lays out the components.
	 */
	private void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		inputPanel.add(new JLabel("New Stocking Interval: "));
		inputPanel.add(Box.createGlue());
		inputPanel.add(stockingField);
		
		add(inputPanel);
		
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
		final Object source = event.getSource();
		if (source == confirmButton) {
			new Thread()
			{
				public void run()
				{
					master.setProcessing((Component)source);
					if(controller.changeMachineStockingInterval(machine, stockingField.getNumber()) == 0) {
						master.popContentPanel();
						master.getStatusBar().setStatus("Stocking interval changed successfully", StatusBar.STATUS_GOOD_COLOR);
					} else {
						master.popContentPanel();
						master.getStatusBar().setStatus("An error occurred while attempting to change the stocking interval", StatusBar.STATUS_BAD_COLOR);
					}
				}
			}.start();
			parent.refreshList();
		} else if (source == cancelButton) {
			master.popContentPanel();
			master.getStatusBar().setStatus("Stocking interval changes canceled", StatusBar.STATUS_WARN_COLOR);
		}
	}
}
