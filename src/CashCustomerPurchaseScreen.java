/**
 * Cash handling screen
 * @author Kyle Savarese
 * 
 */

public class CashCustomerPurchaseScreen extends CustomerPurchaseScreen {

	/**
	 * creates the cashcustomer screen
	 */
	public CashCustomerPurchaseScreen( user, vm ) {
		super( user, vm );
	}
	
	/**
 	 * adds more cash to the cash customer
	 * @param cash the amount of money to add
	 */
	public void addCash ( int cash ) {
		user.setMoney(user.getMoney() + cash);		
	}
}
