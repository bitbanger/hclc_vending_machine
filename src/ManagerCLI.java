import java.util.Collection;
import java.util.LinkedList;

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
						id = Integer.parseInt(CLIUtilities.prompt("Enter your id"));
					}
					catch(NumberFormatException ex) {}
					if (id < 1)
						System.out.println("Invalid id");
				}
				String password = CLIUtilities.prompt("Enter your password");
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
				case 2:
					ManagerStockedItemsScreen itemsScreen = home.manageItems();
					manageItems(itemsScreen);
					break;
				case 3:
					ManagerMachineManagementScreen machinesScreen = home.manageMachines();
					manageMachines(machinesScreen);
					break;
				case 5:
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

	private static void manageItems(ManagerStockedItemsScreen screen)
	{
		while (true)
		{
			int choice = CLIUtilities.option(
				"View Items",
				"Add Item",
				"Modify Item",
				"Exit");
			switch (choice)
			{
				case 0:
					System.out.println("Items:");
					CLIUtilities.printCollection(screen.listItems());
					break;
				case 1:
					addItem(screen);
					break;
				case 2:
					editItem(screen);
					break;
				case 3:
					return;
			}
		}
	}

	private static void manageMachines(ManagerMachineManagementScreen screen)
	{
		while (true)
		{
			int choice = CLIUtilities.option(
				"View Machines",
				"Add Machine",
				"Reactivate Machine",
				"Deacivate Machine",
				"Change Machine Location",
				"Exit");
			switch (choice)
			{
				case 0:
					System.out.println("Machines:");
					CLIUtilities.printCollection(screen.listMachinessAll());
					break;
				case 1:
					addMachine(screen);
					break;
				case 2:
					reactivateMachine(screen);
					break;
				case 3:
					deactivateMachine(screen);
					break;
				case 4:
					changeMachineLocation(screen);
					break;
				case 5:
					return;
			}
		}
	}

	private static void addMachine(ManagerMachineManagementScreen screen)
	{
		String state = CLIUtilities.prompt("State"); // TODO: better location picker
		int zipcode = CLIUtilities.promptInt("Zip Code");
		LinkedList<String> businesses = new LinkedList<String>();
		while (true)
		{
			String bus = CLIUtilities.prompt("Enter nearby business (or type DONE to finish)");
			if (bus.equals("DONE"))
				break;
			businesses.add(bus);
		}
		String[] busArray = businesses.toArray(new String[0]);
		
		int restocking = -1;
		while (restocking <= 0)
			restocking = CLIUtilities.promptInt("Restocking Interval (days)");
		VMLayout layout = screen.listMachinessAll().get(0).getNextLayout(); // TODO: only works if at least one machine is in database. we need to make default
		boolean success = screen.addMachine(zipcode, state, busArray, restocking, layout) != -1;
		if (success)
			System.out.println("Vendng machine added successfully");
		else
			System.out.println("An error occured while attempting to add the vending machine");
	}

	private static void reactivateMachine(ManagerMachineManagementScreen screen)
	{
		VendingMachine machine = vmChooser(screen.listDeactiveMachines());
		boolean success = screen.reactivateMachine(machine);
		if (success)
			System.out.println("Machine reactivated successfully");
		else
			System.out.println("An error occurred while trying to reactivate the machine");
	}

	private static void deactivateMachine(ManagerMachineManagementScreen screen)
	{
		VendingMachine machine = vmChooser(screen.listActiveMachines());
		boolean success = screen.deactivateMachine(machine);
		if (success)
			System.out.println("Machine deactivated successfully");
		else
			System.out.println("An error occurred while trying to deactivate the machine");
	}

	private static void changeMachineLocation(ManagerMachineManagementScreen screen)
	{
		VendingMachine machine = vmChooser(screen.listMachinessAll());

		String state = CLIUtilities.prompt("State"); // TODO: better location picker
		int zipcode = CLIUtilities.promptInt("Zip Code");
		LinkedList<String> businesses = new LinkedList<String>();
		while (true)
		{
			String bus = CLIUtilities.prompt("Enter nearby business (or type DONE to finish)");
			if (bus.equals("DONE"))
				break;
			businesses.add(bus);
		}
		String[] busArray = businesses.toArray(new String[0]);

		boolean success = screen.changeMachineLocation(machine, zipcode, state, busArray);
		if (success)
			System.out.println("Location changed successfully");
		else
			System.out.println("An error occurred while attempting to change the location");
	}

	private static void addItem(ManagerStockedItemsScreen screen)
	{
		String name = CLIUtilities.prompt("New item name");
		int price = CLIUtilities.moneyPrompt("New item price");
		long freshLength = -1;
		while (freshLength <= 0)
			freshLength = CLIUtilities.promptInt("New item fresh length (days)");
		boolean failure = screen.addItem(name, price, freshLength) == ManagerStockedItemsScreen.FAILURE_KEY;
		if (!failure)
			System.out.println("New item added successfully");
		else
			System.out.println("An error occurred while trying to add a new item");
	}

	private static void editItem(ManagerStockedItemsScreen screen)
	{
		FoodItem item = foodItemChooser(screen.listItems());
		while (true)
		{
			System.out.println(item);
			int choice = CLIUtilities.option(
				"Change name",
				"Change price",
				"Change fresh length",
				"Change activation status",
				"Done");
			boolean success = false;
			switch (choice)
			{
				case 0:
					String name = CLIUtilities.prompt("New name");
					success = screen.changeItemName(item, name);
					break;
				case 1:
					int price = CLIUtilities.moneyPrompt("New price");
					success = screen.changeItemPrice(item, price);
					break;
				case 2:
					long freshLength = -1;
					while (freshLength <= 0)
						freshLength = CLIUtilities.promptInt("New item fresh length (days)");
					success = screen.changeItemFreshLength(item, freshLength);
					break;
				case 3:
					boolean active = CLIUtilities.option(
						"Active",
						"Inactive") != 1;
					success = screen.changeItemStatus(item, active);
					break;
				case 4:
					return;
			}
			if (success)
				System.out.println("Item changed successfully");
			else
				System.out.println("An error occurred while attempting to change item");
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
