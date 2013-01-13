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

	public ManagerReportStatsScreen () {
		try {
			transactions = db.getTransactionsAll();
			machines = db.getVendingMachinesAll();
			locations = db.getLocationsAll();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.FATAL, databaseProblem);
		}
	}

	public Collection<VendingMachine> listMachines() {
		return machines;
	}

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

	public Collection<Location> listLocations() {
		return locations;
	}

	public Collection<Transaction> listLocationSales( Location place ) {
		ArrayList<Transaction> trans = new ArrayList<Transaction>();
		for ( Transaction cur : transactions ) {
			if (cur.getMachine().getLocation().equals( place ) )
				trans.add( cur );
		}
		return trans;
	}
}
