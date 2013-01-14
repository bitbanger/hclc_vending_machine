import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Collection;
import java.util.LinkedList;

public class TestUtilities
{
	/**
	 * DatabaseLayer instance
	 **/
	private DatabaseLayer dbl;

	/**
	 * Set of items to use in tests
	 **/
	public ArrayList<FoodItem> items;

	/**
	 * Set of vending machines to use in tests
	 **/
	public ArrayList<VendingMachine> machines;

	/**
	 * Set of customers to use in tests
	 **/
	public ArrayList<Customer> customers;

	/**
	 * Set of managers to use in tests
	 **/
	public ArrayList<Manager> managers;

	/**
	 * Set of transactions to use in tests
	 **/
	public ArrayList<Transaction> transactions;

	/**
	 * Flag to see if food items have already been added by noTestAddFoodItems()
	 **/
	private boolean addedFoodItems;

	/**
	 * Flag to see if vending machine have already been added by noTestAddVendingMachines()
	 **/
	private boolean addedVendingMachines;

	/**
	 * Flag to see if customers have already been added by noTestAddCustomers()
	 **/
	private boolean addedCustomers;

	 /**
	  * Flag to see if managers have already been added by noTestAddManagers()
	  **/
	private boolean addedManagers;

	/**
	 * Flags to see if transactions have already been added by noTestAddTransactions()
	 **/
	private boolean addedTransactions;

	/**
	 * Generates objects for use in tests.
	 * @param addItemsToDatabase If true the items generated are also added to
	 * the database.
	 **/
	public TestUtilities(boolean addItemsToDatabase) throws BadStateException, BadArgumentException, SQLException
	{
		dbl = DatabaseLayer.getInstance();

		initFoodItems();
		initVendingMachines();
		initCustomers();
		initManagers();
		initTransactions();

		addedFoodItems = false;
		addedVendingMachines = false;
		addedCustomers = false;
		addedTransactions = false;

		if (addItemsToDatabase)
		{
			noTestAddFoodItems();
			noTestAddVendingMachines();
			noTestAddCustomers();
			noTestAddManagers();
			noTestAddTransactions();
		}
	}

	/**
	 * Generates objects for use in tests. Does not add items to database.
	 **/
	public TestUtilities() throws BadStateException, BadArgumentException, SQLException
	{
		this(false);
	}

	/**
	 * Creates a set of FoodItems for use in tests
	 **/
	public void initFoodItems() throws BadStateException, BadArgumentException
	{
		items = new ArrayList<FoodItem>();
		items.add(new FoodItem("Twix", 175, 1000));
		items.add(new FoodItem("Snickers", 175, 1000, false));
		items.add(new FoodItem("Chips", 150, 500));
		items.add(new FoodItem("Fish Sandwich", 200, 100000));
	}

	/**
	 * Creates a set of VendingMachines for use in tests
	 **/
	public void initVendingMachines() throws BadStateException, BadArgumentException
	{
		machines = new ArrayList<VendingMachine>();
		Row[][][] testRows = new Row[4][2][2];
		for (int i=0;i<2;++i)
		{
			for (int j=0;j<2;++j)
			{
				for (int k=0;k<4;++k)
				{
					testRows[k][i][j] = new Row(items.get(i*2+j), k+i*k+j, new GregorianCalendar(2013, 1, i+j+k+1));
				}
			}
		}

		testRows[0][0][0] = null;

		Location loc1 = new Location(20622, "Maryland", new String[] {"Mckay's"});
		VMLayout cur1 = new VMLayout(testRows[0], 7);
		VMLayout next1 = new VMLayout(testRows[1], 7);
		machines.add(new VendingMachine(loc1, 1000000, cur1, next1, true));
		
		Location loc2 = new Location(99999, "Pandora", new String[] {"Zed's Medical Supplies", "That other dude's gun shop", "The chick's bar", "Whatever that guy's name who runs the scooter shop. His scooter shop. I should really know his name but I forget and I'm not willing to lookup it up."});
		VMLayout cur2 = new VMLayout(testRows[2], 7);
		VMLayout next2 = new VMLayout(testRows[3], 7);
		next2.setNextVisit(new GregorianCalendar(2013,1,11,5,3,15));
		machines.add(new VendingMachine(loc2, 500000, cur2, next2, false));
	}

	/**
	 * Initializes a set of customers to use in tests.
	 **/
	public void initCustomers() throws BadStateException, BadArgumentException
	{
		customers = new ArrayList<Customer>();
		customers.add(new Customer("Carlton", 2000));
		customers.add(new Customer("President Bush", 50000000));
		customers.add(new Customer("Hank", 100000));
		customers.add(new Customer("Phillips", 10000));
	}

	/**
	 * Initializes a set of managers to use in tests.
	 **/
	public void initManagers() throws BadStateException, BadArgumentException
	{
		managers = new ArrayList<Manager>();
		managers.add(new Manager("Superman", "kypto"));
		managers.add(new Manager("Batman", "robin"));
		managers.add(new Manager("Green Lantern", "powerring"));
	}

	/**
	 * Initializes a set of transactions to use in tests.
	 **/
	public void initTransactions() throws BadStateException, BadArgumentException
	{
		transactions = new ArrayList<Transaction>();
		transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), machines.get(0), customers.get(0), items.get(0), new Pair<Integer, Integer>(0,0)));
		transactions.add(new Transaction(new GregorianCalendar(2012, 12, 21, 23, 59, 59), machines.get(1), customers.get(1), items.get(1), new Pair<Integer, Integer>(1,1)));
		transactions.add(new Transaction(new GregorianCalendar(2013, 1,7,11,31,15), machines.get(0), customers.get(3), items.get(3), new Pair<Integer, Integer>(2,1)));
	}

	/**
	 * Checks if two FoodItems are the same
	 * @param item1 The first item
	 * @param item2 The second item
	 **/
	public static void foodItemEquals(FoodItem item1, FoodItem item2) throws BadStateException, BadArgumentException
	{
		assertTrue(item1.getId() == item2.getId());
		assertTrue(item1.getName().equals(item2.getName()));
		assertTrue(item1.getPrice() == item2.getPrice());
		assertTrue(item1.getFreshLength() == item2.getFreshLength());
		assertTrue(item1.isActive() == item2.isActive());
	}

	/**
	 * Checks if two vending machines are equal
	 * @param machine1 The first vending machine
	 * @param machine2 The second vending machine
	 **/
	public static void vendingMachineEquals(VendingMachine machine1, VendingMachine machine2) throws BadStateException, BadArgumentException
	{
		assertTrue(machine1.getId() == machine2.getId());
		assertTrue(machine1.getStockingInterval() == machine2.getStockingInterval());
		assertTrue(machine1.isActive() == machine2.isActive());
		vmLayoutEquals(machine1.getCurrentLayout(), machine2.getCurrentLayout());
		vmLayoutEquals(machine1.getNextLayout(), machine2.getNextLayout());
		locationEquals(machine1.getLocation(), machine2.getLocation());

	}

	/**
	 * Checks if two locations are equal
	 * @param location1 The first location
	 * @param location2 The second location
	 **/
	public static void locationEquals(Location location1, Location location2) throws BadStateException, BadArgumentException
	{
		assertTrue(location1.getId() == location2.getId());
		assertTrue(location1.getZipCode() == location2.getZipCode());
		assertTrue(location1.getState().equals(location2.getState()));
	}

	/**
	 * Checks if two VMLayouts are equal
	 * @param layout1 The first VMLayout
	 * @param layout2 The second VMLayout
	 **/
	public static void vmLayoutEquals(VMLayout layout1, VMLayout layout2) throws BadStateException, BadArgumentException
	{
		assertTrue(layout1.getId() == layout2.getId());

		Row[][] rows1 = layout1.getRows();
		Row[][] rows2 = layout2.getRows();
		assertTrue(rows1.length + ", " + rows2.length, rows1.length == rows2.length);
		
		for (int i=0;i<rows1.length;++i)
		{
			Row[] rowx1 = rows1[i];
			Row[] rowx2 = rows2[i];
			assertTrue(rowx1.length == rowx2.length);
			for (int j=0;j<rowx1.length;++j)
			{
				Row row1 = rowx1[j];
				Row row2 = rowx2[j];
				if (row1 == null)
					assertTrue(i + "," + j, row2 == null);
				else
				{
					foodItemEquals(row1.getProduct(), row2.getProduct());
					assertTrue(row1.getExpirationDate() + "\n" + row2.getExpirationDate(), row1.getExpirationDate().equals(row2.getExpirationDate()));
					assertTrue(row1.getRemainingQuantity() == row2.getRemainingQuantity());
				}
			}
		}
		assertTrue(layout1.getDepth() == layout2.getDepth());
		GregorianCalendar nextVisit = layout1.getNextVisit();
		
		if (nextVisit == null)
			assertTrue(layout2.getNextVisit() == null);
		else
		{
			assertTrue(nextVisit + "\n\n\n" + layout2.getNextVisit(),
				nextVisit.equals(layout2.getNextVisit()));
		}
	}

	/**
	 * Checks if two customers are equal
	 * @param customer1 The first customer
	 * @param customer2 The second customer
	 **/
	public static void customerEquals(Customer customer1, Customer customer2) throws BadStateException, BadArgumentException
	{
		assertTrue(customer1.getId() == customer2.getId());
		assertTrue(customer1.getName() + ", " + customer2.getName() + "\n",
			customer1.getName().equals(customer2.getName()));
		assertTrue(customer1.getMoney() == customer2.getMoney());
	}

	/**
	 * Checks if two managers are equal
	 * @param manager1 The first manager
	 * @param manager2 The second manager
	 **/
	public static void managerEquals(Manager manager1, Manager manager2) throws BadStateException, BadArgumentException
	{
		assertTrue(manager1.getId() == manager2.getId());
		assertTrue(manager1.getName() + "|" + manager2.getName(),
			manager1.getName().equals(manager2.getName()));
		assertTrue(manager1.getPassword().equals(manager2.getPassword()));
	}

	/**
	 * Checks if two transactions are equal
	 * @param trans1 The first transaction
	 * @param trans2 The second transaction
	 **/
	public static void transactionEquals(Transaction trans1, Transaction trans2) throws BadStateException, BadArgumentException
	{
		assertTrue(trans1.getTimestamp().equals(trans2.getTimestamp()));
		vendingMachineEquals(trans1.getMachine(), trans2.getMachine());
		customerEquals(trans1.getCustomer(), trans2.getCustomer());
		foodItemEquals(trans1.getProduct(), trans2.getProduct());
		assertTrue(trans1.getRow().first == trans2.getRow().first &&
			trans1.getRow().second == trans2.getRow().second);
		assertTrue(trans1.getBalance() == trans2.getBalance());
	}

	/**
	 * Adds FoodItems to the database iff they have not already
	 * been added. Used in several tests.
	 **/
	public void noTestAddFoodItems() throws SQLException, BadStateException, BadArgumentException
	{
		if (addedFoodItems)
			return;
		addedFoodItems = true;
		for (FoodItem item : items)
			dbl.updateOrCreateFoodItem(item);
	}

	/**
	 * Adds vending machines to the database iff they have not
	 * already been added. Used in several tests.
	 **/
	public void noTestAddVendingMachines() throws SQLException, BadStateException, BadArgumentException
	{
		if (addedVendingMachines)
			return;
		addedVendingMachines = true;
		for (VendingMachine machine : machines)
			dbl.updateOrCreateVendingMachine(machine);
	}

	/**
	 * Adds customers to the database iff they have not already
	 * been added. Used in several tests.
	 **/
	public void noTestAddCustomers() throws SQLException, BadStateException, BadArgumentException
	{
		if (addedCustomers)
			return;
		addedCustomers = true;
		for (Customer customer : customers)
			dbl.updateOrCreateCustomer(customer);
	}

	/**
	 * Adds managers to the database iff they have not already been added. Used
	 * in several tests.
	 **/
	public void noTestAddManagers() throws SQLException, BadStateException, BadArgumentException
	{
		if (addedManagers)
			return;
		addedManagers = true;
		for (Manager manager : managers)
			dbl.updateOrCreateManager(manager);
	}

	/**
	 * Adds transactions to the database iff they have not already been added.
	 * Used in several tests.
	 **/
	public void noTestAddTransactions() throws SQLException, BadStateException, BadArgumentException
	{
		if (addedTransactions)
			return;
		addedTransactions = true;
		for (Transaction trans : transactions)
			dbl.updateOrCreateTransaction(trans);
	}

	/**
	 * Adds data to database
	 **/
	public static void main(String[] args) throws Exception
	{
		TestUtilities testUtil = new TestUtilities(true);
	}
}
