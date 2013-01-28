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

	public ArrayList<VendingMachine> displayVendingMachines()
	{
		try
		{
			return db.getVendingMachinesAll();
		}
		catch (Exception generalFailure)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, generalFailure);
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
	 * @return the alter layout screen for the machine, or <tt>null</tt> on failure
	 */
	public ManagerAlterLayoutScreen alterLayout() {
		try {
			return new ManagerAlterLayoutScreen();
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

	/**
	 * Reveals the name of the logged in user.
	 * @return the name
	 */
	public String getUserName()
	{
		return manny.getName();
	}
}
