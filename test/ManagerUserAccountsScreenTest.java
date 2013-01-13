import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.*;

/**
 * Test suite for ManagerUserAccountsScreen
 * @author Kyle Savarese
 *
 */
@RunWith(JUnit4.class)

public class ManagerUserAccountsScreenTest {

	@Test
	public void listCustomersTest() throws SQLException, BadStateException,
		BadArgumentException {
		ManagerUserAccountsScreen test = new ManagerUserAccountsScreen();
		Assert.assertTrue( test.listCustomers().equals( 
			DatabaseLayer.getInstance().getCustomersAll() ) );
	}

	@Test
	public void listManagersTest() throws SQLException, BadStateException, 
		BadArgumentException {
		ManagerUserAccountsScreen test = new ManagerUserAccountsScreen();
		Assert.assertTrue( test.listManagers().equals( 
			DatabaseLayer.getInstance().getManagersAll() ) );
	}

	@Test 
	public void addCustTest() throws SQLException, BadStateException, 
		BadArgumentException {
		ManagerUserAccountsScreen test = new ManagerUserAccountsScreen();
		int id = test.addCustomer("Krutz", 5);
		Assert.assertTrue( test.listCustomers().contains( 
			DatabaseLayer.getInstance().getCustomerById( id ) ) );
	}

	@Test 
	public void addManagerTest() throws SQLException, BadStateException, 
		BadArgumentException {
		ManagerUserAccountsScreen test = new ManagerUserAccountsScreen();
		int id = test.addManager("Krutz", "password");
		Assert.assertTrue( test.listManagers().contains( 
			DatabaseLayer.getInstance().getManagerById( id ) ) );
	}

	@Test 
	public void changePassTest() throws SQLException, BadStateException, 
		BadArgumentException {
		ManagerUserAccountsScreen test = new ManagerUserAccountsScreen();
		int id = test.addManager("Krutz", "password");
		test.changePassword(id, "password2");
		Assert.assertTrue( DatabaseLayer.getInstance().getManagerById( 
			id ).getPassword().equals("password2") );
	}

}
