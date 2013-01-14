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
	private ArrayList<VendingMachine> storefronts;

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

	/**
	 * adds a new machine
	 * @param location the loc of the machine
	 * @param interval the stocking interval
	 * @param layout the initial layout to use
	 * @return the id of the machine
	 */
	public int addMachine( Location location, int interval, VMLayout layout ) {
		try {
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
	public void deactivateMachine( int id ) {
		try {
			VendingMachine vm = db.getVendingMachineById( id );
			vm.makeActive( false );
			db.updateOrCreateVendingMachine( vm );
			storefronts = db.getVendingMachinesAll();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
		}
	}

	/**
	 * reactivates a machine
	 * @param id the id of the machine
	 */
	public void reactivateMachine( int id ) {
		try {
			VendingMachine vm = db.getVendingMachineById( id );
			vm.makeActive( true );
			db.updateOrCreateVendingMachine( vm );
			storefronts = db.getVendingMachinesAll();
		} catch ( Exception databaseProblem ) {
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
		}
	}
	
	/**
	 * changes a machines location
	 * @param id the machines is
	 * @param location the new location
	 * @return whether it succeeded
	 */
	public boolean changeMachineLocation( int id, Location location ) {
		try {
			VendingMachine vm = db.getVendingMachineById( id );
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
