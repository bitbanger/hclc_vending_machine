import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.LinkedList;

/**
 * Panel for displaying a vending machine's layout. Has modes for customer
 * and manager.
 * @author Matthew Koontz
 **/
public class VMLayoutPanel extends JPanel implements ActionListener
{
	/**
	 * Grid of toggle buttons that display the items.
	 **/
	private JToggleButton[][] grid;

	/**
	 * The selected row. Defaults to null.
	 **/
	private Pair<Integer, Integer> selectedRow;

	/**
	 * List of all of the VendingMachineItemChangedListeners for this VMLayoutPanel.
	 **/
	private LinkedList<VendingMachineItemChangedListener> itemChangedListeners;

	/**
	 * Whether this VMLayoutPanel should display in manager mode.
	 **/
	private boolean managerMode;

	/**
	 * Creates a VMLayoutPanel using the given array of items.
	 * @param items The items to display in this panel.
	 * @param managerMode If true it will allow the user to select empty rows
	 * and rows with deactivated items.
	 **/
	public VMLayoutPanel(FoodItem[][] items, boolean managerMode)
	{
		this.managerMode = managerMode;
		itemChangedListeners = new LinkedList<VendingMachineItemChangedListener>();
		grid = new JToggleButton[items.length][items[0].length];
		selectedRow = null;
		addComponents(items);
	}

	/**
	 * Adds the components to the VMLayout
	 * @param items The items to display in this panel.
	 * @param managerMode If true it will allow the user to select empty rows
	 * and rows with deactivated items.
	 **/
	private void addComponents(FoodItem[][] items)
	{
		// Allows the panel to expand
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		// height and width
		int height = items[0].length;
		int width = items.length;

		// Sets the layout to a grid layout
		setLayout(new GridLayout(height, width, 5, 5));
		
		refreshContent(items);
	}

	/**
	 * Refreshes the content by displaying the given 2D array of items.
	 * @param items The food items to display for the user to select.
	 **/
	public void refreshContent(FoodItem[][] items)
	{
		// height and width
		int height = items[0].length;
		int width = items.length;
		removeAll();

		// Add buttons
		for (int i=0;i<height;++i)
		{
			for (int j=0;j<width;++j)
			{
				// Item for this location
				FoodItem item = items[j][i];

				// Set name to item's name or EMPTY if item is null
				String name = "EMPTY";
				if (item != null) {
					name = item.getName();
				}

				// Set price to item's price or "" (the empty string) if item
				// is null
				String price = "";
				if (item != null)
					price = CLIUtilities.formatMoney(item.getPrice());


				// Create toggle button with name and price
				grid[j][i] = new JToggleButton(String.format("<html>%s<br />%s</html>", name, price));
				// Disable the button if it is (null or inactive) and the
				// manager mode is not set
				if ((item == null || !item.isActive()) && !managerMode)
					grid[j][i].setEnabled(false);

				// This class will handle the button being clicked
				grid[j][i].addActionListener(this);

				// Allow the button to expand
				grid[j][i].setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
				
				//Add the button to the panel
				add(grid[j][i]);
			}
		}

		// Clear selected row
		selectedRow = null;

		notifyItemChangedListeners();

		// Needed to ensure the panel is repainted (don't get me started on
		// the difficulty of determining which method I needed to call to have
		// this work).
		revalidate();
	}
	

	/**
	 * @return The row currently selected, or null if no row is selected.
	 **/
	public Pair<Integer, Integer> getSelectedRow()
	{
		return selectedRow;
	}

	/**
	 * Handles a toggle button getting clicked. Ensures the toggle button clicked
	 * remains selected and all the other toggle buttons are not selected. Also
	 * sets selectedRow to the appropriate value.
	 * @param event Contains information regarding the action that was performed.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		JToggleButton source = (JToggleButton)event.getSource();
		source.setSelected(true);
		int height = grid[0].length;
		int width = grid.length;
		for (int i=0;i<height;++i)
		{
			for (int j=0;j<width;++j)
			{
				JToggleButton button = grid[j][i];
				if (button == source)
					selectedRow = new Pair<Integer, Integer>(j, i);
				else
					button.setSelected(false);
			}
		}

		notifyItemChangedListeners();
	}

	/**
	 * Adds a listener to this VMLayoutPanel.
	 * @param listener The listener to add.
	 **/
	public void addVendingMachineItemChangedListener(VendingMachineItemChangedListener listener)
	{
		itemChangedListeners.add(listener);
	}

	/**
	 * @return The number of rows in this panel.
	 **/
	public int getNumberOfRows()
	{
		return grid[0].length;
	}

	/**
	 * Notifies our VendingMachineItemChangedListeners that this VMLayoutPanel
	 * may have changed.
	 **/
	private void notifyItemChangedListeners()
	{
		for (VendingMachineItemChangedListener listener : itemChangedListeners)
			listener.itemChanged();
	}
}
