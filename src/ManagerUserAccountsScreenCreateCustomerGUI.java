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
import java.awt.GridLayout;

public class ManagerUserAccountsScreenCreateCustomerGUI extends JPanel implements ActionListener {
	/** Controller instance to allow this screen's manipulation of the model */
	private ManagerUserAccountsScreen controller;
	
	/** BaseGUI container for this panel */
	private BaseGUI master;
	
	/** The field for the manager's name */
	private JTextField nameField;
	
	/** The field for the customer's initial money balance */
	private MoneyField initialBalanceField;
	
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
		
		nameField = new JTextField();
		
		initialBalanceField = new MoneyField(master.getStatusBar());
		
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

		LabeledFieldPanel fieldPanel = new LabeledFieldPanel();
		fieldPanel.setAlignmentX(LEFT_ALIGNMENT);
		add(fieldPanel);
		
		fieldPanel.addLabeledTextField("Customer Name: ", nameField);
		fieldPanel.addLabeledTextField("Initial Balance: ", initialBalanceField);
		
		returnHomeButton = new JButton("Cancel");
		//returnHomeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnHomeButton.setAlignmentX(LEFT_ALIGNMENT);
		returnHomeButton.addActionListener(this);
		
		confirmButton = new ConditionButton("Add Customer");
		confirmButton.addCondition(new ConditionButtonCondition() {
			public boolean checkCondition() {
				return (!nameField.getText().equals("")) &&
					   initialBalanceField.areContentsValid();
			}
		});
		confirmButton.checkAndSetEnabled();
		confirmButton.setAlignmentX(LEFT_ALIGNMENT);
		confirmButton.addActionListener(this);

		add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(returnHomeButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(confirmButton);
		add(buttonPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == returnHomeButton) {
			master.getStatusBar().clearStatus();
			master.popContentPanel();
			master.getStatusBar().setStatus("Customer changes canceled", StatusBar.STATUS_WARN_COLOR);
		} else if(event.getSource() == confirmButton) {
			controller.addCustomer(nameField.getText(), U.parseMoney(initialBalanceField.getText()));
			
			solsScreen.refreshYourselfYouSmellAwful();
			
			master.popContentPanel();
			master.getStatusBar().setStatus("Customer created");
		}
	}
}
