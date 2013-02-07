import java.io.PrintStream;
import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Utility class for creating a usable CLI.
 * Provides methods to do things like prompt the user, present an options list, etc.
 */
public class CLIUtilities {

	/**
	 * Validation failure reporter
	 */
	private static final PrintStreamValidationComplainer STDOUT_WRAPPER=new PrintStreamValidationComplainer(System.out);

	/**
	 * Scanner instance
	 **/
	private static final Scanner scan = new Scanner(System.in);

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
		System.out.print(message + ": ");
		return scan.nextLine();
	}
	
	/**
	 * Prompts the user with a given message and returns their obscured response.
	 * This method is identical to prompt(), except echoing is disabled.
	 * In the event of a weird console failure, this method falls back to prompt().
	 * 
	 * @param  message	The message to show the user, verbatim, with no newline at the end
	 * @return			The user's response to the message. One line of recorded, and obscured, input
	 */
	public static String promptObscured(String message) {
		try {
			Console c = System.console();
			if(c != null) {
				return new String(c.readPassword(message + ": "));
			} else {
				throw new Exception();
			}
		} catch(Exception e) {
			return prompt(message);
		}
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
		System.out.println();
		for(int i = 0; i < options.length; ++i) {
			System.out.println((i+1) + ". " + options[i]);
		}
		System.out.println();

		int selectedOption = -1;
		do {
			selectedOption = promptInt("Select an option", new DoublyBoundedNumberFormat(1, options.length+1)) - 1;
		} while(selectedOption < 0 || selectedOption >= options.length);

		return selectedOption;
	}

	/**
	 * Provies a method for users to select from a list of objects.
	 * @param options The array list of objects the user will select from
	 * @return The index of the item (0 based) the user selected
	 **/
	public static int option(ArrayList<? extends Object> options) {
		System.out.println();
		for(int i = 0; i < options.size(); ++i) {
			System.out.println((i+1) + ". " + options.get(i));
		}
		System.out.println();

		int selectedOption = -1;
		do {
			selectedOption = promptInt("Select an option", new DoublyBoundedNumberFormat(1, options.size()+1)) - 1;
		} while(selectedOption < 0 || selectedOption >= options.size());

		return selectedOption;
	}

	/**
	 * shows a new title
	 * @param name the name of the new title
	 */
	public static void printTitle( String name ) {
		System.out.println("        ");
		System.out.println("========");
		System.out.println(name);
		System.out.println("========");
	}

	/**
	 * Provides a simple way to get the user to enter a float for cash amounts
	 * @param prompt the prompt to the user to enter an amount of money 
	 * @return moneys the amount of money returned
	 */
	public static int moneyPrompt( String prompt ) {
		final String failureString = "Please enter a valid positive monetary amount";
		final String moneyTooLarge = "The amount that you entered is too large for this system to handle. Please enter a smaller amount.";

		int moneys = -1;
		do {
			String attempt = prompt(prompt);
			moneys = U.parseMoney(attempt);
			if (moneys == U.BAD_MONEY)
			{
				System.out.println(failureString);
				moneys = -1;
			}
			else if(moneys == U.TOO_MUCH_MONEY)
			{
				System.out.println(moneyTooLarge);
				moneys = -1;
			}
		} while (moneys < 0);
		return moneys;
	} 

	/**
	 * Provides a simple way to get a <b>positive</b> integer.
	 * @param prompt the prompt to the screen
	 * @return the integer, which is guaranteed to be valid
	 */
	public static int promptInt(String prompt)
	{
		return promptInt(prompt, MinValueNumberFormat.ONE);
	}

	/**
	 * Provides a way to get a nonnegative integer, optionally including zero.
	 * @param prompt the prompt to the screen
	 * @param allowZero whether to allow zero
	 * @return the integer, which is guaranteed to be valid
	 */
	public static int promptInt(String prompt, boolean allowZero)
	{
		return promptInt(prompt, allowZero ? MinValueNumberFormat.ZERO : MinValueNumberFormat.ONE);
	}

	/**
	 * Provides a flexible way to get a valid integer.
	 * @param prompt the prompt to the screen
	 * @return the integer, which is guaranteed to be valid
	 */
	public static int promptInt(String prompt, NumberFormat condition)
	{
		return promptIntDefault(prompt, condition, null);
	}

	/**
	 * Prompts for a <b>positive</b> integer, using a default if nothing is provided.
	 * @param prompt the prompt to the screen
	 * @param defVal the default integer value, or <tt>null</tt> for none (mandatory input)
	 * @return the integer, which is guranteed to be valid  
	 */
	public static int promptIntDefault(String prompt, int defVal)
	{
		return promptIntDefault(prompt, MinValueNumberFormat.ONE, defVal);
	}

	/**
	 * Prompts for an integer, using a default if nothing is provided.
	 * @param prompt the prompt to the screen
	 * @param condition the condition satisfied for valid inputs
	 * @param defVal the default integer value, or <tt>null</tt> for none (mandatory input)
	 * @return the integer, which is guranteed to be valid  
	 */
	private static int promptIntDefault(String prompt, NumberFormat condition, Integer defVal)
	{
		NumberFormat.setCommunicator(STDOUT_WRAPPER);
		
		boolean invalid=false;
		int choice=-1;
		
		do
		{
			String input;
			
			invalid=false;
			input=prompt(prompt+(defVal==null ? "" : " (enter for default "+defVal+")"));
			
			try
			{
				choice=Integer.parseInt(input);
			}
			catch(NumberFormatException nai)
			{
				if(defVal!=null && input.equals("")) //default available and "requested"
					return defVal;
				else
				{
					System.out.println("Please enter only an integral number");
					invalid=true;
					continue; //we can't allow this to check the condition, since -1 might be valid
				}
			}
		}
		while(!condition.checkLoudly(choice, invalid));
		
		return choice;
	}

	/**
	 * Prompts the user (Y/N) for a message.
	 *
	 * @param  prompt	The message to display to the user before they select Y/N
	 * @return			True if the user said "y" or "Y", false if the user said "n" or "N"
	 */
	public static boolean yesOrNo(String prompt) {
		String response;
		do {
			response = CLIUtilities.prompt(prompt + " (Y/N)");
		} while(!response.toLowerCase().equals("y") && !response.toLowerCase().equals("n"));

		return response.toLowerCase().equals("y");
	}

	/** 
	 * Provides a way to print a collection
	 * @param col the collection to print
	 */
	public static void printCollection( Collection<? extends Object> col ) {
		for ( Object next : col )
			System.out.println( next.toString() );
	}

	/**
	 * Provides a nice way to print a layout
	 * @param rows The layout to print
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(FoodItem[][] rows, boolean hideInactive)
	{
		int height = rows[0].length;
		int width = rows.length;
		for (int i=0;i<height;++i)
		{
			for (int j=0;j<width;++j)
			{
				System.out.printf("(%d, %d) ", j, i);
				FoodItem item = rows[j][i];
				String name = "EMPTY";
				if (item != null) {
					if(item.isActive() || !hideInactive) {
						name = item.getName();
					} else {
						name = "INACTIVE";
					}
				}
				System.out.printf("%-10s ", name);
			}
			System.out.println();
			for (int j=0;j<width;++j)
			{
				FoodItem item = rows[j][i];
				String price = "";
				if (item != null && item.isActive())
					price = U.formatMoney(item.getPrice());
				else if (item != null && !hideInactive)
					price = "(INACTIVE)";
				System.out.printf("       %10s ", price);
			}
			System.out.println();
			System.out.println();
		}
	}

	/**
	 * Provides a nice way to print a layout. Hides inactive items
	 * @param rows The layout to print
	 **/
	public static void printLayout(FoodItem[][] rows)
	{
		printLayout(rows, true);
	}

	/**
	 * Provides a nice way to print a layout
	 * Note: Not tested
	 * @param rows The layout to print
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(Row[][] rows, boolean hideInactive)
	{
		if (rows.length == 0 || rows[0].length == 0)
			return;
		FoodItem[][] temp = new FoodItem[rows.length][rows[0].length];
		for (int i=0;i<rows.length;++i)
		{
			Row[] column = rows[i];
			for (int j=0;j<column.length;++j)
			{
				Row row = column[j];
				FoodItem item = null;
				if (row != null)
					item = row.getProduct();
				temp[i][j] = item;
			}
		}
		printLayout(temp, hideInactive);
	}

	/**
	 * Provides a nice way to print a layout. Hides inactive items.
	 * @param rows The layout to print
	 **/
	public static void printLayout(Row[][] rows)
	{
		printLayout(rows, true);
	}

	/**
	 * Provides a nice way to print a layout
	 * Note: Not tested
	 * @param layout The layout to print
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(VMLayout layout, boolean hideInactive)
	{
		printLayout(layout.getRows(), hideInactive);
	}

	/**
	 * Provides a nice way to print a layout. Hides inactive items.
	 * @param layout The layout to print
	 **/
	public static void printLayout(VMLayout layout)
	{
		printLayout(layout, true);
	}

	/**
	 * Provides a wrapper for reporting validation errors to a print stream.
	 */
	private static class PrintStreamValidationComplainer implements ValidationComplainer
	{
		/** The wrapped stream. */
		private PrintStream stream;

		/**
		 * Constructor.
		 * @param stream to wrap
		 */
		public PrintStreamValidationComplainer(PrintStream stream)
		{
			this.stream=stream;
		}

		/** @inheritDoc */
		public void alert(String message)
		{
			stream.println(message);
		}
	}
}
