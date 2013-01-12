import java.util.Scanner;

/**
 * Utility class for creating a usable CLI.
 * Provides methods to do things like prompt the user, present an options list, etc.
 */
public class CLIUtilities {

	/** Private constructor to disable construction */
	private CLIUtilities() {

	}


	/**
	 * Prompts the user with a given message and returns their response.
	 * The given message is printed verbatim, with no newline, and one line of input is recorded and returned.
	 *
	 * @param  message	The message to show the user, verbatim, with no newline at the end
	 * @return			The user's response to the message. One line of recorded input
	 */
	public static String prompt(String message) {
		System.out.print(message);
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}

	/**
	 * Provides a simple way to get the user to pick an option.
	 * The options passed in as args are listed in numerical order.
	 * Then, the user is prompted for their selection (starting at 1, not 0).
	 * The user is re-prompted for as long as the selection is invalid.
	 * The selected number is then returned, after having 1 subtracted from it.
	 * This is because the programmer should deal with the selection starting from 0, not 1.
	 *
	 * @param  options			Variable arguments list containing all options to display
	 * @return selectedOption	The selected option (numbered from 0, not 1)
	 */
	public static int option(String... options) {
		for(int i = 0; i < options.length; ++i) {
			System.out.println((i+1) + ". " + options[i]);
		}
		System.out.println();

		int selectedOption = -1;
		do {
			try {
				selectedOption = Integer.parseInt(prompt("Select an option: ")) - 1;
			} catch(NumberFormatException e) {
				continue;
			}
		} while(selectedOption < 0 || selectedOption >= options.length);

		return selectedOption;
	}
}