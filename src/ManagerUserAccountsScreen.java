import java.util.*;

/**
 *
 * @author Kyle Savarese
 * 
 */

public class ManagerUserAccountsScreen {
	
	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** the customers */
	private ArrayList<Customer> customers;

	/** the managers */
	private ArrayList<Manager> managers;

	/**
	 * base constructor
	 */
	public ManagerUserAccountsScreen() {
		try {
			customers = db.getCustomersAll();
			managers = db.getManagersAll();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.FATAL, databaseProblem);
		}
	}

	/**
	 * a list of all customers
	 * @return a collection of the customers
	 */
	public ArrayList<Customer> listCustomers() {
		return customers;
	}

	/**
	 * lists the managers
	 * @return a collection of the managers
	 */
	public ArrayList<Manager> listManagers() {
		return managers;
	}

	/**
	 * add a new customer
 	 * @param initialBalance the inital balance
	 * @param name the name of the customer
	 * @return the id of the new customer
	 */
	public int addCustomer( String name, int initialBalance ) {
		try
		{
			Customer cust = new Customer( name, initialBalance );
			db.updateOrCreateCustomer( cust );
			customers.add( cust );
			return cust.getId();
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return -1;
		}
	}

	/**
	 * add a new manager
 	 * @param startingPassword the initial password
	 * @param name the name of the manager
	 * @return int the id of the new manager
	 */
	public int addManager( String name, String startingPassword ) {
		try
		{
			Manager manny = new Manager( name, startingPassword );
			db.updateOrCreateManager( manny );
			managers.add( manny );
			return manny.getId();
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return -1;
		}
	}

	/**
	 * change the password of a manager
	 * @param managerId the manager whose password is being changed
	 * @param newPassword the new password
	 * @return if it succeeded
	 */
	public boolean changePassword( Manager manny, String newPassword ) {
		try
		{
			manny.setPassword( newPassword );
			db.updateOrCreateManager( manny );
			managers = db.getManagersAll();
			return true;
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
	}
}
