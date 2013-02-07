import java.util.ArrayList;

/**
 * CLI for the Customer's perspective.
 * Provides an entry point for the system.
 * @author Lane Lawley <lxl5734@rit.edu>
 * @author Kyle Savarese <kms7341@rit.edu>
 * @author Sol Boucher <slb1566@rit.edu>
 * @author Piper Chester <pwc1203@rit.edu>
 */
public class CustomerCLI {
	
	/**
	 * Prints out an error message if the user is trying to start a machine without an ID."
	 */
	private static void usage() {
		System.err.println("In order to start a machine, you need to enter a valid ID.");
	}

	/**
	 * The main entry point to the program.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			usage();
			System.exit(1);
		}
		
		CustomerLoginScreen backend = null;
		try {
			backend = CustomerLoginScreen.buildInstance(Integer.parseInt(args[0]));
		}
		catch(NumberFormatException wrong) {
			usage();
			System.err.println("<machine ID> must be an integer");
			System.exit(1);
		}
		if(backend == null) {
			usage();
			System.err.println("Supplied <machine ID> is either invalid or inactive");
			System.exit(1);
		}
		
		System.out.println("Welcome to HCLC, LLC's Smart Vending Machine!\n---\n");
		program:while(true) {
			CLIUtilities.printTitle("Main Menu");
			int cashOrId = CLIUtilities.option("I'm paying with cash", "I'll enter my customer ID", "I want to exit");
			switch(cashOrId) {
				case 0:
					anonymousConnection(backend.cashLogin());
					break;
				case 1:
					CustomerPurchaseScreen next = null;
					do {
						int id = CLIUtilities.promptInt("Please enter your customer ID");
						next = backend.tryLogin(id);
					} while (next == null);
					productSelection(next);
					break;
				case 2:
					break program;
			}
		}
		System.out.println("Goodbye!");
	}

	/**
	 * Facility allowing someone without an account to use the machine.
	 * @param wallet the backend connection
	 */
	private static void anonymousConnection(CashCustomerPurchaseScreen wallet) {
		screen:while(true) {
			CLIUtilities.printTitle("Cash Payment");
			System.out.println("You've added " + U.formatMoney(wallet.getBalance()) + " to this machine.");
			int addOrSubtract = CLIUtilities.option("I want to insert cash", "I'm ready to purchase an item", "Return to main menu");
			
			switch(addOrSubtract) {
				case 0:
					wallet.addCash(CLIUtilities.moneyPrompt("Insert money"));
					break;
				case 1:
					productSelection(wallet);
					break;
				case 2:
					System.out.println("Your change is " + U.formatMoney(wallet.getBalance()));
					break screen;
			}
		}
	}

	/**
	 * Facility allowing someone with an account to login.
	 * @param account the backend connection
	 */
	private static void productSelection(CustomerPurchaseScreen account) {
		FoodItem[][] display = account.listLayout();
		screen:while(true) {
			CLIUtilities.printTitle("Product Selection");
			System.out.println("Welcome, " + account.getUserName() + "!");
			System.out.println("Available funds: " + U.formatMoney(account.getBalance()));

			int choice = CLIUtilities.option(
				"Choose from vending machine layout",
				"Choose from frequently bought items",
				"Cancel purchase");
			switch (choice)
			{
				case 0:
					CLIUtilities.printLayout(display);

					if(CLIUtilities.yesOrNo("Would you like to proceed with your purchase?")) {
						
						String message=account.tryPurchase(new Pair<Integer, Integer>(CLIUtilities.promptInt("Enter X", true), CLIUtilities.promptInt("Enter Y", true)));
						if(message.equals("Good")) {
							System.out.println("Purchase complete: remaining balance is " + U.formatMoney(account.getBalance()));
							break screen;
						}
						else
							CLIUtilities.prompt(message + " please press enter to continue");
					} else {
						break screen; // BREAK THE SCREEN
					}
					break;
				case 1:
					ArrayList<FoodItem> items = account.getFrequentlyBought();
					if (items.size() == 0)
					{
						CLIUtilities.prompt("You have no frequently bought items!\nplease press enter to continue");
						break;
					}
					int chosenIndex = CLIUtilities.option(items);
					String freqMessage = account.tryPurchase(items.get(chosenIndex));
					if (freqMessage.equals("Good"))
					{
						System.out.println("Purchase complete: remaining balance is " + U.formatMoney(account.getBalance()));
						break screen;
					}
					else
					{

						CLIUtilities.prompt(freqMessage + " please press enter to continue");
					}
					break;
				case 2:
					break screen;
			}
		}
	}
}
