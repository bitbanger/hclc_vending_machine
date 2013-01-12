import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.ArrayList;

/**
 * unit test suite for RestockerTaskListScreen
 * @author Kyle Savarese <kms7341@rit.edu>
 */
@RunWith(JUnit4.class)

public class RestockerTaskListScreenTest {

	/** The Database instance */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	@Test
	public void testAssembler() throws BadArgumentException, SQLException, BadStateException {
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help0 = helper.machines.get(0);
		VendingMachine help1 = helper.machines.get(1);
		VendingMachine vm = new VendingMachine( help0.getLocation(), 2, 
			help1.getCurrentLayout(), help1.getNextLayout(), true );
		
		ArrayList<String> counter = new ArrayList<String>();
		counter.add("Remove all from 0, 0");
		counter.add("Add 3 Twix to location 0, 0");
		counter.add("Remove all from 0, 1");
		counter.add("Add 4 Snickers to location 0, 1");
		counter.add("Remove all from 1, 0");
		counter.add("Add 6 Chips to location 1, 0");
		counter.add("Remove all from 1, 1");
		counter.add("Add 7 Fish Sandwich to location 1, 1");
		RestockerTaskListScreen hope = new RestockerTaskListScreen( vm );
		String[] bla = hope.assembleStockingList();
		ArrayList<String> inst = new ArrayList<String>();
		for ( String in : bla ) {
			inst.add( in );
		}
		Assert.assertTrue( inst.equals( counter ) );
	}

	@Test
	public void testStockingComplete() throws BadArgumentException, SQLException, BadStateException{
		TestUtilities helper = new TestUtilities( true );
		VendingMachine help0 = helper.machines.get(0);
		VendingMachine help1 = helper.machines.get(1);
		VendingMachine vm = new VendingMachine( help0.getLocation(), 2, 
			help1.getCurrentLayout(), help1.getNextLayout(), true );
		
		RestockerTaskListScreen hope = new RestockerTaskListScreen( vm );
		hope.completeStocking();
		Row[][] first = vm.getCurrentLayout().getRows();
		Row[][] second = vm.getNextLayout().getRows();
		for ( int i = 0; i < 2; i++ ) {
			for ( int j = 0; j < 2; j++ ) {
				Assert.assertTrue( first[i][j].equals( second[i][j] ) );
			}
		}
		Assert.assertTrue( vm.getCurrentLayout().getDepth() 
			== vm.getNextLayout().getDepth() );
	}
}
