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

	/** all the transactions */
	private Collection<Transaction> transactions;

	/** all the machines */
	private Collection<VendingMachine> machines;

	/** all the locations */
	private Collection<Location> locations;

	/**
	 * base constructor
	 */
	public ManagerReportStatsScreen () {
		try {
			transactions = db.getTransactionsAll();
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
	public Collection<VendingMachine> listMachines() {
		return machines;
	}

	/**
	 * get a collection of transactions based on a specific machine
	 * @param machine the machine in question
	 * @return the transactions made at that machine
	 */
	public Collection<Transaction> listMachineSales( VendingMachine machine ) {
		try
		{
			return db.getTransactionsByVendingMachine( machine );
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
			return null;
		}
	}

	/**
	 * get a collection of all locations
	 * @return a collection of the locations present when screen was launched
	 */
	public Collection<Location> listLocations() {
		return locations;
	}

	/**
	 * get a collection of transactions based on a specific location
	 * @param place the location in question
	 * @return the transactions made at that location
	 */
	public Collection<Transaction> listLocationSales( Location place ) {
		ArrayList<Transaction> trans = new ArrayList<Transaction>();
		for ( Transaction cur : transactions ) {
			if (cur.getMachine().getLocation().equals( place ) )
				trans.add( cur );
		}
		return trans;
	}

	public Collection<Transaction> listSalesAll()
	{
		return transactions;
	}
}
