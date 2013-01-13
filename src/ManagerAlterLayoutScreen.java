/**
 *
 * @author Kyle Savarese
 * 
 */

public class ManagerAlterLayoutScreen {

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** the current machine */
	VendingMachine machine;

	/**
	 * base constructor
	 * @param vm the machine to be operated on
 	 */
	public ManagerAlterLayoutScreen( VendingMachine vm ) {
		machine = vm;
	}

	/**
	 * lists the rows in the machine
	 * @return the Food in the rows
	 */
	public FoodItem[][] listRows() {
		Row[][] rows = machine.getNextLayout().getRows();
		FoodItem[][] items = new FoodItem[rows.length][rows[0].length];
		for ( int i = 0; i < rows.length; i++ ) {
			for ( int j = 0; j < rows[i].length; j++ ) {
				items[i][j] = rows[i][j].getProduct();
			}
		}
		return items;
	}

	/**
	 * queues a change to the layout
	 * note: this will change the local copy's next layout 
	 * 	but the database will not reflect this change 
	 * 	until the commitRowChanges()
	 * @param row the row to change
	 * @param it the fooditem in question
	 */
	public void queueRowChange( Pair<Integer, Integer> row, FoodItem it ) {
		Row[][] rows = machine.getNextLayout().getRows();
		try {
			rows[row.first][row.second].setProduct( it );
			rows[row.first][row.second].setRemainingQuantity( 
				machine.getNextLayout().getDepth() );
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.print("     : Dump details ... " );
			databaseProblem.printStackTrace();
			System.err.println();
		}
	}

	/**
	 * commits the queued changes to the database
	 * @return whether the changes succeeded
	 */
	public boolean commitRowChanges() {
		try {
			db.updateOrCreateVendingMachine( machine );
			return true;
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.print("     : Dump details ... " );
			databaseProblem.printStackTrace();
			System.err.println();
			return false;
		}
	}

	/**
	 * exits the screen
	 */
	public void exit() {

	}
}
