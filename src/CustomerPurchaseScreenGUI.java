import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JButton;
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
	 * Creates the panel using the given arguments.
	 * @param controller The controller instance for this GUI.
	 * @param master The master for this GUI.
	 **/
	public CustomerPurchaseScreenGUI(CustomerPurchaseScreen controller, BaseGUI master)
	{
		this.master = master;
		this.controller = controller;
		vmButtons = new VMLayoutPanel(controller.listLayout(), false);

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
		add(vmButtons);

		// Spacing between panel and bottom controls
		add(Box.createGlue());
		add(Box.createRigidArea(new Dimension(0,20)));

		// Panel for bottom controls
		JPanel bottomPanel = new JPanel();
		add(bottomPanel);

		// Horizontal layout for bottom controls
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		// Label that gives current balance
		JLabel fundLabel = new JLabel(String.format("Your funds: %s", CLIUtilities.formatMoney(controller.getBalance())));
		bottomPanel.add(fundLabel);

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
		}
		else
		{
			Pair<Integer, Integer> selected = vmButtons.getSelectedRow();
			if (selected == null)
			{
				master.getStatusBar().setStatus("You haven't selected an item yet!", StatusBar.STATUS_BAD_COLOR);
				return;
			}
			String result = controller.tryPurchase(selected);
			if (result.equals("GOOD"))
			{
				master.getStatusBar().setStatus("Item purchased", StatusBar.STATUS_GOOD_COLOR);
				master.popContentPanel();
			}
			else
			{
				master.getStatusBar().setStatus(result, StatusBar.STATUS_BAD_COLOR);
				return;
			}
		}
	}
}
