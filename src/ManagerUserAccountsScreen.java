/**
 *
 * @author Kyle Savarese
 * 
 */

public class ManagerUserAccountsScreen {
	
	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** the customers */
	private Collection<Customer> customers;

	/** the managers */
	private Collection<Manager> managers;

	/**
	 * base constructor
	 */
	public ManagerUserAccountsScreen() {
		customers = db.getCustomersAll();
		managers = db.getManagersAll();
	}

	/**
	 * a list of all customers
	 * @return a collection of the customers
	 */
	public Collection<Customer> listCustomers() {
		return customers;
	}

	/**
	 * lists the managers
	 * @return a collection of the managers
	 */
	public Collection<Manager> listManagers() {
		return managers;
	}

	/**
	 * add a new customer
 	 * @param initialBalance the inital balance
	 * @param name the name of the customer
	 */
	public void addCustomer( String name, int initialBalance ) {
		Customer cust = new Customer( name, initialBalance );
		db.updateOrCreateCustomer( cust );
		customers.add( cust );
	}

	/**
	 * add a new manager
 	 * @param startingPassword the initial password
	 * @param name the name of the manager
	 */
	public void addManager( String name, String startingPassword ) {
		Manager manny = new Manager( name, startingPassword );
		db.updateOrCreateManager( manny );
		managers.add( manny );
	}

	/**
	 * change the password of a manager
	 * @param managerId the manager whose password is being changed
	 * @param newPassword the new password
	 */
	public void changePassword( int managerId, String newPassword ) {
		Manager manny = db.getManagerById( managerId );
		manny.setPassword( newPassword );
		db.updateOrCreateManager( manny );
		managers = db.getManagersAll();
	}

	/**
	 * exits the current screen
	 */
	public void exit() {
		//TODO determine what this does
	}
}
