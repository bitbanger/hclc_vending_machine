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
	 * @return the id of the added item
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
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... "+databaseProblem);
			return -1;
		}
	}
	
	/**
	 * changes an item name
	 * @param id the id of the item
	 * @param name the new name
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemName( int id, String name ) {
		try {
			FoodItem item = db.getFoodItemById( id );
			item.setName( name );
			db.updateOrCreateFoodItem( item );
			return true;
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... " + databaseProblem);
			return false;
		}
	}

	/** 
	 * change an item's price
 	 * @param id the id of the item
	 * @param price the new price
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemPrice( int id, int price ) {
		try {
			FoodItem item = db.getFoodItemById( id );
			item.setPrice( price );
			db.updateOrCreateFoodItem( item );
			return true;
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... " + databaseProblem);
			return false;
		}
	}

	/**
	 * change item length
	 * @param id the id of the item
	 * @param freshLength the new length
	 * @return boolean whether it succeeded
	 */
	public boolean changeItemFreshLength( int id, long freshLength ) {
		try {
			FoodItem item = db.getFoodItemById( id );
			item.setFreshLength( freshLength );
			db.updateOrCreateFoodItem( item );
			return true;
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... " + databaseProblem);
			return false;
		}
	}

	/** 
	 * exit the screen
	 */
	public void exit() {

	}
}
