/**
 * 
 * @author Kyle Savarese
 * 
 */

public class ManagerReportStatsScreen {

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** all the transactions */
	private ArrayList<Transaction> transactions;

	/** all the machines */
	private ArrayList<VendingMachine> machines;

	/** all the locations */
	private ArrayList<Location> locations;

	public ManagerReportStatsScreen () {
		transactions = db.getTransactionsAll();
		machines = db.getMachinesAll();
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
		for ( int i = 0; i < transactions.size(); i++ ) {
			Transaction cur = transactions.get(i);
			if (cur.getMachine().getLocation().equals( place ) )
				trans.add( cur );
		}
		return trans;
	}
}
