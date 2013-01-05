import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.sql.SQLException;

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
	 * Set of items to use for tests
	 **/
	private ArrayList<FoodItem> items;

	/**
	 * Clears the database and initializes test objects for each test
	 **/
	@Before
	public void setUp() throws SQLException
	{
		dbl = DatabaseLayer.getInstance();
		dbl.nuke();
		initFoodItems();
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
	 * Checks if two FoodItems are the same
	 * @param item1 The first item
	 * @param item2 The second item
	 * @return True iff item1 is the same as item2
	 **/
	private boolean foodItemEquals(FoodItem item1, FoodItem item2)
	{
		return item1.getId() == item2.getId() && 
			item1.getName().equals(item2.getName()) &&
			item1.getPrice() == item2.getPrice() &&
			item1.getFreshLength() == item2.getFreshLength();
	}

	/**
	 * Tests adding FoodItems to the database
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
			assertTrue(foodItemEquals(test, item));
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
			assertTrue(item != null);
			assertTrue(foodItemEquals(test, item));
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
		assertTrue(foodItemEquals(test, change));

		change = items.get(1);
		change.setPrice(300);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		assertTrue(foodItemEquals(test, change));

		change = items.get(2);
		change.setFreshLength(12345);
		dbl.updateOrCreateFoodItem(change);
		test = dbl.getFoodItemById(change.getId());
		assertTrue(foodItemEquals(test,change));
	}
}
