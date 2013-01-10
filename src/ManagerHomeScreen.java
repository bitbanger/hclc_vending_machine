/**
 * 
 * @author Kyle Savarese
 * 
 */

public class ManagerHomeScreen {
	
	/** the manager who is working */
	private Manager manny;

	/** the database */
	private static db = DatabaseLayer.getInstance();	

	/**
	 * base constructor
	 * @param man the manager
	 */
	public ManagerHomeScreen( man ) {
		manny = man;
	}

	/**
	 * create a screen to view stats
	 */
	public ManagerReportStatsScreen viewStats() {
		
	}

	/**
	 * alter the layout of a given machine
	 * @param id the id of the machine
	 * @return the alter layout screen for the machine
	 */
	public ManagerAlterLayoutScreen( int id ) {

	}

	/**
	 * manage the items that are in the database
	 * @return the stocked item screen
	 */
	public ManagerStockedItemsScreen manageItems() {

	}

	/**
	 * manage machines
	 * @return the machine management screen
	 */
	public ManagerMachineManagementScreen manageMachines() {

	}
	
	/**
	 * manage users
	 * @return the user management screen
	 */
	public ManagerUserAccountsScreen manageUsers() {

	}
	
	/**
	 * logs the manager out of the system
	 */
	public ManagerLoginScreen logout() {
		return new ManagerLoginScreen();
	}
}
