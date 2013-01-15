import java.util.Collection;
import java.util.Scanner;

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
		CLIUtilities.printTitle("Welcome to the Restocker interface!");
		pickMachine(restockerMachinePickerScreen);
	}

	/** Allows the Restocker to view the list of tasks that needs to be performed.
	 *
	 * @param restockerTaskListScreen The state of the RestockerTaskListScreen
	 */
	private static void listTasks(RestockerTaskListScreen restockerTaskListScreen) {
		String[] tasks;

		CLIUtilities.printTitle("List of Tasks to Perform");
	
		tasks = restockerTaskListScreen.assembleStockingList();

		for (String task : tasks)
			System.out.println(task);

		while(true) {
			String response = CLIUtilities.prompt("Please enter DONE when the tasks are completed, or QUIT to quit");

			if(response.toLowerCase().equals("done")) {
				restockerTaskListScreen.completeStocking();
				System.out.println("========\nRestocking completed.");
				break;
			}

			if(response.toLowerCase().equals("quit")) {
				System.out.println("========\nRestocking NOT completed.");
				break;
			}
		}
	}
	
	/**
	 * Allows the Restocker to select a machine.
	 *
	 * @param restockerMachinePickerScreen The state of the RestockerMachinePickerScreen
	 */
	private static void pickMachine(RestockerMachinePickerScreen restockerMachinePickerScreen) {
		Collection<VendingMachine> vms = RestockerMachinePickerScreen.listActiveMachines();

		int idNumber;
	
		CLIUtilities.printTitle("Please Pick a Machine");
		
		CLIUtilities.printCollection(vms);

			
		RestockerTaskListScreen restockerTaskListScreen = null;

		while (restockerTaskListScreen == null){
			idNumber = CLIUtilities.promptInt("Please give ID number");

			restockerTaskListScreen = restockerMachinePickerScreen.tryMachine(idNumber);
		}
		

		listTasks(restockerTaskListScreen);
	}
}
