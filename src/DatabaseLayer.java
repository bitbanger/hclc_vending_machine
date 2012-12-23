/**
 * The DatabaseLayer class contains static methods for accessing the sqlite
 * database. It includes methods to get, update, and create objects in the
 * database.
 * 
 * @author Matthew Koontz
 **/

public class DatabaseLayer
{
	/**
	 * File path of the sqlite database.
	 **/
	private static String dbLocation;

	/**
	 * Connection to the sqlite database. Statements are run against this object.
	 **/
	private static Connection db;

	/**
	 * Fetches the item with a given id from the database.
	 * @param id The id of the item to fetch.
	 * @return The item in the database with the given id or null if no item
	 * with the given id exists.
	 **/
	public static Item getItemById(int id)
	{
	}

	/**
	 * Gets all of the items in the database.
	 * @return A collection of all of the items in the database.
	 **/
	public static Collection<Item> getItemsAll()
	{
	}

	/**
	 * Updates the item in the database. If the item does not already exists
	 * (determined by the id of the item) then the item is created. If an item
	 * is created then the id is updated with the auto incremented one.
	 * @param item The item to update
	 **/
	public static void updateOrCreateItem(Item item)
	{
	}

	/**
	 * Fetches the vending machine with the given id.
	 * @param id The id of the vending machine to fetch.
	 * @return The vending machine with the given id or null if the vending
	 * machine does not exist.
	 **/
	public static VendingMachine getVendingMachineById(int id)
	{
	}

	/**
	 * Fetches all of the vending machines in the database.
	 * @return Collection of all of the vending machines in the database.
	 **/
	public static Collection<VendingMachine> getVendingMachinesAll()
	{
	}

	/**
	 * Fetches all of the vending machines at a given zip code.
	 * @param zip The zip code to fetch the vending machines from.
	 * @return Collection of all of the vending machines at the given zip code.
	 **/
	public static Collection<VendingMachine> getVendingMachinesByZip(int zip)
	{
	}

	/**
	 * Fetches all of the vending machines in a state.
	 * @param state The state to fetch the vending machines from.
	 * @return Collection of all of the vending machines in the given state.
	 **/
	public static Collection<VendingMachine> getVendingMachinesByState(String state);
	{
	}

	/**
	 * Updates the given vending machine if it exists in the database (determined
	 * by id) or creates it if it doesn't exist. If it creates a vending machine
	 * then it will update the id with the auto incremented one. Will also
	 * update/create the location, VMLayouts, and Rows that are associated with
	 * the machine.
	 * @param vm The vending machine to update or create.
	 **/
	public static void updateOrCreateVendingMachine(VendingMachine vm)
	{
	}

	/**
	 * Fetches the customer with the given id from the database.
	 * @param id The id of the customer to fetch.
	 * @return The customer with given id or null if the customer does not
	 * exist.
	 **/
	public static Customer getCustomerById(int id)
	{
	}

	/**
	 * Updates the given customer if it exists (determined by id) or creates it
	 * if it does not exist.
	 * @param customer The Customer to update/create.
	 **/
	public static void updateOrCreateCustomer(Customer customer)
	{
	}

	/**
	 * Fetches the manager with the given id.
	 * @param id The id of the manager to fetch.
	 * @return The manager with the given id or null if no such manager exists.
	 **/
	public static Manager getManagerById(int id)
	{
	}

	/**
	 * Updates the given manager if it exists (determined by id) or creates it
	 * if it does not exist.
	 * @param manager The manager to update/create.
	 **/
	public static void updateOrCreateManager(Manager manager)
	{
	}

	/**
	 * Fetches the transaction with the given id. I have no idea why you would
	 * ever use this function but it exists just in case.
	 * @param id The id of the transaction to fetch
	 * @return The transaction with the given id or null if no such transaction
	 * exists.
	 **/
	public static Transaction getTransactionById(int id)
	{
	}

	/**
	 * Fetches all of the transactions that occurred at the given vending
	 * machine.
	 * @param vm The vending machine at which the transactions you desire
	 * occurred.
	 * @return A collection containing the transactions that occurred at the
	 * given vending machine.
	 **/
	public static Collection<Transaction> getTransactionsByVendingMachine(VendingMachine vm)
	{
	}

	/**
	 * Fetches all of the transactions the given customer has made.
	 * @param customer The customer that performed the transactions you desire.
	 * @return A collection containing the transactions the given customer
	 * performed.
	 **/
	public static Collection<Transaction> getTransactionsByCustomer(Customer customer)
	{
	}

	/**
	 * Fetches all of the transactions that have ever occurred.
	 * @return A collection of all of the transactions.
	 **/
	public static Collection<Transaction> getTransactionsAll()
	{
	}

	/**
	 * Updates the given transaction if it exists (determined by id) or creates
	 * it if it doesn't exist.
	 * @param transaction The transaction to create/update.
	 **/
	 public static void updateOrCreateTransaction(Transaction transaction)
	 {
	 }
}
