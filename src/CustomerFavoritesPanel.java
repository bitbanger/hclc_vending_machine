import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.util.LinkedList;

/**
 * Displays buttons that allow the user to select from frequently bought items.
 * @author Matthew Koontz
 **/
public class CustomerFavoritesPanel extends JPanel implements ActionListener
{
	/**
	 * Buttons that allow the user to select their favorite
	 **/
	private ArrayList<JToggleButton> buttons;

	/**
	 * Array of favorite food items.
	 **/
	private ArrayList<FoodItem> favorites;

	/**
	 * Currently selected item. Null if no item is selected.
	 **/
	private FoodItem selected;

	/**
	 * List of all of the VendingMachineItemChangedListeners for this panel.
	 **/
	private LinkedList<VendingMachineItemChangedListener> itemChangedListeners;

	/**
	 * Creates a new favorites panel of the given size and displays the given
	 * favorites.
	 * @param favorites The favorites to display.
	 * @param maxSize The number of rows to split the panel into. Should be the
	 * same as the VMLayoutPanel beside this.
	 **/
	public CustomerFavoritesPanel(ArrayList<FoodItem> favorites, int maxSize)
	{
		itemChangedListeners = new LinkedList<VendingMachineItemChangedListener>();
		this.favorites = new ArrayList<FoodItem>(favorites);
		this.selected = null;
		if (maxSize < this.favorites.size())
			this.favorites.subList(maxSize, this.favorites.size() - 1).clear();
		buttons = new ArrayList<JToggleButton>();
		addComponents(maxSize);
	}

	/**
	 * Adds the components to the panel.
	 * @param size The number of rows to split the panel into.
	 **/
	private void addComponents(int size)
	{
		setLayout(new GridLayout(size, 1, 5, 5));
		for (FoodItem item : favorites)
		{
			JToggleButton button = new JToggleButton(String.format("<html>%s<br />%s</html>", item.getName(), item.getPrice()));
			button.addActionListener(this);
			buttons.add(button);
			add(button);
		}
	}

	/**
	 * Handles button presses.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		for (int i=0;i<buttons.size();++i)
		{
			JToggleButton button = buttons.get(i);
			if (button == source)
			{
				button.setSelected(true);
				selected = favorites.get(i);
			}
			else
			{
				button.setSelected(false);
			}
		}

		notifyItemChangedListeners();
	}

	public void clearSelection()
	{
		selected = null;
		for (int i=0;i<buttons.size();++i)
			buttons.get(i).setSelected(false);
		notifyItemChangedListeners();
	}


	/**
	 * Adds a listener to this panel.
	 * @param listener The listener to add.
	 **/
	public void addVendingMachineItemChangedListener(VendingMachineItemChangedListener listener)
	{
		itemChangedListeners.add(listener);
	}

	/**
	 * @return The selected food item or null if no food item is selected.
	 **/
	public FoodItem getSelectedItem()
	{
		return selected;
	}

	/**
	 * Notifies our VendingMachineItemChangedListeners that this panel may have changed.
	 **/
	private void notifyItemChangedListeners()
	{
		for (VendingMachineItemChangedListener listener : itemChangedListeners)
			listener.itemChanged();
	}
}
