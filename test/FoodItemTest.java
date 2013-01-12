import static org.junit.Assert.*;
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
	private TestUtilitiesSimple util=null;

	private void noTestReadyUtilities()
	{
		if(util==null)
			try
			{
				util=new TestUtilitiesSimple();
			}
			catch(Exception checked)
			{
				throw new IllegalStateException("Failed initializing test utilities");
			}
	}

	@Test
	public void testNormalConstruction() throws BadArgumentException {
		FoodItem food = new FoodItem(
			"Condom",
			312,
			452
		);

		assertTrue(food.getName().equals("Condom"));
		assertTrue(food.getPrice() == 312);
		assertTrue(food.getFreshLength() == 452);
	}

	@Test
	public void testFullConstruction() throws BadArgumentException {
		FoodItem food = new FoodItem(
			"Condom",
			312,
			452,
			false
		);

		assertTrue(food.getName().equals("Condom"));
		assertTrue(food.getPrice() == 312);
		assertTrue(food.getFreshLength() == 452);
		assertFalse(food.isActive());
	}

	@Test
	public void testCopyConstruction() throws BadArgumentException {
		FoodItem f = new FoodItem("Condom", 312, 452);

		FoodItem fCopy = new FoodItem(f);

		assertTrue(f.equals(fCopy));
		assertFalse(f == fCopy);
	}

	@Test
	public void testNullName() {
		boolean testFailed = false;

		try {
			FoodItem f = new FoodItem(null, 312, 452);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			assertTrue(testFailed);
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
			assertTrue(testFailed);
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
			assertTrue(testFailed);
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
			assertTrue(testFailed);
		}
	}

	@Test
	public void testMakeActive()
	{
		noTestReadyUtilities();
		FoodItem noop=util.items.get(0), change=util.items.get(3);
		FoodItem noCop=new FoodItem(noop), chgCopy=new FoodItem(change);
		
		noop.makeActive(true);
		assertTrue(noop.isActive());
		assertTrue(noCop.isActive());
		
		change.makeActive(false);
		assertFalse(change.isActive());
		assertTrue(chgCopy.isActive());
	}

	@Test
	public void testSetFreshLength() throws BadArgumentException
	{
		noTestReadyUtilities();
		FoodItem noop=util.items.get(0), change=util.items.get(3);
		FoodItem noCop=new FoodItem(noop), chgCopy=new FoodItem(change);
		
		long old=noop.getFreshLength();
		noop.setFreshLength(old);
		assertEquals(old, noop.getFreshLength());
		assertEquals(old, noCop.getFreshLength());
		
		old=chgCopy.getFreshLength();
		long newLength=128;
		change.setFreshLength(newLength);
		assertEquals(newLength, change.getFreshLength());
		assertEquals(old, chgCopy.getFreshLength());
	}

	@Test
	public void testSetName() throws BadArgumentException
	{
		noTestReadyUtilities();
		FoodItem noop=util.items.get(0), change=util.items.get(3);
		FoodItem noCop=new FoodItem(noop), chgCopy=new FoodItem(change);
		
		String old=noop.getName();
		noop.setName(old);
		assertEquals(old, noop.getName());
		assertEquals(old, noCop.getName());
		
		old=chgCopy.getName();
		String newt="Newt Gingrich";
		change.setName(newt);
		assertEquals(newt, change.getName());
		assertEquals(old, chgCopy.getName());
	}

	@Test
	public void testSetPrice() throws BadArgumentException
	{
		noTestReadyUtilities();
		FoodItem noop=util.items.get(0), change=util.items.get(3);
		FoodItem noCop=new FoodItem(noop), chgCopy=new FoodItem(change);
		
		int old=noop.getPrice();
		noop.setPrice(old);
		assertEquals(old, noop.getPrice());
		assertEquals(old, noCop.getPrice());
		
		old=chgCopy.getPrice();
		int tooMuch=58;
		change.setPrice(tooMuch);
		assertEquals(tooMuch, change.getPrice());
		assertEquals(old, chgCopy.getPrice());
	}
}
