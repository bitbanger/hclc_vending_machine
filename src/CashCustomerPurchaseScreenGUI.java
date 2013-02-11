import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * GUI that allows customers without an id to purchase goods.
 * @author Matthew Koontz
 **/
public class CashCustomerPurchaseScreenGUI extends JPanel implements ActionListener
{
	/**
	 * Controller for this GUI.
	 **/
	private CashCustomerPurchaseScreen controller;

	/**
	 * Panel that allows the user to select an item.
	 **/
	private VMLayoutPanel vmButtons;

	/**
	 * Master for this panel.
	 **/
	private BaseGUI master;

	/**
	 * Button that cancels the purchase
	 **/
	private JButton cancelButton;

	/**
	 * Button that makes the purchase
	 **/
	private JButton purchaseButton;

	/**
	 * Displays how much money the customer has entered
	 **/
	private JLabel enteredLabel;

	/**
	 * Allows the customer to enter money
	 **/
	private MoneyField enterMoneyField;

	/**
	 * Allows the user to confirm the money they are entering
	 **/
	private ConditionButton enterMoneyButton;

	/**
	 * Amount of money (in cents) that the customer has entered
	 **/
	private int moneyEntered;
	
	/**
	 * CustomerLoginScreenGUI that created this screen.
	 **/
	private CustomerLoginScreenGUI parent;

	/**
	 * Creates the panel using the given arguments.
	 * @param controller The controller instance for this GUI.
	 * @param master The master for this GUI.
	 * @param parent CustomerLoginScreenGUI that created this screen.
	 **/
	public CashCustomerPurchaseScreenGUI(CashCustomerPurchaseScreen controller, BaseGUI master, CustomerLoginScreenGUI parent)
	{
		master.getStatusBar().clearStatus();
		this.master = master;
		this.controller = controller;
		this.parent = parent;
		this.moneyEntered = 0;


		vmButtons = new VMLayoutPanel(controller.listLayout(), false);
		enterMoneyField = new MoneyField(master.getStatusBar());
		enterMoneyButton = new ConditionButton("Enter Money");

		master.getStatusBar().setStatus(String.format("Welcome, %s", controller.getUser().getName()), StatusBar.STATUS_GOOD_COLOR);

		addComponents();
	}

	/**
	 * Lays out the GUI
	 **/
	private void addComponents()
	{
		// Sets the layout to a vertical layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// adds the vending machine layout panel
		vmButtons.setAlignmentX(LEFT_ALIGNMENT);
		add(vmButtons);

		// Spacing between panel and bottom controls
		add(Box.createGlue());
		add(Box.createRigidArea(new Dimension(0,20)));

		// Panel for entering money
		JPanel enterAmountPanel = new JPanel();
		enterAmountPanel.setAlignmentX(LEFT_ALIGNMENT);
		enterAmountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		enterAmountPanel.setLayout(new BoxLayout(enterAmountPanel, BoxLayout.X_AXIS));
		add(enterAmountPanel);

		// Add listener for money field so it can update add money button
		final ConditionButton tempButton = enterMoneyButton;
		enterMoneyField.getDocument().addDocumentListener(new DocumentListener()
		{
			/** @inheritDoc */
			public void changedUpdate(DocumentEvent ignored) {}

			/** @inheritDoc */
			public void insertUpdate(DocumentEvent ignored)
			{
				tempButton.checkAndSetEnabled();
			}

			/** @inheritDoc */
			public void removeUpdate(DocumentEvent ignored)
			{
				tempButton.checkAndSetEnabled();
			}
		});

		// Add money field
		enterAmountPanel.add(enterMoneyField);

		// Add space between money field and button
		enterAmountPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		// Add condition for condition button
		final MoneyField tempMoneyField = enterMoneyField;
		enterMoneyButton.addCondition(new ConditionButtonCondition()
		{
			public boolean checkCondition()
			{
				return tempMoneyField.areContentsValid();
			}
		});

		// Make this class handle when the button is clicked
		enterMoneyButton.addActionListener(this);

		// Add the money button
		enterAmountPanel.add(enterMoneyButton);

		// Set the size of this panel so it isn't huge
		enterAmountPanel.setMaximumSize(enterAmountPanel.getPreferredSize());

		// Space between add money and bottom controls
		add(Box.createRigidArea(new Dimension(0,10)));

		// Panel for bottom controls
		JPanel bottomPanel = new JPanel();
		bottomPanel.setAlignmentX(LEFT_ALIGNMENT);
		add(bottomPanel);

		// Horizontal layout for bottom controls
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		// Label to display amount of money entered
		enteredLabel = new JLabel();
		bottomPanel.add(enteredLabel);
		displayMoneyEntered();

		// Spacing between label and buttons
		bottomPanel.add(Box.createGlue());

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);

		// Spacing between buttons
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// Purchase button
		purchaseButton = new JButton("Purchase!");
		purchaseButton.addActionListener(this);
		bottomPanel.add(purchaseButton);

	}

	private void displayMoneyEntered()
	{
		enteredLabel.setText(String.format("Money Entered: %s", U.formatMoney(moneyEntered)));
	}

	/**
	 * Handles the cancel and purchase buttons being clicked
	 * @param event Contains information regarding the event
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		JButton source = (JButton)event.getSource();
		if (source == cancelButton)
		{
			master.popContentPanel();
			master.getStatusBar().setStatus("Logged out", StatusBar.STATUS_GOOD_COLOR);
			parent.refreshItemPurchased(false, "");
		}
		else if (source == purchaseButton)
		{
			Pair<Integer, Integer> selected = vmButtons.getSelectedRow();
			if (selected == null)
			{
				master.getStatusBar().setStatus("You haven't selected an item yet!", StatusBar.STATUS_BAD_COLOR);
				return;
			}
			String result = controller.tryPurchase(selected);
			if (result.equals("Good"))
			{
				master.popContentPanel();
				parent.refreshItemPurchased(true, "<html><p>Thank you for purchasing " + controller.getPurchasedItem().getName() + "</p><p>Your change is " + U.formatMoney(controller.getBalance()) + "</p>");
			}
			else
			{
				master.getStatusBar().setStatus(result, StatusBar.STATUS_BAD_COLOR);
				return;
			}
		}
		else if (source == enterMoneyButton)
		{
			int moneyEnteredNow = enterMoneyField.getMonetaryAmount();
			controller.addCash(moneyEnteredNow);
			moneyEntered += moneyEnteredNow;
			displayMoneyEntered();
			enterMoneyField.clearMoneyEntered();
			master.getStatusBar().setStatus(String.format("%s entered.", U.formatMoney(moneyEnteredNow), StatusBar.STATUS_GOOD_COLOR));
		}
	}
}
