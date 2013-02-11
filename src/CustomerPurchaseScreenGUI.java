import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * GUI that allows customers with an id to purchase goods.
 * @author Matthew Koontz
 **/
public class CustomerPurchaseScreenGUI extends JPanel implements ActionListener
{
	/**
	 * Controller for this GUI.
	 **/
	private CustomerPurchaseScreen controller;

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
	 * Panel that allows the user to select from frequently bought items.
	 **/
	private CustomerFavoritesPanel favoritesPanel;

	/**
	 * CustomerLoginScreenGUI that created this screen.
	 **/
	private CustomerLoginScreenGUI parent;

	/**
	 * Creates the panel using the given arguments.
	 * @param controller The controller instance for this GUI.
	 * @param master The master for this GUI.
	 * @param parent CustomerLoginScreenGUI that created this screen
	 **/
	public CustomerPurchaseScreenGUI(CustomerPurchaseScreen controller, BaseGUI master, CustomerLoginScreenGUI parent)
	{
		master.getStatusBar().clearStatus();
		this.master = master;
		this.controller = controller;
		this.parent = parent;
		vmButtons = new VMLayoutPanel(controller.listLayout(), false);

		master.getStatusBar().setStatus(String.format("Welcome, %s", controller.getUser().getName()), StatusBar.STATUS_GOOD_COLOR);

		addComponents();
		addLogic();
	}

	/**
	 * Adds logic to the components.
	 **/
	private void addLogic()
	{
		vmButtons.addVendingMachineItemChangedListener(new VendingMachineItemChangedListener()
		{
			@Override
			public void itemChanged()
			{
			 	if (vmButtons.getSelectedRow() != null)
					favoritesPanel.clearSelection();
			}
		});

		favoritesPanel.addVendingMachineItemChangedListener(new VendingMachineItemChangedListener()
		{
			@Override
			public void itemChanged()
			{
				if (favoritesPanel.getSelectedItem() != null)
					vmButtons.clearSelection();
			}
		});

		purchaseButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	/**
	 * Lays out the GUI
	 **/
	private void addComponents()
	{
		// Sets the layout to a vertical layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Creates the preset panel
		JPanel presetPanel = new JPanel();
		presetPanel.setLayout(new BoxLayout(presetPanel, BoxLayout.X_AXIS));
		add(presetPanel);

		// Creates the preset items label
		JLabel presetItems = new JLabel("Frequently Bought");
		presetPanel.add(Box.createVerticalStrut(35));
		presetPanel.add(presetItems);

		// Creates the top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		add(topPanel);

		// adds the vending machine layout panel
		topPanel.add(vmButtons);
		topPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		
		// Creates the favorites panel
		favoritesPanel = new CustomerFavoritesPanel(controller.getFrequentlyBought(), vmButtons.getNumberOfRows());
		topPanel.add(favoritesPanel);


		// Spacing between panel and bottom controls
		add(Box.createGlue());
		add(Box.createRigidArea(new Dimension(0,20)));

		// Panel for bottom controls
		JPanel bottomPanel = new JPanel();
		add(bottomPanel);

		// Horizontal layout for bottom controls
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		// Label that gives current balance
		JLabel fundLabel = new JLabel(String.format("Your funds: %s", U.formatMoney(controller.getBalance())));
		bottomPanel.add(fundLabel);

		// Spacing between label and buttons
		bottomPanel.add(Box.createGlue());

		// Cancel button
		cancelButton = new JButton("Cancel");
		bottomPanel.add(cancelButton);

		// Spacing between buttons
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// Purchase button
		purchaseButton = new JButton("Purchase!");
		bottomPanel.add(purchaseButton);
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
			master.setTitle("Login Screen");
			parent.refreshItemPurchased(false, "");
		}
		else
		{
			Pair<Integer, Integer> selected = vmButtons.getSelectedRow();
			String result;
			if (selected == null)
			{
				FoodItem fav = favoritesPanel.getSelectedItem();
				if (fav == null)
				{
					master.getStatusBar().setStatus("You haven't selected an item yet!", StatusBar.STATUS_BAD_COLOR);
					return;
				}
				else
					result = controller.tryPurchase(fav);
			}
			else
				result = controller.tryPurchase(selected);
			if (result.equals("Good"))
			{
				master.popContentPanel();
				master.getStatusBar().setStatus("Item purchased", StatusBar.STATUS_GOOD_COLOR);
				master.setTitle("Login Screen");
				parent.refreshItemPurchased(true, "Thank you for purchasing " + controller.getPurchasedItem().getName());
			}
			else
			{
				master.getStatusBar().setStatus(result, StatusBar.STATUS_BAD_COLOR);
				return;
			}
		}
	}
}
