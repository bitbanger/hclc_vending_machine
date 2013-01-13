/**
 * CLI for the Restocker's perspective.
 * Provides an entry point for the Restocker.
 * @author Piper Chester <pwc1203@rit.edu>
 */
public class RestockerCLI {

	/** Instantiating a new RestockerMachinePickerScreen*/
	private static RestockerMachinePickerScreen restockerMachinePickerScreen = new RestockerMachinePickerScreen();

	/** Instatntiating a new RestockerTaskListScreen */
	private static RestockerTaskListScreen restockerTaskListScreen;

	/**
	 * Begins the program and intitates primary options for the Restocker.
	 */
	private static void begin() {
		int optionSelect = CLIUtilities.option("I'm viewing the to-do list", "I'm selecting a machine");

		switch(optionSelect) {
			case 0:
				listTasks(restockerTaskListScreen);
				break;
			case 1:
				pickMachine(restockerMachinePickerScreen);
				break;
		}
	}	
	/** The entry point to the program.
	 * 
	 * @param args Arguments required to create String arrays in Java
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to the Restocker interface!\n---\n");
		begin();
	}

	/** Allows the Restocker to view the list of tasks that needs to be performed.
	 *
	 * @param restockerTaskListScreen The state of the RestockerTaskListScreen
	 */
	private static void listTasks(RestockerTaskListScreen restockerTaskListScreen){
		String[] tasks;

		System.out.println("This is the list of tasks that need to be performed.");
	
		tasks = restockerTaskListScreen.assembleStockingList();

		for (String task : tasks)
			System.out.println(task);
	}
	
	/**
	 * Allows the Restocker to select a machine.
	 *
	 * @param restockerMachinePickerScreen The state of the RestockerMachinePickerScreen
	 */
	private static void pickMachine(RestockerMachinePickerScreen restockerMachinePickerScreen) {
		System.out.println("Please enter the number of the machine that needs to be selected.");
		

	}
}
