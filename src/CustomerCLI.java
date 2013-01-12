import java.util.Scanner;

/**
 * CLI for the Customer's perspective.
 * Provides an entry point for the system.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
public class CustomerCLI {


	public static void begin() {
		int cashOrId = CLIUtilities.option("I'm paying with cash", "I'll enter my customer ID");
		switch(cashOrId) {
			case 0:
				System.out.println("This is not yet implemented.");
				break;
			case 1:
				System.out.println("This is not yet implemented.");
				break;
		}
	}

	public static void main(String[] args) {
		System.out.println("Welcome to HCLC, LLC's Smart Vending Machine!\n---\n");
		begin();
	}
}