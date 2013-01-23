import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.*;

/**
 * test suite for ManagerReportStatsScreen
 * @author Kyle Savarese <kms7341@rit.edu>
 */

@RunWith(JUnit4.class)

public class ManagerReportStatsScreenTest {

	/**
	 * DatabaseLayer instance
	 **/
	private DatabaseLayer db;

	/**
	 * Holds test data
	 **/
	private TestUtilities helper;

	/**
	 * Creates and adds test data to the database after nuking it
	 **/
	@Before
	public void setUp() throws SQLException,
		BadStateException, BadArgumentException {
		db = DatabaseLayer.getInstance();
		db.nuke();
		helper = new TestUtilities( true );
	}

	/**
	 * Tests the constructor to ensure it doesn't throw an exception
	 **/
	@Test
	public void testConstructor() throws Exception
	{
		new ManagerReportStatsScreen();
	}

	/**
	 * Tests listing the machines
	 **/
	@Test
	public void testListMachines()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<VendingMachine> test1 = test.listMachines();
		assertTrue(helper.machines.size() == test1.size());
		for(VendingMachine t : helper.machines)
			assertTrue(test1.contains(t));
	}

	/**
	 * Tests listing the customers
	 **/
	@Test
	public void testListCustomers()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Customer> test1 = test.listCustomers();
		assertTrue(helper.customers.size() == test1.size());
		for(Customer c : helper.customers)
			assertTrue(test1.contains(c));
	}

	/**
	 * Tests listing the food items
	 **/
	@Test
	public void testListFoodItems()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<FoodItem> test1 = test.listFoodItems();
		assertTrue(helper.items.size() == test1.size());
		for(FoodItem item : helper.items)
			assertTrue(test1.contains(item));
	}

	/**
	 * Tests listing the sales by machine with valid input
	 **/
	@Test
	public void testListMachineSales1()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listMachineSales(helper.machines.get(0));
		ArrayList<Transaction> compare = new ArrayList<Transaction>();
		for (Transaction t : helper.transactions)
			if (t.getMachine().equals(helper.machines.get(0)))
				compare.add(t);
		assertTrue(compare.size() == test1.size());
		for (Transaction t : compare)
			assertTrue(test1.contains(t));
	}

	/**
	 * Tests listing the sales by machine with invalid input (null)
	 **/
	@Test
	public void testListMachineSales2()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listMachineSales(null);
		assertTrue(test1 == null);
	}

	/**
	 * Tests listing the sales by machine with invalid input (vending machine
	 * that does not exist in the database)
	 **/
	@Test
	public void testListMachineSales3() throws Exception
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		
		VendingMachine vm = new VendingMachine( helper.machines.get(0).getLocation(), 2, helper.machines.get(1).getCurrentLayout(), helper.machines.get(1).getNextLayout(), true );

		ArrayList<Transaction> test1 = test.listMachineSales(vm);
		assertTrue(test1 == null);
	}

	/**
	 * Tests listing the sales by customer with valid input
	 **/
	@Test
	public void testListCustomerSales1()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listCustomerSales(helper.customers.get(0));
		ArrayList<Transaction> compare = new ArrayList<Transaction>();
		for (Transaction t : helper.transactions)
			if (t.getCustomer().equals(helper.customers.get(0)))
				compare.add(t);
		assertTrue(compare.size() == test1.size());
		for (Transaction t : compare)
			assertTrue(test1.contains(t));
	}

	/**
	 * Tests listing the sales by customer with valid input, but it should
	 * return 0 results.
	 **/
	@Test
	public void testListCustomerSales2()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listCustomerSales(helper.customers.get(2));
		assertTrue(test1.size() == 0);
	}

	/**
	 * Tests listing the sales by customer with invalid input (null)
	 **/
	@Test
	public void testListCustomerSales3()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listCustomerSales(null);
		assertTrue(test1 == null);
	}

	/**
	 * Tests listing the sales by customer with invalid input (customer does
	 * not exist in database)
	 **/
	@Test
	public void testListCustomerSales4() throws Exception
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		Customer faker = new Customer("Bobby McGee", 1234);
		ArrayList<Transaction> test1 = test.listCustomerSales(faker);
		assertTrue(test1 == null);
	}

	/**
	 * Tests listing the sales by food item with valid input
	 **/
	@Test
	public void testListFoodItemSales1()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listFoodItemSales(helper.items.get(0));
		ArrayList<Transaction> compare = new ArrayList<Transaction>();
		for (Transaction t : helper.transactions)
			if (t.getProduct().equals(helper.items.get(0)))
				compare.add(t);
		assertTrue(compare.size() == test1.size());
		for (Transaction t : compare)
			assertTrue(test1.contains(t));
	}

	/**
	 * Tests listing the sales by food item with valid input but should return
	 * no results
	 **/
	@Test
	public void testListFoodItemSales2()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listFoodItemSales(helper.items.get(2));
		assertTrue(test1.size() == 0);
	}

	/**
	 * Tests listing the sales by food item with invalid input (null)
	 **/
	@Test
	public void testListFoodItemSales3()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listFoodItemSales(null);
		assertTrue(test1 == null);
	}

	/**
	 * Tests listing the sales by food item with invalid input (item that is
	 * not in the database
	 **/
	@Test
	public void testListFoodItemSales4() throws Exception
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		FoodItem trick = new FoodItem("Trick Can", 10000, 12, true);
		ArrayList<Transaction> test1 = test.listFoodItemSales(trick);
		assertTrue(test1 == null);
	}

	/**
	 * Tests listing all of the sales
	 **/
	@Test
	public void testListSalesAll()
	{
		ManagerReportStatsScreen test = new ManagerReportStatsScreen();
		ArrayList<Transaction> test1 = test.listSalesAll();
		assertTrue(test1.size() == helper.transactions.size());
		for (Transaction t : helper.transactions)
			assertTrue(test1.contains(t));
	}
}
