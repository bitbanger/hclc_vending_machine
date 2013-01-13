import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test suite for the <tt>Manager</tt> class.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class ManagerTest
{
	@Test
	public void testNormalConstruction() throws BadArgumentException
	{
		Manager woman=new Manager("Cruella DeVille", "sharedSecret");
		
		assertEquals(woman.getName(), "Cruella DeVille");
		assertEquals(woman.getPassword(), "sharedSecret");
	}

	@Test(expected=BadArgumentException.class)
	public void testNoNameConstruction() throws BadArgumentException
	{
		Manager woman=new Manager(null, "sharedSecret");
	}

	@Test(expected=BadArgumentException.class)
	public void testNoPasswordConstruction() throws BadArgumentException
	{
		Manager woman=new Manager("Cruella DeVille", null);
	}

	@Test
	public void testCopyConstruction() throws BadArgumentException
	{
		Manager woman=new Manager("Cruella DeVille", "sharedSecret");
		Manager wonderWoman=new Manager(woman);
		
		assertEquals(woman, wonderWoman);
		assertFalse(woman==wonderWoman);
	}

	@Test
	public void testSetName() throws BadArgumentException
	{
		Manager dentist=new Manager("LaFarge", "I hear the gooseberries are doing well this year... and so are the mangoes. / Mine aren't, but the Big Cheese gets his at low tide tonight.");
		
		dentist.setName("Van der Berg");
		assertEquals(dentist.getName(), "Van der Berg");
	}

	@Test
	public void testSetEmptyName() throws BadArgumentException
	{
		Manager dentist=new Manager("LaFarge", "I hear the gooseberries are doing well this year... and so are the mangoes. / Mine aren't, but the Big Cheese gets his at low tide tonight.");
		
		dentist.setName("");
		assertEquals(dentist.getName(), "");
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNullName() throws BadArgumentException
	{
		Manager dentist=new Manager("LaFarge", "I hear the gooseberries are doing well this year... and so are the mangoes. / Mine aren't, but the Big Cheese gets his at low tide tonight.");
		
		dentist.setName(null);
		assertEquals(dentist.getName(), null);
	}

	@Test
	public void testSetNormalPassword() throws BadArgumentException
	{
		Manager woman=new Manager("Cruella DeVille", "sharedSecret");
		
		woman.setPassword("letmein");
		assertEquals(woman.getPassword(), "letmein");
	}

	@Test
	public void testSetEmptyPassword() throws BadArgumentException
	{
		Manager woman=new Manager("Cruella DeVille", "sharedSecret");
		
		woman.setPassword("");
		assertEquals(woman.getPassword(), "");
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNullPassword() throws BadArgumentException
	{
		Manager woman=new Manager("Cruella DeVille", "sharedSecret");
		
		woman.setPassword(null);
	}

	@Test
	public void testComparePassword() throws BadArgumentException
	{
		Manager woman=new Manager("Cruella DeVille", "sharedSecret");
		
		assertFalse(woman.comparePassword("wrongAttempt"));
		assertTrue(woman.comparePassword("sharedSecret"));
		assertFalse(woman.comparePassword(""));
		assertFalse(woman.comparePassword(null));
		
		woman.setPassword("letmein");
		assertFalse(woman.comparePassword("sharedSecret"));
		assertTrue(woman.comparePassword("letmein"));
		
		woman.setPassword("");
		assertFalse(woman.comparePassword("letmein"));
		assertTrue(woman.comparePassword(""));
		assertFalse(woman.comparePassword(null));
	}
}
