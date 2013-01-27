import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;

/**
 * GUI that allows customers with an id to purchase goods.
 * @author Matthew Koontz
 **/
public class CustomerPurchaseScreenGUI extends JPanel
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
	 * Creates the panel using the given arguments.
	 * @param controller The controller instance for this GUI.
	 * @param master The master for this GUI.
	 **/
	public CustomerPurchaseScreenGUI(CustomerPurchaseScreen controller, BaseGUI master)
	{
		this.master = master;
		this.controller = controller;
		vmButtons = new VMLayoutPanel(controller.listLayout(), false);
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
		JButton cancelButton = new JButton("Cancel");
		bottomPanel.add(cancelButton);

		// Spacing between buttons
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// Purchase button
		JButton purchaseButton = new JButton("Purchase!");
		bottomPanel.add(purchaseButton);

	}
}
