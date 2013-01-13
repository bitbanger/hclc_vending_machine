/**
 * CLI for the Restocker's perspective.
 * Provides an entry point for the Restocker.
 * @author Piper Chester <pwc1203@rit.edu>
 */
public class RestockerCLI {

	/**
	 * Begins the program and intitates primary options for the Restocker.
	 */
	private static void begin() {
		int optionSelect = CLIUtilities.option("I'm viewing the to-do list", "I'm selecting a machine");

		switch(optionSelect) {
			case 0:
				viewList();
				break;
			case 1:
				selectMachine();
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

	/** Allows the Restocker to view the list of duties that needs to be performed.
	 *
	 */
	private static void viewList(){
		System.out.println("This is the list of duties that need to be performed.");	

	}
	
	/**
	 * Allows the Restocker to select a machine.
	 *
	 */
	private static void selectMachine() {
		System.out.println("Please enter the number of the machine that needs to be selected.");
	
	}
}
