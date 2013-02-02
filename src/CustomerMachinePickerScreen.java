import java.sql.SQLException;
import java.util.*;

/**
 * Initial Customer prompt
 * 
 * @author Lane Lawley
 *
 */

public class CustomerMachinePickerScreen {
	
	/** The DatabaseLayer object */
	private static DatabaseLayer db=DatabaseLayer.getInstance();

	/**
 	 * attempts to find the specified machine in the database
	 * @param id the id of the vending machine
	 * @return the CustomerLoginScreen based around
	 *	the specified VendingMachine or <tt>null</tt> if the 
	 * 	machine does not exist or there's an error
	 */
	public CustomerLoginScreen tryMachine( int id )
	{
		try
		{
			VendingMachine vm = db.getVendingMachineById(id);
			if ( vm == null || !vm.isActive() )
				return null;

			return new CustomerLoginScreen( vm );
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
}
