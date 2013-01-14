import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.*;

/**
 * test suite for ManagerStockedItemsScreen
 * @author Kyle Savarese
 */

@RunWith(JUnit4.class)

public class ManagerStockedItemsScreenTest {

	@Test
	public void additemTest() throws SQLException, BadArgumentException, 
		BadStateException {
		ManagerStockedItemsScreen test = new ManagerStockedItemsScreen(
			DatabaseLayer.getInstance().getFoodItemsAll() );
		int id = test.addItem("TWINKIES", 50, 9000);
		Assert.assertTrue( test.listItems().contains(
			DatabaseLayer.getInstance().getFoodItemById( id ) ) );
	}

	@Test
	public void changeNameTest() throws SQLException, BadArgumentException, 
		BadStateException {
		ManagerStockedItemsScreen test = new ManagerStockedItemsScreen(
			DatabaseLayer.getInstance().getFoodItemsAll() );
		int id = test.addItem("TWINKIES", 50, 9000);
		test.changeItemName( id, "Now gone :(" );
		Assert.assertTrue( DatabaseLayer.getInstance().getFoodItemById( 
			id ).getName().equals( "Now gone :(" ) );
	}

	@Test
	public void changeNameBadTest() throws SQLException, BadArgumentException, 
		BadStateException {
		ManagerStockedItemsScreen test = new ManagerStockedItemsScreen(
			DatabaseLayer.getInstance().getFoodItemsAll() );
		int id = test.addItem("TWINKIES", 50, 9000);
		boolean result = test.changeItemName( id, null );
		Assert.assertFalse( result );
	}

	@Test
	public void changePriceTest() throws SQLException, BadArgumentException, 
		BadStateException {
		ManagerStockedItemsScreen test = new ManagerStockedItemsScreen(
			DatabaseLayer.getInstance().getFoodItemsAll() );
		int id = test.addItem("TWINKIES", 50, 9000);
		test.changeItemPrice( id, 900 );
		Assert.assertTrue( DatabaseLayer.getInstance().getFoodItemById( 
			id ).getPrice() == 900 );
	}

	@Test
	public void changePriceBadTest() throws SQLException, BadArgumentException, 
		BadStateException {
		ManagerStockedItemsScreen test = new ManagerStockedItemsScreen(
			DatabaseLayer.getInstance().getFoodItemsAll() );
		int id = test.addItem("TWINKIES", 50, 9000);
		boolean result = test.changeItemPrice( id, -1 );
		Assert.assertFalse( result );
	}

	@Test
	public void changeFreshTest() throws SQLException, BadArgumentException, 
		BadStateException {
		ManagerStockedItemsScreen test = new ManagerStockedItemsScreen(
			DatabaseLayer.getInstance().getFoodItemsAll() );
		int id = test.addItem("TWINKIES", 50, 9000);
		test.changeItemFreshLength( id, 900 );
		Assert.assertTrue( DatabaseLayer.getInstance().getFoodItemById( 
			id ).getFreshLength() == 900 );
	}

	@Test
	public void changeFreshTestBad() throws SQLException, BadArgumentException, 
		BadStateException {
		ManagerStockedItemsScreen test = new ManagerStockedItemsScreen(
			DatabaseLayer.getInstance().getFoodItemsAll() );
		int id = test.addItem("TWINKIES", 50, 9000);
		boolean result = test.changeItemFreshLength( id, -1 );
		Assert.assertFalse( result );
	}
}
