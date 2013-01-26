import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Initial screen for a customer's transaction.
 * @author Kyle Savarese
 * @contributor Piper Chester <pwc1203@rit.edu>
 */
public class CustomerLoginScreen {

	/** the Database instance */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** VendingMachine the customer is at */
	private VendingMachine vm;

	/**
	 * Constructor for LoginScreen.
	 * @param curr the VendingMachine that is being logged into
	 * @throws InstantiationException if <tt>curr</tt> is <tt>null</tt> or inactive
 	 */
	public CustomerLoginScreen ( VendingMachine curr ) throws InstantiationException
	{
		if(curr==null || !curr.isActive()) //machine null or inactive
			throw new InstantiationException("Vending machine must be non-null and active");
		vm = curr;
	}	

	/**
 	 * Attempts to use the provided ID to login.
	 * @param id the id to check
	 * @return the CustomerPurchaseScreen associated with the 
	 *  	Customer and the VendingMachine or null if the Customer
	 * 	is not found
	 */
	public CustomerPurchaseScreen tryLogin ( int id )
	{
		try
		{
			Customer user = db.getCustomerById ( id );
			if ( user == null )
				return null;
			return new CustomerPurchaseScreen( user, vm );
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return null;
		}
	}
	
	/**
	 * Creates the CustomerPurchaseScreen state.
	 * @return the CashCustomerPurchaseScreen
	 */
	public CashCustomerPurchaseScreen cashLogin ()
	{
		try
		{
			Customer user = new Customer();
			return new CashCustomerPurchaseScreen( user, vm );
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, databaseProblem);
			return null;
		}
	}

	/**
	 * Fetches a list of active vending machines.
	 * @return the machine instances, which are not to be modified (or <tt>null</tt> on error)
	 */
	public static ArrayList<VendingMachine> listActiveMachines()
	{
		try
		{
			ArrayList<VendingMachine> ourUniverse=db.getVendingMachinesAll();
			
			//only keep active machines:
			Iterator<VendingMachine> trimmer=ourUniverse.iterator();
			while(trimmer.hasNext())
				if(!trimmer.next().isActive())
					trimmer.remove();
			
			return ourUniverse;
		}
		catch(Exception uhOh)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, uhOh);
			
			return null;
		}
	}

	/**
	 * Builder for use in the view.
	 * @param id the ID of the <tt>VendingMachine</tt>
	 * @return an instance representing the machine, or <tt>null</tt> in case of trouble
	 */
	public static CustomerLoginScreen buildInstance(int id)
	{
		VendingMachine mach=null;
		try
		{
			return new CustomerLoginScreen(db.getVendingMachineById(id));
		}
		catch(InstantiationException absentInactive) //not found in database (null) or inactive
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.WARN, absentInactive);
			return null;
		}
		catch(Exception databaseError) //more serious error came from database
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, databaseError);
			return null;
		}
	}
}
