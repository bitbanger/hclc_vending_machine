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

	@Test
	public void addMachineTest() throws SQLException, BadArgumentException,
		BadStateException {	
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		Location loc = vms.get(1).getLocation();
		VMLayout cur = vms.get(0).getCurrentLayout();
		int id = test.addMachine( loc, 5, cur );
		Assert.assertTrue( DatabaseLayer.getInstance().
			getVendingMachineById( id ) != null );
	}

	@Test
	public void activateTests() throws SQLException, BadArgumentException,
		BadStateException {
		TestUtilities helper = new TestUtilities( true );
		ArrayList<VendingMachine> vms = helper.machines;
		ManagerMachineManagementScreen test = new ManagerMachineManagementScreen( vms );
		int id = vms.get(0).getId();
		test.deactivateMachine( id );
		Assert.assertFalse( DatabaseLayer.getInstance().
			getVendingMachineById( id ).isActive() );
		test.reactivateMachine( id );
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
		test.changeMachineLocation( vms.get(0).getId(), loc );
		Assert.assertTrue( DatabaseLayer.getInstance().getVendingMachineById( 
			vms.get(0).getId() ).getLocation().equals( loc ) );
	}
}
