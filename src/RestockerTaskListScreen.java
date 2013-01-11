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
		Row[][] cur = vm.getCurrentLayout().getRows();
		Row[][] next = vm.getNextLayout().getRows();
		if ( next == null ) {
		for ( int i = 0; i < cur.length; i++ ) {
			for ( int j = 0; j < cur[i].length; j++ ) {
				Row items = cur[i][j];
				GregorianCalendar exp = items.getExpirationDate();
				GregorianCalendar nextVisit = (GregorianCalendar)exp.clone();
				exp.roll(GregorianCalendar.DAY_OF_MONTH, 
					vm.getStockingInterval());
				if ( exp.before( nextVisit ) ) {
					instructions.add("Remove all from " +
						i + ", " + j);
				}
				if ( exp.before( nextVisit )  || 
					items.getRemainingQuantity() == 0 ) {
					instructions.add("Add " + vm.getNextLayout().getDepth()
						+ " " + items.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
			}	
		} // end for
		} // end if
		else {
		for ( int i = 0; i < cur.length; i++ ) {
			for ( int j = 0; j < cur[i].length; j++ ) {
				Row items = cur[i][j];
				Row nextItems = next[i][j];
				GregorianCalendar exp = items.getExpirationDate();
				GregorianCalendar nextVisit = (GregorianCalendar)exp.clone();
				exp.roll(GregorianCalendar.DAY_OF_MONTH, 
					vm.getStockingInterval());
				if ( exp.before( nextVisit ) ) {
					instructions.add("Remove all from " +
						i + ", " + j);
					instructions.add("Add " + vm.getNextLayout()
						.getDepth() + " " + nextItems.
						getProduct().getName() + " to " + 
						i + ", " + j);
				}
				if ( !items.getProduct().getName().equals(
					nextItems.getProduct().getName() ) ) {
					instructions.add("Remove all from " +
						i + ", " + j);
					instructions.add("Add " + vm.
						getNextLayout().getDepth() 
						+ " " + nextItems.getProduct().getName() 
						+ " to " + i + ", " + j);
				}
			}
		} // end for loop	
		} // end else
		return instructions.toArray(new String[0]);
	}

	/**
	 * updates the necessary machinery that the stocking is complete
	 */
	public void completeStocking() throws SQLException {
		vm.swapInNextLayout();
		try
		{
			db.updateOrCreateVendingMachine( vm );
		}
		catch(Exception databaseProblem)
		{
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... "+databaseProblem);
		}
	}
}
