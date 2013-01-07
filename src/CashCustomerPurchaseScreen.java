/**
 * Cash handling screen
 * @author Kyle Savarese
 * 
 */

public class CashCustomerPurchaseScreen extends CustomerPurchaseScreen {

	/**
	 * creates the cashcustomer screen
	 * @param user the user
	 * @param vm the vending machine
	 */
	public CashCustomerPurchaseScreen( Customer user, VendingMachine vm ) {
		super( user, vm );
	}
	
	/**
 	 * adds more cash to the cash customer
	 * @param cash the amount of money to add
	 */
	public void addCash ( int cash ) {
		getUser().deductMoney(-cash);
	}
}
