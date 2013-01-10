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
		return null; //TODO remove this!
	}

	/**
	 * alter the layout of a given machine
	 * @param id the id of the machine
	 * @return the alter layout screen for the machine
	 */
	public ManagerAlterLayoutScreen alterLayout( int id ) {
		return null; //TODO remove this!
	}

	/**
	 * manage the items that are in the database
	 * @return the stocked item screen
	 */
	public ManagerStockedItemsScreen manageItems() {
		return null; //TODO remove this!
	}

	/**
	 * manage machines
	 * @return the machine management screen
	 */
	public ManagerMachineManagementScreen manageMachines() {
		return null; //TODO remove this!
	}
	
	/**
	 * manage users
	 * @return the user management screen
	 */
	public ManagerUserAccountsScreen manageUsers() {
		return null; //TODO remove this!
	}
	
	/**
	 * logs the manager out of the system
	 */
	public ManagerLoginScreen logout() {
		return new ManagerLoginScreen();
	}
}
