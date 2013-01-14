import java.util.Collection;

/**
 * CLI for the Customer's perspective.
 * Provides an entry point for the system.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class ManagerCLI
{
	/**
	 * Handles manager login stuff
	 **/
	public static void login()
	{
		while (true)
		{
			System.out.println("Welcome to HCLC, LLC's Smart Vending Machine Manager Interface!\n---\n");
			ManagerLoginScreen loginScreen = new ManagerLoginScreen();
			ManagerHomeScreen homeScreen = null;
			while (homeScreen == null)
			{
				int id = 0;
				while (id < 1)
				{
					try
					{
						id = Integer.parseInt(CLIUtilities.prompt("Enter your id:\n"));
					}
					catch(NumberFormatException ex) {}
					if (id < 1)
						System.out.println("Invalid id");
				}
				String password = CLIUtilities.prompt("Enter your password:\n");
				homeScreen = loginScreen.tryLogin(id, password);
				if (homeScreen == null)
					System.out.println("Invalid id/password combo");
			}
			home(homeScreen);
		}
	}

	/**
	 * Handles manager home screen
	 **/
	public static void home(ManagerHomeScreen home)
	{
		while (true)
		{
			int choice = CLIUtilities.option(
				"View Machine Statistics",
				"Alter Vending Machine Layout",
				"Manage Products",
				"Manage Machines",
				"Manage Users",
				"Logout");
			switch (choice)
			{
				case 0:
					ManagerReportStatsScreen viewStats = home.viewStats();
					viewStats(viewStats);
					break;
				case 1:
					preAlterLayout();
					break;
				case 6:
					return;
			}
		}
	}

	/**
	 * Handles viewing the statistics
	 **/
	public static void viewStats(ManagerReportStatsScreen screen)
	{
		while (true)
		{
			int choice = CLIUtilities.option(
				"View statistics for all vending machines",
				"View statistics for a single vending machine",
				"Return to home");
			switch (choice)
			{
				case 0:
					viewStatsAll(screen);
					break;
				case 1:
					viewStatsMachine(screen);
					break;
				case 2:
					return;
			}
		}
	}

	/**
	 * Displays the statistics for all vending machines
	 **/
	public static void viewStatsAll(ManagerReportStatsScreen screen)
	{
		Collection<Transaction> sales = screen.listSalesAll();
		System.out.println("\n\nAll Sales:");
		CLIUtilities.printCollection(sales);
	}

	/**
	 * Displays the statistics for a single vending machine
	 **/
	public static void viewStatsMachine(ManagerReportStatsScreen screen)
	{
		Collection<VendingMachine> machines = screen.listMachines();
		CLIUtilities.printCollection(machines);
		VendingMachine selected = null;
		while (selected == null)
		{
			int choice = CLIUtilities.promptInt("Input machine id");
			for (VendingMachine machine : machines)
			{
				try
				{
					if (choice == machine.getId())
					{
						selected = machine;
						break;
					}
				}
				catch (Exception wtf)
				{
					ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.FATAL, wtf);
				}
			}
		}
		Collection<Transaction> sales = screen.listMachineSales(selected);
		System.out.printf("\n\nSales from vending machine %s:\n", selected.toString());
		CLIUtilities.printCollection(sales);
	}

	/**
	 * Handles selecting the vending machine to alter
	 **/
	public static void preAlterLayout()
	{

	}

	/**
	 * Handles altering a machine's layout
	 **/
	public static void alterLayout(ManagerAlterLayoutScreen alterLayout)
	{

	}

	/**
	 * Calls login
	 **/
	public static void main(String[] args)
	{
		login();
	}
}
