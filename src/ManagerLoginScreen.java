/**
 *
 * @author Kyle Savarese
 *
 */

public class ManagerLoginScreen {
	
	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();
	
	/** 
	 * base constructor
	 */
	public ManagerLoginScreen() {

	}

	/**
	 * attempts to login to a manager's account
	 * @param id the id of the manager
	 * @param password the attempted password
	 * @return a new home screen if the password is correct null else
	 */
	public ManagerHomeScreen tryLogin( int id, String password ) {
		try
		{
			Manager manny = db.getManagerById( id );
			if ( manny.comparePassword( password ) ) {
				return new ManagerHomeScreen( manny );
			}
			return null;
		}
		catch(Exception databaseProblem)
		{
			System.err.println("ERROR: Database problem encountered!");
			System.err.print("     : Dump details ... ");
			databaseProblem.printStackTrace();
			System.err.println();
			return null;
		}
	}
}
