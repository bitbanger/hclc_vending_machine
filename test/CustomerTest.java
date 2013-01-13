import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.GregorianCalendar;

/**
 * Unit test suite for <tt>Customer</tt>.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
@RunWith(JUnit4.class)
public class CustomerTest {
	@Test
	public void testNormalConstruction() throws BadArgumentException {
		Customer krutz = new Customer("Krutz", 512);

		assertFalse(krutz.isCashCustomer());
		assertTrue(krutz.getName().equals("Krutz"));
		assertTrue(krutz.getMoney() == 512);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testCashCustomerConstruction() throws BadArgumentException, BadStateException
	{
		Customer moneyBags=new Customer();
		
		assertTrue(moneyBags.isCashCustomer());
		assertEquals(moneyBags.getMoney(), 0);
		moneyBags.getId();
	}

	@Test
	public void testCopyConstruction() throws BadArgumentException {
		Customer krutz = new Customer("Krutz", 512);
		Customer kCopy = new Customer(krutz);

		assertTrue(krutz.equals(kCopy));
		assertFalse(krutz == kCopy);
	}

	@Test
	public void testSetName() throws BadArgumentException
	{
		Customer dentist=new Customer("Lemming", 324);
		
		dentist.setName("Stapleton");
		assertEquals(dentist.getName(), "Stapleton");
	}

	@Test
	public void testSetEmptyName() throws BadArgumentException
	{
		Customer dentist=new Customer("Lemming", 324);
		
		dentist.setName("");
		assertEquals(dentist.getName(), "");
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNullName() throws BadArgumentException
	{
		Customer dentist=new Customer("Lemming", 324);
		
		dentist.setName(null);
		assertEquals(dentist.getName(), null);
	}

	@Test
	public void testNegativeMoney() {
		boolean testFailed = false;

		try {
			Customer c = new Customer("Krutz", -5);
		} catch(BadArgumentException e) {
			testFailed = true;
		} finally {
			assertTrue(testFailed);
		}
	}

	@Test
	public void testZeroMoney() throws BadArgumentException
	{
		Customer c=new Customer("Nobody", 0);
		
		assertEquals(c.getMoney(), 0);
	}

	@Test
	public void testSetMoney() throws BadArgumentException
	{
		Customer c=new Customer("Bilbo", 1234);
		
		c.setMoney(4567); //found the ring
		assertEquals(c.getMoney(), 4567);
	}

	@Test
	public void testSetZeroMoney() throws BadArgumentException
	{
		Customer c=new Customer("Bilbo", 1234);
		
		c.setMoney(0); //found the ring
		assertEquals(c.getMoney(), 0);
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNegativeMoney() throws BadArgumentException
	{
		Customer c=new Customer("Bilbo", 1234);
		
		c.setMoney(-999); //found the ring
	}

	@Test
	public void testDeductMoney() throws BadArgumentException
	{
		Customer c=new Customer("Bilbo", 1234);
		
		c.deductMoney(17); //found the ring
		assertEquals(c.getMoney(), 1217);
	}

	@Test
	public void testDeductZeroMoney() throws BadArgumentException
	{
		Customer c=new Customer("Bilbo", 1234);
		
		c.deductMoney(0); //found the ring
		assertEquals(c.getMoney(), 1234);
	}

	@Test
	public void testDeductNegativeMoney() throws BadArgumentException
	{
		Customer c=new Customer("Bilbo", 1234);
		
		c.deductMoney(-6); //found the ring
		assertEquals(c.getMoney(), 1240);
	}
}
