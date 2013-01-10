import java.sql.SQLException;

/**
 * 
 * Initial Restocker prompt
 * RestockerMachinePickerScreen
 * 
 * @author Kyle Savarese
 *
 */

public class RestockerMachinePickerScreen {
	
	/** The DatabaseLayer object */
	private static DatabaseLayer db=DatabaseLayer.getInstance();

	/**
 	 * attempts to find the specified machine in the database
	 * @param id the id of the vending machine
	 * @return the RestockerTaskListScreen based around
	 *	the specified VendingMachine or null if the 
	 * 	machine does not exist
	 */
	public RestockerTaskListScreen tryMachine( int id ) throws SQLException
	{
		try
		{
			VendingMachine vm = db.getVendingMachineById(id);
			if ( vm == null )
				return null;
			return new RestockerTaskListScreen( vm );
		}
		catch(Exception databaseProblem)
		{
			System.err.println("ERROR: Database problem encountered!");
			System.err.println("     : Dump details ... "+databaseProblem);
			return null;
		}
	}
}
