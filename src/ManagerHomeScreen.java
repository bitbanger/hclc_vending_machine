import java.util.*;

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
	 * Fetches a list of active vending machines.
	 * @return the machine instance, which are not to be modified ( or null on error )
	 */
	public static Collection<VendingMachine> listActiveMachines() {
		try {
			Collection<VendingMachine> allMachs = db.getVendingMachinesAll();

			Iterator<VendingMachine> trimmer = allMachs.iterator();
			while ( trimmer.hasNext() ) {
				if ( !trimmer.next().isActive() ) 
					trimmer.remove();
			}
			return allMachs;
		} 
		catch ( Exception uhOh ) {
			ControllerExceptionHandler.registerConcern(
				ControllerExceptionHandler.Verbosity.WARN, uhOh);
			return null;
		}
	}

	/**
	 * create a screen to view stats
	 */
	public ManagerReportStatsScreen viewStats() {
		return new ManagerReportStatsScreen();
	}

	/**
	 * alter the layout of a given machine
	 * @param vm The vending machine
	 * @return the alter layout screen for the machine, or <tt>null</tt> on failure
	 */
	public ManagerAlterLayoutScreen alterLayout(VendingMachine vm) {
		try {
			return new ManagerAlterLayoutScreen( 
				db.getVendingMachineById(vm.getId()) );
		} catch ( Exception generalFailure ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, generalFailure);
			return null;
		}
	}

	/**
	 * manage the items that are in the database
	 * @return the stocked item screen, or <tt>null</tt> on failure
	 */
	public ManagerStockedItemsScreen manageItems() {
		try {
			return new ManagerStockedItemsScreen( 
				db.getFoodItemsAll() );
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
			return null;
		}
	}

	/**
	 * manage machines
	 * @return the machine management screen, or <tt>null</tt> on failure
	 */
	public ManagerMachineManagementScreen manageMachines() {
		try {
			return new ManagerMachineManagementScreen( 
				db.getVendingMachinesAll() );
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
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
}
