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
	 * Clears the database and initializes test objects for each test
	 **/
	@Before
	public void setUp() throws SQLException
	{
		dbl = DatabaseLayer.getInstance();
		dbl.nuke();
		initFoodItems();
		initVendingMachines();
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
		assertTrue(rows1.length == rows2.length);
		
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
				assertTrue(row1.getExpirationDate().equals(row2.getExpirationDate()));
				assertTrue(row1.getRemainingQuantity() == row2.getRemainingQuantity());
			}
		}
	}

	/**
	 * Adds FoodItems to the database without testing. Used in several tests.
	 **/
	private void noTestAddFoodItems() throws SQLException
	{
		for (FoodItem item : items)
		{
			dbl.updateOrCreateFoodItem(item);
			assertTrue(!item.isTempId());
		}
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
}
