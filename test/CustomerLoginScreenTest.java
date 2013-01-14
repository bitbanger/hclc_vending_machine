import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.sql.SQLException;

/**
 * Unit test suite for <tt>CustomerLoginScreen</tt>
 * @author Piper Chester <pwc1203@rit.edu>
 * @contributors Kyle Savarese <kms7341@rit.edu>
 */
@RunWith(JUnit4.class)

public class CustomerLoginScreenTest {
		
	/** The Database instance */
	private DatabaseLayer db = DatabaseLayer.getInstance();
	
	/** The VendingMachine instance */
	private TestUtilities helper;

	@Before
	public void setUp() throws BadArgumentException, BadStateException,
		SQLException {
		helper = new TestUtilities( true );
	}

	@Test(expected = InstantiationException.class)
	public void instantiationTestInactive() throws InstantiationException {
		VendingMachine vm = helper.machines.get(0);
		vm.makeActive(false);
		CustomerLoginScreen test = new CustomerLoginScreen( vm );
	}

	@Test(expected = InstantiationException.class)
	public void instantiationTestNull() throws InstantiationException {
		CustomerLoginScreen test = new CustomerLoginScreen( null );
	}

	@Test
	public void loginTest() throws BadStateException, InstantiationException {
		int id = helper.customers.get(0).getId();
		CustomerLoginScreen cur = new CustomerLoginScreen( helper.machines.get(0) );
		CustomerPurchaseScreen next = cur.tryLogin( id );
		Assert.assertTrue( next != null );
	}

	@Test
	public void loginTestFail() throws InstantiationException {
		CustomerLoginScreen cur = new CustomerLoginScreen( helper.machines.get(0) );
		CustomerPurchaseScreen next = cur.tryLogin( -1 );
		Assert.assertTrue( next == null );
	}

	@Test 
	public void cashLoginTest() throws InstantiationException {
		CustomerLoginScreen cur = new CustomerLoginScreen( helper.machines.get(0) );
		CashCustomerPurchaseScreen next = cur.cashLogin();
		Assert.assertTrue( next != null );
	}

	@Test
	public void testBuild() throws InstantiationException, BadArgumentException,
		BadStateException {
		CustomerLoginScreen cur = CustomerLoginScreen.buildInstance( 
			helper.machines.get(0).getId() );
		Assert.assertTrue( cur != null );
	}

	@Test
	public void testBuildFail() throws InstantiationException {
		CustomerLoginScreen cur = CustomerLoginScreen.buildInstance(-1);
		Assert.assertTrue( cur == null );
	}

}
