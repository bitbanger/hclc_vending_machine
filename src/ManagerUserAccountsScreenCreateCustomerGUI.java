import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Dimension;
import java.util.ArrayList;

public class ManagerUserAccountsScreenCreateCustomerGUI extends JPanel implements ActionListener {
	/** Controller instance to allow this screen's manipulation of the model */
	private ManagerUserAccountsScreen controller;
	
	/** BaseGUI container for this panel */
	private BaseGUI master;
	
	/** The field for the manager's name */
	private JTextField nameField;
	
	/** The field for the customer's initial money balance */
	private JTextField initialBalanceField;
	
	/** Button to return to the user accounts screen */
	private JButton returnHomeButton;
	
	/** Button to confirm the addition */
	private ConditionButton confirmButton;
	
	private ManagerUserAccountsScreenGUI solsScreen;
	
	public ManagerUserAccountsScreenCreateCustomerGUI(ManagerUserAccountsScreen controller, BaseGUI master, ManagerUserAccountsScreenGUI solsScreen) {
		this.controller = controller;
		this.master = master;
		this.solsScreen = solsScreen;
		
		master.getStatusBar().clearStatus();
		addComponents();
	}
	
	private void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setAlignmentX(CENTER_ALIGNMENT);
		
		nameField = new JTextField();
		nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)nameField.getPreferredSize().getHeight()));
		
		initialBalanceField = new JTextField();
		initialBalanceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)initialBalanceField.getPreferredSize().getHeight()));
		
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				confirmButton.checkAndSetEnabled();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				confirmButton.checkAndSetEnabled();
			}
		});
		
		initialBalanceField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				confirmButton.checkAndSetEnabled();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				confirmButton.checkAndSetEnabled();
			}
		});
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		namePanel.add(new JLabel("Customer Name: "));
		namePanel.add(nameField);
		
		JPanel initialBalancePanel = new JPanel();
		initialBalancePanel.setLayout(new BoxLayout(initialBalancePanel, BoxLayout.X_AXIS));
		initialBalancePanel.add(new JLabel("Initial Balance: "));
		initialBalancePanel.add(initialBalanceField);
		
		returnHomeButton = new JButton("Return to User Accounts Screen");
		//returnHomeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnHomeButton.setAlignmentX(CENTER_ALIGNMENT);
		returnHomeButton.addActionListener(this);
		
		confirmButton = new ConditionButton("Add Customer");
		confirmButton.addCondition(new ConditionButtonCondition() {
			public boolean checkCondition() {
				return (!nameField.getText().equals("")) &&
					   U.parseMoney(initialBalanceField.getText()) != U.BAD_MONEY;
			}
		});
		confirmButton.checkAndSetEnabled();
		confirmButton.setAlignmentX(CENTER_ALIGNMENT);
		confirmButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(returnHomeButton);
		buttonPanel.add(confirmButton);
		
		this.add(Box.createGlue());
		this.add(namePanel);
		this.add(Box.createGlue());
		this.add(initialBalancePanel);
		this.add(Box.createGlue());
		this.add(buttonPanel);
		this.add(Box.createGlue());
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == returnHomeButton) {
			master.getStatusBar().clearStatus();
			master.popContentPanel();
		} else if(event.getSource() == confirmButton) {
			controller.addCustomer(nameField.getText(), U.parseMoney(initialBalanceField.getText()));
			
			solsScreen.refreshYourselfYouSmellAweful();
			
			master.popContentPanel();
		}
	}
}