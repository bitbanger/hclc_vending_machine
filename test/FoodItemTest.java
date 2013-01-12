import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.GregorianCalendar;

/**
 * Unit test suite for <tt>FoodItem</tt>.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
@RunWith(JUnit4.class)
public class FoodItemTest {
	@Test
	public void testNormalConstruction() throws BadArgumentException {
		FoodItem food = new FoodItem(
			"Condom",
			312,
			452
		);

		Assert.assertTrue(food.getName().equals("Condom"));
		Assert.assertTrue(food.getPrice() == 312);
		Assert.assertTrue(food.getFreshLength() == 452);
	}

	@Test
	public void testCopyConstruction() throws BadArgumentException {
		FoodItem f = new FoodItem("Condom", 312, 452);

		FoodItem fCopy = new FoodItem(f);

		Assert.assertTrue(f.equals(fCopy));
		Assert.assertFalse(f == fCopy);
	}

	@Test
	public void testNullName() {
		boolean testFailed = false;

		try {
			FoodItem f = new FoodItem(null, 312, 452);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}

	@Test
	public void testNegativePrice() {
		boolean testFailed = false;

		try {
			FoodItem t = new FoodItem("Condom", -4, 452);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}

	@Test
	public void testZeroFreshLength() {
		boolean testFailed = false;

		try {
			FoodItem t = new FoodItem("Condom", 312, 0);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}

	@Test
	public void testNegativeFreshLength() {
		boolean testFailed = false;

		try {
			FoodItem t = new FoodItem("Condom", 312, -4);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}
}
