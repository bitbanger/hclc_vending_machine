import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;
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

	/** the CURRENT VMLayout as of each instruction execution */
	private VMLayout status;

	/** The list of instructions to execute */
	private ArrayList<String> instructions;

	/**
	 * Constructor
	 * Creates an instance with the specified VendingMachine
	 * @param cur the machine the restocker is using
	 */	
	public RestockerTaskListScreen ( VendingMachine cur ) {
		vm = cur;
		instructions = new ArrayList<String>();
		status = vm.getCurrentLayout();
		assembleStockingList();
	}

	/**
	 * creates a list of instructions for the restocker to follow
	 * compares the next layout and the current layout to determine 
	 *	the changes that need to be made
	 */
	private void assembleStockingList() {
		Row[][] cur = vm.getCurrentLayout().getRows();
		Row[][] next = vm.getNextLayout().getRows();
		for ( int i = 0; i < cur.length; i++ ) {
			for ( int j = 0; j < cur[i].length; j++ ) {
				if ( next[i][j] == null ) {
					if ( cur[i][j] != null && 
						cur[i][j].getRemainingQuantity() != 0 )
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
						vm.getCurrentLayout().getDepth()
						+ " " + nextItems.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
				else if ( items.getRemainingQuantity() == 0 ) {
					// Manager didn't change this row's product and it's empty
					instructions.add("Add " + vm.getCurrentLayout().getDepth()
						+ " " + items.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
				else if ( items.getId() == nextItems.getId() ) {
					continue; // Manager didn't update the row and other conditions are fine
				}
				else if ( !items.getProduct().equals( 
					nextItems.getProduct() ) ) {
					// next products not the same	
					instructions.add("Remove all from " +
						i + ", " + j);	
					instructions.add("Add " + 
						vm.getCurrentLayout().getDepth()
						+ " " + nextItems.getProduct().getName() 
						+ " to location " + i + ", " + j);
				}
				} catch ( Exception databaseProblem ) {
					ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
				}
			}	
		} // end for
	}

	/**
	 * gets the current list of instructions that remain
	 * @return String[] the list of instructions
	 */
	public String[] getInstructions() {
		return instructions.toArray(new String[0]);
	}

	/** 
	 * removes the specified instruction from the list of instuctions
	 * @param id the id of the instruction being removed
	 * @return boolean whether it succeeded
	 */
	public boolean removeInstruction( int id ) {
		Row[][] rows = status.getRows();
		String inst = instructions.get( id );
		if ( inst.startsWith("Remove") ) {
			//remove all from 'i', 'j'
			inst = inst.substring( 15 );
			inst.replace(',', ' ');
			int i = 0;
			for ( i = 0; i < inst.length(); ++i ) {
				if ( inst.charAt( i ) == ' ' )
					break;
			}
			int x = Integer.parseInt( inst.substring(0, i) );
			int y = Integer.parseInt( inst.substring(i) );
			rows[x][y] = null;
		}
		else {
			//add 'depth' 'name' to location 'i', 'j'
			inst.replace(',', ' ');
			try {
				String[] split = inst.split("\\s+");
				int x = Integer.parseInt( split[5] );
				int y = Integer.parseInt( split[6] );
				rows[x][y].setProduct( vm.getNextLayout().getRows()[x][y].getProduct() );
				rows[x][y].setRemainingQuantity( Integer.parseInt( split[2] ) );
			} catch (PatternSyntaxException ex) {
				System.err.println("You see nothing.");
			}
		}
		instructions.remove( id );
		status = new VMLayout( rows, status.getDepth() );
		return true;
	}

	/**
	 * updates the necessary machinery that the stocking is complete only
	 *	if all instructions are complete
	 * @return boolean if the stocking was successfully finished
	 */
	public boolean completeStocking() {
		if ( instructions.size() == 0 ) 
			vm.swapInNextLayout( status );
		else
			return false;
		try
		{
			db.updateOrCreateVendingMachine( vm );
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, databaseProblem);
		}
		return true;
	}

	/**
	 * Builder for use in the view.
	 * @param id the ID of the <tt>VendingMachine</tt>
	 * @return an instance representing the task list screen, or <tt>null</tt> in case of trouble
	 */
	public static RestockerTaskListScreen buildInstance(int id)
	{
		VendingMachine mach=null;
		try
		{
			return new RestockerTaskListScreen(db.getVendingMachineById(id));
		}
		//catch(InstantiationException absentInactive) //not found in database (null) or inactive
	//	{
	//		ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, absentInactive);
	//		return null;
	//	}
		catch(Exception databaseError) //more serious error came from database
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, databaseError);
			return null;
		}
	}
}
