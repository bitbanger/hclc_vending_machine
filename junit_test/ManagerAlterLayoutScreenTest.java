import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Collection;
import java.util.LinkedList;

public class ManagerAlterLayoutScreenTest
{
	/**
	 * Holds fake data used in tests
	 **/
	private TestUtilities testUtil;

	/**
	 * Database driver
	 **/
	private DatabaseLayer dbl;

	/**
	 * Runs before each of the following tests. Does the following:
	 * 1. Gets the instance of the DatabaseLayer
	 * 2. Nukes the database
	 * 3. Creates a new instance of the TestUtilities class that gets the data
	 * and adds it to the database.
	 * 4. Sets machine to the first vending machine generated by testUtil.
	 **/
	@Before
	public void ManagerAlterLayoutScreenTest() throws SQLException, BadStateException, BadArgumentException
	{
		dbl = DatabaseLayer.getInstance();
		dbl.nuke();
		testUtil = new TestUtilities(true);
	}

	/**
	 * Tests the constuctor
	 **/
	@Test
	public void testContructor()
	{
		ManagerAlterLayoutScreen test = new ManagerAlterLayoutScreen();
	}
	
	/**
	 * Tests listRows
	 **/
	@Test
	public void listRowsTest() throws BadStateException, BadArgumentException
	{
		ManagerAlterLayoutScreen test = new ManagerAlterLayoutScreen();
		FoodItem[][] test1 = test.listRows();
		Row[][] rows = testUtil.machines.get(0).getNextLayout().getRows();
		assertTrue(test1.length == rows.length);
		for (int i=0;i<test1.length;++i)
		{
			FoodItem[] testRow = test1[i];
			Row[] compareRow = rows[i];
			assertTrue(testRow.length == compareRow.length);
			for (int j=0;j<testRow.length;++j)
			{
				FoodItem testItem = testRow[j];
				FoodItem compareItem = compareRow[j].getProduct();
				TestUtilities.foodItemEquals(testItem, compareItem);
			}
		}
	}
	
	@Test
	public void listItemsTest() throws BadStateException, BadArgumentException,
		SQLException {
		ManagerAlterLayoutScreen test = new ManagerAlterLayoutScreen();
		assertTrue( dbl.getFoodItemsAll().equals( test.listItems() ) );
	}

	@Test
	public void queueRowChangeTestSuccess() throws BadStateException, BadArgumentException {
		ManagerAlterLayoutScreen test = new ManagerAlterLayoutScreen();
		FoodItem[][] layout = test.listRows();
		FoodItem next = new FoodItem( "Twinkies", 387, Integer.MAX_VALUE );
		assertTrue( test.queueRowChange( new 
			Pair<Integer, Integer>( 0, 0 ), next ) == 0 );
	}

	@Test
	public void queueRowChangeToSoon() throws BadStateException, BadArgumentException {
		ManagerAlterLayoutScreen test = new ManagerAlterLayoutScreen();
		FoodItem[][] layout = test.listRows();
		FoodItem next = new FoodItem( "Twinkies", 387, 1 );
		assertTrue( test.queueRowChange( new 
			Pair<Integer, Integer>( 0, 0 ), next ) == 1);
	}

	@Test
	public void commitChangesTestSuccess() throws BadStateException, BadArgumentException {
		ManagerAlterLayoutScreen test = new ManagerAlterLayoutScreen();
		FoodItem[][] layout = test.listRows();
		FoodItem next = new FoodItem( "Twinkies", 387, 900000 );
		test.queueRowChange( new Pair<Integer, Integer>( 0, 0 ), next );
		assertTrue( test.commitRowChanges() );
	}
}