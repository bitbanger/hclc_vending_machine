import java.sql.SQLException;

/**
 * 1/3/2012
 * 
 * @author Kyle Savarese
 *
 * CustomerLoginScreen.java
 * initial screen for a customer's transaction
 */

public class CustomerLoginScreen {

	/** the Database instance */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** VendingMachine the customer is at */
	private VendingMachine vm;

	/**
	 * Constructor for LoginScreen
	 * @param curr the VendingMachine that is being logged into
 	 */
	public CustomerLoginScreen ( VendingMachine curr ) {
		vm = curr;
	}	

	/**
 	 * attempts to use the provided id to login
	 * @param id the id to check
	 * @return the CustomerPurchaseScreen associated with the 
	 *  	Customer and the VendingMachine or null if the Customer
	 * 	is not found
	 * @throws SQLException from the DatabaseLayer
	 */
	public CustomerPurchaseScreen tryLogin ( int id ) throws SQLException {
		try
		{
			Customer user = db.getCustomerById ( id );
			if ( user == null )
				return null;
			return new CustomerPurchaseScreen( user, vm );
		}
		catch(Exception databaseProblem)
		{
			System.err.println("ERROR: Database problem encountered!");
			System.err.print("     : Dump details ... ");
			databaseProblem.printStackTrace();
			System.err.println();
			return null;
		}
	}
	
	/**
	 * creates the CustomerPurchaseScreen state
	 * @return the CashCustomerPurchaseScreen
	 * @throws SQLException from the DatabaseLayer
	 */
	public CashCustomerPurchaseScreen cashLogin () throws SQLException {
		try
		{
			Customer user = new Customer();
			return new CashCustomerPurchaseScreen( user, vm );
		}
		catch(Exception databaseProblem)
		{
			System.err.println("ERROR: Database problem encountered!");
			System.err.print("     : Dump details ... ");
			databaseProblem.printStackTrace();
			System.err.println();
			return null;
		}
	}
}
