import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.GregorianCalendar;

/**
 * unit test suite for CustomerPurchaseScreen
 * @author Kyle Savarese <kms7341@rit.edu>
 */

@RunWith(JUnit4.class)

public class CustomerPurchaseScreenTest {

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
		Assert.assertTrue( "GOOD".equals( screen.tryPurchase( 
			new Pair<Integer, Integer> (0, 0) ) ) );
	}

	@Test
	public void testPurchaseBadLoc() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "INVALID LOCATION".equals( screen.tryPurchase( 
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
		Assert.assertTrue( "NO PRODUCT".equals( screen.tryPurchase( 
			new Pair<Integer, Integer> (0, 0) ) ) );
	}

	@Test
	public void testPurchaseSoldOut() throws IllegalArgumentException, 
		SQLException, BadArgumentException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help = helper.machines.get(1);
		Row[][] next = help.getNextLayout().getRows();
		next[0][0].setRemainingQuantity( 0 );
		help.setNextLayout( new VMLayout( next, 7 ) );
		help.swapInNextLayout();
		Customer user = helper.customers.get(0);
		CustomerPurchaseScreen screen = new 
			CustomerPurchaseScreen( user, help );
		Assert.assertTrue( "ITEM SOLD OUT".equals( screen.tryPurchase( 
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
		Assert.assertTrue( "ITEM INACTIVE".equals( screen.tryPurchase( 
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
		Assert.assertTrue( "INSUFFICIENT FUNDS".equals( 
			screen.tryPurchase( new Pair<Integer, Integer> (0, 0) ) ) );
	}
}
