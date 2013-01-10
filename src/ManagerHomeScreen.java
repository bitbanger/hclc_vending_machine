/**
 * 
 * @author Kyle Savarese
 * 
 */

public class ManagerHomeScreen {
	
	/** the manager who is working */
	private Manager manny;

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();	

	/**
	 * base constructor
	 * @param man the manager
	 */
	public ManagerHomeScreen( Manager man ) {
		manny = man;
	}

	/**
	 * create a screen to view stats
	 */
	public ManagerReportStatsScreen viewStats() {
		return new ManagerReportStatsScreen();
	}

	/**
	 * alter the layout of a given machine
	 * @param id the id of the machine
	 * @return the alter layout screen for the machine
	 */
	public ManagerAlterLayoutScreen alterLayout( int id ) {
		try {
			return new ManagerAlterLayoutScreen( 
				db.getVendingMachineById( id ) );
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... " + databaseProblem);
			return null;
		}
	}

	/**
	 * manage the items that are in the database
	 * @return the stocked item screen
	 */
	public ManagerStockedItemsScreen manageItems() {
		try {
			return new ManagerStockedItemsScreen( 
				db.getFoodItemsAll() );
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... " + databaseProblem);
			return null;
		}
	}

	/**
	 * manage machines
	 * @return the machine management screen
	 */
	public ManagerMachineManagementScreen manageMachines() {
		try {
			return new ManagerMachineManagementScreen( 
				db.getVendingMachinesAll() );
		} catch ( Exception databaseProblem ) {
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... " + databaseProblem);
			return null;
		}
	}
	
	/**
	 * manage users
	 * @return the user management screen
	 */
	public ManagerUserAccountsScreen manageUsers() {
		return new ManagerUserAccountsScreen();
	}
	
	/**
	 * logs the manager out of the system
	 */
	public ManagerLoginScreen logout() {
		return new ManagerLoginScreen();
	}
}
