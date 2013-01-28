import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * unit test suite for RestockerTaskListScreen
 * @author Kyle Savarese <kms7341@rit.edu>
 */
@RunWith(JUnit4.class)

public class RestockerTaskListScreenTest {

	/** The Database instance */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	@Test
	public void testAssembler() throws BadArgumentException, SQLException, BadStateException {
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		FoodItem animalCrackers=new FoodItem("Animal crackers", 150, 60);
		FoodItem condom=new FoodItem("Condom", 175, 350);
		FoodItem grapefruit=new FoodItem("Live grapefruit", 415, 6);
		FoodItem petIguana=new FoodItem("Pet iguana", 8469, 4);
		GregorianCalendar first=new GregorianCalendar(), second=new GregorianCalendar(), third=new GregorianCalendar(), fourth=new GregorianCalendar();
		first.add(first.DAY_OF_YEAR, 60);
		second.add(second.DAY_OF_YEAR, 350);
		third.add(third.DAY_OF_YEAR, 6);
		fourth.add(fourth.DAY_OF_YEAR, 4);
		Row[][] grid={	{new Row(animalCrackers, 8, first), new Row(grapefruit, 5, third)},
						{new Row(petIguana, 1, fourth), new Row(condom, 11, second)}	};
		VMLayout spread=new VMLayout(grid, 12);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		dispenser.getCurrentLayout().getRows()[0][0].decrementRemainingQuantity();
		for(int count=1; count<=6; ++count)
			dispenser.getCurrentLayout().getRows()[1][1].decrementRemainingQuantity();
		Row temp=dispenser.getNextLayout().getRows()[0][1];
		dispenser.getNextLayout().getRows()[0][1]=dispenser.getNextLayout().getRows()[1][0];
		dispenser.getNextLayout().getRows()[1][0]=temp;
		
		RestockerTaskListScreen hope = new RestockerTaskListScreen( dispenser );
		
		HashMap<Integer, Pair<String, Boolean>> bla = hope.getInstructions();
		ArrayList<String> inst = new ArrayList<String>();
		for ( Pair<String, Boolean> in : bla.values() ) {
			inst.add( in.first );
		}

		ArrayList<String> counter = new ArrayList<String>();
		counter.add("Remove all from 0, 1");
		counter.add("Add 12 Pet iguana to location 0, 1");
		counter.add("Remove all from 1, 0");
		counter.add("Add 12 Live grapefruit to location 1, 0");
		Assert.assertTrue( counter.equals( inst ) );
		Assert.assertFalse( hope.completeStocking() );
	} 

	@Test
	public void testStockingComplete() throws BadArgumentException, SQLException, BadStateException{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		FoodItem animalCrackers=new FoodItem("Animal crackers", 150, 60);
		FoodItem condom=new FoodItem("Condom", 175, 350);
		FoodItem grapefruit=new FoodItem("Live grapefruit", 415, 6);
		FoodItem petIguana=new FoodItem("Pet iguana", 8469, 4);
		db.updateOrCreateFoodItem( animalCrackers );
		db.updateOrCreateFoodItem( condom );
		db.updateOrCreateFoodItem( grapefruit );
		db.updateOrCreateFoodItem( petIguana );
		GregorianCalendar first=new GregorianCalendar(), second=new GregorianCalendar(), third=new GregorianCalendar(), fourth=new GregorianCalendar();
		first.add(first.DAY_OF_YEAR, 60);
		second.add(second.DAY_OF_YEAR, 350);
		third.add(third.DAY_OF_YEAR, 6);
		fourth.add(fourth.DAY_OF_YEAR, 4);
		Row[][] grid={	{new Row(animalCrackers, 8, first), new Row(grapefruit, 5, third)},
						{new Row(petIguana, 1, fourth), new Row(condom, 11, second)}	};
		VMLayout spread=new VMLayout(grid, 12);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		dispenser.getCurrentLayout().getRows()[0][0].decrementRemainingQuantity();
		for(int count=1; count<=6; ++count)
			dispenser.getCurrentLayout().getRows()[1][1].decrementRemainingQuantity();
		Row temp=dispenser.getNextLayout().getRows()[0][1];
		dispenser.getNextLayout().getRows()[0][1]=dispenser.getNextLayout().getRows()[1][0];
		dispenser.getNextLayout().getRows()[1][0]=temp;
		
		RestockerTaskListScreen hope = new RestockerTaskListScreen( dispenser );
			
		HashMap<Integer, Pair<String, Boolean>> bla = hope.getInstructions();
		ArrayList<String> inst = new ArrayList<String>();
		for ( Pair<String, Boolean> next : bla.values() ) {
			inst.add(next.first);
		}
		/*System.err.println();
		System.err.println("=========");
		System.err.println("INSTRUCTIONS: " + inst);
		System.err.println("=========");*/
		
		int instCount = bla.size();
		ArrayList<Integer> helper = new ArrayList<Integer>();
		for ( Integer i : bla.keySet() ) {
			helper.add( i );
		}
		for ( Integer i : helper ) {
			hope.removeInstruction( i );
		}

		Assert.assertTrue( hope.completeStocking() );
		
		Assert.assertEquals(dispenser.getCurrentLayout().getRows()[0][0].getRemainingQuantity(), 7);
		Assert.assertEquals(dispenser.getNextLayout().getRows()[0][0].getRemainingQuantity(), 7);
		Assert.assertEquals(dispenser.getCurrentLayout().getRows()[0][1].getRemainingQuantity(), 12);
		Assert.assertEquals(dispenser.getNextLayout().getRows()[0][1].getRemainingQuantity(), 12);
		Assert.assertEquals(dispenser.getCurrentLayout().getRows()[1][0].getRemainingQuantity(), 12);
		Assert.assertEquals(dispenser.getNextLayout().getRows()[1][0].getRemainingQuantity(), 12);
		Assert.assertEquals(dispenser.getCurrentLayout().getRows()[1][1].getRemainingQuantity(), 5);
		Assert.assertEquals(dispenser.getNextLayout().getRows()[1][1].getRemainingQuantity(), 5);
		
		Assert.assertTrue( dispenser.getCurrentLayout().getDepth() 
			== dispenser.getNextLayout().getDepth() );
	}
}
