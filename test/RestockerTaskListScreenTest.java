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
	public void testAssembler() throws BadArgumentException, SQLException {
		String[] busis = new String[1];
		busis[0] = "NOTHING";
		Location loc = new Location( 00000, "NJ", busis );
		Row[][] stuff = new Row[2][3];
		Row[][] newStuff = new Row[2][3];
		FoodItem first = new FoodItem( "first", 1, 2 );
		FoodItem second = new FoodItem( "second", 2, 3);
		ArrayList<String> counter = new ArrayList<String>();
		for ( int i = 0; i < 2; i++ ) {
			for ( int j = 0; j < 3; j++ ) {
				stuff[i][j] = new Row( first, 4, new GregorianCalendar() );
				newStuff[i][j] = new Row ( second, 4, new GregorianCalendar() );
				counter.add("Remove all from " + i + ", " + j);
				counter.add("Add 4 first to location " + i + ", " + j);
			}
		}
		VMLayout tester = new VMLayout( stuff, 4 );
		VMLayout newLayout = new VMLayout( newStuff, 4 );
		VendingMachine vm = new VendingMachine(loc, 2, tester);
		vm.setNextLayout( newLayout );
		RestockerTaskListScreen hope = new RestockerTaskListScreen( vm );
		String[] bla = hope.assembleStockingList();
		ArrayList<String> inst = new ArrayList<String>();
		for ( String in : bla ) {
			inst.add( in );
		}
		Assert.assertTrue( inst.equals( counter ) );
	}

	@Test
	public void testStockingComplete() throws BadArgumentException, SQLException{
		String[] busis = new String[1];
		busis[0] = "NOTHING";
		Location loc = new Location( 00000, "NJ", busis );
		Row[][] stuff = new Row[2][3];
		Row[][] newStuff = new Row[2][3];
		FoodItem first = new FoodItem( "first", 1, 2 );
		FoodItem second = new FoodItem( "second", 2, 3);
		for ( int i = 0; i < 2; i++ ) {
			for ( int j = 0; j < 3; j++ ) {
				stuff[i][j] = new Row( first, 4, new GregorianCalendar() );
				newStuff[i][j] = new Row ( second, 4, new GregorianCalendar() );
			}
		}
		VMLayout tester = new VMLayout( stuff, 4 );
		VMLayout newLayout = new VMLayout( newStuff, 4 );
		VendingMachine vm = new VendingMachine(loc, 2, tester);
		vm.setNextLayout( newLayout );
		RestockerTaskListScreen hope = new RestockerTaskListScreen( vm );
		hope.completeStocking();
		Assert.assertTrue( vm.getCurrentLayout().equals( vm.getNextLayout() ) );
	}
}
