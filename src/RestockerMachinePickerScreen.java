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
	DatabaseLayer db;

	/**
	 *  Standard constructor for RestockerMachinePickerScreen
	 */
	public RestockerMachinePickerScreen () {
		db = DatabaseLayer.getInstance();
	}
	
	/**
 	 * attempts to find the specified machine in the database
	 * @param id: the id of the vending machine
	 * @return: the RestockerTaskListScreen based around
	 *	the specified VendingMachine or null if the 
	 * 	machine does not exist
	 */
	public RestockerTaskListScreen tryMachine( int id ) throws SQLException
	{
		VendingMachine vm = db.getVendingMachineById(id);
		if ( vm == null )
			return null;
		return new RestockerTaskListScreen( vm );
	}
}
