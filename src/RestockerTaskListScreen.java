import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * 1/1/2013
 * TaskListScreen for restockers
 * 
 * @author Kyle Savarese
 */

import java.util.Collection;
import java.util.GregorianCalendar;

public class RestockerTaskListScreen {

	/** The Database */
	private static DatabaseLayer db=DatabaseLayer.getInstance();

	/** the vending machine the restocker is working on */
	private VendingMachine vm;

	/**
	 * Constructor
	 * Creates an instance with the specified VendingMachine
	 * @param cur the machine the restocker is using
	 */	
	public RestockerTaskListScreen ( VendingMachine cur ) throws SQLException {
		vm = cur;
	}

	/**
	 * creates a list of instructions for the restocker to follow
	 * compares the next layout and the current layout to determine 
	 *	the changes that need to be made
	 * @return String[]: list of instructions
	 */
	public String[] assembleStockingList() {
		ArrayList<String> instructions = new ArrayList<String>();
		if ( vm.getNextLayout() == null )
			//Need to discuss what to do here
			return null; //TODO return something useful
		Row[][] cur = vm.getCurrentLayout().getRows();
		Row[][] next = vm.getNextLayout().getRows();
		for ( int i = 0; i < cur.length; i++ ) {
			for ( int j = 0; j < cur[i].length; j++ ) {
				Row items = cur[i][j];
				Row nextItems = next[i][j];
				GregorianCalendar exp = items.getExpirationDate();
				//TODO check if item expires before next visit
				GregorianCalendar nextVisit = (GregorianCalendar)exp.clone();
				exp.roll(GregorianCalendar.MONTH, 1);
				//don't have any data for when that is, assuming 1 month
				if ( exp.before( nextVisit ) || 
					items.getProduct().getName().equals(
					nextItems.getProduct().getName() ) )
					instructions.add("Remove all from " +
						i + ", " + j);
				instructions.add("Add " + nextItems.getRemainingQuantity()
					+ " " + nextItems.getProduct().getName() 
					+ " to location " + i + ", " + j);
			}
		}
		return instructions.toArray(new String[0]);
	}

	/**
	 * updates the necessary machinery that the stocking is complete
	 */
	public void completeStocking() throws SQLException {
		vm.swapInNextLayout();
		db.updateOrCreateVendingMachine( vm );
	}
}
