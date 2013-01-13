import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test suite for the <tt>ModelBase</tt> class.
 * @author Sol Boucher <slb1566@rit.edu>
 */
@RunWith(JUnit4.class)
public class ModelBaseTest
{
	@Test(expected=BadStateException.class)
	public void testPlainConstructor() throws BadStateException
	{
		ReBase firstBase=new ReBase();
		assertTrue(firstBase.isTempId());
		firstBase.getId();
	}

	@Test(expected=BadStateException.class)
	public void testFreshCopyConstructor() throws BadStateException
	{
		ReBase firstBase=new ReBase();
		assertTrue(firstBase.isTempId());
		ReBase secondBase=new ReBase(firstBase);
		assertTrue(secondBase.isTempId());
		assertEquals(firstBase, secondBase);
		assertNotSame(firstBase, secondBase);
		secondBase.getId();
	}

	@Test
	public void testUsedCopyConstructor() throws BadArgumentException, BadStateException
	{
		ReBase secondBase=new ReBase();
		secondBase.setId(19);
		ReBase thirdBase=new ReBase(secondBase);
		assertEquals(thirdBase.getId(), 19);
		assertEquals(secondBase, thirdBase);
		assertNotSame(secondBase, thirdBase);
	}

	@Test(expected=BadArgumentException.class)
	public void testInvalidSetId() throws BadArgumentException, BadStateException
	{
		ReBase thirdBase=new ReBase();
		thirdBase.setId(-1);
	}

	@Test
	public void testGoodSetId() throws BadArgumentException, BadStateException
	{
		ReBase homePlate=new ReBase();
		assertTrue(homePlate.isTempId());
		homePlate.setId(21);
		assertFalse(homePlate.isTempId());
		assertEquals(homePlate.getId(), 21);
	}

	@Test(expected=BadStateException.class)
	public void testDoubleSetId() throws BadArgumentException, BadStateException
	{
		ReBase homePlate=new ReBase();
		assertTrue(homePlate.isTempId());
		homePlate.setId(21);
		assertFalse(homePlate.isTempId());
		assertEquals(homePlate.getId(), 21);
		homePlate.setId(20); //failed to make it to home
	}
}

class ReBase extends ModelBase
{
	public ReBase()
	{
		super();
	}

	public ReBase(ReBase o)
	{
		super(o);
	}
}
