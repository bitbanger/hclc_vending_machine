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

public class ManagerUserAccountsScreenChangeManagerPasswordGUI extends JPanel implements ActionListener {
	/** Controller instance to allow this screen's manipulation of the model */
	private ManagerUserAccountsScreen controller;
	
	/** BaseGUI container for this panel */
	private BaseGUI master;
	
	/** Manager whose password we wish to change */
	private Manager toChange;
	
	/** The field for the manager's name */
	private JTextField nameField;
	
	/** The field for the new password */
	private JPasswordField passwordField;
	
	/** Password confirmation field for added certainty */
	private JPasswordField confirmPasswordField;
	
	/** Button to return to the user accounts screen */
	private JButton returnHomeButton;
	
	/** Button to confirm the change or manager addition */
	private ConditionButton confirmButton;
	
	/** Text to use for the confirmation button (either manager change or addition) */
	private String confirmButtonText;
	
	private ManagerUserAccountsScreenGUI solsScreen;
	
	public ManagerUserAccountsScreenChangeManagerPasswordGUI(ManagerUserAccountsScreen controller, BaseGUI master, Manager toChange, ManagerUserAccountsScreenGUI solsScreen) {
		this.controller = controller;
		this.master = master;
		this.toChange = toChange;
		this.solsScreen = solsScreen;
		
		if(toChange == null) {
			this.confirmButtonText = "Add as New Manager";
		} else {
			this.confirmButtonText = "Update Manager";
		}
		
		master.getStatusBar().clearStatus();
		addComponents();
	}
	
	private void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setAlignmentX(CENTER_ALIGNMENT);
		
		nameField = new JTextField();
		passwordField = new JPasswordField();
		confirmPasswordField = new JPasswordField();
		
		if(toChange != null) {
			nameField.setText(toChange.getName());
			nameField.setEnabled(false);
			passwordField.setText(toChange.getPassword());
			confirmPasswordField.setText(toChange.getPassword());
		}
		
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
		
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
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
		
		confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
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
		
		returnHomeButton = new JButton("Cancel");
		returnHomeButton.addActionListener(this);
		
		confirmButton = new ConditionButton(confirmButtonText);
		confirmButton.addCondition(new ConditionButtonCondition() {
			public boolean checkCondition() {
				String name = nameField.getText();
				String pass1 = new String(passwordField.getPassword());
				String pass2 = new String(confirmPasswordField.getPassword());
				
				return (!pass1.equals("")) &&
					   (!pass2.equals("")) &&
					   (pass1.equals(pass2));
			}
		});
		confirmButton.checkAndSetEnabled();
		confirmButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(returnHomeButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(confirmButton);
		
		LabeledFieldPanel fieldsPanel = new LabeledFieldPanel();
		fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		nameField.setColumns(20);
		fieldsPanel.addLabeledTextField("Manager name:", nameField);
		fieldsPanel.addLabeledTextField("Manager Password:", passwordField);
		fieldsPanel.addLabeledTextField("Confirm Manager Password:", confirmPasswordField);
		this.add(fieldsPanel);

		this.add(buttonPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == returnHomeButton) {
			master.getStatusBar().clearStatus();
			master.popContentPanel();
			master.getStatusBar().setStatus("Manager changes canceled", StatusBar.STATUS_WARN_COLOR);
		} else if(event.getSource() == confirmButton) {
			master.popContentPanel();
			if(toChange != null) {
				controller.changePassword(toChange, new String(passwordField.getPassword()));
				master.getStatusBar().setStatus("Manager edited");
			} else {
				controller.addManager(nameField.getText(), new String(passwordField.getPassword()));
				master.getStatusBar().setStatus("Manager created");
			}
			
			solsScreen.refreshYourselfYouSmellAwful();
			
		}
	}
}
