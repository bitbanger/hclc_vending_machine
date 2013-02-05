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
		nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)nameField.getPreferredSize().getHeight()));
		passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)passwordField.getPreferredSize().getHeight()));
		confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)confirmPasswordField.getPreferredSize().getHeight()));
		//nameField.setAlignmentX(CENTER_ALIGNMENT);
		
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
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		namePanel.add(new JLabel("Manager Name: "));
		namePanel.add(nameField);
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		passwordPanel.add(new JLabel("Manager Password: "));
		passwordPanel.add(passwordField);
		
		JPanel confirmPasswordPanel = new JPanel();
		confirmPasswordPanel.setLayout(new BoxLayout(confirmPasswordPanel, BoxLayout.X_AXIS));
		confirmPasswordPanel.add(new JLabel("Confirm Manager Password: "));
		confirmPasswordPanel.add(confirmPasswordField);
		//passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//passwordField.setAlignmentX(CENTER_ALIGNMENT);
		//confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//confirmPasswordField.setAlignmentX(CENTER_ALIGNMENT);
		
		returnHomeButton = new JButton("Return to User Accounts Screen");
		//returnHomeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		returnHomeButton.setAlignmentX(CENTER_ALIGNMENT);
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
		confirmButton.setAlignmentX(CENTER_ALIGNMENT);
		confirmButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(returnHomeButton);
		buttonPanel.add(confirmButton);
		
		this.add(Box.createGlue());
		this.add(namePanel);
		this.add(Box.createGlue());
		this.add(passwordPanel);
		this.add(Box.createGlue());
		this.add(confirmPasswordPanel);
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
			if(toChange != null) {
				controller.changePassword(toChange, new String(passwordField.getPassword()));
			} else {
				controller.addManager(nameField.getText(), new String(passwordField.getPassword()));
			}
			
			solsScreen.refreshYourselfYouSmellAwful();
			
			master.popContentPanel();
		}
	}
}