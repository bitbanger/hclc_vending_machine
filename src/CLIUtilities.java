import java.util.Scanner;
import java.util.*;

/**
 * Utility class for creating a usable CLI.
 * Provides methods to do things like prompt the user, present an options list, etc.
 */
public class CLIUtilities {

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
			selectedOption = promptInt("Select an option") - 1;
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
			selectedOption = promptInt("Select an option") - 1;
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
	 * @param prompt 
	 * @return moneys the amount of money returned
	 */
	public static int moneyPrompt( String prompt ) {
		float moneys = -1;
		do {
			try {
				moneys = Float.parseFloat(prompt(prompt) );
				moneys *= 100;
				
				if(moneys<0)
				{
					moneys = -1;
					System.out.println("Please don't enter negative monetary amounts");
					continue;
				}
				else if(Math.abs(moneys-(int)moneys)>Math.pow(10, -23))
				{
					moneys = -1;
					System.out.println("Please use only two places after the decimal");
					continue;
				}
			} catch (NumberFormatException e) {
				moneys = -1;
				System.out.println("Please only enter valid real numbers");
				continue;
			}
		} while (moneys < 0);
		return (int)moneys;
	} 

	/**
	 * Provides a simple way to get a <b>positive</b> integer.
	 * @param prompt the prompt to the screen
	 * @return the integer, which is guaranteed to be valid
	 */
	public static int promptInt(String prompt)
	{
		return promptInt(prompt, false);
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
	 * @param condition the condition satisfied for valid inputs
	 * @return the integer, which is guaranteed to be valid
	 */
	public static int promptInt(String prompt, NumberFormat condition)
	{
		boolean invalid=false;
		int choice=-1;
		
		do
		{
			invalid=false;
			try
			{
				choice=Integer.parseInt(prompt(prompt));
			}
			catch(NumberFormatException nai)
			{
				System.out.println("Please enter only an integral number");
				invalid=true;
				continue; //we can't allow this to check the condition, since -1 might be valid
			}
		}
		while(!condition.checkLoudly(choice, invalid));
		
		return choice;
	}

	public static int promptIntDefault(String prompt, int defVal) {
		int theInt = -1;
		do {
			try {
				String thePrompt = prompt(prompt + " (default " + defVal + ")");
				if(thePrompt.equals("")) {
					return defVal;
				}

				theInt = Integer.parseInt(thePrompt);
			} catch(NumberFormatException e) {
				theInt = -1;
				continue;
			}
		} while(theInt < 0);
		return theInt;
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
	 * Allows easily formatting quantities of money.
	 * @param amount the amount of money, in cents
	 */
	public static String formatMoney(int amount)
	{
		String amt=String.format("%03d", amount);
		int split=amt.length()-2;
		
		return "$"+amt.substring(0, split)+'.'+amt.substring(split);
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
					price = formatMoney(item.getPrice());
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
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(VMLayout layout)
	{
		printLayout(layout, true);
	}

	/**
	 * Base class for number format functors.
	 * These are used to customize integral input validation.
	 */
	private static abstract class NumberFormat
	{
		/**
		 * Checks whether the specified number classifies as valid input.
		 * @param input the guess to validate
		 * @return whether it is considered valid
		 */
		public abstract boolean validate(int input);

		/**
		 * Prints the validation error associated with a bad value.
		 * @return a message to be printed on bad input
		 */
		public abstract String toString();

		/**
		 * Checks the input and maybe prints the output.
		 * Validates a number and prints the validation error only on failure.
		 * @param input the number to be validated
		 * @param cont whether to skip the check and return false
		 * @return the result of the validation
		 */
		public final boolean checkLoudly(int input, boolean cont)
		{
			if(cont)
				return false;
			
			boolean valid=validate(input);
			
			if(!valid)
				System.out.println(this);
			
			return valid;
		}
	}

	/**
	 * Number format functor specifying a particular minimum acceptable number.
	 */
	public static class MinValueNumberFormat extends NumberFormat
	{
		/** An instance allowing any nonnegative number. */
		public static final MinValueNumberFormat ZERO=new MinValueNumberFormat(0);

		/** An instance allowing only positive numbers. */
		public static final MinValueNumberFormat ONE=new MinValueNumberFormat(1);

		/** The lowest number allowed to validate successfully. */
		private final int MINIMUM;

		/**
		 * Constructor.
		 * Sets the minimum value to be used.
		 * @param minimum the lowest number to be accepted
		 */
		public MinValueNumberFormat(int minimum)
		{
			this.MINIMUM=minimum;
		}

		/** {@inheritDoc} */
		@Override
		public boolean validate(int input)
		{
			return input>=MINIMUM;
		}

		/** {@inheritDoc} */
		public String toString()
		{
			if(this.MINIMUM==ZERO.MINIMUM)
				return "Please enter only a nonnegative number";
			else if(this.MINIMUM==ONE.MINIMUM)
				return "Please enter only a positive number";
			else
				return "Please enter only a number greater than or equal to "+MINIMUM;
		}
	}
}
