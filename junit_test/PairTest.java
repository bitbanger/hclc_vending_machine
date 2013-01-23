import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.GregorianCalendar;

/**
 * Unit test suite for <tt>Pair</tt>.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
@RunWith(JUnit4.class)
public class PairTest {
	@Test
	public void testBlankConstruction() {
		Pair<Integer, Integer> blank = new Pair<Integer, Integer>();

		Assert.assertTrue(blank.first == null);
		Assert.assertTrue(blank.second == null);
	}
	@Test
	public void testNormalConstruction() {
		Pair<Integer, Integer> pear = new Pair<Integer, Integer>(5, 6);

		Assert.assertTrue(pear.first == 5);
		Assert.assertTrue(pear.second == 6);
	}

	@Test
	public void testCopyConstruction() {
		Pair<Integer, Integer> pear = new Pair<Integer, Integer>(5, 6);
		Pair<Integer, Integer> pCopy = new Pair<Integer, Integer>(pear);

		Assert.assertTrue(pear.equals(pCopy));
		Assert.assertFalse(pear == pCopy);
	}
}
