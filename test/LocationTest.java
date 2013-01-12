import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.GregorianCalendar;

/**
 * Unit test suite for <tt>Location</tt>.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
@RunWith(JUnit4.class)
public class LocationTest {
	@Test
	public void testNormalConstruction() throws BadArgumentException {
		String[] nearby = {"Dave's", "Joe's", "Jack's"};

		Location loc = new Location(
			54321,
			"New York",
			nearby
		);

		Assert.assertTrue(loc.getZipCode() == 54321);
		Assert.assertTrue(loc.getState().equals("New York"));
		Assert.assertTrue(loc.getNearbyBusinesses()[1].equals("Joe's"));
	}

	@Test
	public void testCopyConstruction() throws BadArgumentException {
		String[] nearby = {"Dave's", "Joe's", "Jack's"};

		Location loc = new Location(14586, "New York", nearby);

		Location locCopy = new Location(loc);

		Assert.assertTrue(loc.equals(locCopy));
		Assert.assertFalse(loc == locCopy);
	}

	@Test
	public void testNegativeZipCode() {
		boolean testFailed = false;

		String[] nearby = {"Dave's", "Joe's", "Jack's"};

		try {
			Location t = new Location(-6, "New York", nearby);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}

	@Test
	public void testBadZipCodeSet() {
		boolean testFailed = false;

		String[] nearby = {"Dave's", "Joe's", "Jack's"};

		try {
			Location loc = new Location(14586, "New York", nearby);
			loc.setZipCode(-4);
		} catch(BadArgumentException e) {
			testFailed = true;
		}

		Assert.assertTrue(testFailed);
	}

	@Test
	public void testNoStateGiven() {
		boolean testFailed = false;

		String[] nearby = {"Dave's", "Joe's", "Jack's"};

		try {
			Location t = new Location(14586, null, nearby);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}

	@Test
	public void testNoNearbyBusinessesGiven() {
		boolean testFailed = false;

		try {
			Location t = new Location(14586, "New York", null);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}
}
