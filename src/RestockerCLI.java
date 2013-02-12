import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.Scanner;
import java.util.HashMap;
import java.util.TreeSet;
/**
 * CLI for the Restocker's perspective.
 * Provides an entry point for the Restocker.
 * @author Piper Chester <pwc1203@rit.edu>
 */
public class RestockerCLI {

	/** Instantiating a new RestockerMachinePickerScreen */
	private static RestockerMachinePickerScreen restockerMachinePickerScreen = new RestockerMachinePickerScreen();

	/** The entry point to the program.
	 * 
	 * @param args Arguments required to create String arrays in Java
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to HCLC, LLC's Smart Vending Machine Restocker Interface!\n---\n");
		pickMachine(restockerMachinePickerScreen);
	}

	/** Allows the Restocker to view the list of tasks that needs to be performed.
	 *
	 * @param restockerTaskListScreen The state of the RestockerTaskListScreen
	 */
	private static void listTasks(RestockerTaskListScreen restockerTaskListScreen) {
		HashMap<Integer, Pair<String, Boolean>> tasks;

		CLIUtilities.printTitle("List of Tasks to Perform");
	
		tasks = restockerTaskListScreen.getInstructions();
		SortedSet<Integer> list = new TreeSet<Integer>();
		list.addAll( tasks.keySet() );
		for ( Integer task : list ) {
			System.out.print(task + ": " + tasks.get(task).first);
			if ( tasks.get(task).second )
				System.out.print("\tREQUIRED");
			System.out.println();
		}

		if ( list.size() == 0 ) {
			System.out.println("There are no instructions to complete.");
			return;
		}
	
		while(true) {
			int response = CLIUtilities.promptInt("Enter the number of the instruction completed, 0 to attempt to finish, or -1 to quit", new MinValueNumberFormat( -1 ) );
			if ( response == 0 ) {
				boolean done = restockerTaskListScreen.completeStocking();
				if ( done ) {
					System.out.println("========\nRestocking completed.");
					break;
				} else {
					System.out.println("========\nRestocking not yet complete, please finish all required tasks.");
				}
			} else if ( response == -1 ) {
				System.out.println("========\nRestocking NOT completed.");
				break;
			} else {
				if ( !restockerTaskListScreen.removeInstruction( response ) )
					System.out.println("Please remove from that row prior to adding new items");	
			}
		}

		System.out.println("Goodbye!");
	}
	
	/**
	 * Allows the Restocker to select a machine.
	 *
	 * @param restockerMachinePickerScreen The state of the RestockerMachinePickerScreen
	 */
	private static void pickMachine(RestockerMachinePickerScreen restockerMachinePickerScreen) {
		ArrayList<VendingMachine> vms = RestockerMachinePickerScreen.listActiveMachines();
		if ( vms.size() == 0 ) {
			System.out.println("There are no machines to restock.");
			return;
		}
		int idNumber;
	
		CLIUtilities.printTitle("Please Pick a Machine");
		
		//CLIUtilities.printCollection(vms);

			
		RestockerTaskListScreen restockerTaskListScreen = null;

		while (restockerTaskListScreen == null){
			idNumber = CLIUtilities.option(vms)+1;

			restockerTaskListScreen = restockerMachinePickerScreen.tryMachine(idNumber);
		}

		listTasks(restockerTaskListScreen);
	}
}
