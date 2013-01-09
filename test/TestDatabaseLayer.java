import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.GregorianCalendar;

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
	 * Clears the database and initializes test objects for each test
	 **/
	@Before
	public void setUp() throws SQLException
	{
		dbl = DatabaseLayer.getInstance();
		dbl.nuke();
		initFoodItems();
		initVendingMachines();
		initCustomers();
		initManagers();
		addedFoodItems = false;
		addedVendingMachines = false;
		addedCustomers = false;
		addedManagers = false;
	}

	/**
	 * Creates a set of FoodItems for use in tests
	 **/
	private void initFoodItems()
	{
		items = new ArrayList<FoodItem>();
		items.add(new FoodItem("Twix", 175, 1000));
		items.add(new FoodItem("Snickers", 175, 1000));
		items.add(new FoodItem("Chips", 150, 500));
		items.add(new FoodItem("Fish Sandwich", 200, 100000));
	}

	/**
	 * Creates a set of VendingMachines for use in tests
	 **/
	private void initVendingMachines()
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

		Location loc1 = new Location(20622, "Maryland", new String[] {"Mckay's"});
		VMLayout cur1 = new VMLayout(testRows[0]);
		VMLayout next1 = new VMLayout(testRows[1]);
		machines.add(new VendingMachine(loc1, 1000000, cur1, next1, true));
		
		Location loc2 = new Location(99999, "Pandora", new String[] {"Zed's Medical Supplies", "That other dude's gun shop", "The chick's bar", "Whatever that guy's name who runs the scooter shop. His scooter shop. I should really know his name but I forget and I'm not willing to lookup it up."});
		VMLayout cur2 = new VMLayout(testRows[2]);
		VMLayout next2 = new VMLayout(testRows[3]);
		machines.add(new VendingMachine(loc2, 500000, cur2, next2, false));
	}

	/**
	 * Initializes a set of customers to use in tests.
	 **/
	private void initCustomers()
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
	private void initManagers()
	{
		managers = new ArrayList<Manager>();
		managers.add(new Manager("Superman", "kypto"));
		managers.add(new Manager("Batman", "robin"));
		managers.add(new Manager("Green Lantern", "powerring"));
	}

	/**
	 * Checks if two FoodItems are the same
	 * @param item1 The first item
	 * @param item2 The second item
	 **/
	private void foodItemEquals(FoodItem item1, FoodItem item2)
	{
		assertTrue(item1.getId() == item2.getId());
		assertTrue(item1.getName().equals(item2.getName()));
		assertTrue(item1.getPrice() == item2.getPrice());
		assertTrue(item1.getFreshLength() == item2.getFreshLength());
	}

	/**
	 * Checks if two vending machines are equal
	 * @param machine1 The first vending machine
	 * @param machine2 The second vending machine
	 **/
	private void vendingMachineEquals(VendingMachine machine1, VendingMachine machine2)
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
	private void locationEquals(Location location1, Location location2)
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
	private void vmLayoutEquals(VMLayout layout1, VMLayout layout2)
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
				foodItemEquals(row1.getProduct(), row2.getProduct());
				assertTrue(row1.getExpirationDate() + "\n" + row2.getExpirationDate(), row1.getExpirationDate().equals(row2.getExpirationDate()));
				assertTrue(row1.getRemainingQuantity() == row2.getRemainingQuantity());
			}
		}
	}

	/**
	 * Checks if two customers are equal
	 * @param customer1 The first customer
	 * @param customer2 The second customer
	 **/
	private void customerEquals(Customer customer1, Customer customer2)
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
	private void managerEquals(Manager manager1, Manager manager2)
	{
		assertTrue(manager1.getId() == manager2.getId());
		assertTrue(manager1.getName() + "|" + manager2.getName(),
			manager1.getName().equals(manager2.getName()));
		assertTrue(manager1.getPassword().equals(manager2.getPassword()));
	}

	/**
	 * Adds FoodItems to the database iff they have not already
	 * been added. Used in several tests.
	 **/
	private void noTestAddFoodItems() throws SQLException
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
	private void noTestAddVendingMachines() throws SQLException
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
	private void noTestAddCustomers() throws SQLException
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
	private void noTestAddManagers() throws SQLException
	{
		if (addedManagers)
			return;
		addedManagers = true;
		for (Manager manager : managers)
			dbl.updateOrCreateManager(manager);
	}

	/**
	 * Tests adding FoodItems to the database.
	 * Note: This only tests if the id was changed and SQL exceptions. getFoodItem() check if
	 * FoodItems can be added correctly.
	 **/
	@Test
	public void addFoodItem() throws SQLException
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
	public void getFoodItem() throws SQLException
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
	public void changeFoodItem() throws SQLException
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
	}

	/**
	 * Tests adding vending machines to the database.
	 * Note: This only tests if the id was changed and SQL exceptions.
	 * getVendingMachines() really * tests if vending machines can be added
	 * correctly.
	 **/
	@Test
	public void addVendingMachine() throws SQLException
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
	 public void getVendingMachine() throws SQLException
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
	 * Tests changing a vending machine's next layout to a layout already in
	 * the database.
	 **/
	 @Test
	 public void changeVendingMachine1() throws SQLException
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
	public void changeVendingMachine2() throws SQLException
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
	public void changeVendingMachine3() throws SQLException
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
	public void changeVendingMachine4() throws SQLException
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
	public void changeVendingMachine5() throws SQLException
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
	public void changeVendingMachine6() throws SQLException
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
	 public void changeVendingMachine7() throws SQLException
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

		VMLayout testLayout = new VMLayout(testRows);
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
	 public void changeVendingMachine8() throws SQLException
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

		VMLayout testLayout = new VMLayout(testRows);
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
	public void changeVendingMachine9() throws SQLException
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
	 * Tests running changeVendingMachine* in succession.
	 **/
	@Test
	public void changeVendingMachineAll() throws SQLException
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
	 }

	/**
	 * Tests adding customers to the database.
	 * Note: This test only ensures there are no SQL errors and the id is
	 * changed. Testing to ensure correct data was added is done in the
	 * getCustomer() test.
	 **/
	@Test
	public void addCustomer() throws SQLException
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
	public void getCustomer() throws SQLException
	{
		noTestAddCustomers();
		for (Customer customer : customers)
			customerEquals(dbl.getCustomerById(customer.getId()), customer);
	}


	/**
	 * Tests changing customers in the database.
	 **/
	@Test
	public void changeCustomer() throws SQLException
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
	 * Note: This test only ensures there are no SQL erros and the id is
	 * changed. Testing to ensure correct data was added is done in
	 * the getManager() test.
	 **/
	@Test
	public void addManager() throws SQLException
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
	public void getManager() throws SQLException
	{
		noTestAddManagers();
		for (Manager manager : managers)
			managerEquals(dbl.getManagerById(manager.getId()), manager);
	}

	/**
	 * Tests changing managers in the database.
	 **/
	@Test
	public void changeManager() throws SQLException
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
}
