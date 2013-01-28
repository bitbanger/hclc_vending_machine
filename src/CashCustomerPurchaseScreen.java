/**
 * This is the class for the cash handling screen.
 * @author Kyle Savarese
 * @author Piper Chester  
 */

public class CashCustomerPurchaseScreen extends CustomerPurchaseScreen {

	/**
	 * Creates the cash customer screen.
	 * @param user the user
	 * @param vm the vending machine
	 */
	public CashCustomerPurchaseScreen( Customer user, VendingMachine vm ) {
		super( user, vm );
	}
	
	/**
 	 * Adds more cash to the cash customer.
	 * @param cash the amount of money to add
	 * @return true if the cash to add was valid, false if the cash was negative
	 */
	public boolean addCash ( int cash ) {
		if ( cash <= 0 )
			return false;
		else
			getUser().deductMoney(-cash);
			return true;
	}
}

