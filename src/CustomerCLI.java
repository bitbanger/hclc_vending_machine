import java.util.Scanner;

/**
 * CLI for the Customer's perspective.
 * Provides an entry point for the system.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class CustomerCLI
{
	private static void usage()
	{
		System.err.println("USAGE: java -jar CustomerCLI.jar <machine ID>");
	}

	public static void main(String[] args) {
		if(args.length!=1)
		{
			usage();
			System.exit(1);
		}
		
		CustomerLoginScreen backend=null;
		try
		{
			backend=CustomerLoginScreen.buildInstance(Integer.parseInt(args[0]));
		}
		catch(NumberFormatException wrong)
		{
			usage();
			System.err.println("<machine ID> must be an integer");
			System.exit(1);
		}
		if(backend==null)
		{
			usage();
			System.err.println("Supplied <machine ID> invalid; please ensure it is nonnegative");
			System.exit(1);
		}
		
		System.out.println("Welcome to HCLC, LLC's Smart Vending Machine!\n---\n");
		program:while(true)
		{
			CLIUtilities.printTitle("Main Menu");
			int cashOrId = CLIUtilities.option("I'm paying with cash", "I'll enter my customer ID", "I want to exit");
			switch(cashOrId) {
				case 0:
					anonymousConnection(backend.cashLogin());
					break;
				case 1:
					System.out.println("This is not yet implemented.");
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
	private static void anonymousConnection(CashCustomerPurchaseScreen wallet)
	{
		screen:while(true)
		{
			CLIUtilities.printTitle("Cash Payment");
			System.out.println("You've added $"+wallet.getBalance()/100+" to this machine.");
			int addOrSubtract=CLIUtilities.option("I want to insert cash", "I'm ready to purchase an item", "Return to main menu");
			
			switch(addOrSubtract)
			{
				case 0:
					wallet.addCash(CLIUtilities.moneyPrompt("Insert money"));
					break;
				case 1:
					loggedInSuccessfully(wallet);
					break;
				case 2:
					System.out.println("Your change is $"+wallet.getBalance());
					break screen;
			}
		}
	}

	/**
	 * Facility allowing someone with an account to login.
	 * @param account the backend connection
	 */
	private static void loggedInSuccessfully(CustomerPurchaseScreen account)
	{
		/*screen:while(true)
		{*/
			CLIUtilities.printTitle("Product Selection");
			System.out.println("Welcome, "+account.getUser().getName()+"!");
			System.out.println("Available funds: "+account.getBalance()/100);
		//}
	}
}
