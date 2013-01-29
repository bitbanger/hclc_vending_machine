import java.util.*;

/**
 * The main home screen for the Manager application. 
 * @author Kyle Savarese
 * 
 */
public class ManagerHomeScreen {
	
	/** The manager who is working. */
	private Manager manny;

	/** The database. */
	private static DatabaseLayer db = DatabaseLayer.getInstance();	

	/**
	 * The base constructor.
	 * @param man the manager
	 */
	public ManagerHomeScreen( Manager man ) {
		manny = man;
	}

	/**
	 * Displays all of the vending machines.
	 * @return the array list of vending machines to be displayed
	 */
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
	 * Creates a screen to view statistics.
	 * @return the screen for the Manager to view the stats.
	 */
	public ManagerReportStatsScreen viewStats() {
		return new ManagerReportStatsScreen();
	}

	/**
	 * Alter the layout of a given machine.
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
	 * Manage the items that are in the database.
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
	 * Manage machines.
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
	 * Manage users.
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
