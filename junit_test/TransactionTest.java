import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.GregorianCalendar;

/**
 * Unit test suite for <tt>Transaction</tt>.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
@RunWith(JUnit4.class)
public class TransactionTest {
	@Test
	public void testNormalConstruction() throws BadArgumentException {
		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(4, 2);

		Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);

		Assert.assertTrue(transaction.getTimestamp() == calendar);
		Assert.assertTrue(transaction.getMachine() == machine);
		Assert.assertTrue(transaction.getCustomer() == customer);
		Assert.assertTrue(transaction.getProduct() == product);
		Assert.assertTrue(transaction.getRow() == whichRow);
		Assert.assertTrue(transaction.getBalance() == product.getPrice());
	}

	@Test
	public void testFullConstruction() throws BadArgumentException
	{
		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(4, 2);

		Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow, 14);

		Assert.assertTrue(transaction.getTimestamp() == calendar);
		Assert.assertTrue(transaction.getMachine() == machine);
		Assert.assertTrue(transaction.getCustomer() == customer);
		Assert.assertTrue(transaction.getProduct() == product);
		Assert.assertTrue(transaction.getRow() == whichRow);
		Assert.assertTrue(transaction.getBalance() == 14);
	}

	@Test
	public void testCopyConstruction() throws BadArgumentException {
		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		Location loc = new Location(14586, "New York", new String[] {"A", "B", "C"});
		VendingMachine machine = new VendingMachine(loc, 1, layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(4, 2);

		Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);

		Transaction tCopy = new Transaction(transaction);

		Assert.assertTrue(transaction.equals(tCopy));
		Assert.assertFalse(transaction == tCopy);
	}

	@Test
	public void testNullTimestampConstruction() throws BadArgumentException {
		boolean testFailed = false;

		GregorianCalendar calendar = null;
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(4, 2);

		try {
			Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}

	@Test
	public void testNullMachineConstruction() throws BadArgumentException {
		boolean testFailed = false;

		GregorianCalendar calendar = new GregorianCalendar();
		VendingMachine machine = null;
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(4, 2);

		try {
			Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}

	@Test
	public void testNullCustomerConstruction() throws BadArgumentException {
		boolean testFailed = false;

		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = null;
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(4, 2);

		try {
			Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}

	@Test
	public void testNullRowConstruction() throws BadArgumentException {
		boolean testFailed = false;

		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = null;

		try {
			Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}

	@Test
	public void testNullRowCoordinateConstruction() throws BadArgumentException {
		boolean testFailed = false;

		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(null, 2);

		try {
			Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}

	@Test
	public void testNegativeRowCoordinateConstruction() throws BadArgumentException {
		boolean testFailed = false;

		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(-4, 2);

		try {
			Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}

	@Test
	public void testOtherNegativeRowCoordinateConstruction() throws BadArgumentException {
		boolean testFailed = false;

		GregorianCalendar calendar = new GregorianCalendar();
		VMLayout layout = new VMLayout(3, 3, 3);
		VendingMachine machine = new VendingMachine(new Location(14586, "New York", new String[] {"A", "B", "C"}),
													1,
													layout);
		Customer customer = new Customer("Krutz", 512);
		FoodItem product = new FoodItem("Pasta", 3, 128);
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(2, -2);

		try {
			Transaction transaction = new Transaction(calendar, machine, customer, product, whichRow);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}
}
