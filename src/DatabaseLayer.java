import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.GregorianCalendar;

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
		initializeDatabase();
	}

	/**
	 * Creates all of the necessary tables in the database
	 * @throws SQLException in case of a database error
	 **/
	private void initializeDatabase() throws SQLException
	{
		Statement stmt = db.createStatement();
		
		stmt.addBatch("CREATE TABLE IF NOT EXISTS Location( locationId INTEGER PRIMARY KEY AUTOINCREMENT, zipCode INTEGER, state TEXT);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS Item( itemId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, price INTEGER NOT NULL, freshLength INTEGER NOT NULL);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS VMLayout( layoutId INTEGER PRIMARY KEY AUTOINCREMENT);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS VMRow( vmRowId INTEGER PRIMARY KEY AUTOINCREMENT, productId INTEGER REFERENCES Item(itemId), layoutId INTEGER REFERENCES VMLayout(layoutId), expirationDate INTEGER NOT NULL, remainingQuant INTEGER NOT NULL, rowX INTEGER NOT NULL, rowY INTEGER NOT NULL);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS VendingMachine( machineId INTEGER PRIMARY KEY AUTOINCREMENT, active INTEGER NOT NULL, currentLayoutId INTEGER REFERENCES VMLayout(layoutId), nextLayoutId INTEGER REFERENCES VMLayout(layoutId), locationId INTEGER REFERENCES Location(locationId));");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS NearbyBusiness( locationId INTEGER REFERENCES Location(locationId), name TEXT NOT NULL);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS VMUser( userId INTEGER PRIMARY KEY AUTOINCREMENT);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS Customer( customerId INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER REFERENCES VMUser(userId), money INTEGER NOT NULL);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS Manager( managerId INTEGER PRIMARY KEY AUTOINCREMENT, password TEXT NOT NULL);");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS VMTransaction( transactionId INTEGER PRIMARY KEY AUTOINCREMENT, timestamp INTEGER NOT NULL, machineId INTEGER REFERENCES VendingMachine(machineId), customerId INTEGER REFERENCES Customer(customerId), productId INTEGER REFERENCES Item(itemId), rowX INTEGER NOT NULL, rowY INTEGER NOT NULL);");

		stmt.executeBatch();
		stmt.close();
	}

	/**
	 * Fetches the item with a given id from the database.
	 * @param id The id of the item to fetch.
	 * @return The item in the database with the given id or null if no item
	 * @throws SQLException in case of a database error
	 * with the given id exists.
	 **/
	public FoodItem getFoodItemById(int id) throws SQLException
	{
		FoodItem returnValue = null;
		Statement stmt = db.createStatement();
		ResultSet results = stmt.executeQuery("SELECT itemId, name, price, freshLength FROM Item WHERE itemId=" + id);
		if (results.next())
			returnValue = new FoodItem(results.getInt(1), results.getString(2), results.getInt(3), results.getInt(4));
		results.close();
		return returnValue;
	}

	/**
	 * Gets all of the items in the database.
	 * @return A collection of all of the items in the database.
	 * @throws SQLException in case of a database error
	 **/
	public Collection<FoodItem> getFoodItemsAll() throws SQLException
	{
		Collection<FoodItem> returnSet = new LinkedList<FoodItem>();
		Statement stmt = db.createStatement();
		ResultSet results = stmt.executeQuery("SELECT itemId, name, price, freshLength FROM Item");
		while (results.next())
		{
			returnSet.add(new FoodItem(results.getInt(1), results.getString(2), results.getInt(3), results.getInt(4)));
		}
		results.close();
		return returnSet;
	}

	/**
	 * Updates the item in the database. If the item does not already exists
	 * (determined by the id of the item) then the item is created. If an item
	 * is created then the id is updated with the auto incremented one.
	 * @param item The item to update
	 * @throws SQLException in case of a database error
	 **/
	public void updateOrCreateFoodItem(FoodItem item) throws SQLException
	{
		Statement qStmt = db.createStatement();
		ResultSet results = qStmt.executeQuery("SELECT COUNT(itemId) FROM Item WHERE itemId=" + item.getId());
		results.next();
		int count = results.getInt(1);
		if (count == 0)
		{
			Statement insertStmt = db.createStatement();
			String query = String.format("INSERT INTO Item(name, price, freshLength) VALUES(\"%s\", %d, %d)", item.getName(), item.getPrice(), item.getFreshLength());
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			int id = keys.getInt(1);
			item.setId(id);
		}
		else
		{
			Statement updateStmt = db.createStatement();
			String query = String.format("UPDATE Item SET name=\"%s\" price=%d freshLength=%d WHERE itemId=%d", item.getName(), item.getPrice(), item.getFreshLength(), item.getId());
			updateStmt.executeUpdate(query);
		}
	}

	/**
	 * Fetches the VMLayout with the given id. Should only be called by the methods
	 * inside this class that need to work with VMLayouts, which is mostly the
	 * VendingMachine methods.
	 * @param id The id of the VMLayout to get.
	 * @return The VMLayout from the database with the given id or null if no such
	 * VMLayout exists.
	 * @throws SQLException in case of a database error.
	 **/
	private VMLayout getVMLayoutById(int id) throws SQLException
	{
		Statement rowStmt = db.createStatement();
		ResultSet rowResults = rowStmt.executeQuery("SELECT vmRowId, productId, expirationDate, remainingQuant, rowX, rowY FROM VMRow WHERE layoutId=" + id);
		int maxX = -1;
		int maxY = -1;
		LinkedList<Pair<Row,Pair<Integer,Integer>>> raw = new LinkedList<Pair<Row,Pair<Integer,Integer>>>();
		while (rowResults.next())
		{
			int productId = rowResults.getInt(2);
			long dateInt = rowResults.getInt(3);
			int rowX = rowResults.getInt(5);
			int rowY = rowResults.getInt(6);

			maxX = Math.max(maxX, rowX);
			maxY = Math.min(maxY, rowY);

			FoodItem item = getFoodItemById(productId);
			
			GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(dateInt);

			raw.add(new Pair<Row,Pair<Integer,Integer>>(new Row(rowResults.getInt(1), item, rowResults.getInt(4), date), new Pair<Integer, Integer>(rowX, rowY)));
		}

		rowStmt.close();

		if (maxX == -1 || maxY == -1)
			return null;

		Row[][] rows = new Row[maxY+1][maxX+1];
		for (Pair<Row,Pair<Integer,Integer>> entry : raw)
			rows[entry.second.second][entry.second.first] = entry.first;
		return new VMLayout(id, rows);
	}

	/**
	 * Fetches the Location with the given id. Only this class should ever need
	 * to do that.
	 * @param id The id of the Location to fetch.
	 * @return The Location with the given id or null if no such Location exists.
	 * @throws SQLException in case of a database error.
	 **/
	private Location getLocationById(int id) throws SQLException
	{
		Location returnValue = null;
		Statement locStmt = db.createStatement();
		ResultSet locSet = locStmt.executeQuery("SELECT locationId, zipCode, state FROM Location WHERE locationId=" + id);
		if (locSet.next())
		{
			Statement busStmt = db.createStatement();
			ResultSet busSet = busStmt.executeQuery("SELECT name FROM NearbyBusiness WHERE locationId=" + id);
			LinkedList<String> busList = new LinkedList<String>();
			while (busSet.next())
				busList.add(busSet.getString(1));
			returnValue = new Location(locSet.getInt(1), locSet.getInt(2), locSet.getString(3), busList.toArray(new String[0]));
		}
		locStmt.close();
		return returnValue;
	}

	/**
	 * Fetches the vending machine with the given id.
	 * @param id The id of the vending machine to fetch.
	 * @return The vending machine with the given id or null if the vending
	 * machine does not exist.
	 * @throws SQLException in case of a database error
	 **/
	public VendingMachine getVendingMachineById(int id) throws SQLException
	{
		VendingMachine returnValue = null;
		Statement vmStmt = db.createStatement();
		ResultSet vmResults = vmStmt.executeQuery("SELECT machineId, active, currentLayoutId, nextLayoutId, locationId FROM VendingMachine WHERE machineId=" + id);
		if (vmResults.next())
		{
			id = vmResults.getInt(1);
			boolean active = !(vmResults.getInt(2) == 0);
			int curId = vmResults.getInt(3);
			int nextId = vmResults.getInt(4);
			int locationId = vmResults.getInt(5);
			
			VMLayout cur = getVMLayoutById(curId);
			VMLayout next = getVMLayoutById(nextId);
			Location loc = getLocationById(locationId);
			returnValue = new VendingMachine(id, loc, cur, next, active);
		}
		vmResults.close();
		return returnValue;
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
