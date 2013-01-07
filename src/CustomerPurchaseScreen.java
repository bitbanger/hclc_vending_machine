import java.sql.SQLException;
import java.util.GregorianCalendar;

/**
 *
 * 1/3/2012
 * @author Kyle Savarese
 * 
 * CustomerPurchaseScreen
 */

public class CustomerPurchaseScreen {
	
	/** The Database instance */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** The current Customer */
	private Customer user;

 	/** The current VendingMachine */
	private VendingMachine machine;

	/** 
	 * The Standard CustomerPurchaseScreen constructor
	 * @param cust the current customer
	 * @param vm the VendingMachine the user is at
	 */
	public CustomerPurchaseScreen( Customer cust, VendingMachine vm ) {
		user = cust;
		machine = vm;
	}

	/** 
	 * lists the layout of all the items
	 * @return the layout of items
 	 */
	public FoodItem[][] listLayout() {
		VMLayout locs = machine.getCurrentLayout();
		Row[][] rows = locs.getRows();
		FoodItem[][] items = new FoodItem[rows.length][rows[0].length];
		for ( int i = 0; i < rows.length; i++ ) {
			for ( int j = 0; j < rows[i].length; j++ ) {
				items[i][j] = rows[i][j].getProduct();
			}
		}
		return items;
	}

	/**
	 * attempts to purchase the item at the specified location
	 * @param product the location (as a Pair of Integers) of the product
	 * @return whether the purchase succeeded
 	 */
	public boolean tryPurchase( Pair<Integer, Integer> product ) throws IllegalArgumentException, SQLException {
		VMLayout locs = machine.getCurrentLayout();
		Row[][] rows = locs.getRows();
		if ( product.first < 0 || rows.length < product.first ||
			product.second < 0 || rows[product.first].length < product.second )
			return false; //not a valid location
		if ( rows[product.first][product.second].getRemainingQuantity() <= 0 )
			return false; //check if there is some remaining
		FoodItem item = rows[product.first][product.second].getProduct();
		int cash = this.getBalance();
		int price = item.getPrice();
		if ( cash < price )
			return false; //insufficient funds
		// otherwise we're good
		Transaction trans = new Transaction(new GregorianCalendar(), 
			machine, user, item, product);
		db.updateOrCreateTransaction( trans );
		user.setMoney( cash - price );
		db.updateOrCreateCustomer(user);
		rows[product.first][product.second].decrementRemainingQuantity();
		db.updateOrCreateVendingMachine(machine);
		return true;
	}

	/**
	 * cancels the current purchase
	 */
	public void cancel() {
		//TODO determine what this should do
	}

	/**
	 * This method is <i>not</i> intended for use by the view.
	 * @return the customer logged into the vending machine
	 */
	public Customer getUser()
	{
		return user;
	}

	/**
	 * gets the current balance of the user
	 * @return the amount of money available
	 */
	public int getBalance() {
		return user.getMoney();
	}
}
