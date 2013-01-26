import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.GregorianCalendar;

/**
 * Unit test suite for the <tt>VendingMachine</tt> class.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class VendingMachineTest
{
	@Test
	public void testNormalConstruction() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		assertTrue(dispenser.isActive());
		assertEquals(dispenser.getLocation(), hometown);
		assertEquals(dispenser.getStockingInterval(), 3);
		assertEquals(dispenser.getCurrentLayout(), spread);
		assertFalse(dispenser.getNextLayout()==spread);
		assertFalse(dispenser.getNextLayout()==null);
	}

	@Test(expected=BadArgumentException.class)
	public void testNoLocationConstruction() throws BadArgumentException
	{
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(null, 3, spread);
	}

	@Test(expected=BadArgumentException.class)
	public void testZeroFrequencyConstruction() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 0, spread);
	}

	@Test(expected=BadArgumentException.class)
	public void testNegativeFrequencyConstruction() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, -1, spread);
	}

	@Test(expected=NullPointerException.class)
	public void testNoLayoutConstruction() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VendingMachine dispenser=new VendingMachine(hometown, 3, null);
	}

	@Test
	public void testExtendedConstruction() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VMLayout myWings=new VMLayout(5, 12, 13);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread, myWings, false);
		
		assertFalse(dispenser.isActive());
		assertEquals(dispenser.getLocation(), hometown);
		assertEquals(dispenser.getStockingInterval(), 3);
		assertEquals(dispenser.getCurrentLayout(), spread);
		assertEquals(dispenser.getNextLayout(), myWings);
	}

	@Test(expected=BadArgumentException.class)
	public void testNoPresentConstruction() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout myWings=new VMLayout(5, 12, 13);
		VendingMachine dispenser=new VendingMachine(hometown, 3, null, myWings, false);
	}

	@Test(expected=BadArgumentException.class)
	public void testNoFutureConstruction() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread, null, false);
	}

	@Test
	public void testSetLocation() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		Location homeAwayFromHome=new Location(6665, "The Batcave, R&D Division, Wayne Enterprises", new String[] {"Batsuit", "Batwings", "Batcycle", "Batmobile", "Fox's den"});
		
		dispenser.setLocation(homeAwayFromHome);
		assertEquals(dispenser.getLocation(), homeAwayFromHome);
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNoLocation() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		dispenser.setLocation(null);
	}

	@Test
	public void testSetCloseStockingInterval() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		GregorianCalendar oldPlan=dispenser.getCurrentLayout().getNextVisit();
		dispenser.setStockingInterval(2);
		assertEquals(dispenser.getStockingInterval(), 2);
		assertFalse(dispenser.getCurrentLayout().getNextVisit().equals(oldPlan));
	}

	@Test
	public void testSetDistantStockingInterval() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		GregorianCalendar oldPlan=dispenser.getCurrentLayout().getNextVisit();
		dispenser.setStockingInterval(45);
		assertEquals(dispenser.getStockingInterval(), 45);
		assertEquals(dispenser.getCurrentLayout().getNextVisit(), oldPlan); //we *still* can't let food expire this time!
	}

	@Test(expected=BadArgumentException.class)
	public void testSetZeroStockingInterval() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		Location homeAwayFromHome=new Location(6665, "The Batcave, R&D Division, Wayne Enterprises", new String[] {"Batsuit", "Batwings", "Batcycle", "Batmobile", "Fox's den"});
		
		dispenser.setStockingInterval(0);
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNegativeStockingInterval() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		Location homeAwayFromHome=new Location(6665, "The Batcave, R&D Division, Wayne Enterprises", new String[] {"Batsuit", "Batwings", "Batcycle", "Batmobile", "Fox's den"});
		
		dispenser.setStockingInterval(-1);
	}

	@Test
	public void testSetLayout() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		VMLayout betterSpot=new VMLayout(3, 6, 12);
		
		dispenser.setNextLayout(betterSpot);
		assertEquals(dispenser.getNextLayout(), betterSpot);
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNullLayout() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		dispenser.setNextLayout(null);
	}

	@Test
	public void testSwapInLayout() throws BadArgumentException, BadStateException
	{
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
		
		dispenser.swapInNextLayout();
		//assertEquals(dispenser.getCurrentLayout().getRows()[0][0].getRemainingQuantity(), 7);
		//assertEquals(dispenser.getNextLayout().getRows()[0][0].getRemainingQuantity(), 7);
		assertEquals(dispenser.getCurrentLayout().getRows()[0][1].getRemainingQuantity(), 1);
		assertEquals(dispenser.getNextLayout().getRows()[0][1].getRemainingQuantity(), 1);
		assertEquals(dispenser.getCurrentLayout().getRows()[1][0].getRemainingQuantity(), 5);
		assertEquals(dispenser.getNextLayout().getRows()[1][0].getRemainingQuantity(), 5);
		//assertEquals(dispenser.getCurrentLayout().getRows()[1][1].getRemainingQuantity(), 5);
		//assertEquals(dispenser.getNextLayout().getRows()[1][1].getRemainingQuantity(), 5);
	}

	@Test
	public void testMakeActive() throws BadArgumentException
	{
		Location hometown=new Location(5556, "Gotham City, DC", new String[] {"Superman", "Batman", "Wonder Woman", "Flash", "Green Lantern"});
		VMLayout spread=new VMLayout(30, 60, 90);
		VendingMachine dispenser=new VendingMachine(hometown, 3, spread);
		
		dispenser.makeActive(false);
		assertFalse(dispenser.isActive());
		dispenser.makeActive(true);
		assertTrue(dispenser.isActive());
	}
}
