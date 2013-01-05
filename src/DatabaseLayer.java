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

		stmt.addBatch("CREATE TABLE IF NOT EXISTS VendingMachine( machineId INTEGER PRIMARY KEY AUTOINCREMENT, active INTEGER NOT NULL, stockingInterval INTEGER NOT NULL, currentLayoutId INTEGER REFERENCES VMLayout(layoutId), nextLayoutId INTEGER REFERENCES VMLayout(layoutId), locationId INTEGER REFERENCES Location(locationId));");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS NearbyBusiness( locationId INTEGER REFERENCES Location(locationId), name TEXT NOT NULL);");

		stmt.addBatch(" CREATE TABLE IF NOT EXISTS Customer( customerId INTEGER PRIMARY KEY AUTOINCREMENT, money INTEGER NOT NULL, name TEXT)");

		stmt.addBatch(" CREATE TABLE IF NOT EXISTS Manager( managerId INTEGER PRIMARY KEY AUTOINCREMENT, password TEXT NOT NULL, name TEXT)");

		stmt.addBatch("CREATE TABLE IF NOT EXISTS VMTransaction( transactionId INTEGER PRIMARY KEY AUTOINCREMENT, timestamp INTEGER NOT NULL, machineId INTEGER REFERENCES VendingMachine(machineId), customerId INTEGER REFERENCES Customer(customerId), productId INTEGER REFERENCES Item(itemId), rowX INTEGER NOT NULL, rowY INTEGER NOT NULL);");

		stmt.executeBatch();
		stmt.close();
	}

	/**
	 * Deletes all data from database. Meant for testing purposes only!
	 * DO NOT USE THIS METHOD UNLESS YOU ARE WRITING A TEST!!!
	 *@throws SQLException in case of a database error
	 **/
	public void nuke() throws SQLException
	{
		Statement stmt = db.createStatement();
		stmt.executeUpdate("DELETE FROM Item; DELETE FROM Location; DELETE FROM VMLayout; DELETE FROM VMRow; DELETE FROM VendingMachine; DELETE FROM NearbyBusiness; DELETE FROM Customer; DELETE FROM Manager; DELETE FROM VMTransaction");
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
		{
			returnValue = new FoodItem(results.getString(2), results.getInt(3), results.getInt(4));
			returnValue.setId(results.getInt(1));
		}
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
			FoodItem item = new FoodItem(results.getString(2), results.getInt(3), results.getInt(4));
			item.setId(results.getInt(1));
			returnSet.add(item);
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
		if (item.isTempId())
		{
			Statement insertStmt = db.createStatement();
			String query = String.format("INSERT INTO Item(name, price, freshLength) VALUES(\"%s\", %d, %d)", item.getName(), item.getPrice(), item.getFreshLength());
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			int id = keys.getInt(1);
			item.setId(id);
			insertStmt.close();
		}
		else
		{
			Statement updateStmt = db.createStatement();
			String query = String.format("UPDATE Item SET name=\"%s\", price=%d, freshLength=%d WHERE itemId=%d", item.getName(), item.getPrice(), item.getFreshLength(), item.getId());
			updateStmt.executeUpdate(query);
			updateStmt.close();
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
			maxY = Math.max(maxY, rowY);

			FoodItem item = getFoodItemById(productId);
			
			GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(dateInt);

			Row row = new Row(item, rowResults.getInt(4), date);
			row.setId(rowResults.getInt(1));

			raw.add(new Pair<Row,Pair<Integer,Integer>>(row, new Pair<Integer, Integer>(rowX, rowY)));
		}

		rowStmt.close();

		if (maxX == -1 || maxY == -1)
			return null;

		Row[][] rows = new Row[maxY+1][maxX+1];
		for (Pair<Row,Pair<Integer,Integer>> entry : raw)
			rows[entry.second.second][entry.second.first] = entry.first;
		VMLayout layout = new VMLayout(rows);
		layout.setId(id);
		return layout;
	}

	/**
	 * Updates the VMLayout and its rows if it exists in the database. If it does not exist
	 * then it and its rows are created.
	 * @param layout The VMLayout to update/create.
	 **/
	private void updateOrCreateVMLayout(VMLayout layout) throws SQLException
	{
		if (layout.isTempId())
		{
			Statement insertStmt = db.createStatement();
			String query = String.format("INSERT INTO VMLayout(layoutId) VALUES(NULL)");
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			int id = keys.getInt(1);
			layout.setId(id);
			insertStmt.close();
			
			Row[][] grid = layout.getRows();
			for (int y=0;y<grid.length;++y)
			{
				for (int x=0;x<grid[y].length;++x)
				{
					Row row = grid[y][x];
					Statement rowStmt = db.createStatement();
					String rowQuery = String.format("INSERT INTO VMRow(productId, layoutId, expirationDate, remainingQuant, rowX, rowY) VALUES(%d, %d, %d, %d, %d, %d)", row.getProduct().getId(), layout.getId(), row.getExpirationDate().getTimeInMillis(), row.getRemainingQuantity(), x, y);
					rowStmt.executeUpdate(rowQuery);
					ResultSet rowKeys = rowStmt.getGeneratedKeys();
					rowKeys.next();
					row.setId(rowKeys.getInt(1));
					rowStmt.close();
				}
			}
		}
		else
		{
			Statement deleteStmt = db.createStatement();
			deleteStmt.executeUpdate("DELETE FROM VMRow WHERE layoutId=" + layout.getId());
			deleteStmt.close();
			Row[][] grid = layout.getRows();
			for (int y=0;y<grid.length;++y)
			{
				for (int x=0;x<grid[y].length;++x)
				{
					Row row = grid[y][x];
					Statement rowStmt = db.createStatement();
					String query = String.format("INSERT INTO VMRow(productId, layoutId, expirationDate, remainingQuant, rowX, rowY) VALUES(%d, %d, %d, %d, %d, %d)", row.getProduct().getId(), layout.getId(), row.getExpirationDate().getTimeInMillis(), row.getRemainingQuantity(), x, y);
					rowStmt.executeUpdate(query);
					ResultSet rowKeys = rowStmt.getGeneratedKeys();
					rowKeys.next();
					row.setId(rowKeys.getInt(1));
					rowStmt.close();
				}
			}
		}
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
			returnValue = new Location(locSet.getInt(2), locSet.getString(3), busList.toArray(new String[0]));
			returnValue.setId(locSet.getInt(1));
			busStmt.close();
		}
		locStmt.close();
		return returnValue;
	}

	/**
	 * Updates the given location if it exists in the database. If it does not
	 * exist then it is created.
	 * @param location The location to create/update.
	 * @throws SQLException in case of a database error.
	 **/
	private void updateOrCreateLocation(Location location) throws SQLException
	{
		if (location.isTempId())
		{
			Statement insertStmt = db.createStatement();
			String query = String.format("INSERT INTO Location(zipCode, state) VALUES(%d, \"%s\")", location.getZipCode(), location.getState());
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			int id = keys.getInt(1);
			location.setId(id);
			insertStmt.close();

			for (String business : location.getNearbyBusinesses())
			{
				Statement busStmt = db.createStatement();
				String busQuery = String.format("INSERT INTO NearbyBusiness(locationId, name) VALUES(%d, \"%s\")", location.getId(), business);
				busStmt.executeUpdate(busQuery);
				busStmt.close();
			}
		}
		else
		{
			Statement updateStmt = db.createStatement();
			String query = String.format("UPDATE Location SET zipCode=%d, state=\"%s\" WHERE locationId=%d", location.getZipCode(), location.getState(), location.getId());
			updateStmt.executeUpdate(query);
			updateStmt.close();

			Statement delStatement = db.createStatement();
			delStatement.executeUpdate("DELETE FROM NearbyBusiness WHERE locationId=" + location.getId());
			delStatement.close();

			for (String business : location.getNearbyBusinesses())
			{
				Statement busStmt = db.createStatement();
				String busQuery = String.format("INSERT INTO NearbyBusiness(locationId, name) VALUES(%d, \"%s\")", location.getId(), business);
				busStmt.executeUpdate(busQuery);
				busStmt.close();
			}
		}
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
		ResultSet vmResults = vmStmt.executeQuery("SELECT machineId, active, currentLayoutId, nextLayoutId, locationId, stockingInterval FROM VendingMachine WHERE machineId=" + id);
		if (vmResults.next())
		{
			id = vmResults.getInt(1);
			boolean active = !(vmResults.getInt(2) == 0);
			int interval = vmResults.getInt(6);
			int curId = vmResults.getInt(3);
			int nextId = vmResults.getInt(4);
			int locationId = vmResults.getInt(5);
			
			VMLayout cur = getVMLayoutById(curId);
			VMLayout next = getVMLayoutById(nextId);
			Location loc = getLocationById(locationId);
			returnValue = new VendingMachine(loc, interval, cur, next, active);
			returnValue.setId(id);
		}
		vmResults.close();
		return returnValue;
	}

	/**
	 * Fetches all of the vending machines in the database.
	 * @return Collection of all of the vending machines in the database.
	 * @throws SQLException in case of a database error
	 **/
	public Collection<VendingMachine> getVendingMachinesAll() throws SQLException
	{
		Collection<VendingMachine> returnSet = null;
		Statement vmStmt = db.createStatement();
		ResultSet vmResults = vmStmt.executeQuery("SELECT machineId, active, currentLayoutId, nextLayoutId, locationId, stockingInterval FROM VendingMachine");
		while (vmResults.next())
		{
			int id = vmResults.getInt(1);
			boolean active = !(vmResults.getInt(2) == 0);
			int interval = vmResults.getInt(6);
			int curId = vmResults.getInt(3);
			int nextId = vmResults.getInt(4);
			int locationId = vmResults.getInt(5);
			
			VMLayout cur = getVMLayoutById(curId);
			VMLayout next = getVMLayoutById(nextId);
			Location loc = getLocationById(locationId);
			VendingMachine machine = new VendingMachine(loc, interval, cur, next, active);
			machine.setId(id);
			returnSet.add(machine);
		}
		vmResults.close();
		return returnSet;
	}

	/**
	 * Fetches all of the vending machines at a given zip code.
	 * @param zip The zip code to fetch the vending machines from.
	 * @return Collection of all of the vending machines at the given zip code.
	 * @throws SQLException in case of a database error
	 **/
	public Collection<VendingMachine> getVendingMachinesByZip(int zip) throws SQLException
	{
		Collection<VendingMachine> returnSet = null;
		Statement vmStmt = db.createStatement();
		ResultSet vmResults = vmStmt.executeQuery("SELECT machineId, active, currentLayoutId, nextLayoutId, VendingMachine.locationId, stockingInterval FROM VendingMachine JOIN Location ON Location.locationId = VendingMachine.locationId WHERE Location.zipCode=" + zip);
		while (vmResults.next())
		{
			int id = vmResults.getInt(1);
			boolean active = !(vmResults.getInt(2) == 0);
			int curId = vmResults.getInt(3);
			int nextId = vmResults.getInt(4);
			int locationId = vmResults.getInt(5);
			int interval = vmResults.getInt(6);
			
			VMLayout cur = getVMLayoutById(curId);
			VMLayout next = getVMLayoutById(nextId);
			Location loc = getLocationById(locationId);
			VendingMachine machine = new VendingMachine(loc, interval, cur, next, active);
			machine.setId(id);
			returnSet.add(machine);
		}
		vmResults.close();
		return returnSet;
	}

	/**
	 * Fetches all of the vending machines in a state.
	 * @param state The state to fetch the vending machines from.
	 * @return Collection of all of the vending machines in the given state.
	 * @throws SQLException in case of a database error
	 **/
	public Collection<VendingMachine> getVendingMachinesByState(String state) throws SQLException
	{
		Collection<VendingMachine> returnSet = null;
		Statement vmStmt = db.createStatement();
		ResultSet vmResults = vmStmt.executeQuery("SELECT machineId, active, currentLayoutId, nextLayoutId, VendingMachine.locationId, stockingInterval FROM VendingMachine JOIN Location ON Location.locationId = VendingMachine.locationId WHERE Location.state=" + state);
		while (vmResults.next())
		{
			int id = vmResults.getInt(1);
			boolean active = !(vmResults.getInt(2) == 0);
			int curId = vmResults.getInt(3);
			int nextId = vmResults.getInt(4);
			int locationId = vmResults.getInt(5);
			int interval = vmResults.getInt(6);
			
			VMLayout cur = getVMLayoutById(curId);
			VMLayout next = getVMLayoutById(nextId);
			Location loc = getLocationById(locationId);
			VendingMachine machine = new VendingMachine(loc, interval, cur, next, active);
			machine.setId(id);
			returnSet.add(machine);
		}
		vmResults.close();
		return returnSet;
	}

	/**
	 * Updates the given vending machine if it exists in the database (determined
	 * by id) or creates it if it doesn't exist. If it creates a vending machine
	 * then it will update the id with the auto incremented one. Will also
	 * update/create the location, VMLayouts, and Rows that are associated with
	 * the machine.
	 * @param vm The vending machine to update or create.
	 * @throws SQLException in case of a database error
	 **/
	public void updateOrCreateVendingMachine(VendingMachine vm) throws SQLException
	{
		updateOrCreateVMLayout(vm.getCurrentLayout());
		updateOrCreateVMLayout(vm.getNextLayout());
		updateOrCreateLocation(vm.getLocation());

		if (vm.isTempId())
		{
			Statement insertStmt = db.createStatement();
			String query = String.format("INSERT INTO VendingMachine(active, stockingInterval, currentLayoutId, nextLayoutId, locationId) VALUES(%d, %d, %d, %d)", vm.isActive() ? 1 : 0, vm.getStockingInterval(), vm.getCurrentLayout().getId(), vm.getNextLayout().getId(), vm.getLocation().getId());
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			int id = keys.getInt(1);
			vm.setId(id);
			insertStmt.close();
		}
		else
		{
			Statement updateStmt = db.createStatement();
			String query = String.format("UPDATE VendingMachine SET active=%d, stockingInterval=%d, currentLayoutId=%d, nextLayoutId=%d, locationId=%d WHERE machineId=%d", vm.isActive() ? 1 : 0, vm.getStockingInterval(),vm.getCurrentLayout().getId(), vm.getNextLayout().getId(), vm.getLocation().getId(), vm.getId());
			updateStmt.executeUpdate(query);
			updateStmt.close();
		}
	}

	/**
	 * Fetches the customer with the given id from the database.
	 * @param id The id of the customer to fetch.
	 * @return The customer with given id or null if the customer does not
	 * @throws SQLException in case of a database error
	 * exist.
	 **/
	public Customer getCustomerById(int id) throws SQLException
	{
		Customer returnValue = null;
		Statement stmt = db.createStatement();
		String query = "SELECT customerId, money, name FROM Customer WHERE customerId=" + id;
		ResultSet results = stmt.executeQuery(query);
		if (results.next())
		{
			returnValue = new Customer(results.getString(3), results.getInt(2));
			returnValue.setId(results.getInt(1));
		}
		results.close();
		return returnValue;
	}

	/**
	 * Updates the given customer if it exists (determined by id) or creates it
	 * if it does not exist.
	 * @param customer The Customer to update/create.
	 * @throws SQLException in case of a database error
	 **/
	public void updateOrCreateCustomer(Customer customer) throws SQLException
	{
		if (customer.isTempId())
		{
			Statement insertStmt = db.createStatement();
			String query = String.format("INSERT INTO Customer(money, name) VALUES(%d, \"%s\")", customer.getMoney(), customer.getName());
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			customer.setId(keys.getInt(1));
			insertStmt.close();
		}
		else
		{
			Statement updateStmt = db.createStatement();
			String query = String.format("UPDATE Customer SET money=%d, name=\"%s\" WHERE customerId=%d", customer.getMoney(), customer.getName(), customer.getId());
			updateStmt.executeUpdate(query);
			updateStmt.close();
		}
	}

	/**
	 * Fetches the manager with the given id.
	 * @param id The id of the manager to fetch.
	 * @return The manager with the given id or null if no such manager exists.
	 * @throws SQLException in case of a database error
	 **/
	public Manager getManagerById(int id) throws SQLException
	{
		Manager returnValue = null;
		Statement stmt = db.createStatement();
		String query = "SELECT managerId, password, name FROM Manager WHERE managerId=" + id;
		ResultSet results = stmt.executeQuery(query);
		if (results.next())
		{
			returnValue = new Manager(results.getString(3), results.getString(2));
			returnValue.setId(results.getInt(1));
		}
		results.close();
		return returnValue;
	}

	/**
	 * Updates the given manager if it exists (determined by id) or creates it
	 * if it does not exist.
	 * @param manager The manager to update/create.
	 * @throws SQLException in case of a database error
	 **/
	public void updateOrCreateManager(Manager manager) throws SQLException
	{
		if (manager.isTempId())
		{
			Statement insertStmt = db.createStatement();
			String query = String.format("INSERT INTO Manager(password, name) VALUES(\"%s\", \"%s\")", manager.getPassword(), manager.getName());
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			manager.setId(keys.getInt(1));
			insertStmt.close();
		}
		else
		{
			Statement updateStmt = db.createStatement();
			String query = String.format("UPDATE Manager SET password=\"%s\", name=\"%s\" WHERE managerId=%d", manager.getPassword(), manager.getName(), manager.getId());
			updateStmt.executeUpdate(query);
			updateStmt.close();
		}
	}

	/**
	 * Fetches the transaction with the given id. I have no idea why you would
	 * ever use this function but it exists just in case.
	 * @param id The id of the transaction to fetch
	 * @return The transaction with the given id or null if no such transaction
	 * exists.
	 * @throws SQLException in case of a database error
	 **/
	public Transaction getTransactionById(int id) throws SQLException
	{
		Transaction returnValue = null;
		Statement stmt = db.createStatement();
		String query = "SELECT transactionId, timestamp, machineId, customerId, productId, rowX, rowY FROM VMTransaction WHERE transactionId=" + id;
		ResultSet results = stmt.executeQuery(query);
		if (results.next())
		{
			GregorianCalendar time = new GregorianCalendar();
			time.setTimeInMillis(results.getInt(2));
			VendingMachine machine = getVendingMachineById(results.getInt(3));
			Customer customer = getCustomerById(results.getInt(4));
			FoodItem product = getFoodItemById(results.getInt(5));
			Pair<Integer, Integer> row = new Pair<Integer, Integer>(results.getInt(6), results.getInt(7));
			returnValue = new Transaction(time, machine, customer, product, row);
			returnValue.setId(id);
			
		}
		results.close();
		return returnValue;
	}

	/**
	 * Fetches all of the transactions that occurred at the given vending
	 * machine.
	 * @param vm The vending machine at which the transactions you desire
	 * occurred.
	 * @return A collection containing the transactions that occurred at the
	 * given vending machine.
	 * @throws SQLException in case of a database error
	 **/
	public Collection<Transaction> getTransactionsByVendingMachine(VendingMachine vm) throws SQLException
	{
		Collection<Transaction> transactions = new LinkedList<Transaction>();
		Statement stmt = db.createStatement();
		String query = "SELECT transactionId, timestamp, machineId, customerId, productId, rowX, rowY FROM VMTransaction WHERE machineId=" + vm.getId();
		ResultSet results = stmt.executeQuery(query);
		while (results.next())
		{
			int id = results.getInt(1);
			GregorianCalendar time = new GregorianCalendar();
			time.setTimeInMillis(results.getInt(2));
			VendingMachine machine = getVendingMachineById(results.getInt(3));
			Customer customer = getCustomerById(results.getInt(4));
			FoodItem product = getFoodItemById(results.getInt(5));
			Pair<Integer, Integer> row = new Pair<Integer, Integer>(results.getInt(6), results.getInt(7));
			Transaction transaction = new Transaction(time, machine, customer, product, row);
			transaction.setId(id);
			transactions.add(transaction);
		}
		results.close();
		return transactions;
	}

	/**
	 * Fetches all of the transactions the given customer has made.
	 * @param customer The customer that performed the transactions you desire.
	 * @return A collection containing the transactions the given customer
	 * performed.
	 * @throws SQLException in case of a database error
	 **/
	public Collection<Transaction> getTransactionsByCustomer(Customer customer) throws SQLException
	{
		Collection<Transaction> transactions = new LinkedList<Transaction>();
		Statement stmt = db.createStatement();
		String query = "SELECT transactionId, timestamp, machineId, customerId, productId, rowX, rowY FROM VMTransaction WHERE customerId=" + customer.getId();
		ResultSet results = stmt.executeQuery(query);
		while (results.next())
		{
			int id = results.getInt(1);
			GregorianCalendar time = new GregorianCalendar();
			time.setTimeInMillis(results.getInt(2));
			VendingMachine machine = getVendingMachineById(results.getInt(3));
			Customer cust = getCustomerById(results.getInt(4));
			FoodItem product = getFoodItemById(results.getInt(5));
			Pair<Integer, Integer> row = new Pair<Integer, Integer>(results.getInt(6), results.getInt(7));
			Transaction transaction = new Transaction(time, machine, cust, product, row);
			transaction.setId(id);
			transactions.add(transaction);
		}
		results.close();
		return transactions;
	}

	/**
	 * Fetches all of the transactions that have ever occurred.
	 * @return A collection of all of the transactions.
	 * @throws SQLException in case of a database error
	 **/
	public Collection<Transaction> getTransactionsAll() throws SQLException
	{
		Collection<Transaction> transactions = new LinkedList<Transaction>();
		Statement stmt = db.createStatement();
		String query = "SELECT transactionId, timestamp, machineId, customerId, productId, rowX, rowY FROM VMTransaction";
		ResultSet results = stmt.executeQuery(query);
		while (results.next())
		{
			int id = results.getInt(1);
			GregorianCalendar time = new GregorianCalendar();
			time.setTimeInMillis(results.getInt(2));
			VendingMachine machine = getVendingMachineById(results.getInt(3));
			Customer customer = getCustomerById(results.getInt(4));
			FoodItem product = getFoodItemById(results.getInt(5));
			Pair<Integer, Integer> row = new Pair<Integer, Integer>(results.getInt(6), results.getInt(7));
			Transaction transaction = new Transaction(time, machine, customer, product, row);
			transaction.setId(id);
			transactions.add(transaction);
		}
		results.close();
		return transactions;
	}

	/**
	 * Updates the given transaction if it exists (determined by id) or creates
	 * it if it doesn't exist.
	 * @param transaction The transaction to create/update.
	 * @throws SQLException in case of a database error
	 **/
	public void updateOrCreateTransaction(Transaction transaction) throws SQLException
	{
		if (transaction.isTempId())
		{
			String query = String.format("INSERT INTO VMTransaction(timestamp, machineId, customerId, productId, rowX, rowY) VALUES(%d, %d, %d, %d, %d, %d)", transaction.getTimestamp().getTimeInMillis(), transaction.getMachine().getId(), transaction.getCustomer().getId(), transaction.getProduct().getId(), transaction.getRow().first, transaction.getRow().second);
			Statement insertStmt = db.createStatement();
			insertStmt.executeUpdate(query);
			ResultSet keys = insertStmt.getGeneratedKeys();
			keys.next();
			transaction.setId(keys.getInt(1));
			insertStmt.close();
		}
		else
		{
			String query = String.format("UPDATE VMTransaction SET timestamp=%d, machineId=%d, customerId=%d, productId=%d, rowX=%d, rowY=%d WHERE transactionId=%d", transaction.getTimestamp().getTimeInMillis(), transaction.getMachine().getId(), transaction.getCustomer().getId(), transaction.getProduct().getId(), transaction.getRow().first, transaction.getRow().second, transaction.getId());
			Statement updateStmt = db.createStatement();
			updateStmt.executeUpdate(query);
			updateStmt.close();
		}
	}
}
