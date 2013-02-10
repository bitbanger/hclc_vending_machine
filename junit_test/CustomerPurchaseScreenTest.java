import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.ArrayList;

/**
 * unit test suite for CustomerPurchaseScreen
 * @author Kyle Savarese <kms7341@rit.edu>
 */

@RunWith(JUnit4.class)

public class CustomerPurchaseScreenTest {

	@Before
	public void setUp() throws Exception
	{
		DatabaseLayer.getInstance().nuke();
	}

	@Test
	public void testLayoutList() throws BadArgumentException, 
		BadStateException, SQLException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		FoodItem[][] answer = screen.listLayout();
		Row[][] rows = help.getCurrentLayout().getRows();
		for ( int i = 0; i < rows.length; i++ ) {
			for ( int j = 0; j < rows[i].length; j++ ) {
				Assert.assertTrue( answer[i][j].equals(
					rows[i][j].getProduct() ) );
			}
		}
	}
	
	@Test
	public void testPurchaseSuccess() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "Good".equals( screen.tryPurchase( 
			new Pair<Integer, Integer> (0, 0) ) ) );
		Assert.assertTrue( screen.getPurchasedItem().equals( help.getCurrentLayout().getRows()[0][0].getProduct() ) ); // now also tests that the 
	}

	@Test
	public void testPurchaseBadLoc() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "Invalid location".equals( screen.tryPurchase( 
			new Pair<Integer, Integer> (0, 15) ) ) );
	}

	@Test
	public void testPurchaseNoProd() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(0);
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "No product".equals( screen.tryPurchase( 
			new Pair<Integer, Integer> (0, 0) ) ) );
	}

	@Test
	public void testPurchaseSoldOut() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Row[][] next = help.getNextLayout().getRows();
		next[0][0].setRemainingQuantity( 0 );
		help.swapInNextLayout( new VMLayout( next, 7 ) );
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "Item sold out".equals( screen.tryPurchase( 
			new Pair<Integer, Integer> (0, 0) ) ) );
	}

	@Test
	public void testPurchaseInactive() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Row[][] next = help.getNextLayout().getRows();
		next[0][0].getProduct().makeActive( false );
		help.setNextLayout( new VMLayout( next, 7 ) );
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "Item inactive".equals( screen.tryPurchase( 
			new Pair<Integer, Integer> (0, 0) ) ) );
	}

	@Test
	public void testPurchaseNoFunds() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Customer user = helper.customers.get(0);
		user.setMoney( 0 );
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "Insufficient funds".equals( 
			screen.tryPurchase( new Pair<Integer, Integer> (0, 0) ) ) );
	}

	@Test
	public void testName() throws IllegalArgumentException,
		SQLException, BadArgumentException, BadStateException
	{
		TestUtilities helper=new TestUtilities(true);
		VendingMachine help=helper.machines.get(1);
		Customer user=helper.customers.get(0);
		CustomerPurchaseScreen screen=new CustomerPurchaseScreen(user, help);
		Assert.assertEquals(screen.getUserName(), user.getName());
	}

	/**
	 * Tests getting the frequently bought items
	 **/
	@Test
	public void testFrequentlyBought1() throws Exception
	{
		TestUtilities helper=new TestUtilities(true);
		DatabaseLayer dbl = DatabaseLayer.getInstance();
		VendingMachine help=helper.machines.get(1);
		Customer user=helper.customers.get(0);
		for (int i=0;i<30;++i)
			helper.transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), helper.machines.get(0), helper.customers.get(0), helper.items.get(0), new Pair<Integer, Integer>(0,0)));
		for (int i=0;i<10;++i)
			helper.transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), helper.machines.get(0), helper.customers.get(0), helper.items.get(1), new Pair<Integer, Integer>(0,0)));
		for (int i=0;i<50;++i)
			helper.transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), helper.machines.get(0), helper.customers.get(0), helper.items.get(3), new Pair<Integer, Integer>(0,0)));

		for (Transaction trans : helper.transactions)
			dbl.updateOrCreateTransaction(trans);
		CustomerPurchaseScreen screen=new CustomerPurchaseScreen(user, help);
		ArrayList<FoodItem> test1 = screen.getFrequentlyBought();
		Assert.assertTrue(test1.size() == 3);
		Assert.assertTrue(test1.get(0).equals(helper.items.get(3)));
		Assert.assertTrue(test1.get(1).equals(helper.items.get(0)));
		Assert.assertTrue(test1.get(2).equals(helper.items.get(1)));
	}

	/**
	 * Tests getting the frequently bought items when not all of the items are
	 * currently stocked.
	 **/
	@Test
	public void testFrequentlyBought2() throws Exception
	{
		TestUtilities helper=new TestUtilities(true);
		DatabaseLayer dbl = DatabaseLayer.getInstance();
		VendingMachine help=helper.machines.get(1);
		Customer user=helper.customers.get(0);
		FoodItem howAboutW = new FoodItem("W", 125, 125, true);
		dbl.updateOrCreateFoodItem(howAboutW);
		for (int i=0;i<30;++i)
			helper.transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), helper.machines.get(0), helper.customers.get(0), helper.items.get(0), new Pair<Integer, Integer>(0,0)));
		for (int i=0;i<10;++i)
			helper.transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), helper.machines.get(0), helper.customers.get(0), helper.items.get(1), new Pair<Integer, Integer>(0,0)));
		for (int i=0;i<50;++i)
			helper.transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), helper.machines.get(0), helper.customers.get(0), helper.items.get(3), new Pair<Integer, Integer>(0,0)));
		for (int i=0;i<70;++i)
			helper.transactions.add(new Transaction(new GregorianCalendar(2013, 1, 8, 14, 15, 3), helper.machines.get(0), helper.customers.get(0), howAboutW, new Pair<Integer, Integer>(0,0)));

		for (Transaction trans : helper.transactions)
			dbl.updateOrCreateTransaction(trans);
		CustomerPurchaseScreen screen=new CustomerPurchaseScreen(user, help);
		ArrayList<FoodItem> test1 = screen.getFrequentlyBought();
		Assert.assertTrue(test1.size() == 3);
		Assert.assertTrue(test1.get(0).equals(helper.items.get(3)));
		Assert.assertTrue(test1.get(1).equals(helper.items.get(0)));
		Assert.assertTrue(test1.get(2).equals(helper.items.get(1)));
	}

	/**
	 * Tests purchasing and finding an item
	 **/
	@Test
	public void testTryPurchaseProduct1() throws Exception
	{
		TestUtilities helper=new TestUtilities(true);
		DatabaseLayer dbl = DatabaseLayer.getInstance();
		VendingMachine help=helper.machines.get(1);
		Customer user=helper.customers.get(0);
		CustomerPurchaseScreen screen=new CustomerPurchaseScreen(user, help);
		Assert.assertTrue(screen.tryPurchase(helper.items.get(0)).equals("Good"));
	}

	/**
	 * Tests purchasing and finding an item failure
	 **/
	@Test
	public void testTryPurchaseProduct2() throws Exception
	{
		TestUtilities helper=new TestUtilities(true);
		DatabaseLayer dbl = DatabaseLayer.getInstance();
		VendingMachine help=helper.machines.get(1);
		Customer user=helper.customers.get(0);
		CustomerPurchaseScreen screen=new CustomerPurchaseScreen(user, help);
		FoodItem howAboutW = new FoodItem("W", 125, 125, true);
		Assert.assertTrue(screen.tryPurchase(howAboutW).equals("Item not found"));
	}

	/**
	 * Tests purchasing and finding an item failure
	 **/
	@Test
	public void testTryPurchaseProduct3() throws Exception
	{
		TestUtilities helper=new TestUtilities(true);
		DatabaseLayer dbl = DatabaseLayer.getInstance();
		VendingMachine help=helper.machines.get(1);
		Customer user=helper.customers.get(0);
		CustomerPurchaseScreen screen=new CustomerPurchaseScreen(user, help);
		FoodItem howAboutW = null;
		Assert.assertTrue(screen.tryPurchase(howAboutW).equals("Item not found"));
	}
}
