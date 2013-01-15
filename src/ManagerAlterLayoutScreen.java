/**
 *
 * @author Kyle Savarese
 * 
 */

import java.util.Collection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ManagerAlterLayoutScreen {

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** the current machine */
	ArrayList<VendingMachine> machines;

	/**
	 * base constructor
 	 */
	public ManagerAlterLayoutScreen() {
		try
		{
			machines = db.getVendingMachinesAll();
		}
		catch (Exception generalFault)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, generalFault);
		}
	}

	/**
	 * lists the rows in the machine
	 * @return the Food in the rows
	 */
	public FoodItem[][] listRows() {
		Row[][] rows = machines.get(0).getNextLayout().getRows();
		FoodItem[][] items = new FoodItem[rows.length][rows[0].length];
		for ( int i = 0; i < rows.length; i++ ) {
			for ( int j = 0; j < rows[i].length; j++ ) {
				if (rows[i][j] == null)
					items[i][j] = null;
				else
					items[i][j] = rows[i][j].getProduct();
			}
		}
		return items;
	}

	/**
	 * Returns all machines
	 **/
	public ArrayList<VendingMachine> listMachines() {
		return machines;
	}

	/**
	 * Lists the food items in the database.
	 * Note: we may want to change this to list only active items
	 * @return A collection of food items upon success or null on any type of
	 * failure.
	 **/
	public ArrayList<FoodItem> listItems()
	{
		try
		{
			return db.getFoodItemsAll();
		}
		catch (Exception generalFault)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, generalFault);
			return null;
		}
	}

	/**
	 * queues a change to the layout
	 * note: this will change the local copy's next layout 
	 * 	but the database will not reflect this change 
	 * 	until the commitRowChanges()
	 * @param row the row to change
	 * @param it the fooditem in question
	 * @return 0 on success, 1 if the item expires too soon, and -1 on failure
	 */
	public int queueRowChange( Pair<Integer, Integer> row, FoodItem it ) {
		if (it != null)
		{
			for (VendingMachine machine : machines)
			{
				if (machine.getStockingInterval() > it.getFreshLength())
					return 1;
			}
		}
		for (VendingMachine machine : machines)
		{
			Row[][] rows = machine.getNextLayout().getRows();
			try {
				if (it == null)
					rows[row.first][row.second] = null;
				else
					rows[row.first][row.second] = new Row(it, machine.getNextLayout().getDepth(), new GregorianCalendar());
			} catch ( Exception generalFault ) {
				ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, generalFault);
				return -1;
			}
		}
		return 0;
	}

	/**
	 * commits the queued changes to the database
	 * @return whether the changes succeeded
	 */
	public boolean commitRowChanges() {
		for (VendingMachine machine : machines)
		{
			try {
				db.updateOrCreateVendingMachine( machine );
			} catch ( Exception databaseProblem ) {
				ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
				return false;
			}
		}
		return true;
	}
}
