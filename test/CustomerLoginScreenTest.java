import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.sql.SQLException;

/**
 * Unit test suite for <tt>CustomerLoginScreen</tt>
 * @author Piper Chester <pwc1203@rit.edu>
 */
@RunWith(JUnit4.class)

public class CustomerLoginScreenTest {
		
	/** The Database instance */
	private DatabaseLayer db = DatabaseLayer.getInstance();
	
	/** VendingMachine instance */
	private VendingMachine vm;

	@Test
	public void testNormalConstruction() {
		CustomerLoginScreen cls = new CustomerLoginScreen(vm);	
	}
	
	@Test
	public void testNegativeID() throws SQLException {
		boolean testFailed = false;
		
		CustomerLoginScreen custLogScreen = new CustomerLoginScreen(vm);
		if (custLogScreen.tryLogin(-1) == null){
			testFailed = true;
			Assert.assertTrue(testFailed);
		}
	}
}
