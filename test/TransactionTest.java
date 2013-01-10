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
	public void testNormalConstruction() {
		Pair<Integer, Integer> whichRow = new Pair<Integer, Integer>(4, 2);
		Transaction t = new Transaction(
			123,
			new GregorianCalendar(),
			null,
			null,
			null,
			whichRow
		);

		Assert.assertTrue(t.getId() == 123);
		Assert.assertTrue(t.getRow().first.equals(whichRow.first));
		Assert.assertTrue(t.getRow().second.equals(whichRow.second));
	}

	@Test
	public void testCopyConstruction() {
		Transaction t = new Transaction(42, new GregorianCalendar(), null, null, null, new Pair<Integer, Integer>(4, 2));

		Transaction tCopy = new Transaction(t);

		Assert.assertTrue(t.equals(tCopy));
		Assert.assertFalse(t == tCopy);
	}

	@Test
	public void testBadRowConstruction() {
		Pair<Integer, Integer> badRow = new Pair<Integer, Integer>(-4, -2);

		boolean testFailed = false;

		try {
			Transaction t = new Transaction(123, new GregorianCalendar(), null, null, null, badRow);
		} catch(IllegalArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}
}
