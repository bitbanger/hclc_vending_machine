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
	private static void login()
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
	private static void home(ManagerHomeScreen home)
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
					preAlterLayout(home);
					break;
				case 6:
					return;
			}
		}
	}

	/**
	 * Handles viewing the statistics
	 **/
	private static void viewStats(ManagerReportStatsScreen screen)
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
	private static void viewStatsAll(ManagerReportStatsScreen screen)
	{
		Collection<Transaction> sales = screen.listSalesAll();
		System.out.println("\n\nAll Sales:");
		CLIUtilities.printCollection(sales);
	}

	private static VendingMachine vmChooser(Collection<VendingMachine> machines)
	{
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
		return selected;
	}

	private static FoodItem foodItemChooser(Collection<FoodItem> items)
	{
		CLIUtilities.printCollection(items);
		FoodItem selected = null;
		while (selected == null)
		{
			int choice = CLIUtilities.promptInt("Input food item id");
			for (FoodItem item : items)
			{
				try
				{
					if (choice == item.getId())
					{
						selected = item;
						break;
					}
				}
				catch (Exception wtf)
				{
					ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.FATAL, wtf);
				}
			}
		}
		return selected;
	}

	/**
	 * Displays the statistics for a single vending machine
	 **/
	private static void viewStatsMachine(ManagerReportStatsScreen screen)
	{
		Collection<VendingMachine> machines = screen.listMachines();
		VendingMachine selected = vmChooser(machines);
		Collection<Transaction> sales = screen.listMachineSales(selected);
		System.out.printf("\n\nSales from vending machine %s:\n", selected.toString());
		CLIUtilities.printCollection(sales);
	}

	/**
	 * Handles selecting the vending machine to alter
	 **/
	private static void preAlterLayout(ManagerHomeScreen screen)
	{
		Collection<VendingMachine> machines = screen.displayVendingMachines();
		VendingMachine selected = vmChooser(machines);
		ManagerAlterLayoutScreen alterLayout = new ManagerAlterLayoutScreen(selected);
		alterLayout(alterLayout);
	}

	/**
	 * Handles altering a machine's layout
	 **/
	private static void alterLayout(ManagerAlterLayoutScreen alterLayout)
	{
		while (true)
		{
			int choice = CLIUtilities.option(
				"Change another row",
				"Commit changes",
				"Exit");
			switch (choice)
			{
				case 0:
					FoodItem[][] layout = alterLayout.listRows();
					CLIUtilities.printLayout(layout);
					int x = -1;
					int y = -1;
					while (x < 0 || y < 0 || x >= layout.length || y >= layout[0].length)
					{
						x = CLIUtilities.promptInt("X Value:");
						y = CLIUtilities.promptInt("Y Value:");
					}
					System.out.println(layout[y][x]);
					Collection<FoodItem> items = alterLayout.listItems();
					FoodItem item = foodItemChooser(items);
					boolean success = alterLayout.queueRowChange(new Pair<Integer, Integer>(y,x), item);
					if (success)
						System.out.println("Change queued successfully.\nYou still need to commit the changes before the changes become permanent");
					else
						System.out.println("An error occurred while attempting to queue the change");
					break;
				case 1:
					boolean commitSuccess = alterLayout.commitRowChanges();
					if (commitSuccess)
						System.out.println("Changes committed successfully");
					else
						System.out.println("An error occurred while attempting to commit the changes");
				case 2:
					return;
			}
		}
	}

	/**
	 * Calls login
	 **/
	public static void main(String[] args)
	{
		login();
	}
}
