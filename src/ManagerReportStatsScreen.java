import java.util.Collection;
import java.util.ArrayList;

/**
 * 
 * @author Kyle Savarese
 * 
 */

public class ManagerReportStatsScreen {

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** all the machines */
	private ArrayList<VendingMachine> machines;

	/** all the locations */
	private ArrayList<Location> locations;

	/**
	 * base constructor
	 */
	public ManagerReportStatsScreen () {
		try {
			machines = db.getVendingMachinesAll();
			locations = db.getLocationsAll();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.FATAL, databaseProblem);
		}
	}

	/**
	 * get a collection of all machines
	 * @return a collection of the machines present when screen was launched
	 */
	public ArrayList<VendingMachine> listMachines() {
		return machines;
	}

	/**
	 * get a collection of transactions based on a specific machine
	 * @param machine the machine in question
	 * @return the transactions made at that machine
	 */
	public ArrayList<Transaction> listMachineSales( VendingMachine machine ) {
		try
		{
			return db.getTransactionsByVendingMachine( machine );
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return null;
		}
	}

	/**
	 * get a collection of all locations
	 * @return a collection of the locations present when screen was launched
	 */
	public ArrayList<Location> listLocations() {
		return locations;
	}

	/**
	 * get a collection of transactions based on a specific location
	 * @param place the location in question
	 * @return the transactions made at that location
	 */
	public ArrayList<Transaction> listLocationSales( Location place ) {
		if ( place == null )
			return null;
		ArrayList<Transaction> trans = new ArrayList<Transaction>();
		try {
			ArrayList<Transaction> transactions = db.getTransactionsAll();
			for ( Transaction cur : transactions ) {
				if (cur.getMachine().getLocation().equals( place ) )
					trans.add( cur );
			}
		} catch (Exception databaseProblem){
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
		}

		return trans;
	}

	public ArrayList<Transaction> listSalesAll()
	{
		try {
			ArrayList<Transaction> transactions = db.getTransactionsAll();
			return transactions;
		} catch (Exception databaseProblem){
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
			return null;
		}

	}
}
