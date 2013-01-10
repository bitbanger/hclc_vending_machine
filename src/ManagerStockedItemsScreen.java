import java.util.Collection;
import java.util.ArrayList;

/**
 *
 * @author Kyle Savarese
 *
 */

public class ManagerStockedItemsScreen {

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** all the fooditems */
	private ArrayList<FoodItem> stockpile;

	/**
 	 * constructor
	 */
	public ManagerStockedItemsScreen( Collection<FoodItem> items ) {
		stockpile = new ArrayList<FoodItem> ( items );
	}

	/**
	 * lists the available items
	 */
	public Collection<FoodItem> listItems() {
		return stockpile;
	}
	
	/**
	 * adds an item to the collection
	 * @param name the name of the item
	 * @param price the price in cents
	 * @param freshLength the amount of time the item stays fresh
 	 */
	public void addItem( String name, int price, long freshLength ) {
		FoodItem next = new FoodItem( name, price, freshLength );
		db.updateOrCreateFoodItem( next );
		stockpile.add( next );
	}
	
	/**
	 * changes an item name
	 * @param id the id of the item
	 * @param name the new name
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemName( int id, String name ) {

	}

	/** 
	 * change an item's price
 	 * @param id the id of the item
	 * @param price the new price
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemPrice( int id, int price ) {

	}

	/**
	 * change item length
	 * @param id the id of the item
	 * @param freshLength the new length
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemFreshLength( int id, long freshLength ) {

	}

	/** 
	 * exit the screen
	 */
	public void exit() {

	}
}
