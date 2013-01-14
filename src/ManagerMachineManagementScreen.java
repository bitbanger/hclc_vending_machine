import java.util.*;

/**
 *
 * @author Kyle Savarese
 *
 */

public class ManagerMachineManagementScreen {

	/** the database */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** the machines */
	private ArrayList<VendingMachine> storefronts; // are we running a bank making operation in the back or something?

	/**
	 * base constructor
	 * @param vms the machines
	 */
	public ManagerMachineManagementScreen( Collection<VendingMachine> vms ) {
		storefronts = new ArrayList<VendingMachine>( vms );
	}

	/**
	 * lists all machines
	 * @return a collection of all machines
	 */
	public ArrayList<VendingMachine> listMachinessAll() {
		return storefronts;
	}

	/**
	 * lists machines by location
	 * @param loc the location to find machines from
	 * @return lists the machines, return null if null is input
	 */
	public ArrayList<VendingMachine> listMachinesByLocation( Location loc ) {
		if ( loc == null )
			return null;
		ArrayList<VendingMachine> vms = new ArrayList<VendingMachine>();
		for ( VendingMachine vm : storefronts ) {
			if ( vm.getLocation().equals( loc ) ) 
				vms.add( vm );
		}
		return vms;
	}

	public ArrayList<VendingMachine> listDeactiveMachines()
	{
		ArrayList<VendingMachine> machines = new ArrayList<VendingMachine>();
		for (VendingMachine vm : storefronts)
		{
			if (!vm.isActive())
				machines.add(vm);
		}
		return machines;
	}

	public ArrayList<VendingMachine> listActiveMachines()
	{
		ArrayList<VendingMachine> machines = new ArrayList<VendingMachine>();
		for (VendingMachine vm : storefronts)
		{
			if (vm.isActive())
				machines.add(vm);
		}
		return machines;
	}

	/**
	 * adds a new machine
	 * @param location the loc of the machine
	 * @param interval the stocking interval
	 * @param layout the initial layout to use
	 * @return the id of the machine
	 */
	public int addMachine( int zipCode, String state, String[] nearbyBusinesses, int interval, VMLayout layout ) {
		try {
			Location location = new Location(zipCode, state, nearbyBusinesses);
			VendingMachine machine = new VendingMachine( location, interval, layout );
			db.updateOrCreateVendingMachine( machine );
			storefronts.add( machine );
			return machine.getId();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return -1;
		}
	}

	/**
	 * deactivate a machine
	 * @param id the id of the machine
	 */
	public boolean deactivateMachine( VendingMachine vm ) {
		try {
			vm.makeActive( false );
			db.updateOrCreateVendingMachine( vm );
			storefronts = db.getVendingMachinesAll();
			return true;
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
	}

	/**
	 * reactivates a machine
	 * @param id the id of the machine
	 */
	public boolean reactivateMachine( VendingMachine vm ) {
		try {
			vm.makeActive( true );
			db.updateOrCreateVendingMachine( vm );
			storefronts = db.getVendingMachinesAll();
			return true;
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
	}
	
	/**
	 * changes a machines location
	 * @param id the machines is
	 * @param location the new location
	 * @return whether it succeeded
	 */
	public boolean changeMachineLocation( VendingMachine vm, int zipCode, String state, String[] nearbyBusinesses ) {
		try {
			Location location = new Location(zipCode, state, nearbyBusinesses);
			vm.setLocation( location );
			db.updateOrCreateVendingMachine( vm );
			storefronts = db.getVendingMachinesAll();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
			return false;
		}
		return true;
	}
}
