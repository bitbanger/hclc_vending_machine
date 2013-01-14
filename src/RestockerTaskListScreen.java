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
	 * @return String[]: list of instructions, or <tt>null</tt> on a backend error
	 */
	public String[] assembleStockingList() {
		ArrayList<String> instructions = new ArrayList<String>();
		Row[][] cur = vm.getCurrentLayout().getRows();
		Row[][] next = vm.getNextLayout().getRows();
		for ( int i = 0; i < cur.length; i++ ) {
			for ( int j = 0; j < cur[i].length; j++ ) {
				if ( next[i][j] == null ) {
					instructions.add("Remove all from " + i
						 + ", " + j);
					continue;
				}
				if ( cur[i][j] == null ) {
					instructions.add("Add " + 
						next[i][j].getRemainingQuantity()
						+ " " + next[i][j].getProduct().getName() 
						+ " to location " + i + ", " + j);
					continue;
				}
				Row items = cur[i][j];
				Row nextItems = next[i][j];
				GregorianCalendar exp = items.getExpirationDate();
				GregorianCalendar nextVisit = (GregorianCalendar)exp.clone();
				exp.roll(GregorianCalendar.DAY_OF_MONTH, 
					vm.getStockingInterval());
				try {
				if ( exp.before( nextVisit ) ) {
					// expiration
					instructions.add("Remove all from " +
						i + ", " + j);	
					instructions.add("Add " + 
						nextItems.getRemainingQuantity()
						+ " " + nextItems.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
				else if ( items.getRemainingQuantity() == 0 
					&& items.getId() == nextItems.getId() ) {
					// Manager didn't change this row and it's empty
					instructions.add("Add " + vm.getNextLayout().getDepth()
						+ " " + items.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
				else if ( !items.getProduct().equals( 
					nextItems.getProduct() ) ) {
					// next products not the same	
					instructions.add("Remove all from " +
						i + ", " + j);	
					instructions.add("Add " + 
						nextItems.getRemainingQuantity()
						+ " " + nextItems.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
				else if ( items.getRemainingQuantity() !=
					nextItems.getRemainingQuantity() ) {
					// differing remaining quantities 
					// of the same product
					instructions.add("Remove all from " +
						i + ", " + j);	
					instructions.add("Add " + 
						nextItems.getRemainingQuantity()
						+ " " + nextItems.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
				} catch ( Exception databaseProblem ) {
					ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
					return null;
				}
			}	
		} // end for
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
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, databaseProblem);
		}
	}
}
