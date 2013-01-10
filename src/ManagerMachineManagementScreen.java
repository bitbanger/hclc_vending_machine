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
	private Collection<VendingMachine> storefronts;

	/**
	 * base constructor
	 * @param vms the machines
	 */
	public ManagerMachineManagementScreen( Collection<VendingMachine> vms ) {
		storefronts = vms;
	}

	/**
	 * lists all machines
	 * @return a collection of all machines
	 */
	public Collection<VendingMachine> listMachinessAll() {
		return storefronts;
	}

	/**
	 * lists machines by location
	 * @return lists the machines
	 */
	public Collection<VendingMachine> listMachinesByLocation() {
		return null; //TODO remove this!
	}

	/**
	 * adds a new machine
	 * @param location the loc of the machine
	 */
	public void addMachine( Location location ) {

	}

	/**
	 * deactivate a machine
	 * @param id the id of the machine
	 */
	public void deactivateMachine( int id ) {

	}

	/**
	 * reactivates a machine
	 * @param id the id of the machine
	 */
	public void reactivateMachine( int id ) {

	}
	
	/**
	 * changes a machines location
	 * @param id the machines is
	 * @param location the new location
	 * @return whether it succeeded
	 */
	public boolean changeMachineLocation( int id, Location location ) {
		return false; //TODO remove this!
	}

	/**
	 * exits the screen
	 */
	public void exit() {

	}
}
