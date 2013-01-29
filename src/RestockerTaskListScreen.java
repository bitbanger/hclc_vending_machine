import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;
import java.util.HashMap;
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
	private HashMap<Integer, Pair<String, Boolean>> instructions;

	/**
	 * Constructor
	 * Creates an instance with the specified VendingMachine
	 * @param cur the machine the restocker is using
	 */	
	public RestockerTaskListScreen ( VendingMachine cur ) {
		vm = cur;
		instructions = new HashMap<Integer, Pair<String, Boolean>>();
		status = vm.getCurrentLayout();
		this.assembleStockingList();
	}

	private void checkRows() {
		Row[][] curRows=vm.getCurrentLayout().getRows(), newRows=vm.getNextLayout().getRows();
		for ( int i = 0; i < curRows.length; i++ ) {
			for ( int j = 0; j < curRows[i].length; j++ ) {
				if( curRows[i][j]!=null && newRows[i][j]!=null &&
					curRows[i][j].getProduct().equals(newRows[i][j].getProduct())) {
					try {
						if(curRows[i][j].getRemainingQuantity()<newRows[i][j].getRemainingQuantity())
							newRows[i][j].setRemainingQuantity(curRows[i][j].getRemainingQuantity());
					} catch (BadArgumentException impossible) {
						System.err.println("CRITICAL:Model detected a problem previously though impossible");
						System.err.print("    DUMP : ");
						impossible.printStackTrace();	
						System.err.println();
					}
				}
			}
		}
		try {
			VMLayout next = new VMLayout( newRows, vm.getCurrentLayout().getDepth() );
			vm.setNextLayout( next );
		}
		catch (BadArgumentException impossible) {
			System.err.println("CRITICAL:Model detected a problem previously though impossible");
			System.err.print("    DUMP : ");
			impossible.printStackTrace();	
			System.err.println();
		} 
	}

	/**
	 * creates a list of instructions for the restocker to follow
	 * compares the next layout and the current layout to determine 
	 *	the changes that need to be made
	 */
	private void assembleStockingList() {
		int count = 0;
		Row[][] cur = vm.getCurrentLayout().getRows();
		Row[][] next = vm.getNextLayout().getRows();
		for ( int i = 0; i < cur.length; i++ ) {
			for ( int j = 0; j < cur[i].length; j++ ) {
				if ( next[i][j] == null ) {
					if ( cur[i][j] != null && 
						cur[i][j].getRemainingQuantity() != 0 )
						instructions.put(count++, new Pair<String, Boolean>(("Remove " +
						"all from " + i+ ", " + j), false) );
					continue;
				}
				if ( cur[i][j] == null ) {
					instructions.put(count++, new Pair<String, Boolean>(("Add " + 
						next[i][j].getRemainingQuantity()
						+ " " + next[i][j].getProduct().getName() 
						+ " to location " + i + ", " + j), false) );
					continue;
				}
				Row items = cur[i][j];
				Row nextItems = next[i][j];
				GregorianCalendar exp = items.getExpirationDate();
				GregorianCalendar nextVisit = (GregorianCalendar)exp.clone();
				exp.roll(GregorianCalendar.DAY_OF_YEAR, 
					vm.getStockingInterval());
				try {
				if ( exp.before( nextVisit ) ) {
					// expiration
					instructions.put(count++, new Pair<String, Boolean>(("Remove all from " +
						i + ", " + j), true) );	
					instructions.put(count++, new Pair<String, Boolean>(("Add " + 
						vm.getCurrentLayout().getDepth()
						+ " " + nextItems.getProduct().getName() 
						+ " to location " + i + ", " + j), false) );
				}
				else if ( items.getRemainingQuantity() == 0 ) {
					// Manager didn't change this row's product and it's empty
					instructions.put(count++, new Pair<String, Boolean>(("Add " + 
						vm.getCurrentLayout().getDepth()+ " " 
						+ items.getProduct().getName() 
						+ " to location " + i + ", " + j), false) );
				}
				else if ( items.getId() == nextItems.getId() ) {
					continue; // Manager didn't update the row and other conditions are fine
				}
				else if ( !items.getProduct().equals( 
					nextItems.getProduct() ) ) {
					// next products not the same	
					instructions.put(count++, new Pair<String, Boolean>(("Remove all from " +
						i + ", " + j), true) );	
					instructions.put(count++, new Pair<String, Boolean>(("Add " + 
						vm.getCurrentLayout().getDepth()
						+ " " + nextItems.getProduct().getName() 
						+ " to location " + i + ", " + j), false) );
				}
				} catch ( Exception databaseProblem ) {
					ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
				}
			}	
		} // end for
	}

	/**
	 * gets the current list of instructions that remain
	 * @return HashMap<Integer, Pair<String, Boolean>> the list of instructions and if required
	 */
	public HashMap<Integer, Pair<String, Boolean>> getInstructions() {
		return instructions;
	}

	/** 
	 * removes the specified instruction from the list of instuctions
	 * @param id the id of the instruction being removed - gotten from a list
	 * @return boolean whether it succeeded
	 */
	public boolean removeInstruction( int id ) {
		Row[][] rows = status.getRows();
		Pair it = instructions.get( id );
		String inst = (String)it.first;
		if ( inst.startsWith("Remove") ) {
			//remove all from 'i', 'j'
			String next = inst.replace(',', ' ');
			try {
				String[] split = next.split("\\s+");
				int i;
				for ( i = 0; i < split.length; i++ ) {
					if ( split[i].equals("from") )
						break;
				}
				int x = Integer.parseInt( split[i + 1] );
				int y = Integer.parseInt( split[i + 2] );
				rows[x][y] = null;
			} catch (PatternSyntaxException ex) {
				System.err.println("You see nothing.");
			}
		}
		else if ( inst.startsWith("Add") ) {
			//add 'depth' 'name' to location 'i', 'j'
			String next = inst.replace(',', ' ');
			try {
				String[] split = next.split("\\s+");
				int i;
				for ( i = 0; i < split.length; i++ ) {
					if ( split[i].equals("location") )
						break;
				}
				int x = Integer.parseInt( split[i + 1] );
				int y = Integer.parseInt( split[i + 2] );
				if ( rows[x][y] != null )
					return false; // Don't add to non null rows
				FoodItem prod = vm.getNextLayout().getRows()[x][y].getProduct();
				int quantity = Integer.parseInt( split[1] );
				GregorianCalendar cal = new GregorianCalendar();
				cal.add(cal.DAY_OF_YEAR, (int)prod.getFreshLength());
				rows[x][y] = new Row( vm.getNextLayout().getRows()[x][y].getProduct(), quantity, cal );
			} catch (PatternSyntaxException ex) {
				System.err.println("You see nothing.");
			} catch (BadArgumentException impossible) {
				ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, impossible);
				return false;
			}
		} else {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, new Exception("How did we accidentally do the wrong command?") );
		}	
		instructions.remove( id );
		try {
			status = new VMLayout( rows, status.getDepth() );
		} catch (BadArgumentException impossible) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, impossible);
		}
		return true;
	}

	/**
	 * updates the necessary machinery that the stocking is complete only
	 *	if all instructions are complete
	 * @return boolean if the stocking was successfully finished
	 */
	public boolean completeStocking() {
		checkRows();
		for ( Pair<String, Boolean> next : instructions.values() ) {
			if ( next.second )
				return false;
		}
		vm.swapInNextLayout( status );
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
}
