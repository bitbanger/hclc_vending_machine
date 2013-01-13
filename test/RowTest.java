import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.GregorianCalendar;

/**
 * Unit test suite for the <tt>Row</tt> class.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class RowTest
{
	@Test
	public void testNormalConstruction() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		
		assertEquals(fight.getProduct(), banana);
		assertEquals(fight.getRemainingQuantity(), 4);
		assertEquals(fight.getExpirationDate(), dragging);
	}

	@Test(expected=BadArgumentException.class)
	public void testNoProductConstruction() throws BadArgumentException
	{
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(null, 4, dragging);
	}

	@Test
	public void testZeroQuantityConstruction() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 0, dragging);
	}

	@Test(expected=BadArgumentException.class)
	public void testNegativeQuantityConstruction() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, -1, dragging);
	}

	@Test(expected=BadArgumentException.class)
	public void testNoExpirationConstruction() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		Row fight=new Row(banana, 4, null);
	}

	@Test
	public void testCopyConstruction() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		Row noise=new Row(fight);
		
		assertEquals(fight, noise);
		assertFalse(fight==noise);
	}

	@Test
	public void testSetProduct() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		FoodItem raspberries=new FoodItem("Raspberries", 120, 9);
		
		fight.setProduct(raspberries); //we've done them!
		assertEquals(fight.getProduct(), raspberries);
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNullProduct() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		FoodItem pointedStick=null;
		
		fight.setProduct(pointedStick);
	}

	@Test
	public void testSetRemainingQuantity() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		
		fight.setRemainingQuantity(16);
		assertEquals(fight.getRemainingQuantity(), 16);
	}

	@Test
	public void testSetZeroRemainingQuantity() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		
		fight.setRemainingQuantity(0);
		assertEquals(fight.getRemainingQuantity(), 0);
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNegativeRemainingQuantity() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		
		fight.setRemainingQuantity(-1);
		assertEquals(fight.getRemainingQuantity(), -1);
	}

	@Test
	public void testSetExpirationDate() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		GregorianCalendar temporalAnomaly=new GregorianCalendar();
		
		temporalAnomaly.add(temporalAnomaly.SECOND, 17);
		fight.setExpirationDate(temporalAnomaly);
		assertEquals(fight.getExpirationDate(), temporalAnomaly);
	}

	@Test(expected=BadArgumentException.class)
	public void testSetNullExpirationDate() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 4, dragging);
		
		fight.setExpirationDate(null);
	}

	@Test
	public void testDecrementRemainingQuantity() throws BadArgumentException
	{
		FoodItem banana=new FoodItem("Banana", 75, 11);
		GregorianCalendar dragging=new GregorianCalendar();
		Row fight=new Row(banana, 2, dragging);
		
		assertTrue(fight.decrementRemainingQuantity());
		assertEquals(fight.getRemainingQuantity(), 1);
		assertTrue(fight.decrementRemainingQuantity());
		assertEquals(fight.getRemainingQuantity(), 0);
		assertFalse(fight.decrementRemainingQuantity());
		assertEquals(fight.getRemainingQuantity(), 0);
	}
}
