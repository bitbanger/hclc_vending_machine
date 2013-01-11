import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Runs tests on the DatabaseLayer
 * @author Matthew Koontz
 **/
public class TestDatabaseLayer
{
	/**
	 * DatabaseLayer instance
	 **/
	private DatabaseLayer dbl;

	/**
	 * Set of items to use in tests
	 **/
	private ArrayList<FoodItem> items;

	/**
	 * Set of vending machines to use in tests
	 **/
	private ArrayList<VendingMachine> machines;

	/**
	 * Set of customers to use in tests
	 **/
	private ArrayList<Customer> customers;

	/**
	 * Set of managers to use in tests
	 **/
	private ArrayList<Manager> managers;

	/**
	 * Set of transactions to use in tests
	 **/
	private ArrayList<Transaction> transactions;

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
	 * Clears the database and initializes test objects for each test
	 **/
	@Before
	public void setUp() throws SQLException, BadStateException, BadArgumentException
	{
		dbl = DatabaseLayer.getInstance();
		dbl.nuke();
		initFoodItems();
		initVendingMachines();
		initCustomers();
		initManagers();
		initTransactions();
		addedFoodItems = false;
		addedVendingMachines = false;
		addedCustomers = false;
		addedManagers = false;
		addedTransactions = false;
	}

	/**
	 * Creates a set of FoodItems for use in tests
	 **/
	private void initFoodItems() throws BadStateException, BadArgumentException
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
	private void initVendingMachines() throws BadStateException, BadArgumentException
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
	private void initCustomers() throws BadStateException, BadArgumentException
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
	private void initManagers() throws BadStateException, BadArgumentException
	{
		managers = new ArrayList<Manager>();
		managers.add(new Manager("Superman", "kypto"));
		managers.add(new Manager("Batman", "robin"));
		managers.add(new Manager("Green Lantern", "powerring"));
	}

	/**
	 * Initializes a set of transactions to use in tests.
	 **/
	private void initTransactions() throws BadStateException, BadArgumentException
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
	private void foodItemEquals(FoodItem item1, FoodItem item2) throws BadStateException, BadArgumentException
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
	private void vendingMachineEquals(VendingMachine machine1, VendingMachine machine2) throws BadStateException, BadArgumentException
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
	private void locationEquals(Location location1, Location location2) throws BadStateException, BadArgumentException
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
	private void vmLayoutEquals(VMLayout layout1, VMLayout layout2) throws BadStateException, BadArgumentException
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
	private void customerEquals(Customer customer1, Customer customer2) throws BadStateException, BadArgumentException
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
	private void managerEquals(Manager manager1, Manager manager2) throws BadStateException, BadArgumentException
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
	private void transactionEquals(Transaction trans1, Transaction trans2) throws BadStateException, BadArgumentException
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
	private void noTestAddFoodItems() throws SQLException, BadStateException, BadArgumentException
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
	private void noTestAddVendingMachines() throws SQLException, BadStateException, BadArgumentException
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
	private void noTestAddCustomers() throws SQLException, BadStateException, BadArgumentException
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
	private void noTestAddManagers() throws SQLException, BadStateException, BadArgumentException
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
	private void noTestAddTransactions() throws SQLException, BadStateException, BadArgumentException
	{
		if (addedTransactions)
			return;
		addedTransactions = true;
		for (Transaction trans : transactions)
			dbl.updateOrCreateTransaction(trans);
	}

	/**
	 * Tests adding FoodItems to the database.
	 * Note: This only tests if the id was changed and SQL exceptions. getFoodItem() check if
	 * FoodItems can be added correctly.
	 **/
	@Test
	public void addFoodItem() throws SQLException, BadStateException, BadArgumentException
	{
		for (FoodItem item : items)
		{
			dbl.updateOrCreateFoodItem(item);
			assertTrue(!item.isTempId());
		}
	}

	/**
	 * Tests getting items from the database
	 **/
	@Test
	public void getFoodItem() throws SQLException, BadStateException, BadArgumentException
	{
		for (FoodItem item : items)
			dbl.updateOrCreateFoodItem(item);
		for (FoodItem item : items)
		{
			int id = item.getId();
			FoodItem test = dbl.getFoodItemById(id);
			foodItemEquals(test, item);
		}
		for (FoodItem test : dbl.getFoodItemsAll())
		{
			FoodItem item = null;
			for (FoodItem check : items)
			{
				if (check.getId() == test.getId())
				{
					item = check;
					break;
				}
			}
			String failure = String.format("Item I tried to find:\n %5d %10s\nI found:\n", test.getId(), test.getName());
			for (FoodItem check : items)
			{
				failure += String.format("%5d %10s\n", check.getId(), check.getName());
			}
			assertTrue(failure, item != null);
			foodItemEquals(test, item);
		}
	}

	/**
	 * Tests updating items in the database
	 **/
	@Test
	public void changeFoodItem() throws SQLException, BadStateException, BadArgumentException
	{
		for (FoodItem item : items)
			dbl.updateOrCreateFoodItem(item);
		FoodItem change = items.get(0);
		change.setName("Name change");
		dbl.updateOrCreateFoodItem(change);
		FoodItem test = dbl.getFoodItemById(change.getId());
		foodItemEquals(test, change);
		for (int i=1;i<items.size();++i)
			foodItemEquals(dbl.getFoodItemById(items.get(i).getId()), items.get(i));

		change = items.get(1);
		change.setPrice(300);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		foodItemEquals(test, change);
		for (int i=2;i<items.size();++i)
			foodItemEquals(dbl.getFoodItemById(items.get(i).getId()), items.get(i));

		change = items.get(2);
		change.setFreshLength(12345);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		foodItemEquals(test,change);
		for (int i=3;i<items.size();++i)
			foodItemEquals(dbl.getFoodItemById(items.get(i).getId()), items.get(i));

		change = items.get(3);
		change.makeActive(false);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		foodItemEquals(test, change);
	}

	/**
	 * Tests adding vending machines to the database.
	 * Note: This only tests if the id was changed and SQL exceptions.
	 * getVendingMachines() really * tests if vending machines can be added
	 * correctly.
	 **/
	@Test
	public void addVendingMachine() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		for (VendingMachine machine : machines)
		{
			dbl.updateOrCreateVendingMachine(machine);
			assertTrue(!machine.isTempId());
		}
	}

	/**
	 * Tests retrieving vending machines from the database.
	 **/
	 @Test
	 public void getVendingMachine() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		for (VendingMachine machine : machines)
			dbl.updateOrCreateVendingMachine(machine);

		for (VendingMachine machine : machines)
		{
			VendingMachine test = dbl.getVendingMachineById(machine.getId());
			vendingMachineEquals(machine, test);
		}
	 }

	 /**
	  * Tests fetching vending machines from the database by state
	  **/
	 @Test
	 public void getVendingMachineState() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		Collection<VendingMachine> test = dbl.getVendingMachinesByState("Maryland");
		
		LinkedList<VendingMachine> sameSet = new LinkedList<VendingMachine>();
		for (VendingMachine machine : machines)
		{
			if (machine.getLocation().getState().equals("Maryland"))
				sameSet.add(machine);
		}

		for (VendingMachine t : test)
		{
			VendingMachine same = null;
			for (VendingMachine attempt : sameSet)
			{
				if (attempt.getId() == t.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			vendingMachineEquals(t, same);
		}

		test = dbl.getVendingMachinesByState("Pandora");
		
		sameSet = new LinkedList<VendingMachine>();
		for (VendingMachine machine : machines)
		{
			if (machine.getLocation().getState().equals("Pandora"))
				sameSet.add(machine);
		}

		for (VendingMachine t : test)
		{
			VendingMachine same = null;
			for (VendingMachine attempt : sameSet)
			{
				if (attempt.getId() == t.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			vendingMachineEquals(t, same);
		}
	 }

	 /**
	  * Tests fetching vending machines from the database by zip code
	  **/
	 @Test
	 public void getVendingMachineZipCode() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		Collection<VendingMachine> test = dbl.getVendingMachinesByZip(20622);
		
		LinkedList<VendingMachine> sameSet = new LinkedList<VendingMachine>();
		for (VendingMachine machine : machines)
		{
			if (machine.getLocation().getZipCode() == 20622)
				sameSet.add(machine);
		}

		for (VendingMachine t : test)
		{
			VendingMachine same = null;
			for (VendingMachine attempt : sameSet)
			{
				if (attempt.getId() == t.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			vendingMachineEquals(t, same);
		}

		test = dbl.getVendingMachinesByZip(99999);
		
		sameSet = new LinkedList<VendingMachine>();
		for (VendingMachine machine : machines)
		{
			if (machine.getLocation().getZipCode() == 99999)
				sameSet.add(machine);
		}

		for (VendingMachine t : test)
		{
			VendingMachine same = null;
			for (VendingMachine attempt : sameSet)
			{
				if (attempt.getId() == t.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			vendingMachineEquals(t, same);
		}

	 }

	/**
	 * Tests fetching all vending machines from the database
	 **/
	@Test
	public void getVendingMachineAll() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();

		Collection<VendingMachine> test = dbl.getVendingMachinesAll();

		for (VendingMachine t : test)
		{
			VendingMachine same = null;
			for (VendingMachine attempt : machines)
			{
				if (attempt.getId() == t.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			vendingMachineEquals(t, same);
		}
	}

	/**
	 * Tests changing a vending machine's next layout to a layout already in
	 * the database.
	 **/
	 @Test
	 public void changeVendingMachine1() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		VMLayout testLayout = new VMLayout(machines.get(1).getCurrentLayout());
		machines.get(0).setNextLayout(testLayout);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		vendingMachineEquals(test, machines.get(0));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	}

	/**
	 * Tests changing the active flag on a vending machine.
	 **/
	@Test
	public void changeVendingMachine2() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();

		machines.get(1).makeActive(true);
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		vendingMachineEquals(test, machines.get(1));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	}

	/**
	 * Tests changing current layout to next layout in a vending machine.
	 **/
	@Test
	public void changeVendingMachine3() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();

		machines.get(1).swapInNextLayout();
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		vendingMachineEquals(test, machines.get(1));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	}

	/**
	 * Tests changing the stocking interval of a vending machine.
	 **/
	@Test
	public void changeVendingMachine4() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();

		machines.get(0).setStockingInterval(10);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		vendingMachineEquals(test, machines.get(0));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	}

	/**
	 * Tests changing the zip code of the location of a vending machine.
	 **/
	@Test
	public void changeVendingMachine5() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();

		machines.get(1).getLocation().setZipCode(11111);
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		vendingMachineEquals(test, machines.get(1));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	}

	/**
	 * Tests changing the state of the location of a vending machine.
	 **/
	@Test
	public void changeVendingMachine6() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();

		machines.get(1).getLocation().setState("Hawaii");
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		vendingMachineEquals(test, machines.get(1));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing the next layout of a vending machine to one that does
	  * not exist in the database and that has rows which also do not exist.
	  **/
	 @Test
	 public void changeVendingMachine7() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		Row[][] testRows = new Row[3][3];
		for (int i=0;i<3;++i)
		{
			for (int j=0;j<3;++j)
			{
				testRows[i][j] = new Row(items.get(i%items.size()), 5, new GregorianCalendar());
			}
		}

		VMLayout testLayout = new VMLayout(testRows, 8);
		machines.get(0).setNextLayout(testLayout);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		vendingMachineEquals(test, machines.get(0));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	 }

	 /**
	  * Tests changing the next layout of a vending machine to one that does
	  * not exist in the database and that has rows which do not exist and
	  * rows that do exist.
	  **/
	 @Test
	 public void changeVendingMachine8() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		Row[][] testRows = new Row[3][3];
		for (int i=0;i<3;++i)
		{
			for (int j=0;j<3;++j)
			{
				testRows[i][j] = new Row(items.get(i%items.size()), 5, new GregorianCalendar());
			}
		}

		testRows[2][1] = machines.get(1).getNextLayout().getRows()[0][1];

		VMLayout testLayout = new VMLayout(testRows, 8);
		machines.get(0).setNextLayout(testLayout);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		vendingMachineEquals(test, machines.get(0));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	 }

	/**
	 * Tests changing the location of a vending machine.
	 **/
	@Test
	public void changeVendingMachine9() throws SQLException, BadStateException, BadArgumentException
	 {
		 noTestAddFoodItems();
		 noTestAddVendingMachines();

		 Location newPlace = new Location(12121, "Kentucky", new String[] {"A Farm"});
		 machines.get(1).setLocation(newPlace);
		 dbl.updateOrCreateVendingMachine(machines.get(1));
		 VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		 vendingMachineEquals(test, machines.get(1));
		 vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing a row of a vending machine to null
	  **/
	 @Test
	 public void changeVendingMachine10() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		machines.get(1).getNextLayout().getRows()[1][0] = null;
		 dbl.updateOrCreateVendingMachine(machines.get(1));
		 VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		 vendingMachineEquals(test, machines.get(1));
		 vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing the next visit of a VMLayout to null
	  **/
	 @Test
	 public void changeVendingMachine11() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		machines.get(1).getNextLayout().setNextVisit(null);
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		vendingMachineEquals(test, machines.get(1));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing the next visit of a VMLayout to a non-null
	  **/
	 @Test
	 public void changeVendingMachine12() throws SQLException, BadStateException, BadArgumentException
	 {
		noTestAddFoodItems();
		noTestAddVendingMachines();

		GregorianCalendar testVisit = new GregorianCalendar(1994,1,10,3,2,10);
		machines.get(0).getNextLayout().setNextVisit(testVisit);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		vendingMachineEquals(test, machines.get(0));
		vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	 }

	/**
	 * Tests running changeVendingMachine* in succession.
	 **/
	@Test
	public void changeVendingMachineAll() throws SQLException, BadStateException, BadArgumentException
	 {
		 noTestAddFoodItems();
		 noTestAddVendingMachines();

		 changeVendingMachine1();
		 changeVendingMachine2();
		 changeVendingMachine3();
		 changeVendingMachine4();
		 changeVendingMachine5();
		 changeVendingMachine6();
		 changeVendingMachine7();
		 changeVendingMachine8();
		 changeVendingMachine9();
		 changeVendingMachine10();
		 changeVendingMachine11();
		 changeVendingMachine12();
	 }

	/**
	 * Tests adding customers to the database.
	 * Note: This test only ensures there are no SQL errors and the id is
	 * changed. Testing to ensure correct data was added is done in the
	 * getCustomer() test.
	 **/
	@Test
	public void addCustomer() throws SQLException, BadStateException, BadArgumentException
	{
	    for (Customer customer : customers)
	    {
			dbl.updateOrCreateCustomer(customer);
	   		assertTrue(!customer.isTempId());
	    }
	}

	/**
	 * Tests fetching customers from the database.
	 **/
	@Test
	public void getCustomer() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddCustomers();
		for (Customer customer : customers)
			customerEquals(dbl.getCustomerById(customer.getId()), customer);
	}

	/**
	 * Tests fetching all of the customers from the database
	 **/
	@Test
	public void getCustomerAll() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddCustomers();
		Collection<Customer> testSet = dbl.getCustomersAll();
		for (Customer test : testSet)
		{
			Customer same = null;
			for (Customer customer : customers)
			{
				if (customer.getId() == test.getId())
				{
					same = customer;
				}
			}
			assertTrue(same != null);
			customerEquals(test, same);
		}
	}


	/**
	 * Tests changing customers in the database.
	 **/
	@Test
	public void changeCustomer() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddCustomers();

		customers.get(0).setName("Beaenkes");
		dbl.updateOrCreateCustomer(customers.get(0));
		for (int i=0;i<customers.size();i++)
			customerEquals(dbl.getCustomerById(customers.get(i).getId()), customers.get(i));

		customers.get(1).setMoney(customers.get(1).getMoney() - 150);
		dbl.updateOrCreateCustomer(customers.get(1));
		for (int i=0;i<customers.size();i++)
			customerEquals(dbl.getCustomerById(customers.get(i).getId()), customers.get(i));
	}

	/**
	 * Tests adding managers to the database.
	 * Note: This test only ensures there are no SQL errors and the id is
	 * changed. Testing to ensure correct data was added is done in
	 * the getManager() test.
	 **/
	@Test
	public void addManager() throws SQLException, BadStateException, BadArgumentException
	{
		for (Manager manager : managers)
		{
			dbl.updateOrCreateManager(manager);
			assertTrue(!manager.isTempId());
		}
	}

	/**
	 * Tests fetching managers from the database.
	 **/
	@Test
	public void getManager() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddManagers();
		for (Manager manager : managers)
			managerEquals(dbl.getManagerById(manager.getId()), manager);
	}

	/**
	 * Tests fetching all of the managers from the database
	 **/
	@Test
	public void getManagerAll() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddManagers();
		Collection<Manager> testSet = dbl.getManagersAll();
		for (Manager test : testSet)
		{
			Manager same = null;
			for (Manager manager : managers)
			{
				if (manager.getId() == test.getId())
				{
					same = manager;
				}
			}
			assertTrue(same != null);
			managerEquals(test, same);
		}
	}

	/**
	 * Tests changing managers in the database.
	 **/
	@Test
	public void changeManager() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddManagers();

		managers.get(0).setName("Wonder Woman");
		dbl.updateOrCreateManager(managers.get(0));
		for (Manager manager : managers)
			managerEquals(dbl.getManagerById(manager.getId()), manager);

		managers.get(1).setPassword("invisibleplane");
		dbl.updateOrCreateManager(managers.get(1));
		for (Manager manager : managers)
			managerEquals(dbl.getManagerById(manager.getId()), manager);

		managers.get(0).setName("Hulk");
		managers.get(0).setPassword("banner");
		dbl.updateOrCreateManager(managers.get(0));
		for (Manager manager : managers)
			managerEquals(dbl.getManagerById(manager.getId()), manager);
	}

	/**
	 * Tests adding transactions to the database.
	 * Note: This test only ensures there are no SQL errors and the id is
	 * changed. Testing to ensure correct data was added is performed in
	 * the getTransaction() test.
	 **/
	@Test
	public void addTransactions() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();

		for (Transaction trans : transactions)
		{
			dbl.updateOrCreateTransaction(trans);
			assertTrue(!trans.isTempId());
		}
	}

	/**
	 * Tests fetching transactions from the database by id
	 **/
	@Test
	public void getTransaction() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		for (Transaction trans : transactions)
			transactionEquals(dbl.getTransactionById(trans.getId()), trans);
	}

	/**
	 * Tests fetching transactions from the database by zip code
	 **/
	@Test
	public void getTransactionZip() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		Collection<Transaction> test = dbl.getTransactionsByZipCode(20622);
		LinkedList<Transaction> compare = new LinkedList<Transaction>();
		for (Transaction trans : transactions)
		{
			if (trans.getMachine().getLocation().getZipCode() == 20622)
				compare.add(trans);
		}
		assertTrue(test.size() == compare.size());
		for (Transaction trans : test)
		{
			Transaction same = null;
			for (Transaction attempt : compare)
			{
				if (attempt.getId() == trans.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			transactionEquals(trans, same);
		}

		test = dbl.getTransactionsByZipCode(99999);
		compare = new LinkedList<Transaction>();
		for (Transaction trans : transactions)
		{
			if (trans.getMachine().getLocation().getZipCode() == 99999)
				compare.add(trans);
		}
		assertTrue(test.size() == compare.size());
		for (Transaction trans : test)
		{
			Transaction same = null;
			for (Transaction attempt : compare)
			{
				if (attempt.getId() == trans.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			transactionEquals(trans, same);
		}
	}

	/**
	 * Tests fetching transactions by state
	 **/
	@Test
	public void getTransactionState() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		Collection<Transaction> test = dbl.getTransactionsByState("Pandora");
		LinkedList<Transaction> compare = new LinkedList<Transaction>();
		for (Transaction trans : transactions)
		{
			if (trans.getMachine().getLocation().getState().equals("Pandora"))
				compare.add(trans);
		}
		assertTrue(test.size() == compare.size());
		for (Transaction trans : test)
		{
			Transaction same = null;
			for (Transaction attempt : compare)
			{
				if (attempt.getId() == trans.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			transactionEquals(trans, same);
		}

		test = dbl.getTransactionsByState("Maryland");
		compare = new LinkedList<Transaction>();
		for (Transaction trans : transactions)
		{
			if (trans.getMachine().getLocation().getState().equals("Maryland"))
				compare.add(trans);
		}
		assertTrue(test.size() == compare.size());
		for (Transaction trans : test)
		{
			Transaction same = null;
			for (Transaction attempt : compare)
			{
				if (attempt.getId() == trans.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			transactionEquals(trans, same);
		}
	}

	/**
	 * Tests fetching all transactions
	 **/
	@Test
	public void getTransactionAll() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		Collection<Transaction> test = dbl.getTransactionsAll();
		assertTrue(test.size() == transactions.size());
		for (Transaction trans : test)
		{
			Transaction same = null;
			for (Transaction attempt : transactions)
			{
				if (attempt.getId() == trans.getId())
				{
					same = attempt;
					break;
				}
			}
			assertTrue(same != null);
			transactionEquals(trans, same);
		}
	}

	/**
	 * Tests changing the timestamp of a transaction
	 **/
	@Test
	public void changeTransaction1() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		transactions.get(0).setTimestamp(new GregorianCalendar(2011, 10, 11, 5, 3, 2));
		dbl.updateOrCreateTransaction(transactions.get(0));
		for (Transaction trans : transactions)
			transactionEquals(dbl.getTransactionById(trans.getId()), trans);
	}

	/**
	 * Tests changing the vending machine of a transaction
	 **/
	@Test
	public void changeTransaction2() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		transactions.get(0).setMachine(machines.get(1));
		dbl.updateOrCreateTransaction(transactions.get(0));
		for (Transaction trans : transactions)
			transactionEquals(dbl.getTransactionById(trans.getId()), trans);
	}

	/**
	 * Tests changing the customer of a transaction
	 **/
	@Test
	public void changeTransaction3() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		transactions.get(1).setProduct(items.get(0));
		dbl.updateOrCreateTransaction(transactions.get(1));
		for (Transaction trans : transactions)
			transactionEquals(dbl.getTransactionById(trans.getId()), trans);
	}

	/**
	 * Tests changing the row of a transaction
	 **/
	@Test
	public void changeTransaction4() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		transactions.get(1).setRow(new Pair<Integer, Integer>(1,3));
		dbl.updateOrCreateTransaction(transactions.get(1));
		for (Transaction trans : transactions)
			transactionEquals(dbl.getTransactionById(trans.getId()), trans);
	}

	/**
	 * Runs all of the changeTransaction* tests consecutively 
	 **/
	@Test
	public void changeTransactionAll() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		noTestAddCustomers();
		noTestAddVendingMachines();
		noTestAddTransactions();

		changeTransaction1();
		changeTransaction2();
		changeTransaction3();
		changeTransaction4();
	}

	/**
	 * Tests fetching all of the locations from the database
	 **/
	@Test
	public void getLocationAll() throws SQLException, BadStateException, BadArgumentException
	{
		noTestAddFoodItems();
		noTestAddVendingMachines();
		Collection<Location> testSet = dbl.getLocationsAll();
		for (Location test : testSet)
		{
			Location same = null;
			for (VendingMachine machine : machines)
			{
				if (machine.getLocation().getId() == test.getId())
				{
					same = machine.getLocation();
				}
			}
			assertTrue(same != null);
			locationEquals(test, same);
		}
	}
}
