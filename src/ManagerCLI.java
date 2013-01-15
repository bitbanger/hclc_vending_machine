import java.util.LinkedList;
import java.util.ArrayList;

/**
 * CLI for the Customer's perspective.
 * Provides an entry point for the system.
 * @author Lane Lawley <lxl5734@rit.edu>
 * @contributor Piper Chester <pwc1203@rit.edu>
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
				int id = -1;
				while (id < 0)
				{
					try
					{
						id = Integer.parseInt(CLIUtilities.prompt("Enter Your ID (or 0 to quit)"));
					}
					catch(NumberFormatException ex) {}
					if (id < 0)
						System.out.println("Invalid ID");
				}
				if (id == 0)
					return;
				String password = CLIUtilities.prompt("Enter Your Password");
				homeScreen = loginScreen.tryLogin(id, password);
				if (homeScreen == null)
					System.out.println("Invalid ID/password combo");
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

			CLIUtilities.printTitle("Home Screen");

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
				case 4:
					ManagerUserAccountsScreen userAccount = home.manageUsers();
					manageUsers(userAccount);
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
		CLIUtilities.printTitle("Machine Statistic Options");
		if(screen.listMachines().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}

		while (true)
		{
			int choice = CLIUtilities.option(
				"View Statistics For All Machines",
				"View Statistics For Single Machine",
				"Return Home");
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
		ArrayList<Transaction> sales = screen.listSalesAll();
		System.out.println("\n\nAll Sales:");
		CLIUtilities.printCollection(sales);
	}

	/**
	 * Handles the dialog for choosing a vending machine from a collection
	 * @param machines An array list of machines you want the user to select from
	 * @return The vending machine the user selected
	 **/
	private static VendingMachine vmChooser(ArrayList<VendingMachine> machines)
	{
		int choice = CLIUtilities.option(machines);
		return machines.get(choice);
	}

	/**
	 * Handles the dialog for choosing a food item from a collection
	 * @param items An array list of machines you want the user to select from
	 * @return The food item the user selected
	 **/
	private static FoodItem foodItemChooser(ArrayList<FoodItem> items)
	{
		int choice = CLIUtilities.option(items);
		return items.get(choice);
	}

	/**
	 * Handles the dialog for choosing a manager from a collection
	 * @param managers An array list  of managers you want the user to select from
	 * @return The manager the user selected
	 **/
	private static Manager managerChooser(ArrayList<Manager> managers)
	{
		int choice = CLIUtilities.option(managers);
		return managers.get(choice);
	}

	/**
	 * Displays the statistics for a single vending machine
	 **/
	private static void viewStatsMachine(ManagerReportStatsScreen screen)
	{
		ArrayList<VendingMachine> machines = screen.listMachines();
		VendingMachine selected = vmChooser(machines);
		ArrayList<Transaction> sales = screen.listMachineSales(selected);
		System.out.printf("\n\nSales from vending machine %s:\n", selected.toString());
		CLIUtilities.printCollection(sales);
	}

	/**
	 * Handles selecting the vending machine to alter
	 **/
	private static void preAlterLayout(ManagerHomeScreen screen)
	{
		ManagerAlterLayoutScreen alterLayout = new ManagerAlterLayoutScreen();
		alterLayout(alterLayout);
	}

	/**
	 * Handles altering a machine's layout
	 **/
	private static void alterLayout(ManagerAlterLayoutScreen alterLayout)
	{
		if(alterLayout.listMachines().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}

		while (true)
		{
			CLIUtilities.printTitle("Alter Layout Screen");

			int choice = CLIUtilities.option(
				"Change Row",
				"Empty Row",
				"Commit Changes",
				"Return Home");
			switch (choice)
			{
				case 0:
					changeRow(alterLayout);
					break;
				case 1:
					makeRowEmpty(alterLayout);
					break;
				case 2:
					boolean commitSuccess = alterLayout.commitRowChanges();
					if (commitSuccess)
						System.out.println("Changes committed successfully");
					else
						System.out.println("An error occurred while attempting to commit the changes");
					break;
				case 3:
					return;
			}
		}
	}

	private static void printRow( FoodItem row ) {
		if ( row == null )
			System.out.println("Empty row");
		else
			System.out.println( row );
	}

	private static void changeRow(ManagerAlterLayoutScreen alterLayout)
	{
		if(alterLayout.listMachines().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}
		
		FoodItem[][] layout = alterLayout.listRows();
		CLIUtilities.printLayout(layout, false);
		int x = -1;
		int y = -1;
		while (x < 0 || y < 0 || x >= layout.length || y >= layout[0].length)
		{
			x = CLIUtilities.promptInt("Enter X value", true);
			y = CLIUtilities.promptInt("Enter Y value", true);
		}
		printRow(layout[x][y]);
		ArrayList<FoodItem> items = alterLayout.listItems();
		FoodItem item = foodItemChooser(items);
		int success = alterLayout.queueRowChange(new Pair<Integer, Integer>(x,y), item);
		if (success == 0)
			System.out.println("Change queued successfully.\nYou still need to commit the changes before the changes become permanent");
		else if (success == 1)
			System.out.println("This change will cause items to expire quicker than the longest restocking interval. I prevented this change from being made. :)");
		else
			System.out.println("An error occurred while attempting to queue the change");
	}

	private static void makeRowEmpty(ManagerAlterLayoutScreen alterLayout)
	{
		if(alterLayout.listMachines().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}

		FoodItem[][] layout = alterLayout.listRows();
		CLIUtilities.printLayout(layout, false);
		int x = -1;
		int y = -1;
		while (x < 0 || y < 0 || x >= layout.length || y >= layout[0].length)
		{
			x = CLIUtilities.promptInt("Enter X value", true);
			y = CLIUtilities.promptInt("Enter Y value", true);
		}
		int success = alterLayout.queueRowChange(new Pair<Integer, Integer>(x,y), null);
		if (success == 0)
			System.out.println("Change queued successfully.\nYou still need to commit the changes before the changes become permanent");
		else if (success == 1)
			System.out.println("This change will cause items to expire quicker than the longest restocking interval. I prevented this change from being made. :)");
		else
			System.out.println("An error occurred while attempting to queue the change");
	}

	/**
	 * Handles the screen to manage food items
	 **/
	private static void manageItems(ManagerStockedItemsScreen screen)
	{
		while (true)
		{
			
			CLIUtilities.printTitle("Manage Items Screen");

			int choice = CLIUtilities.option(
				"View Items",
				"Add Item",
				"Modify Item",
				"Return Home");
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

	/**
	 * Handles the scree to manage vending machines
	 **/
	private static void manageMachines(ManagerMachineManagementScreen screen)
	{
		while (true)
		{

			CLIUtilities.printTitle("Manage Machines Screen");

			int choice = CLIUtilities.option(
				"View Machines",
				"Add Machine",
				"Reactivate Machine",
				"Deactivate Machine",
				"Change Machine Location",
				"Change Machine Stocking Interval",
				"Return Home");
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
					changeMachineStockingInterval(screen);
					break;
				case 6:
					return;
			}
		}
	}

	/**
	 * Handles adding a machine
	 **/
	private static void addMachine(ManagerMachineManagementScreen screen)
	{
		String state = CLIUtilities.prompt("Enter the state where the machine resides"); // TODO: better location picker
		int zipcode = CLIUtilities.promptInt("Enter the ZIP code of the machine", true);
		LinkedList<String> businesses = new LinkedList<String>();
		while (true)
		{
			String bus = CLIUtilities.prompt("Enter a nearby business (or type DONE to finish)");
			if (bus.equals("DONE"))
				break;
			businesses.add(bus);
		}
		String[] busArray = businesses.toArray(new String[0]);
		
		int restocking = CLIUtilities.promptInt("Restocking interval (days)");
		
		ArrayList<VendingMachine> machs=screen.listMachinessAll();
		VMLayout layout=null;
		if(machs.size()==0)
		{
			int rows=CLIUtilities.promptInt("Enter machine's height (in product rows)");
			int cols=CLIUtilities.promptInt("Enter machine's width (in product columns)");
			int deep=CLIUtilities.promptInt("Enter machine's depth (products per compartment)");
			
			try
			{
				layout=new VMLayout(cols, rows, deep);
			}
			catch(BadArgumentException no)
			{
				System.err.println("addMachine caught invalid row/col/dep?!");
			}
		}
		else
			layout = new VMLayout(screen.listMachinessAll().get(0).getNextLayout());
		
		boolean success = screen.addMachine(zipcode, state, busArray, restocking, layout) != -1;
		if (success)
			System.out.println("Vending machine added successfully");
		else
			System.out.println("An error occurred while attempting to add the vending machine");
	}

	/**
	 * Handles reactivating a machine
	 **/
	private static void reactivateMachine(ManagerMachineManagementScreen screen)
	{
		if(screen.listMachinessAll().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}

		VendingMachine machine = vmChooser(screen.listDeactiveMachines());
		boolean success = screen.reactivateMachine(machine);
		if (success)
			System.out.println("Machine reactivated successfully");
		else
			System.out.println("An error occurred while trying to reactivate the machine");
	}

	/**
	 * Handles deactivating a machine
	 **/
	private static void deactivateMachine(ManagerMachineManagementScreen screen)
	{
		if(screen.listMachinessAll().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}

		VendingMachine machine = vmChooser(screen.listActiveMachines());
		boolean success = screen.deactivateMachine(machine);
		if (success)
			System.out.println("Machine deactivated successfully");
		else
			System.out.println("An error occurred while trying to deactivate the machine");
	}

	/**
	 * Handles changing a machine's location
	 **/
	private static void changeMachineLocation(ManagerMachineManagementScreen screen)
	{
		if(screen.listMachinessAll().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}

		VendingMachine machine = vmChooser(screen.listMachinessAll());

		String state = CLIUtilities.prompt("Enter the state where the machine resides"); // TODO: better location picker
		int zipcode = CLIUtilities.promptInt("Enter the ZIP code of the machine", true);
		
		LinkedList<String> businesses = new LinkedList<String>();
		while (true)
		{
			String bus = CLIUtilities.prompt("Enter a nearby business (or type DONE to finish)");
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

	/**
	 * Handles changing a machine's stocking interval
	 **/
	private static void changeMachineStockingInterval(ManagerMachineManagementScreen screen)
	{
		if(screen.listMachinessAll().size() == 0) {
			System.out.println("No machines currently exist! Please add one to continue.");
			return;
		}

		VendingMachine machine = vmChooser(screen.listMachinessAll());

		System.out.println("Current stocking interval: " + machine.getStockingInterval() + " days");
		int newInterval = CLIUtilities.promptInt("New stocking interval (in days)");

		int returnCode = screen.changeMachineStockingInterval(machine, newInterval);

		switch(returnCode) {
			case -1:
				System.out.println("\nAn error occurred while attempting to change the stocking interval\n");
				break;
			case 0:
				System.out.println("\nStocking interval changed successfully\n");
				break;
			default:
				System.out.println("\nThat stocking interval would cause one of your vending machine items to expire. I've prevented you from setting it :)");
				System.out.println("To fix this, the interval should be <= " + returnCode + " days.\n");
				break;

		}
			
	}

	/**
	 * Handles adding a food item the the system
	 **/
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

	/**
	 * Handles modifying an item in the system
	 **/
	private static void editItem(ManagerStockedItemsScreen screen)
	{
		if(screen.listItems().size() == 0) {
			System.out.println("No food items currently exist! Please add one to continue.");
			return;
		}

		FoodItem item = foodItemChooser(screen.listItems());
		while (true)
		{
			
			CLIUtilities.printTitle("Edit Item Screen");

			System.out.println(item);
			int choice = CLIUtilities.option(
				"Change Name",
				"Change Price",
				"Change Fresh Length",
				"Change Activation Status",
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
						freshLength = CLIUtilities.promptInt("New jtem fresh length (days)");
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
	 * Handles the user management screen
	 **/
	private static void manageUsers(ManagerUserAccountsScreen screen)
	{
		while (true)
		{

			CLIUtilities.printTitle("Manage Users Screen");

			int choice = CLIUtilities.option(
				"Manage Customers",
				"Manage Managers",
				"Return Home");
			switch (choice)
			{
				case 0:
					manageCustomers(screen);
					break;
				case 1:
					manageManagers(screen);
					break;
				case 2:
					return;
			}
		}
	}

	/**
	 * Handles managing customers
	 **/
	private static void manageCustomers(ManagerUserAccountsScreen screen)
	{
		while (true)
		{

			CLIUtilities.printTitle("Manage Customers Screen");

			int choice = CLIUtilities.option(
				"View Customers",
				"Add Customer",
				"Return Home");
			switch (choice)
			{
				case 0:
					System.out.println("Customers");
					CLIUtilities.printCollection(screen.listCustomers());
					break;
				case 1:
					String name = CLIUtilities.prompt("Name");
					int balance = CLIUtilities.moneyPrompt("Initial balance");
					boolean success = screen.addCustomer(name, balance) != -1;
					if (success)
						System.out.println("Customer added successfully");
					else
						System.out.println("An error occurred while attempting to add customer");
					break;
				case 2:
					return;
			}
		}
	}

	/**
	 * Handles managing managers
	 **/
	private static void manageManagers(ManagerUserAccountsScreen screen)
	{
		while (true)
		{

			CLIUtilities.printTitle("Manage Managers Screen");

			int choice = CLIUtilities.option(
				"View Managers",
				"Add Manager",
				"Change Manager's Password",
				"Return Home");
			switch (choice)
			{
				case 0:
					System.out.println("Managers:");
					CLIUtilities.printCollection(screen.listManagers());
					break;
				case 1:
					String name = CLIUtilities.prompt("Name");
					String password = CLIUtilities.prompt("Password");
					boolean addSuccess = screen.addManager(name, password) != -1;
					if (addSuccess)
						System.out.println("Manager added successfully");
					else
						System.out.println("An error occurred while attempting to add the manager");
					break;
				case 2:
					Manager manny = managerChooser(screen.listManagers());
					String newPassword = CLIUtilities.prompt("New Password");
					boolean changeSuccess = screen.changePassword(manny, newPassword);
					if (changeSuccess)
						System.out.println("Password changed successfully");
					else
						System.out.println("An error occurred while attempting to change the password");
					break;
				case 3:
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
		System.out.println("Goodbye!");
	}
}
