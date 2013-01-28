import java.sql.SQLException;
import java.util.*;

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
	 *	the specified VendingMachine or <tt>null</tt> if the 
	 * 	machine does not exist or there's an error
	 */
	public RestockerTaskListScreen tryMachine( int id )
	{
		try
		{
			VendingMachine vm = db.getVendingMachineById(id);
			if ( vm == null || !vm.isActive() )
				return null;

			return new RestockerTaskListScreen( vm );
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
			return null;
		}
	}

	/**
	 * Fetches a list of active vending machines.
	 * @return the machine instances, which are not to be modified ( or null on an error )
	 */
	public static ArrayList<VendingMachine> listActiveMachines() {
		try {
			ArrayList<VendingMachine> allMach = db.getVendingMachinesAll();
			Iterator<VendingMachine> trimmer = allMach.iterator();
			while ( trimmer.hasNext() ) {
				if ( !trimmer.next().isActive() )
					trimmer.remove();
			}	
			return allMach;
		}
		catch ( Exception uhOh ) {
			ControllerExceptionHandler.registerConcern( 
				ControllerExceptionHandler.Verbosity.WARN, uhOh);
			return null;
		}
	}

	/**
	 * Builder for use in the view.
	 * @param id the ID of the <tt>VendingMachine</tt>
	 * @return an instance representing the task list screen, or <tt>null</tt> in case of trouble
	 */
	public static RestockerMachinePickerScreen buildInstance()
	{
		VendingMachine mach=null;
		try
		{
			return new RestockerMachinePickerScreen();
		}
		//catch(InstantiationException absentInactive) //not found in database (null) or inactive
	//	{
	//		ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, absentInactive);
	//		return null;
	//	}
		catch(Exception databaseError) //more serious error came from database
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, databaseError);
			return null;
		}
	}
}
