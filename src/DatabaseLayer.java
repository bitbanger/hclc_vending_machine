import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DatabaseLayer class contains static methods for accessing the sqlite
 * database. It includes methods to get, update, and create objects in the
 * database.
 * 
 * @author Matthew Koontz
 **/

public class DatabaseLayer
{
	/** Default location of the database. */
	public static final String DEFAULT_DB_LOCATION="hclc.db";

	/** Driver with which to access the database. */
	private static final String DB_DRIVER="jdbc:sqlite:";

	/** Package and class providing the requested driver. */
	private static final String DRIVER_CLASS="org.sqlite.JDBC";

	/** File path to the sqlite database. */
	private static String dbLocation=DEFAULT_DB_LOCATION;

	/** Singleton instance itself. */
	private static DatabaseLayer instance=null;

	/** Connection to the database. Statements are run against this object. */
	private Connection db;

	/**
	 * Selects a custom database location.
	 * This is only useful if the instance has not yet been constructed; otherwise, it does nothing.
	 * @param path valid readable/writable path to the database file to create or use
	 * @return whether the path could be set (i.e. <tt>getInstance()</tt> has never been called
	 */
	public static boolean setDatabaseLocation(String path)
	{
		if(instance==null) //instance not yet constructed
		{
			dbLocation=path;
			
			return true;
		}
		else
			return false;
	}

	/**
	 * Retrieves singleton instance.
	 * The database will be located at the default location unless <tt>setDatabaseLocation(String)</tt> has first been used.
	 * @return the <tt>DatabaseLayer</tt> instance, or <tt>null</tt> upon error
	 */
	public static DatabaseLayer getInstance()
	{
		try
		{
			Class.forName(DRIVER_CLASS); //load driver
			
			if(instance==null) //instance not yet constructed
				instance=new DatabaseLayer();
			
			return instance;
		}
		catch(ClassNotFoundException gone)
		{
			System.err.println("ERROR: Unable to locate database driver! Is your classpath set correctly?");
			System.err.println("Technical details: "+gone);
			
			return null;
		}
		catch(SQLException disconnected)
		{
			System.err.println("ERROR: Unable to connect to the database! Do you have the proper permissions?");
			System.err.println("Technical details: "+disconnected);
			
			return null;
		}
	}

	/**
	 * Instance constructor.
	 * This will only be invoked once per run.
	 * Precondition: The database driver must already be loaded.
	 * @throws SQLException in case of a database error
	 */
	private DatabaseLayer() throws SQLException
	{
		db=DriverManager.getConnection(DB_DRIVER+dbLocation);
	}

	/**
	 * Fetches the item with a given id from the database.
	 * @param id The id of the item to fetch.
	 * @return The item in the database with the given id or null if no item
	 * with the given id exists.
	 **/
	public Item getItemById(int id)
	{
	}

	/**
	 * Gets all of the items in the database.
	 * @return A collection of all of the items in the database.
	 **/
	public Collection<Item> getItemsAll()
	{
	}

	/**
	 * Updates the item in the database. If the item does not already exists
	 * (determined by the id of the item) then the item is created. If an item
	 * is created then the id is updated with the auto incremented one.
	 * @param item The item to update
	 **/
	public void updateOrCreateItem(Item item)
	{
	}

	/**
	 * Fetches the vending machine with the given id.
	 * @param id The id of the vending machine to fetch.
	 * @return The vending machine with the given id or null if the vending
	 * machine does not exist.
	 **/
	public VendingMachine getVendingMachineById(int id)
	{
	}

	/**
	 * Fetches all of the vending machines in the database.
	 * @return Collection of all of the vending machines in the database.
	 **/
	public Collection<VendingMachine> getVendingMachinesAll()
	{
	}

	/**
	 * Fetches all of the vending machines at a given zip code.
	 * @param zip The zip code to fetch the vending machines from.
	 * @return Collection of all of the vending machines at the given zip code.
	 **/
	public Collection<VendingMachine> getVendingMachinesByZip(int zip)
	{
	}

	/**
	 * Fetches all of the vending machines in a state.
	 * @param state The state to fetch the vending machines from.
	 * @return Collection of all of the vending machines in the given state.
	 **/
	public Collection<VendingMachine> getVendingMachinesByState(String state);
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
	public void updateOrCreateVendingMachine(VendingMachine vm)
	{
	}

	/**
	 * Fetches the customer with the given id from the database.
	 * @param id The id of the customer to fetch.
	 * @return The customer with given id or null if the customer does not
	 * exist.
	 **/
	public Customer getCustomerById(int id)
	{
	}

	/**
	 * Updates the given customer if it exists (determined by id) or creates it
	 * if it does not exist.
	 * @param customer The Customer to update/create.
	 **/
	public void updateOrCreateCustomer(Customer customer)
	{
	}

	/**
	 * Fetches the manager with the given id.
	 * @param id The id of the manager to fetch.
	 * @return The manager with the given id or null if no such manager exists.
	 **/
	public Manager getManagerById(int id)
	{
	}

	/**
	 * Updates the given manager if it exists (determined by id) or creates it
	 * if it does not exist.
	 * @param manager The manager to update/create.
	 **/
	public void updateOrCreateManager(Manager manager)
	{
	}

	/**
	 * Fetches the transaction with the given id. I have no idea why you would
	 * ever use this function but it exists just in case.
	 * @param id The id of the transaction to fetch
	 * @return The transaction with the given id or null if no such transaction
	 * exists.
	 **/
	public Transaction getTransactionById(int id)
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
	public Collection<Transaction> getTransactionsByVendingMachine(VendingMachine vm)
	{
	}

	/**
	 * Fetches all of the transactions the given customer has made.
	 * @param customer The customer that performed the transactions you desire.
	 * @return A collection containing the transactions the given customer
	 * performed.
	 **/
	public Collection<Transaction> getTransactionsByCustomer(Customer customer)
	{
	}

	/**
	 * Fetches all of the transactions that have ever occurred.
	 * @return A collection of all of the transactions.
	 **/
	public Collection<Transaction> getTransactionsAll()
	{
	}

	/**
	 * Updates the given transaction if it exists (determined by id) or creates
	 * it if it doesn't exist.
	 * @param transaction The transaction to create/update.
	 **/
	public void updateOrCreateTransaction(Transaction transaction)
	{
	}
}
