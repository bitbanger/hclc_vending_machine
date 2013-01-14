import java.util.Collection;
import java.util.ArrayList;

/**
 *
 * @author Kyle Savarese
 *
 */

public class ManagerStockedItemsScreen {
	/** a sentinel key indicating backend failure */
	public static final int FAILURE_KEY=-1;

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** all the fooditems */
	private ArrayList<FoodItem> stockpile;

	/**
 	 * constructor
	 * @param items The items this screen should handle. Should always be all
	 * of the items.
	 */
	public ManagerStockedItemsScreen( Collection<FoodItem> items ) {
		stockpile = new ArrayList<FoodItem> ( items );
	}

	/**
	 * lists the available items
	 * @return An array list containing the items this screen handles.
	 */
	public ArrayList<FoodItem> listItems() {
		return stockpile;
	}
	
	/**
	 * adds an item to the collection
	 * @param name the name of the item
	 * @param price the price in cents
	 * @param freshLength the amount of time the item stays fresh
	 * @return the id of the added item, or <tt>FAILURE_KEY</tt> on failure
 	 */
	public int addItem( String name, int price, long freshLength ) {
		try
		{
			FoodItem next = new FoodItem( name, price, freshLength );
			db.updateOrCreateFoodItem( next );
			stockpile.add( next );
			return next.getId();
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return FAILURE_KEY; // I wish we were doing this in SML or Haskell
		}
	}

	/**
	 * change an item's activation level
	 * @param item The food item to change
	 * @param val the new value of the item
	 * @return whether it succeeded
	 */
	public boolean changeItemStatus( FoodItem item, boolean val ) {
		try {
			item.makeActive( val );
			db.updateOrCreateFoodItem( item );
			return true;
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
	}
	
	/**
	 * changes an item name
	 * @param item The food item to change
	 * @param name the new name
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemName( FoodItem item, String name ) {
		try {
			item.setName( name );
			db.updateOrCreateFoodItem( item );
			return true;
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
	}

	/** 
	 * change an item's price
 	 * @param item The food item to change
	 * @param price the new price
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemPrice( FoodItem item, int price ) {
		try {
			item.setPrice( price );
			db.updateOrCreateFoodItem( item );
			return true;
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
	}

	/**
	 * change the length (in days) the item is good for
	 * @param item The food item to change
	 * @param freshLength the new length
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemFreshLength( FoodItem item, long freshLength ) {
		try {
			item.setFreshLength( freshLength );
			db.updateOrCreateFoodItem( item );
			return true;
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
	}
}
