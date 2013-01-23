import java.util.Collection;
import java.util.ArrayList;

/**
 * 
 * @author Kyle Savarese
 * 
 */

public class ManagerReportStatsScreen {

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** all the machines */
	private ArrayList<VendingMachine> machines;

	/**
	 * All of the customers
	 **/
	private ArrayList<Customer> customers;

	/**
	 * All of the items
	 **/
	private ArrayList<FoodItem> items;

	/**
	 * base constructor
	 */
	public ManagerReportStatsScreen () {
		try {
			machines = db.getVendingMachinesAll();
			customers = db.getCustomersAll();
			items = db.getFoodItemsAll();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.FATAL, databaseProblem);
		}
	}

	/**
	 * get a collection of all machines
	 * @return a collection of the machines present when screen was launched
	 */
	public ArrayList<VendingMachine> listMachines() {
		return machines;
	}

	/**
	 * get a collection of transactions based on a specific machine
	 * @param machine the machine in question
	 * @return the transactions made at that machine
	 */
	public ArrayList<Transaction> listMachineSales( VendingMachine machine ) {
		try
		{
			return db.getTransactionsByVendingMachine( machine );
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return null;
		}
	}

	/**
	 * Gets all of the customers 
	 * @return An ArrayList containing all the customers.
	 **/
	public ArrayList<Customer> listCustomers()
	{
		return customers;
	}

	/**
	 * Gets all of the transactions by a specific customer
	 * @param customer The customer that completed the transactions
	 * @return An ArrayList containing all of the transactions by the given
	 * customer.
	 **/
	public ArrayList<Transaction> listCustomerSales(Customer customer)
	{
		try
		{
			return db.getTransactionsByCustomer(customer);
		}
		catch (Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return null;
		}
	}

	/**
	 * Gets all of the items
	 * @return An ArrayList containing all the items
	 **/
	public ArrayList<FoodItem> listFoodItems()
	{
		return items;
	}

	/**
	 * Gets all of the transactions during which a specific item was bought
	 * @param item The item that was purchased in the transactions you desire
	 * @return An ArrayList containing all of the transactions during which
	 * the given item was bought
	 **/
	public ArrayList<Transaction> listFoodItemSales(FoodItem item)
	{
		try
		{
			return db.getTransactionsByFoodItem(item);
		}
		catch (Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return null;
		}
	}

	/**
	 * Gets all of the transactions from all VendingMachines.
	 * @return An ArrayList of all of the transactions.
	 **/
	public ArrayList<Transaction> listSalesAll()
	{
		try {
			ArrayList<Transaction> transactions = db.getTransactionsAll();
			return transactions;
		} catch (Exception databaseProblem){
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return null;
		}

	}
}
