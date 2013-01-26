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
	 * Holds testing data
	 **/
	private TestUtilities testUtil;

	/**
	 * Clears the database and initializes test objects for each test
	 **/
	@Before
	public void setUp() throws SQLException, BadStateException, BadArgumentException
	{
		dbl = DatabaseLayer.getInstance();
		dbl.nuke();
		testUtil = new TestUtilities();
		items = testUtil.items;
		machines = testUtil.machines;
		customers = testUtil.customers;
		managers = testUtil.managers;
		transactions = testUtil.transactions;
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
			TestUtilities.foodItemEquals(test, item);
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
			TestUtilities.foodItemEquals(test, item);
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
		TestUtilities.foodItemEquals(test, change);
		for (int i=1;i<items.size();++i)
			TestUtilities.foodItemEquals(dbl.getFoodItemById(items.get(i).getId()), items.get(i));

		change = items.get(1);
		change.setPrice(300);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		TestUtilities.foodItemEquals(test, change);
		for (int i=2;i<items.size();++i)
			TestUtilities.foodItemEquals(dbl.getFoodItemById(items.get(i).getId()), items.get(i));

		change = items.get(2);
		change.setFreshLength(12345);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		TestUtilities.foodItemEquals(test,change);
		for (int i=3;i<items.size();++i)
			TestUtilities.foodItemEquals(dbl.getFoodItemById(items.get(i).getId()), items.get(i));

		change = items.get(3);
		change.makeActive(false);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		TestUtilities.foodItemEquals(test, change);
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
		testUtil.noTestAddFoodItems();
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
		testUtil.noTestAddFoodItems();
		for (VendingMachine machine : machines)
			dbl.updateOrCreateVendingMachine(machine);

		for (VendingMachine machine : machines)
		{
			VendingMachine test = dbl.getVendingMachineById(machine.getId());
			TestUtilities.vendingMachineEquals(machine, test);
		}
	 }

	 /**
	  * Tests fetching vending machines from the database by state
	  **/
	 @Test
	 public void getVendingMachineState() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

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
			TestUtilities.vendingMachineEquals(t, same);
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
			TestUtilities.vendingMachineEquals(t, same);
		}
	 }

	 /**
	  * Tests fetching vending machines from the database by zip code
	  **/
	 @Test
	 public void getVendingMachineZipCode() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

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
			TestUtilities.vendingMachineEquals(t, same);
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
			TestUtilities.vendingMachineEquals(t, same);
		}

	 }

	/**
	 * Tests fetching all vending machines from the database
	 **/
	@Test
	public void getVendingMachineAll() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

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
			TestUtilities.vendingMachineEquals(t, same);
		}
	}

	/**
	 * Tests changing a vending machine's next layout to a layout already in
	 * the database.
	 **/
	 @Test
	 public void changeVendingMachine1() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		VMLayout testLayout = new VMLayout(machines.get(1).getCurrentLayout());
		machines.get(0).setNextLayout(testLayout);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(0));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	}

	/**
	 * Tests changing the active flag on a vending machine.
	 **/
	@Test
	public void changeVendingMachine2() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		machines.get(1).makeActive(true);
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(1));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	}

	/**
	 * Tests changing current layout to next layout in a vending machine.
	 **/
	@Test
	public void changeVendingMachine3() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		machines.get(1).swapInNextLayout( machines.get(1).getNextLayout() );
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(1));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	}

	/**
	 * Tests changing the stocking interval of a vending machine.
	 **/
	@Test
	public void changeVendingMachine4() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		machines.get(0).setStockingInterval(10);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(0));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	}

	/**
	 * Tests changing the zip code of the location of a vending machine.
	 **/
	@Test
	public void changeVendingMachine5() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		machines.get(1).getLocation().setZipCode(11111);
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(1));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	}

	/**
	 * Tests changing the state of the location of a vending machine.
	 **/
	@Test
	public void changeVendingMachine6() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		machines.get(1).getLocation().setState("Hawaii");
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(1));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing the next layout of a vending machine to one that does
	  * not exist in the database and that has rows which also do not exist.
	  **/
	 @Test
	 public void changeVendingMachine7() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

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
		TestUtilities.vendingMachineEquals(test, machines.get(0));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	 }

	 /**
	  * Tests changing the next layout of a vending machine to one that does
	  * not exist in the database and that has rows which do not exist and
	  * rows that do exist.
	  **/
	 @Test
	 public void changeVendingMachine8() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

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
		TestUtilities.vendingMachineEquals(test, machines.get(0));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	 }

	/**
	 * Tests changing the location of a vending machine.
	 **/
	@Test
	public void changeVendingMachine9() throws SQLException, BadStateException, BadArgumentException
	 {
		 testUtil.noTestAddFoodItems();
		 testUtil.noTestAddVendingMachines();

		 Location newPlace = new Location(12121, "Kentucky", new String[] {"A Farm"});
		 machines.get(1).setLocation(newPlace);
		 dbl.updateOrCreateVendingMachine(machines.get(1));
		 VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		 TestUtilities.vendingMachineEquals(test, machines.get(1));
		 TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing a row of a vending machine to null
	  **/
	 @Test
	 public void changeVendingMachine10() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		machines.get(1).getNextLayout().getRows()[1][0] = null;
		 dbl.updateOrCreateVendingMachine(machines.get(1));
		 VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		 TestUtilities.vendingMachineEquals(test, machines.get(1));
		 TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing the next visit of a VMLayout to null
	  **/
	 @Test
	 public void changeVendingMachine11() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		machines.get(1).getNextLayout().setNextVisit(null);
		dbl.updateOrCreateVendingMachine(machines.get(1));
		VendingMachine test = dbl.getVendingMachineById(machines.get(1).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(1));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(0).getId()), machines.get(0));
	 }

	 /**
	  * Tests changing the next visit of a VMLayout to a non-null
	  **/
	 @Test
	 public void changeVendingMachine12() throws SQLException, BadStateException, BadArgumentException
	 {
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();

		GregorianCalendar testVisit = new GregorianCalendar(1994,1,10,3,2,10);
		machines.get(0).getNextLayout().setNextVisit(testVisit);
		dbl.updateOrCreateVendingMachine(machines.get(0));
		VendingMachine test = dbl.getVendingMachineById(machines.get(0).getId());
		TestUtilities.vendingMachineEquals(test, machines.get(0));
		TestUtilities.vendingMachineEquals(dbl.getVendingMachineById(machines.get(1).getId()), machines.get(1));
	 }

	/**
	 * Tests running changeVendingMachine* in succession.
	 **/
	@Test
	public void changeVendingMachineAll() throws SQLException, BadStateException, BadArgumentException
	 {
		 testUtil.noTestAddFoodItems();
		 testUtil.noTestAddVendingMachines();

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
		testUtil.noTestAddCustomers();
		for (Customer customer : customers)
			TestUtilities.customerEquals(dbl.getCustomerById(customer.getId()), customer);
	}

	/**
	 * Tests fetching all of the customers from the database
	 **/
	@Test
	public void getCustomerAll() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddCustomers();
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
			TestUtilities.customerEquals(test, same);
		}
	}


	/**
	 * Tests changing customers in the database.
	 **/
	@Test
	public void changeCustomer() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddCustomers();

		customers.get(0).setName("Beaenkes");
		dbl.updateOrCreateCustomer(customers.get(0));
		for (int i=0;i<customers.size();i++)
			TestUtilities.customerEquals(dbl.getCustomerById(customers.get(i).getId()), customers.get(i));

		customers.get(1).setMoney(customers.get(1).getMoney() - 150);
		dbl.updateOrCreateCustomer(customers.get(1));
		for (int i=0;i<customers.size();i++)
			TestUtilities.customerEquals(dbl.getCustomerById(customers.get(i).getId()), customers.get(i));
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
		testUtil.noTestAddManagers();
		for (Manager manager : managers)
			TestUtilities.managerEquals(dbl.getManagerById(manager.getId()), manager);
	}

	/**
	 * Tests fetching all of the managers from the database
	 **/
	@Test
	public void getManagerAll() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddManagers();
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
			TestUtilities.managerEquals(test, same);
		}
	}

	/**
	 * Tests changing managers in the database.
	 **/
	@Test
	public void changeManager() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddManagers();

		managers.get(0).setName("Wonder Woman");
		dbl.updateOrCreateManager(managers.get(0));
		for (Manager manager : managers)
			TestUtilities.managerEquals(dbl.getManagerById(manager.getId()), manager);

		managers.get(1).setPassword("invisibleplane");
		dbl.updateOrCreateManager(managers.get(1));
		for (Manager manager : managers)
			TestUtilities.managerEquals(dbl.getManagerById(manager.getId()), manager);

		managers.get(0).setName("Hulk");
		managers.get(0).setPassword("banner");
		dbl.updateOrCreateManager(managers.get(0));
		for (Manager manager : managers)
			TestUtilities.managerEquals(dbl.getManagerById(manager.getId()), manager);
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
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddCustomers();

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
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddCustomers();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddTransactions();

		for (Transaction trans : transactions)
			TestUtilities.transactionEquals(dbl.getTransactionById(trans.getId()), trans);
	}

	/**
	 * Tests fetching transactions from the database by zip code
	 **/
	@Test
	public void getTransactionZip() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddCustomers();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddTransactions();

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
			TestUtilities.transactionEquals(trans, same);
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
			TestUtilities.transactionEquals(trans, same);
		}
	}

	/**
	 * Tests fetching transactions by state
	 **/
	@Test
	public void getTransactionState() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddCustomers();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddTransactions();

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
			TestUtilities.transactionEquals(trans, same);
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
			TestUtilities.transactionEquals(trans, same);
		}
	}

	/**
	 * Tests fetching transactions with a specific item
	 **/
	@Test
	public void getTransactionItem() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddCustomers();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddTransactions();

		ArrayList<Transaction> test = dbl.getTransactionsByFoodItem(testUtil.items.get(0));
		ArrayList<Transaction> compare = new ArrayList<Transaction>();
		for (Transaction trans : transactions)
		{
			if (trans.getProduct().equals(testUtil.items.get(0)))
				compare.add(trans);
		}
		assertTrue(test.size() == compare.size());
		for (Transaction trans : test)
		{
			assertTrue(compare.contains(trans));
		}

		test = dbl.getTransactionsByFoodItem(testUtil.items.get(2));
		compare = new ArrayList<Transaction>();
		assertTrue(test.size() == compare.size());
		for (Transaction trans : transactions)
		{
			if (trans.getProduct().equals(testUtil.items.get(2)))
				compare.add(trans);
		}
		for (Transaction trans : test)
		{
			assertTrue(compare.contains(trans));
		}
	}

	/**
	 * Tests fetching all transactions
	 **/
	@Test
	public void getTransactionAll() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddCustomers();
		testUtil.noTestAddVendingMachines();
		testUtil.noTestAddTransactions();

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
			TestUtilities.transactionEquals(trans, same);
		}
	}

	/**
	 * Tests fetching all of the locations from the database
	 **/
	@Test
	public void getLocationAll() throws SQLException, BadStateException, BadArgumentException
	{
		testUtil.noTestAddFoodItems();
		testUtil.noTestAddVendingMachines();
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
			TestUtilities.locationEquals(test, same);
		}
	}
}
