/**
 *
 * @author Kyle Savarese
 * 
 */

public class ManagerAlterLayoutScreen {

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
		Row[][] rows = machine.getCurrentLayout().getRows();
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
	 * @param row the row to change
	 * @param it the fooditem in question
	 */
	public void queueRowChange( Pair<Integer, Integer> row, FoodItem it ) {

	}

	/**
	 * commits the queued changes
	 * @return whether the changes succeeded
	 */
	public boolean commitRowChanges() {
		return false; //TODO remove this!
	}

	/**
	 * exits the screen
	 */
	public void exit() {

	}
}
