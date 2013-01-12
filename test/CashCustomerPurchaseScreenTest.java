import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test suite for <tt>Customer</tt>
 * @author Piper Chester <pwc1203@rit.edu>
 */
@RunWith(JUnit4.class)
public class CashCustomerPurchaseScreenTest {
	
	/** The VendingMachine instance */
	private VendingMachine vm;

	/** The Customer instance */
	private Customer user;
	
	@Test
	public void testNormalConstruction() {
		CashCustomerPurchaseScreen cashCustPurchScreen = new CashCustomerPurchaseScreen(user, vm);
	}

	@Test
	public void testNegativeCash() {
		boolean testFailed = false;

		int cash;

		CashCustomerPurchaseScreen cashCustPurchScreen = new CashCustomerPurchaseScreen(user, vm);
	
		if (!cashCustPurchScreen.addCash(-1)){
			testFailed = true;
			Assert.assertTrue(testFailed);
		}
	}	
}
