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
	 */
	public ManagerStockedItemsScreen( Collection<FoodItem> items ) {
		stockpile = new ArrayList<FoodItem> ( items );
	}

	/**
	 * lists the available items
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
	 * @param id the id of the item
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
	 * @param id the id of the item
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
 	 * @param id the id of the item
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
	 * change item length
	 * @param id the id of the item
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
