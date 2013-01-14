import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.*;

/**
 * Test suite for ManagerMachineManagementScreen
 * @author Kyle Savarese <kms7341@rit.edu>
 */

@RunWith(JUnit4.class)

public class ManagerMachineManagementScreenTest {

	@Test
	public void listLocTest() throws SQLException, BadArgumentException,
		BadStateException {
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		Location loc = vms.get(1).getLocation();
		VendingMachine found = vms.get(1);
		ArrayList<VendingMachine> comp = new ArrayList<VendingMachine>(
			test.listMachinesByLocation( loc ) );
		vms = new ArrayList<VendingMachine>();
		vms.add(found);
		Assert.assertTrue( vms.equals( comp ) );
	}

	@Test(expected=NullPointerException.class)
	public void listLocTestBad() throws SQLException, BadArgumentException,
		BadStateException {
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		ArrayList<VendingMachine> comp = new ArrayList<VendingMachine>(
			test.listMachinesByLocation( null ) );
	}

	@Test
	public void addMachineTest() throws SQLException, BadArgumentException,
		BadStateException {	
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		Location loc = vms.get(1).getLocation();
		VMLayout cur = vms.get(0).getCurrentLayout();
		int id = test.addMachine( loc.getZipCode(), loc.getState(), loc.getNearbyBusinesses(), 5, cur );
		Assert.assertTrue( DatabaseLayer.getInstance().
			getVendingMachineById( id ) != null );
	}

	@Test
	public void addMachineTestBad() throws SQLException, BadArgumentException,
		BadStateException {	
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		Location loc = vms.get(1).getLocation();
		VMLayout cur = vms.get(0).getCurrentLayout();
		int id = test.addMachine( loc.getZipCode(), loc.getState(), loc.getNearbyBusinesses(), -1, cur );
		Assert.assertTrue( id == -1 );
	}

	@Test
	public void activateTests() throws SQLException, BadArgumentException,
		BadStateException {
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		int id = vms.get(0).getId();
		test.deactivateMachine( vms.get(0) );
		Assert.assertFalse( DatabaseLayer.getInstance().
			getVendingMachineById( id ).isActive() );
		test.reactivateMachine( vms.get(0) );
		Assert.assertTrue( DatabaseLayer.getInstance().
			getVendingMachineById( id ).isActive() );
	}

	@Test
	public void changeLocTest() throws SQLException, BadArgumentException,
		BadStateException {	
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		Location loc = vms.get(1).getLocation();
		test.changeMachineLocation( vms.get(0), loc.getZipCode(), loc.getState(), loc.getNearbyBusinesses() );
		Location test1 = DatabaseLayer.getInstance().getVendingMachineById(vms.get(0).getId()).getLocation();
		Assert.assertTrue(test1.getZipCode() == vms.get(1).getLocation().getZipCode());
		Assert.assertTrue(test1.getState().equals(vms.get(1).getLocation().getState()));
		String[] arr1 = test1.getNearbyBusinesses();
		String[] arr2 = vms.get(1).getLocation().getNearbyBusinesses();
		big: for (String t : arr1)
		{
			for (String s : arr2)
			{
				if (t.equals(s))
					continue big;
			}
			Assert.assertTrue(false);
		}
	}

	/*@Test
	public void changeLocTestBad() throws SQLException, BadArgumentException,
		BadStateException {	
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		boolean result = test.changeMachineLocation( vms.get(0).getId(), null );
		Assert.assertTrue( result == false );
	}*/
}
