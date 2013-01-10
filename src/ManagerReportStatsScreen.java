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
		transactions = db.getTransactionsAll();
		machines = db.getVendingMachinesAll();
		locatons = db.getLocationsAll();
	}

	public Collection<VendingMachine> listMachines() {
		return machines;
	}

	public Collection<Transaction> listMachineSales( VendingMachine machine ) {
		return db.getTransactionsByVendingMachine( machine );
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
